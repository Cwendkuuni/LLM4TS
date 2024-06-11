import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setup() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();

        // Check if the point was added successfully
        assertEquals(1, foxHuntFrame.points.size());
    }

    @Test
    public void testLoadLandmarks() {
        // Test loading landmarks from file
        foxHuntFrame.loadLandmarks();

        // Check if landmarks were successfully loaded
        assertNotNull(foxHuntFrame.display.getLandmarks());
    }

    @Test
    public void testError() {
        // Test error handling
        foxHuntFrame.error("Sample error message");

        // Check if program exits on error
        assertFalse(foxHuntFrame.isVisible());
    }
}