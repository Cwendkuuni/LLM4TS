package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JComponent;
import java.util.*;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(45.0, 90.0, 0.0, true);
        display.addPoint(point);
        
        Vector<TrigPoint> points = display.points;
        assertEquals(1, points.size());
        assertEquals(point.getY() / 90.0, points.elementAt(0).getY() / 90.0, 0.001);
        assertEquals(point.getX() / 180.0, points.elementAt(0).getX() / 180.0, 0.001);
        assertEquals(point.getTheta(), points.elementAt(0).getTheta(), 0.001);
        assertEquals(point.isFox(), points.elementAt(0).isFox());
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45.0, 90.0, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        
        Vector<Landmark> landmarks = display.landmarks;
        assertEquals(1, landmarks.size());
        assertEquals(landmark.getLat() / 90.0, landmarks.elementAt(0).getLat() / 90.0, 0.001);
        assertEquals(landmark.getLon() / 180.0, landmarks.elementAt(0).getLon() / 180.0, 0.001);
        assertEquals(landmark.getIcon(), landmarks.elementAt(0).getIcon());
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        TrigPoint point1 = new TrigPoint(0.0, 0.0, 0.0, false);
        TrigPoint point2 = new TrigPoint(0.5, 0.5, 0.0, true);
        display.points.add(point1);
        display.points.add(point2);
        
        Landmark landmark1 = new Landmark(0.0, 0.0, new ImageIcon("icon1.png"));
        Landmark landmark2 = new Landmark(0.5, 0.5, new ImageIcon("icon2.png"));
        display.landmarks.add(landmark1);
        display.landmarks.add(landmark2);
        
        display.paintComponent(g);
        
        // Assert the drawing of points and landmarks
    }
}