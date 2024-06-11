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
        
        Vector expectedPoints = new Vector();
        TrigPoint expectedPoint = new TrigPoint(37.7749, -122.4194, 45);
        expectedPoints.add(expectedPoint);
        assertEquals(expectedPoints, frame.points);
        
        Vector expectedFox = new Vector();
        expectedFox.add(expectedPoint);
        assertEquals(expectedFox, frame.fox);
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals(expectedPoint.getY(), frame.posModel.getValueAt(0, 0));
        assertEquals(expectedPoint.getX(), frame.posModel.getValueAt(0, 1));
        
    }
    
    // Additional test cases for potential edge cases and exceptions
    
    @Test
    public void testAddPointWithNegativeAngle() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("-45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        Vector expectedPoints = new Vector();
        TrigPoint expectedPoint = new TrigPoint(37.7749, -122.4194, -45);
        expectedPoints.add(expectedPoint);
        assertEquals(expectedPoints, frame.points);
        
        Vector expectedFox = new Vector();
        expectedFox.add(expectedPoint);
        assertEquals(expectedFox, frame.fox);
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals(expectedPoint.getY(), frame.posModel.getValueAt(0, 0));
        assertEquals(expectedPoint.getX(), frame.posModel.getValueAt(0, 1));
    }
    
    @Test
    public void testAddPointWithInvalidLatitude() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("invalid");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        Vector expectedPoints = new Vector();
        assertEquals(expectedPoints, frame.points);
        
        Vector expectedFox = new Vector();
        assertEquals(expectedFox, frame.fox);
        assertEquals(0, frame.posModel.getRowCount());
    }
    
    @Test
    public void testAddPointWithInvalidLongitude() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("invalid");
        
        frame.addPoint();
        
        Vector expectedPoints = new Vector();
        assertEquals(expectedPoints, frame.points);
        
        Vector expectedFox = new Vector();
        assertEquals(expectedFox, frame.fox);
        assertEquals(0, frame.posModel.getRowCount());
    }
    
}