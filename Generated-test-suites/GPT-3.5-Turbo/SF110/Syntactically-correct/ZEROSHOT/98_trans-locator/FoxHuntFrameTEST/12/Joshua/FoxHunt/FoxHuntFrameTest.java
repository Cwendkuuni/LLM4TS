import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame frame;
    
    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.getPointCount());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        
        frame.theta.setText("90");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.1278");
        
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.getPointCount());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
    }
    
    @Test
    public void testLoadLandmarks() {
        // Assuming landmarks.cfg file exists with valid landmarks
        frame.loadLandmarks();
        
        assertEquals(3, frame.display.getLandmarkCount());
    }
}