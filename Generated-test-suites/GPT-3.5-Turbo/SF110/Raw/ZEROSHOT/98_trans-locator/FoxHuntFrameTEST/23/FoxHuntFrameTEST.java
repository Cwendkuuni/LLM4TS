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
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        
        frame.theta.setText("30");
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        
        TrigPoint addedPoint = (TrigPoint) frame.points.lastElement();
        TrigPoint foxPoint = (TrigPoint) frame.fox.lastElement();
        assertEquals(addedPoint, foxPoint);
        assertEquals(frame.display.points.get(1), foxPoint);
        
        frame.theta.setText("60");
        frame.lat.setText("39.9526");
        frame.lon.setText("-75.1652");
        frame.addPoint();
        assertEquals(3, frame.points.size());
        assertEquals(3, frame.display.points.size());
        assertEquals(2, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
        
        addedPoint = (TrigPoint) frame.points.lastElement();
        foxPoint = (TrigPoint) frame.fox.lastElement();
        assertEquals(addedPoint, foxPoint);
        assertEquals(frame.display.points.get(2), foxPoint);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(9, frame.display.landmarks.size());
        
        Landmark landmark1 = frame.display.landmarks.get(0);
        assertEquals(37.77493, landmark1.lat, 0.00001);
        assertEquals(-122.41942, landmark1.lon, 0.00001);
        assertNotNull(landmark1.icon);
        
        Landmark landmark2 = frame.display.landmarks.get(1);
        assertEquals(34.05223, landmark2.lat, 0.00001);
        assertEquals(-118.24368, landmark2.lon, 0.00001);
        assertNotNull(landmark2.icon);
        
        // assertions for other landmarks...
    }
    
    // Other test methods for the remaining methods in FoxHuntFrame class
    
}