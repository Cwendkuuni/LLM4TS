import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame foxHuntFrame;
    
    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        // Typical Use Case
        // Adding a valid trig point to the frame
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.points.size());
        assertEquals(0, foxHuntFrame.fox.size());
        assertEquals(0, foxHuntFrame.posModel.getRowCount());
        
        // Edge Case
        // Adding a valid trig point with negative latitude
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("-37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
        assertEquals(1, foxHuntFrame.posModel.getRowCount());
        
        // Error Scenario
        // Adding a trig point with invalid angle (greater than 90)
        foxHuntFrame.theta.setText("100");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
        assertEquals(1, foxHuntFrame.posModel.getRowCount());
    }
    
    @Test
    public void testLoadLandmarks() {
        // Typical Use Case
        // Loading a valid landmarks file
        foxHuntFrame.loadLandmarks();
        assertEquals(3, foxHuntFrame.display.landmarks.size());
        
        // Error Scenario
        // Loading a non-existent landmarks file
        foxHuntFrame.loadLandmarks();
        assertEquals(0, foxHuntFrame.display.landmarks.size());
    }
    
    @Test
    public void testError() {
        // Typical Use Case
        // Displaying an error message and choosing to continue
        assertEquals(1, foxHuntFrame.error("Error: Can't Find the file"));
        
        // Error Scenario
        // Displaying an error message and choosing to abort
        assertEquals(0, foxHuntFrame.error("Error: Error communicating with the file"));
    }
}