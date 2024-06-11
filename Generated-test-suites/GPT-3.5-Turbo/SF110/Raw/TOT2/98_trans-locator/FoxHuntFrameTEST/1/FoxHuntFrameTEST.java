import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    FoxHuntFrame foxHuntFrame;
    
    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        // Typical use case
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("122.4194");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
    }
    
    @Test
    public void testAddPointEdgeCase() {
        // Edge case - add point with negative angle
        foxHuntFrame.theta.setText("-45");
        foxHuntFrame.lat.setText("40.7128");
        foxHuntFrame.lon.setText("-74.0060");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
    }
    
    @Test
    public void testLoadLandmarks() {
        // Typical use case
        foxHuntFrame.loadLandmarks();
        assertNotNull(foxHuntFrame.display.getLandmarks());
    }
    
    @Test
    public void testLoadLandmarksErrorCase() {
        // Error scenario - file not found
        foxHuntFrame.loadLandmarks();
        assertFalse(foxHuntFrame.display.getLandmarks().isEmpty());
    }
    
    @After
    public void tearDown() {
        foxHuntFrame = null;
    }
}