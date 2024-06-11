
package Joshua.FoxHunt;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static org.junit.Assert.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
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
        TrigPoint point = new TrigPoint(90, 180, 0, true);
        huntDisplay.addPoint(point);

        Vector<TrigPoint> points = (Vector<TrigPoint>) huntDisplay.points;
        assertEquals(point, points.get(0));
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        huntDisplay.addLandMark(landmark);

        Vector<Landmark> landmarks = (Vector<Landmark>) huntDisplay.landmarks;
        assertEquals(landmark, landmarks.get(0));
    }

    @Test
    public void testPaintComponent() {
        Graphics g = new JPanel().getGraphics();
        huntDisplay.paintComponent(g);

        // Test will pass if there are no exceptions
        assertTrue(true);
    }
}
