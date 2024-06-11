import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        // Verify that the point is added to the points vector
        assertEquals(1, huntDisplay.points.size());
        
        // Verify that the point is added correctly
        assertEquals(45/90.0, huntDisplay.points.get(0).getY());
        assertEquals(90/180.0, huntDisplay.points.get(0).getX());
        assertEquals(0, huntDisplay.points.get(0).getTheta());
        assertEquals(true, huntDisplay.points.get(0).isFox());
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Verify that the landmark is added to the landmarks vector
        assertEquals(1, huntDisplay.landmarks.size());
        
        // Verify that the landmark is added correctly
        assertEquals(45/90.0, huntDisplay.landmarks.get(0).getLat());
        assertEquals(90/180.0, huntDisplay.landmarks.get(0).getLon());
        assertNotNull(huntDisplay.landmarks.get(0).getIcon());
    }
    
    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension expectedSize = new Dimension(200, 200);
        
        assertEquals(expectedSize, huntDisplay.getPreferredSize());
    }
    
    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension expectedSize = new Dimension(50, 50);
        
        assertEquals(expectedSize, huntDisplay.getMinimumSize());
    }
    
    @Test
    public void testPaintComponent() {
        // Test the paintComponent method, but it is difficult to test it thoroughly using JUnit
        // as it involves graphics and images. So, this test case will simply call the method
        // and check if it executes without any errors.
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics g = null; // You can replace this with a real Graphics object if required
        huntDisplay.paintComponent(g);
    }
}