import static org.junit.Assert.*;
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
        String thetaValue = "45";
        String latValue = "37.7749";
        String lonValue = "-122.4194";
        
        foxHuntFrame.theta.setText(thetaValue);
        foxHuntFrame.lat.setText(latValue);
        foxHuntFrame.lon.setText(lonValue);
        
        foxHuntFrame.addPoint();
        
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
    }
    
    @Test
    public void testAddPointWithInvalidValues() {
        String thetaValue = "abc";
        String latValue = "37.7749";
        String lonValue = "-122.4194";
        
        foxHuntFrame.theta.setText(thetaValue);
        foxHuntFrame.lat.setText(latValue);
        foxHuntFrame.lon.setText(lonValue);
        
        foxHuntFrame.addPoint();
        
        assertEquals(0, foxHuntFrame.points.size());
        assertEquals(0, foxHuntFrame.fox.size());
    }
    
    @Test
    public void testAddPointWithDuplicatePoints() {
        String thetaValue = "45";
        String latValue = "37.7749";
        String lonValue = "-122.4194";
        
        foxHuntFrame.theta.setText(thetaValue);
        foxHuntFrame.lat.setText(latValue);
        foxHuntFrame.lon.setText(lonValue);
        
        foxHuntFrame.addPoint();
        foxHuntFrame.addPoint();
        
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
    }
    
    @Test
    public void testAddPointWithNoPoints() {
        assertEquals(0, foxHuntFrame.points.size());
        assertEquals(0, foxHuntFrame.fox.size());
    }
    
    @Test
    public void testAddPointWithMultiplePoints() {
        String thetaValue1 = "45";
        String latValue1 = "37.7749";
        String lonValue1 = "-122.4194";
        
        String thetaValue2 = "90";
        String latValue2 = "34.0522";
        String lonValue2 = "-118.2437";
        
        foxHuntFrame.theta.setText(thetaValue1);
        foxHuntFrame.lat.setText(latValue1);
        foxHuntFrame.lon.setText(lonValue1);
        
        foxHuntFrame.addPoint();
        
        foxHuntFrame.theta.setText(thetaValue2);
        foxHuntFrame.lat.setText(latValue2);
        foxHuntFrame.lon.setText(lonValue2);
        
        foxHuntFrame.addPoint();
        
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
    }
}