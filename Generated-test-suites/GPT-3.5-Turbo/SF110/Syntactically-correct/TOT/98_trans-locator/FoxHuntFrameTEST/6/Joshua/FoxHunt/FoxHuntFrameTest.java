import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame frame;
    
    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint_typicalCase() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(37.7749, ((TrigPoint)frame.points.get(0)).getLat(), 0.001);
        assertEquals(-122.4194, ((TrigPoint)frame.points.get(0)).getLon(), 0.001);
        assertEquals(45, ((TrigPoint)frame.points.get(0)).getAngle());
        assertNotNull(frame.display.getPoint(37.7749, -122.4194));
        assertEquals(37.7749, ((TrigPoint)frame.fox.get(0)).getLat(), 0.001);
        assertEquals(-122.4194, ((TrigPoint)frame.fox.get(0)).getLon(), 0.001);
    }
    
    @Test
    public void testAddPoint_multiplePoints() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        
        frame.theta.setText("90");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(40.7128, ((TrigPoint)frame.points.get(1)).getLat(), 0.001);
        assertEquals(-74.0060, ((TrigPoint)frame.points.get(1)).getLon(), 0.001);
        assertEquals(90, ((TrigPoint)frame.points.get(1)).getAngle());
        assertNotNull(frame.display.getPoint(37.7749, -122.4194));
        assertNotNull(frame.display.getPoint(40.7128, -74.0060));
        assertEquals(40.7128, ((TrigPoint)frame.fox.get(0)).getLat(), 0.001);
        assertEquals(-74.0060, ((TrigPoint)frame.fox.get(0)).getLon(), 0.001);
    }
    
    @Test
    public void testAddPoint_edgeCase_smallestAngle() {
        frame.theta.setText("0");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(37.7749, ((TrigPoint)frame.points.get(0)).getLat(), 0.001);
        assertEquals(-122.4194, ((TrigPoint)frame.points.get(0)).getLon(), 0.001);
        assertEquals(0, ((TrigPoint)frame.points.get(0)).getAngle());
        assertNotNull(frame.display.getPoint(37.7749, -122.4194));
        assertEquals(37.7749, ((TrigPoint)frame.fox.get(0)).getLat(), 0.001);
        assertEquals(-122.4194, ((TrigPoint)frame.fox.get(0)).getLon(), 0.001);
    }
    
    @Test
    public void testAddPoint_edgeCase_largestAngle() {
        frame.theta.setText("360");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(37.7749, ((TrigPoint)frame.points.get(0)).getLat(), 0.001);
        assertEquals(-122.4194, ((TrigPoint)frame.points.get(0)).getLon(), 0.001);
        assertEquals(360, ((TrigPoint)frame.points.get(0)).getAngle());
        assertNotNull(frame.display.getPoint(37.7749, -122.4194));
        assertEquals(37.7749, ((TrigPoint)frame.fox.get(0)).getLat(), 0.001);
        assertEquals(-122.4194, ((TrigPoint)frame.fox.get(0)).getLon(), 0.001);
    }
    
    @Test
    public void testAddPoint_errorCase_invalidAngle() {
        frame.theta.setText("abc");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.fox.size());
        assertNull(frame.display.getPoint(37.7749, -122.4194));
    }
    
    @Test
    public void testAddPoint_errorCase_invalidLat() {
        frame.theta.setText("45");
        frame.lat.setText("abc");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.fox.size());
        assertNull(frame.display.getPoint(37.7749, -122.4194));
    }
    
    @Test
    public void testAddPoint_errorCase_invalidLon() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("abc");
        
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.fox.size());
        assertNull(frame.display.getPoint(37.7749, -122.4194));
    }
    
}