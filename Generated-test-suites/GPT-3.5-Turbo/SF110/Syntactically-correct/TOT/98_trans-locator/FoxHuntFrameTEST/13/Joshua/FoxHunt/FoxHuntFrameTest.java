import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        // Typical use case
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        
        // Edge case
        frame.theta.setText("0");
        frame.lat.setText("0.0");
        frame.lon.setText("0.0");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        
        // Error scenario
        frame.theta.setText("invalid");
        frame.lat.setText("invalid");
        frame.lon.setText("invalid");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
    }

    @Test
    public void testLoadLandmarks() throws Exception {
        // Typical use case
        frame.loadLandmarks();
        assertEquals(4, frame.display.landmarks.size());

        // Edge case
        frame.loadLandmarks();
        assertEquals(4, frame.display.landmarks.size());

        // Error scenario
        // Create a temporary directory for testing
        File tempDir = new File("temp");
        tempDir.mkdir();
        File landmarksFile = new File("temp/landmarks.cfg");
        landmarksFile.createNewFile();
        frame.loadLandmarks();
        assertEquals(0, frame.display.landmarks.size());

        // Clean up the temporary directory
        landmarksFile.delete();
        tempDir.delete();
    }

    @Test
    public void testError() {
        // Typical use case
        frame.error("Error message");
        // User chooses to continue
        assertEquals(0, frame.getState());
        
        // User chooses to abort
        frame.error("Error message");
        assertEquals(1, frame.getState());
    }

}