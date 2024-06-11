import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        HuntDisplay display = new HuntDisplay();
        
        display.addPoint(point);
        
        Vector<TrigPoint> points = display.points;
        assertEquals(1, points.size());
        assertEquals(point, points.get(0));
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        HuntDisplay display = new HuntDisplay();
        
        display.addLandMark(landmark);
        
        Vector<Landmark> landmarks = display.landmarks;
        assertEquals(1, landmarks.size());
        assertEquals(landmark, landmarks.get(0));
    }
    
    @Test
    public void testPaintComponent() {
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        HuntDisplay display = new HuntDisplay();
        
        display.addPoint(point);
        display.addLandMark(landmark);
        
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        display.paintComponent(g);
        
        // TODO: Additional assertions for paintComponent method
        
        g.dispose();
    }
}