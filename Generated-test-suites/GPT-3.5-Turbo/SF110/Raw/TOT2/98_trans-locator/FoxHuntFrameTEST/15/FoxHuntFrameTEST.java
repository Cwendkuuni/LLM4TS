package Joshua.FoxHunt;

import org.junit.Test;

import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");

        foxHuntFrame.addPoint();

        // Check that the point was added correctly
        assertEquals(1, foxHuntFrame.points.size());

        // Check if fox vector was updated correctly
        assertEquals(1, foxHuntFrame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        // Check if landmarks are loaded correctly
        foxHuntFrame.loadLandmarks();
        assertNotNull(foxHuntFrame.display.landmarks);
    }

    @Test
    public void testError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        // Check if error handling works correctly
        assertEquals(1, foxHuntFrame.error("Test Error"));
    }
}