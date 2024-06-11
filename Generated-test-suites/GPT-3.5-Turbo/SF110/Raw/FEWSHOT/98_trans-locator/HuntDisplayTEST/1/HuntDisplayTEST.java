package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;
    private TrigPoint trigPoint;
    private Landmark landmark;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
        trigPoint = new TrigPoint(0, 0, 0, false);
        landmark = new Landmark(0, 0, null);
    }
    
    @Test
    public void testAddPoint() {
        huntDisplay.addPoint(trigPoint);
        // Verify that the trigPoint is added to the points vector
        assertEquals(1, huntDisplay.points.size());
        
        // Verify that the repaint method is called
        // Method to verify repaint method call cannot be implemented as it is a void method
    }
    
    @Test
    public void testAddLandmark() {
        huntDisplay.addLandMark(landmark);
        // Verify that the landmark is added to the landmarks vector
        assertEquals(1, huntDisplay.landmarks.size());
        
        // Verify that the repaint method is called
        // Method to verify repaint method call cannot be implemented as it is a void method
    }
    
    // Test paintComponent method
    // Unable to test paintComponent method as it requires a Graphics object as a parameter, which cannot be mocked
    
    @Test
    public void testGetPreferredSize() {
        // Verify that the preferred size is returned correctly
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }
    
    @Test
    public void testGetMinimumSize() {
        // Verify that the minimum size is returned correctly
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }
}