import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testGetPreferredSize() {
        Dimension prefSize = huntDisplay.getPreferredSize();
        assertNotNull(prefSize);
        assertEquals(200, prefSize.getWidth(), 0);
        assertEquals(200, prefSize.getHeight(), 0);
    }
    
    @Test
    public void testGetMinimumSize() {
        Dimension minSize = huntDisplay.getMinimumSize();
        assertNotNull(minSize);
        assertEquals(50, minSize.getWidth(), 0);
        assertEquals(50, minSize.getHeight(), 0);
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1, 1, 0, true);
        huntDisplay.addPoint(point);
        
        // Verify that the point was added
        assertEquals(1, huntDisplay.points.size());
        
        // Verify that the point is correctly added
        TrigPoint addedPoint = (TrigPoint) huntDisplay.points.elementAt(0);
        assertEquals(1, addedPoint.getX(), 0);
        assertEquals(1, addedPoint.getY(), 0);
        assertEquals(0, addedPoint.getTheta(), 0);
        assertTrue(addedPoint.isFox());
    }
    
    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(1, 1, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        
        // Verify that the landmark was added
        assertEquals(1, huntDisplay.landmarks.size());
        
        // Verify that the landmark is correctly added
        Landmark addedLandmark = (Landmark) huntDisplay.landmarks.elementAt(0);
        assertEquals(1, addedLandmark.getLon(), 0);
        assertEquals(1, addedLandmark.getLat(), 0);
        assertNotNull(addedLandmark.getIcon());
    }
    
    @Test
    public void testPaintComponent() {
        // TODO: Implement test for paintComponent
    }
}