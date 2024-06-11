import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        
        // Test adding a point
        assertTrue(foxHuntFrame.points.isEmpty());
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("40.7128");
        foxHuntFrame.lon.setText("-74.0060");
        foxHuntFrame.addPoint();
        assertFalse(foxHuntFrame.points.isEmpty());
        
        // Test adding second point and calculation
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("34.0522");
        foxHuntFrame.lon.setText("-118.2437");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        // Test loading landmarks from file
        foxHuntFrame.loadLandmarks();
        assertFalse(foxHuntFrame.display.landmarks.isEmpty());
    }

    @Test
    public void testError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        // Test error handling
        assertEquals(0, foxHuntFrame.error("Test Error"));
    }
}