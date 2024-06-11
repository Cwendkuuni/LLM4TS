
package Joshua.FoxHunt;

import org.junit.Assert;
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
        foxHuntFrame.lat.setText("40.123");
        foxHuntFrame.lon.setText("-73.456");
        
        foxHuntFrame.addPoint();
        
        Assert.assertEquals(1, foxHuntFrame.points.size());
        Assert.assertEquals(1, foxHuntFrame.fox.size());
        Assert.assertEquals(1, foxHuntFrame.posModel.getRowCount());
        
        TrigPoint point = (TrigPoint) foxHuntFrame.points.get(0);
        Assert.assertEquals(45, point.getAngle());
        Assert.assertEquals(40.123, point.getLatitude(), 0.001);
        Assert.assertEquals(-73.456, point.getLongitude(), 0.001);
        
        point = (TrigPoint) foxHuntFrame.fox.get(0);
        Assert.assertEquals(45, point.getAngle());
        Assert.assertEquals(40.123, point.getLatitude(), 0.001);
        Assert.assertEquals(-73.456, point.getLongitude(), 0.001);
        
        Assert.assertEquals("40.123", foxHuntFrame.posModel.getValueAt(0, 0));
        Assert.assertEquals("-73.456", foxHuntFrame.posModel.getValueAt(0, 1));
    }
    
    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();
        
        Assert.assertEquals(3, foxHuntFrame.display.numLandMarks());
    }
}
