package Joshua.FoxHunt;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {
    private HuntDisplay display;
 
    @Before
    public void setUp() {
        display = new HuntDisplay();
    }
 
    @Test
    public void testAddPoint() {
        TrigPoint point1 = new TrigPoint(10, 20, 30, true);
        TrigPoint point2 = new TrigPoint(40, 50, 60, false);
        
        display.addPoint(point1);
        display.addPoint(point2);
        
        assertEquals(2, display.points.size());
        assertTrue(display.points.contains(point1));
        assertTrue(display.points.contains(point2));
    }
 
    @Test
    public void testAddLandMark() {
        Landmark landmark1 = new Landmark(10, 20, new ImageIcon("image1.png"));
        Landmark landmark2 = new Landmark(30, 40, new ImageIcon("image2.png"));
        
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        
        assertEquals(2, display.landmarks.size());
        assertTrue(display.landmarks.contains(landmark1));
        assertTrue(display.landmarks.contains(landmark2));
    }
 
    @Test
    public void testPaintComponent() {
        TrigPoint point1 = new TrigPoint(10, 20, 30, true);
        TrigPoint point2 = new TrigPoint(40, 50, 60, false);
        Landmark landmark1 = new Landmark(10, 20, new ImageIcon("image1.png"));
        Landmark landmark2 = new Landmark(30, 40, new ImageIcon("image2.png"));
        
        display.addPoint(point1);
        display.addPoint(point2);
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        
        Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        // Test paintComponent
        display.paintComponent(g);
        
        // Add asserts for the expected behavior of paintComponent
        
    }
}