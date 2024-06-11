import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    // Test cases for the addPoint() method
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("37.7749");
        fhf.lon.setText("-122.4194");
        fhf.addPoint();
        assertEquals(1, fhf.points.size());
        assertEquals(1, fhf.fox.size());
    }
    
    @Test
    public void testAddPointWithInvalidInput() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("abc");
        fhf.lat.setText("37.7749");
        fhf.lon.setText("-122.4194");
        fhf.addPoint();
        assertEquals(0, fhf.points.size());
        assertEquals(0, fhf.fox.size());
    }
    
    // Test cases for the loadLandmarks() method
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.loadLandmarks();
        assertEquals(NUM, fhf.display.landmarks.size());
    }
    
    @Test
    public void testLoadNonExistingLandmarksFile() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.loadLandmarks();
        assertEquals(0, fhf.display.landmarks.size());
    }
    
}