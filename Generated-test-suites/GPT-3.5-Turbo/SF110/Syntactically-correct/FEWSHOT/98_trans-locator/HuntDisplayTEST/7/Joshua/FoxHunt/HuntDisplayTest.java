package Joshua.FoxHunt;

import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import org.junit.Test;
import org.junit.Assert;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(10, 20, 30, true);
        display.addPoint(point);
        Vector points = display.points;
        Assert.assertEquals(1, points.size());
        TrigPoint addedPoint = (TrigPoint)points.elementAt(0);
        Assert.assertEquals(point.getY()/90.0, addedPoint.getY(), 0.01);
        Assert.assertEquals(point.getX()/180.0, addedPoint.getX(), 0.01);
        Assert.assertEquals(point.getTheta(), addedPoint.getTheta());
        Assert.assertEquals(point.isFox(), addedPoint.isFox());
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(60, 120, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        Vector landmarks = display.landmarks;
        Assert.assertEquals(1, landmarks.size());
        Landmark addedLandmark = (Landmark)landmarks.elementAt(0);
        Assert.assertEquals(landmark.getLat()/90.0, addedLandmark.getLat(), 0.01);
        Assert.assertEquals(landmark.getLon()/180.0, addedLandmark.getLon(), 0.01);
        Assert.assertEquals(landmark.getIcon(), addedLandmark.getIcon());
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        display.paintComponent(g);
        // Assertion statements
    }
}