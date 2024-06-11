import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.*;
import java.util.Vector;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90.0, 180.0, 0, true);
        huntDisplay.addPoint(point);

        Vector<TrigPoint> points = getField(huntDisplay, "points");

        assertEquals(1, points.size());
        assertEquals(point, points.get(0));
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(90.0, 180.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);

        Vector<Landmark> landmarks = getField(huntDisplay, "landmarks");

        assertEquals(1, landmarks.size());
        assertEquals(landmark, landmarks.get(0));
    }

    @Test
    public void testPaintComponentNoPointsAndLandmarks() {
        Graphics g = new DummyGraphics();
        huntDisplay.paintComponent(g);

        Color backgroundColor = getField(huntDisplay, "background");

        assertEquals(Color.WHITE, backgroundColor);
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        TrigPoint point1 = new TrigPoint(90.0, 180.0, 0, true);
        TrigPoint point2 = new TrigPoint(0.0, 0.0, 0, false);
        Landmark landmark1 = new Landmark(45.0, 90.0, new ImageIcon());
        Landmark landmark2 = new Landmark(0.0, 0.0, new ImageIcon());

        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);

        Graphics g = new DummyGraphics();
        huntDisplay.paintComponent(g);

        Vector<TrigPoint> points = getField(huntDisplay, "points");
        Vector<Landmark> landmarks = getField(huntDisplay, "landmarks");
        Color pointColor = getField(huntDisplay, "point");
        Color foxColor = getField(huntDisplay, "fox");

        assertEquals(Color.BLACK, pointColor);
        assertEquals(Color.GREEN, foxColor);
        assertEquals(2, points.size());
        assertEquals(2, landmarks.size());
    }

    private <T> T getField(Object object, String fieldName) {
        Field field;
        try {
            field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class DummyGraphics extends Graphics {
        // Override required methods for test purpose
    }
}