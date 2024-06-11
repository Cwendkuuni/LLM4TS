import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

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
        assertEquals(expected, huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        Dimension expected = new Dimension(50, 50);
        assertEquals(expected, huntDisplay.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90.0, 180.0, 45.0, true);
        huntDisplay.addPoint(point);
        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(point);
        assertEquals(expectedPoints, huntDisplay.points);
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(90.0, 180.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> expectedLandmarks = new Vector<>();
        expectedLandmarks.add(landmark);
        assertEquals(expectedLandmarks, huntDisplay.landmarks);
    }

    @Test
    public void testPaintComponent() {
        Graphics mockGraphics = new MockGraphics();
        huntDisplay.paintComponent(mockGraphics);
        // TODO: Verify expected behavior of paintComponent
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testAddPoint_multiplePoints() {
        TrigPoint point1 = new TrigPoint(90.0, 180.0, 45.0, true);
        TrigPoint point2 = new TrigPoint(0.0, 0.0, 0.0, false);
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(point1);
        expectedPoints.add(point2);
        assertEquals(expectedPoints, huntDisplay.points);
    }

    @Test
    public void testAddPoint_nullPoint() {
        TrigPoint point = null;
        huntDisplay.addPoint(point);
        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(null);
        assertEquals(expectedPoints, huntDisplay.points);
    }

    @Test
    public void testAddLandMark_multipleLandmarks() {
        Landmark landmark1 = new Landmark(90.0, 180.0, new ImageIcon());
        Landmark landmark2 = new Landmark(0.0, 0.0, new ImageIcon());
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        Vector<Landmark> expectedLandmarks = new Vector<>();
        expectedLandmarks.add(landmark1);
        expectedLandmarks.add(landmark2);
        assertEquals(expectedLandmarks, huntDisplay.landmarks);
    }

    @Test
    public void testAddLandMark_nullLandmark() {
        Landmark landmark = null;
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> expectedLandmarks = new Vector<>();
        expectedLandmarks.add(null);
        assertEquals(expectedLandmarks, huntDisplay.landmarks);
    }
}