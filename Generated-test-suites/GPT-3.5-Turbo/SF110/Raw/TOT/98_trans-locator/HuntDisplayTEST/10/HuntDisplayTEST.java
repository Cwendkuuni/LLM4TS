import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    private static HuntDisplay huntDisplay;

    @BeforeClass
    public static void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testGetPreferredSize() {
        Dimension expected = new Dimension(200, 200);
        Dimension actual = huntDisplay.getPreferredSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension expected = new Dimension(50, 50);
        Dimension actual = huntDisplay.getMinimumSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90, 180, 0, false);
        huntDisplay.addPoint(point);
        // Check if the point has been added to the vector of points
        assertEquals(1, huntDisplay.points.size());
        // Check if the added point has the correct coordinates and properties
        TrigPoint addedPoint = huntDisplay.points.get(0);
        assertEquals(1.0, addedPoint.getY(), 0);
        assertEquals(1.0, addedPoint.getX(), 0);
        assertEquals(0, addedPoint.getTheta());
        assertFalse(addedPoint.isFox());
    }

    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Check if the landmark has been added to the vector of landmarks
        assertEquals(1, huntDisplay.landmarks.size());
        // Check if the added landmark has the correct coordinates and icon
        Landmark addedLandmark = huntDisplay.landmarks.get(0);
        assertEquals(1.0, addedLandmark.getLat(), 0);
        assertEquals(1.0, addedLandmark.getLon(), 0);
        assertNotNull(addedLandmark.getIcon());
    }

    @Test
    public void testPaintComponentWithNoPointsAndNoLandmarks() {
        // Simulate the case where there are no points and no landmarks
        huntDisplay.points = new Vector();
        huntDisplay.landmarks = new Vector();
        // Verify that the component is displayed correctly without any points or landmarks
        // The expected outcome is an invisible component (no visual elements)
        // Therefore, there is no specific assertion to be made
    }

    @Test
    public void testPaintComponentWithPoints() {
        // Simulate the case where the component has points but no landmarks
        huntDisplay.points = new Vector();
        huntDisplay.points.add(new TrigPoint(90, 180, 0, false));
        huntDisplay.points.add(new TrigPoint(0, 0, 0, true));

        // Verify that the points are displayed correctly
        // The expected outcome is the points being drawn on the component
        // Since the representation of points is not defined, we can't make specific assertions
    }

    @Test
    public void testPaintComponentWithLandmarks() {
        // Simulate the case where the component has landmarks but no points
        huntDisplay.landmarks = new Vector();
        huntDisplay.landmarks.add(new Landmark(90, 180, new ImageIcon()));
        huntDisplay.landmarks.add(new Landmark(0, 0, new ImageIcon()));

        // Verify that the landmarks are displayed correctly
        // The expected outcome is the landmarks being drawn on the component
        // Since the representation of landmarks is not defined, we can't make specific assertions
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        // Simulate the case where the component has both points and landmarks
        huntDisplay.points = new Vector();
        huntDisplay.points.add(new TrigPoint(90, 180, 0, false));
        huntDisplay.landmarks = new Vector();
        huntDisplay.landmarks.add(new Landmark(90, 180, new ImageIcon()));

        // Verify that the points and landmarks are displayed correctly
        // The expected outcome is the points and landmarks being drawn on the component
        // Since the representation of points and landmarks is not defined, we can't make specific assertions
    }

}