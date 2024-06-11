package Joshua.FoxHunt;

import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension expected = new Dimension(200, 200);
        assertEquals(expected, hd.getPreferredSize());
    }
    
    @Test
    public void testGetMinimumSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension expected = new Dimension(50, 50);
        assertEquals(expected, hd.getMinimumSize());
    }
    
    @Test
    public void testAddPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        hd.addPoint(point);
        Vector<TrigPoint> points = hd.points;
        assertTrue(points.contains(point));
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        hd.addLandMark(landmark);
        Vector<Landmark> landmarks = hd.landmarks;
        assertTrue(landmarks.contains(landmark));
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay hd = new HuntDisplay();
        Graphics g = new Graphics();
        hd.paintComponent(g);
        // Test something here
    }
}