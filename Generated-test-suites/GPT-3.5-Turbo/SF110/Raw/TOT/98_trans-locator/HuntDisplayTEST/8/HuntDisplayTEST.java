import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

import static org.junit.Assert.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testGetPreferredSize() {
        Dimension expectedSize = new Dimension(200, 200);
        Dimension actualSize = huntDisplay.getPreferredSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension expectedSize = new Dimension(50, 50);
        Dimension actualSize = huntDisplay.getMinimumSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, Math.PI, true);
        int initialPointsSize = huntDisplay.points.size();
        huntDisplay.addPoint(point);
        int updatedPointsSize = huntDisplay.points.size();
        assertEquals(initialPointsSize + 1, updatedPointsSize);
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(60, 120, new ImageIcon());
        int initialLandmarksSize = huntDisplay.landmarks.size();
        huntDisplay.addLandMark(landmark);
        int updatedLandmarksSize = huntDisplay.landmarks.size();
        assertEquals(initialLandmarksSize + 1, updatedLandmarksSize);
    }

    @Test
    public void testPaintComponentWithPointsOnly() {
        Graphics g = new JPanel().getGraphics();
        huntDisplay.points.add(new TrigPoint(45, 90, Math.PI, true));
        huntDisplay.points.add(new TrigPoint(60, 120, Math.PI/2, false));
        huntDisplay.paintComponent(g);
        // TODO: verify the expected visual representation of the points
    }

    @Test
    public void testPaintComponentWithLandmarksOnly() {
        Graphics g = new JPanel().getGraphics();
        huntDisplay.landmarks.add(new Landmark(45, 90, new ImageIcon()));
        huntDisplay.landmarks.add(new Landmark(60, 120, new ImageIcon()));
        huntDisplay.paintComponent(g);
        // TODO: verify the expected visual representation of the landmarks
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        Graphics g = new JPanel().getGraphics();
        huntDisplay.points.add(new TrigPoint(45, 90, Math.PI, true));
        huntDisplay.points.add(new TrigPoint(60, 120, Math.PI/2, false));
        huntDisplay.landmarks.add(new Landmark(45, 90, new ImageIcon()));
        huntDisplay.landmarks.add(new Landmark(60, 120, new ImageIcon()));
        huntDisplay.paintComponent(g);
        // TODO: verify the expected visual representation of both points and landmarks
    }
}