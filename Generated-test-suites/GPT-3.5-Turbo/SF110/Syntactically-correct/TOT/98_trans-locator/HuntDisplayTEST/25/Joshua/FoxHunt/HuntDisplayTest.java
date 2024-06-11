import org.junit.*;
import static org.junit.Assert.*;
import org.junit.rules.ExpectedException;
import java.awt.*;
import javax.swing.*;

public class HuntDisplayTest {

    private HuntDisplay display;

    @Before
    public void setUp() {
        display = new HuntDisplay();
    }

    @Test
    public void testAddPoint_typicalCase() {
        TrigPoint point = new TrigPoint(60, 120, 45, false);
        display.addPoint(point);
        assertEquals(1, display.points.size());
        assertEquals(point.getY() / 90.0, display.points.get(0).getY());
        assertEquals(point.getX() / 180.0, display.points.get(0).getX());
        assertEquals(point.getTheta(), display.points.get(0).getTheta());
        assertEquals(point.isFox(), display.points.get(0).isFox());
    }

    @Test
    public void testAddPoint_zeroCoordinates() {
        TrigPoint point = new TrigPoint(0, 0, 0, true);
        display.addPoint(point);
        assertEquals(1, display.points.size());
        assertEquals(0.0, display.points.get(0).getY(), 0.001);
        assertEquals(0.0, display.points.get(0).getX(), 0.001);
        assertEquals(0, display.points.get(0).getTheta());
        assertEquals(true, display.points.get(0).isFox());
    }

    @Test
    public void testAddLandmark_typicalCase() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        display.addLandmark(landmark);
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark.getLat() / 90.0, display.landmarks.get(0).getLat());
        assertEquals(landmark.getLon() / 180.0, display.landmarks.get(0).getLon());
        assertEquals(landmark.getIcon(), display.landmarks.get(0).getIcon());
    }

    @Test
    public void testAddLandmark_zeroCoordinates() {
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandmark(landmark);
        assertEquals(1, display.landmarks.size());
        assertEquals(0.0, display.landmarks.get(0).getLat(), 0.001);
        assertEquals(0.0, display.landmarks.get(0).getLon(), 0.001);
        assertEquals(landmark.getIcon(), display.landmarks.get(0).getIcon());
    }

    @Test
    public void testPaintComponent_noPoints() {
        Graphics g = new JPanel().getGraphics();
        display.paintComponent(g);
    }

    @Test
    public void testPaintComponent_withPoints() {
        TrigPoint point = new TrigPoint(45, 90, 60, false);
        display.addPoint(point);
        Graphics g = new JPanel().getGraphics();
        display.paintComponent(g);
    }

    @Test
    public void testPaintComponent_withLandmarks() {
        Landmark landmark = new Landmark(30, 60, new ImageIcon());
        display.addLandmark(landmark);
        Graphics g = new JPanel().getGraphics();
        display.paintComponent(g);
    }

}