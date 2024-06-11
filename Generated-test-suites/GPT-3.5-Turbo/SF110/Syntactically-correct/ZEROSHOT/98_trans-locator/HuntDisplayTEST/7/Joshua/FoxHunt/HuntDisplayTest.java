package Joshua.FoxHunt;

import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @After
    public void tearDown() {
        huntDisplay = null;
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
        TrigPoint point = new TrigPoint(45, 90, 0, false);
        huntDisplay.addPoint(point);
        Vector expectedPoints = new Vector();
        expectedPoints.add(new TrigPoint(0.5, 0.5, 0, false));
        assertEquals(expectedPoints, huntDisplay.points);
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector expectedLandmarks = new Vector();
        expectedLandmarks.add(new Landmark(0.5, 0.5, new ImageIcon()));
        assertEquals(expectedLandmarks, huntDisplay.landmarks);
    }

    @Test
    public void testPaintComponent() {
        // Test the paintComponent method visually, cannot be automated
        // Manually verify the desired graphics behavior
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.getContentPane().add(huntDisplay);
        frame.setVisible(true);
    }

}