/**
 * Author: SACHIN
 * Date: 10/30/2016.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClipImage {

    private BufferedImage sourceImage = null;
    private List<Integer> xCordList = null;
    private List<Integer> yCordList = null;
    private Polygon polygon = null;

    public static void main(String[] args) {
        List<Integer> xCord = new ArrayList<Integer>();
        xCord.add(4);
        xCord.add(70);
        xCord.add(70);
        xCord.add(35);
        xCord.add(20);
        xCord.add(4);
        List<Integer> yCord = new ArrayList<Integer>();
        yCord.add(4);
        yCord.add(4);
        yCord.add(70);
        yCord.add(35);
        yCord.add(20);
        yCord.add(70);

        try {
            new ClipImage(xCord,yCord,new File("sachin.PNG")).applyPolyPoints();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ClipImage(List<Integer> xCordList, List<Integer> yCordList,File sourceImage) throws FileNotFoundException {
        this.xCordList = xCordList;
        this.yCordList = yCordList;
        try {
            this.sourceImage = ImageIO.read(sourceImage);
        } catch (IOException e) {
            throw new FileNotFoundException("File not found");
        }
        polygon = new Polygon();

    }

    public BufferedImage applyPolyPoints(){

        int totalPoints = xCordList.size()-1;

        for(int i = 0;i<totalPoints;i++){
            plotNextPixel(xCordList.get(i),yCordList.get(i),xCordList.get(i+1),yCordList.get(i+1));
        }
        plotNextPixel(xCordList.get(totalPoints),yCordList.get(totalPoints),xCordList.get(0),yCordList.get(0));

        for(int i=0;i<sourceImage.getWidth();i++){
            for(int j=0;j<sourceImage.getHeight();j++){
                if(!polygon.contains(i,j)){
                    sourceImage.setRGB(i,j,new Color(0,255,0).getRGB());
                }
            }
        }

        try {
            ImageIO.write(sourceImage,"PNG",new File("output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sourceImage;
    }


    private void plotNextPixel(int x,int y,int x2, int y2) {
        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            polygon.addPoint(x,y);
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
    }
}
