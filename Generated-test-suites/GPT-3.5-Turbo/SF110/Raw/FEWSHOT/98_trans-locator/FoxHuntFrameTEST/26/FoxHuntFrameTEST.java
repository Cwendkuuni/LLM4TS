package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame foxHuntFrame;
    
    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        
        assertNotNull(foxHuntFrame.points);
        assertEquals(1, foxHuntFrame.points.size());
        
        assertNotNull(foxHuntFrame.fox);
        assertEquals(0, foxHuntFrame.fox.size());
        
        assertNotNull(foxHuntFrame.display);
    }
    
    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();
        
        assertNotNull(foxHuntFrame.display);
    }
}