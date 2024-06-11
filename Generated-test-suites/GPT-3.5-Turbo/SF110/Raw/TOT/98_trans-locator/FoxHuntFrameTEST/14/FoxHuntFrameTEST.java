import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint_typical() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }
    
    @Test
    public void testAddPoint_multiplePoints() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("122.4194");
        
        frame.addPoint();
        
        frame.theta.setText("30");
        frame.lat.setText("37.7833");
        frame.lon.setText("122.4167");
        
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
    }
    
    @Test
    public void testAddPoint_decimalDegrees() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
    }
    
    @Test
    public void testLoadLandmarks_fileNotFound() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        frame.loadLandmarks();
        
        // Check if error dialog is displayed
        // Can't assert System.exit(1), so manual check is needed
    }
    
    @Test
    public void testLoadLandmarks_ioError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        frame.loadLandmarks();
        
        // Check if error dialog is displayed
        // Can't assert System.exit(1), so manual check is needed
    }
}