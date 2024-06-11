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
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        huntDisplay.addPoint(point);
        // Assert that the point has been added to the points Vector
        assertEquals(1, huntDisplay.points.size()); 
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Assert that the landmark has been added to the landmarks Vector
        assertEquals(1, huntDisplay.landmarks.size()); 
    }

    @Test
    public void testGetPreferredSize() {
        Dimension preferredSize = huntDisplay.getPreferredSize();
        // Assert that the preferred size is not null
        assertNotNull(preferredSize);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension minimumSize = huntDisplay.getMinimumSize();
        // Assert that the minimum size is not null
        assertNotNull(minimumSize);
    }

}