import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.123");
        frame.lon.setText("-122.456");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(0, frame.fox.size());
        
        frame.theta.setText("90");
        frame.lat.setText("38.456");
        frame.lon.setText("-123.789");
        
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.displayPrep();
        
        assertEquals(0, frame.display.getLandmarks().size());
        
        frame.loadLandmarks();
        
        assertEquals(3, frame.display.getLandmarks().size());
    }
    
    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        frame.error("This is an error");
        
        assertEquals(1, frame.showOptionDialogCallCount);
        assertEquals(1, frame.exitCallCount);
    }
}