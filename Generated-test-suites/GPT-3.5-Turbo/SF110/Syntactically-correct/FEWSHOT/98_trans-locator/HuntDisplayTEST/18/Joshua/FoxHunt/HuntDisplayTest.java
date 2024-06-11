import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import javax.swing.ImageIcon;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(0, 0, 0, true);
        huntDisplay.addPoint(point);
        
        assertEquals(1, huntDisplay.points.size());
        assertTrue(huntDisplay.points.contains(point));
        assertEquals(Color.GREEN, huntDisplay.getBackground());
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        
        assertEquals(1, huntDisplay.landmarks.size());
        assertTrue(huntDisplay.landmarks.contains(landmark));
        assertEquals(Color.WHITE, huntDisplay.getBackground());
    }
    
    @Test
    public void testPaintComponent() {
        Graphics g = null; // You need to create a mock of the Graphics class
        
        huntDisplay.repaint();
        
        assertNotNull(g); // Verify that the repaint method invokes the paintComponent method
    }
}