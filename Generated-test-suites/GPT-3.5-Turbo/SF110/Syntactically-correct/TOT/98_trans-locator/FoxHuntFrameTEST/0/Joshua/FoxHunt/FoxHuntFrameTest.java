import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Vector;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test adding a valid point
        frame.theta.setText("45");
        frame.lat.setText("40.1234");
        frame.lon.setText("-80.9876");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        
        // Test adding a second valid point
        frame.theta.setText("60");
        frame.lat.setText("39.5678");
        frame.lon.setText("-81.6543");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
        
        // Test adding a point with invalid angle
        frame.theta.setText("-90");
        frame.lat.setText("38.8765");
        frame.lon.setText("-82.4433");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
    }
    
    @Test
    public void testAddPointEdgeCases() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test adding the minimum angle
        frame.theta.setText("-90");
        frame.lat.setText("40.0000");
        frame.lon.setText("-80.0000");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        
        // Test adding the maximum angle
        frame.theta.setText("90");
        frame.lat.setText("40.0000");
        frame.lon.setText("-80.0000");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test loading landmarks from a valid file
        frame.loadLandmarks();
        assertEquals(3, frame.display.landmarks.size());
        
        // Test loading landmarks from an invalid file
        frame.error("Can't Find the file");
        frame.loadLandmarks();
        assertEquals(0, frame.display.landmarks.size());
    }
    
    // Other unit tests for the remaining methods in the FoxHuntFrame class
    
}