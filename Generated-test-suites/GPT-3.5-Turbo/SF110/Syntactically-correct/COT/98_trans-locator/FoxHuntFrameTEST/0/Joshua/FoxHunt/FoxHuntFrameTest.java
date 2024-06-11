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
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");

        foxHuntFrame.addPoint();

        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.points.size());
        assertEquals(0, foxHuntFrame.fox.size());
    }

    @Test
    public void testAddPointWithExistingPoints() {
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");

        // Add the first point
        foxHuntFrame.addPoint();

        foxHuntFrame.theta.setText("180");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");

        // Add the second point
        foxHuntFrame.addPoint();

        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
        assertEquals(0, foxHuntFrame.posModel.getRowCount());
    }

    @Test
    public void testLoadLandmarks() {
        // Set up landmarks.cfg file with 1 landmark
        // ...

        foxHuntFrame.loadLandmarks();

        assertEquals(1, foxHuntFrame.display.landmarks.size());
    }

    @Test
    public void testLoadLandmarksWithInvalidFile() {
        // Set up nonexistent landmarks.cfg file

        foxHuntFrame.loadLandmarks();

        assertEquals(0, foxHuntFrame.display.landmarks.size());
    }
}