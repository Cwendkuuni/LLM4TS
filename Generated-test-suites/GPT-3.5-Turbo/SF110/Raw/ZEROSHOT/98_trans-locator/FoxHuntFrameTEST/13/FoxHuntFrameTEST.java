import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        
        frame.theta.setText("90");
        frame.lat.setText("41.8781");
        frame.lon.setText("-87.6298");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(3, frame.display.getNumberOfLandmarks());
    }
}