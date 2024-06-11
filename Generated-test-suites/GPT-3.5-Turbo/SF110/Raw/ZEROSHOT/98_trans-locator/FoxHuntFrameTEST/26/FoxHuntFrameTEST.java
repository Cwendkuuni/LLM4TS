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
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37*30.1234");
        foxHuntFrame.lon.setText("122*10.5678");
        
        foxHuntFrame.addPoint();
        
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.points.size());
        
        TrigPoint point = (TrigPoint) foxHuntFrame.points.firstElement();
        TrigPoint displayPoint = (TrigPoint) foxHuntFrame.display.points.firstElement();
        
        assertEquals(45, point.angle);
        assertEquals(37.50139, point.LAT, 0.00001);
        assertEquals(122.17613, point.LON, 0.00001);
        
        assertEquals(45, displayPoint.angle);
        assertEquals(37.50139, displayPoint.LAT, 0.00001);
        assertEquals(122.17613, displayPoint.LON, 0.00001);
        
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("38*30.9876");
        foxHuntFrame.lon.setText("123*10.5432");
        
        foxHuntFrame.addPoint();
        
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.points.size());
        
        point = (TrigPoint) foxHuntFrame.points.elementAt(1);
        displayPoint = (TrigPoint) foxHuntFrame.display.points.elementAt(1);
        
        assertEquals(90, point.angle);
        assertEquals(38.51648, point.LAT, 0.00001);
        assertEquals(123.17572, point.LON, 0.00001);
        
        assertEquals(90, displayPoint.angle);
        assertEquals(38.51648, displayPoint.LAT, 0.00001);
        assertEquals(123.17572, displayPoint.LON, 0.00001);
    }
    
    @Test
    public void testLoadLandmarks() {
        // Test loading landmarks from file
        // Assuming a correct file is present with 3 landmarks
        
        foxHuntFrame.loadLandmarks();
        
        assertEquals(3, foxHuntFrame.display.landmarks.size());
        
        Landmark landmark1 = (Landmark) foxHuntFrame.display.landmarks.elementAt(0);
        Landmark landmark2 = (Landmark) foxHuntFrame.display.landmarks.elementAt(1);
        Landmark landmark3 = (Landmark) foxHuntFrame.display.landmarks.elementAt(2);
        
        assertEquals(37.6936, landmark1.latitude, 0.0001);
        assertEquals(-122.1830, landmark1.longitude, 0.0001);
        
        assertEquals(37.7749, landmark2.latitude, 0.0001);
        assertEquals(-122.4194, landmark2.longitude, 0.0001);
        
        assertEquals(34.0522, landmark3.latitude, 0.0001);
        assertEquals(-118.2437, landmark3.longitude, 0.0001);
    }
}