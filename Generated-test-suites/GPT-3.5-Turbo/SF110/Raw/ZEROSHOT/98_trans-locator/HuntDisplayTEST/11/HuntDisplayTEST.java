import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
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
        TrigPoint point = new TrigPoint(45, 90, 0, false);
        huntDisplay.addPoint(point);
        // Add assertions
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Add assertions
    }

    @Test
    public void testPaintComponent() {
        Graphics g = new JPanel().getGraphics();
        // Add assertions
    }
}