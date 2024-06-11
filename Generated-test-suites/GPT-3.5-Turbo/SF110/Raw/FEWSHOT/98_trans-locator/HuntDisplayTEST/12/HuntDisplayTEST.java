package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        display.addPoint(point);
        
        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(new TrigPoint(0.5, 0.5, 0, true));
        
        assertEquals(expectedPoints, display.points);
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(45, 90, icon);
        display.addLandMark(landmark);
        
        Vector<Landmark> expectedLandmarks = new Vector<>();
        expectedLandmarks.add(new Landmark(0.5, 0.5, icon));
        
        assertEquals(expectedLandmarks, display.landmarks);
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        
        TrigPoint point1 = new TrigPoint(45, 90, 0, true);
        TrigPoint point2 = new TrigPoint(60, 120, 45, false);
        display.addPoint(point1);
        display.addPoint(point2);
        
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(45, 90, icon);
        display.addLandMark(landmark);
        
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        display.paintComponent(g);
        
        // Verify painting logic
    }
}