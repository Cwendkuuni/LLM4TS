import org.junit.*;
import static org.junit.Assert.*;
import java.awt.Color;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testGetPreferredSize() {
        Dimension expected = new Dimension(200, 200);
        Dimension result = huntDisplay.getPreferredSize();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension expected = new Dimension(50, 50);
        Dimension result = huntDisplay.getMinimumSize();
        assertEquals(expected, result);
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90, 180, 0, false);
        huntDisplay.addPoint(point);
        Vector<TrigPoint> points = huntDisplay.points;
        assertTrue(points.contains(point));
    }

    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> landmarks = huntDisplay.landmarks;
        assertTrue(landmarks.contains(landmark));
    }

    @Test
    public void testPaintComponent() {
        Graphics g = new Graphics();
        huntDisplay.paintComponent(g);
        // Additional assertions can be added based on specific requirements
    }
}