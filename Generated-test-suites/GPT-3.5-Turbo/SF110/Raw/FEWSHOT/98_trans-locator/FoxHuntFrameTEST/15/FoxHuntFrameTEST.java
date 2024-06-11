package Joshua.FoxHunt;

import org.junit.Assert;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        Assert.assertEquals(1, frame.points.size());
        Assert.assertEquals(1, frame.display.getPoints().size());
        Assert.assertEquals(0, frame.fox.size());
        Assert.assertEquals(0, frame.posModel.getRowCount());
        
        frame.addPoint();
        
        Assert.assertEquals(2, frame.points.size());
        Assert.assertEquals(2, frame.display.getPoints().size());
        Assert.assertEquals(1, frame.fox.size());
        Assert.assertEquals(1, frame.posModel.getRowCount());
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        frame.loadLandmarks();
        
        Assert.assertFalse(frame.display.getLandmarks().isEmpty());
    }
}