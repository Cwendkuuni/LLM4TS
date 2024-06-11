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
        foxHuntFrame.lat.setText("10.12345");
        foxHuntFrame.lon.setText("20.12345");
        
        foxHuntFrame.addPoint();
        
        Assert.assertEquals(1, foxHuntFrame.points.size());
        TrigPoint trigPoint = foxHuntFrame.points.elementAt(0);
        Assert.assertEquals(45, trigPoint.getAngle());
        Assert.assertEquals(10.12345, trigPoint.getLat(), 0.00001);
        Assert.assertEquals(20.12345, trigPoint.getLon(), 0.00001);
        
        Assert.assertEquals(1, foxHuntFrame.display.getPoints().size());
        Assert.assertEquals(trigPoint, foxHuntFrame.display.getPoints().get(0));
        
        Assert.assertEquals(0, foxHuntFrame.fox.size());
        
        Assert.assertEquals(0, foxHuntFrame.posModel.getRowCount());
        
        Assert.assertEquals(0, foxHuntFrame.display.getFoxes().size());
        
    }
    
    @Test
    public void testAddPointWithExistingPoints() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("10.12345");
        foxHuntFrame.lon.setText("20.12345");
        
        foxHuntFrame.addPoint();
        
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("15.12345");
        foxHuntFrame.lon.setText("25.12345");
        
        foxHuntFrame.addPoint();
        
        Assert.assertEquals(2, foxHuntFrame.points.size());
        
        TrigPoint trigPoint1 = foxHuntFrame.points.elementAt(0);
        Assert.assertEquals(45, trigPoint1.getAngle());
        Assert.assertEquals(10.12345, trigPoint1.getLat(), 0.00001);
        Assert.assertEquals(20.12345, trigPoint1.getLon(), 0.00001);
        
        TrigPoint trigPoint2 = foxHuntFrame.points.elementAt(1);
        Assert.assertEquals(90, trigPoint2.getAngle());
        Assert.assertEquals(15.12345, trigPoint2.getLat(), 0.00001);
        Assert.assertEquals(25.12345, trigPoint2.getLon(), 0.00001);
        
        Assert.assertEquals(2, foxHuntFrame.display.getPoints().size());
        Assert.assertEquals(trigPoint1, foxHuntFrame.display.getPoints().get(0));
        Assert.assertEquals(trigPoint2, foxHuntFrame.display.getPoints().get(1));
        
        Assert.assertEquals(1, foxHuntFrame.fox.size());
        TrigPoint fox = foxHuntFrame.fox.elementAt(0);
        Assert.assertEquals(11.56867, fox.getLat(), 0.00001);
        Assert.assertEquals(21.56867, fox.getLon(), 0.00001);
        
        Assert.assertEquals(1, foxHuntFrame.posModel.getRowCount());
        Assert.assertEquals(fox.getLat(), foxHuntFrame.posModel.getValueAt(0, 0));
        Assert.assertEquals(fox.getLon(), foxHuntFrame.posModel.getValueAt(0, 1));
        
        Assert.assertEquals(1, foxHuntFrame.display.getFoxes().size());
        Assert.assertEquals(fox, foxHuntFrame.display.getFoxes().get(0));
    }
    
    @Test
    public void testAddPointInvalidInput() {
        foxHuntFrame.theta.setText("abc");
        foxHuntFrame.lat.setText("10.12345");
        foxHuntFrame.lon.setText("20.12345");
        
        foxHuntFrame.addPoint();
        
        Assert.assertEquals(0, foxHuntFrame.points.size());
        
        Assert.assertEquals(0, foxHuntFrame.display.getPoints().size());
        
        Assert.assertEquals(0, foxHuntFrame.fox.size());
        
        Assert.assertEquals(0, foxHuntFrame.posModel.getRowCount());
        
        Assert.assertEquals(0, foxHuntFrame.display.getFoxes().size());
    }
}