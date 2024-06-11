import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(0, 0, 0, true);
        huntDisplay.addPoint(point);
        // Check if the point was added to the vector of points
        assertEquals(1, huntDisplay.points.size());
        
        // Check if the point was added correctly 
        assertEquals(point.getY() / 90.0, huntDisplay.points.get(0).getY());
        assertEquals(point.getX() / 180.0, huntDisplay.points.get(0).getX());
        assertEquals(point.getTheta(), huntDisplay.points.get(0).getTheta());
        assertEquals(point.isFox(), huntDisplay.points.get(0).isFox());
    }

    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Check if the landmark was added to the vector of landmarks
        assertEquals(1, huntDisplay.landmarks.size());
        
        // Check if the landmark was added correctly
        assertEquals(landmark.getLat() / 90.0, huntDisplay.landmarks.get(0).getLat());
        assertEquals(landmark.getLon() / 180.0, huntDisplay.landmarks.get(0).getLon());
        assertEquals(landmark.getIcon(), huntDisplay.landmarks.get(0).getIcon());
    }

    @Test
    public void testPaintComponent() {
        // The functionality of paintComponent cannot be directly tested through JUnit
        // We will check if the method executes without throwing exception
        huntDisplay.paintComponent(null);
        assertNotNull(huntDisplay);
    }

    @Test
    public void testGetPreferredSize() {
        Dimension prefSize = huntDisplay.getPreferredSize();
        assertNotNull(prefSize);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension minSize = huntDisplay.getMinimumSize();
        assertNotNull(minSize);
    }
}