import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        Vector<TrigPoint> points = frame.points;
        assertEquals(1, points.size());
        
        TrigPoint point = points.elementAt(0);
        assertEquals(45, point.getAngle());
        assertEquals(37.7749, point.getLat(), 0.000001);
        assertEquals(-122.4194, point.getLon(), 0.000001);
        
        Vector<TrigPoint> fox = frame.fox;
        assertEquals(0, fox.size());
        
        assertEquals(1, frame.posModel.getRowCount());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        frame.loadLandmarks();
        
        // Assert the landmarks are loaded correctly
        assertEquals(3, frame.display.getLandmarks().size());
        Landmark landmark1 = frame.display.getLandmarks().get(0);
        assertEquals(37.7833, landmark1.getLat(), 0.000001);
        assertEquals(-122.4167, landmark1.getLon(), 0.000001);
        assertNotNull(landmark1.getIcon());
    }
}