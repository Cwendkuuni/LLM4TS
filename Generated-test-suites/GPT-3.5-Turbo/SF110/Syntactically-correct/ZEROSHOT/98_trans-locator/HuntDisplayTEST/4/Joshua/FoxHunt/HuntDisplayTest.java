import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import static org.junit.Assert.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testGetPreferredSize() {
        Dimension prefSize = huntDisplay.getPreferredSize();
        assertNotNull(prefSize);
        assertEquals(200, prefSize.width);
        assertEquals(200, prefSize.height);
    }
    
    @Test
    public void testGetMinimumSize() {
        Dimension minSize = huntDisplay.getMinimumSize();
        assertNotNull(minSize);
        assertEquals(50, minSize.width);
        assertEquals(50, minSize.height);
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45.0, 90.0, 0.0, false);
        huntDisplay.addPoint(point);
        Vector<TrigPoint> points = huntDisplay.points;
        assertEquals(1, points.size());
        assertEquals(point, points.elementAt(0));
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45.0, 90.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> landmarks = huntDisplay.landmarks;
        assertEquals(1, landmarks.size());
        assertEquals(landmark, landmarks.elementAt(0));
    }
    
    @Test
    public void testPaintComponent() {
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).createGraphics();
        
        TrigPoint point1 = new TrigPoint(0.1, 0.2, 0.0, false);
        TrigPoint point2 = new TrigPoint(0.5, 0.8, 0.0, true);
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        
        Landmark landmark1 = new Landmark(0.3, 0.4, new ImageIcon());
        Landmark landmark2 = new Landmark(0.9, 0.7, new ImageIcon());
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        
        huntDisplay.paintComponent(g);
        // Add additional assertions based on the expected results
    }
}