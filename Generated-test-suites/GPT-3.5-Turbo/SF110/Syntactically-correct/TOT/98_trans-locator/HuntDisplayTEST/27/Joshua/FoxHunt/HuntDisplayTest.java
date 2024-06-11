import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    private HuntDisplay display;

    @Before
    public void setUp() {
        display = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, 0, false);
        display.addPoint(point);
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.elementAt(0));
    }

    @Test
    public void testAddPointWithFox() {
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        display.addPoint(point);
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.elementAt(0));
    }

    @Test
    public void testAddNegativePoint() {
        TrigPoint point = new TrigPoint(-45, -90, 0, false);
        display.addPoint(point);
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.elementAt(0));
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.elementAt(0));
    }

    @Test
    public void testAddNegativeLandMark() {
        Landmark landmark = new Landmark(-45, -90, new ImageIcon());
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.elementAt(0));
    }

    @Test
    public void testPaintComponentWithNoPointsAndLandmarks() {
        assertEquals(0, display.points.size());
        assertEquals(0, display.landmarks.size());
        display.paintComponent(null); // Mocking Graphics object
        // TODO: Assert statements for expected output
    }

    @Test
    public void testPaintComponentWithPointsOnly() {
        display.addPoint(new TrigPoint(45, 90, 0, false));
        display.addPoint(new TrigPoint(-45, -90, 0, true));
        display.addPoint(new TrigPoint(0, 0, 0, false));
        assertEquals(3, display.points.size());
        assertEquals(0, display.landmarks.size());
        display.paintComponent(null); // Mocking Graphics object
        // TODO: Assert statements for expected output
    }

    @Test
    public void testPaintComponentWithLandmarksOnly() {
        display.addLandMark(new Landmark(45, 90, new ImageIcon()));
        display.addLandMark(new Landmark(-45, -90, new ImageIcon()));
        display.addLandMark(new Landmark(0, 0, new ImageIcon()));
        assertEquals(0, display.points.size());
        assertEquals(3, display.landmarks.size());
        display.paintComponent(null); // Mocking Graphics object
        // TODO: Assert statements for expected output
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        display.addPoint(new TrigPoint(45, 90, 0, false));
        display.addPoint(new TrigPoint(-45, -90, 0, true));
        display.addPoint(new TrigPoint(0, 0, 0, false));
        display.addLandMark(new Landmark(45, 90, new ImageIcon()));
        display.addLandMark(new Landmark(-45, -90, new ImageIcon()));
        display.addLandMark(new Landmark(0, 0, new ImageIcon()));
        assertEquals(3, display.points.size());
        assertEquals(3, display.landmarks.size());
        display.paintComponent(null); // Mocking Graphics object
        // TODO: Assert statements for expected output
    }

}