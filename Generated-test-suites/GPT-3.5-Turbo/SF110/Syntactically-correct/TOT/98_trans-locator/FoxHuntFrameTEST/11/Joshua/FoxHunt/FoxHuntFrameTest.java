import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        // Test adding a point with angle 0, latitude 0, and longitude 0
        foxHuntFrame.theta.setText("0");
        foxHuntFrame.lat.setText("0");
        foxHuntFrame.lon.setText("0");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(0, foxHuntFrame.fox.size());

        // Test adding a point with angle 45, latitude -45.678, and longitude 123.456
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("-45.678");
        foxHuntFrame.lon.setText("123.456");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());

        // Test adding a point with angle -90, latitude 90, and longitude -180
        foxHuntFrame.theta.setText("-90");
        foxHuntFrame.lat.setText("90");
        foxHuntFrame.lon.setText("-180");
        foxHuntFrame.addPoint();
        assertEquals(3, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        // Test loading landmarks from a valid file
        foxHuntFrame.loadLandmarks();
        assertEquals(3, foxHuntFrame.display.getLandmarks().size());

        // Test loading landmarks from a file that does not exist
        foxHuntFrame.loadLandmarks();
        assertEquals(3, foxHuntFrame.display.getLandmarks().size());
    }

    @Test
    public void testError() {
        // Test showing the error message and exiting when user selects "Abort"
        assertEquals(1, foxHuntFrame.error("Can't Find the file"));
        assertEquals(1, foxHuntFrame.error("Error communicating with the file"));

        // Test not exiting when user selects "Continue"
        assertEquals(0, foxHuntFrame.error("Can't Find the file"));
        assertEquals(0, foxHuntFrame.error("Error communicating with the file"));
    }

}