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
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        // Add assertion here
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Add assertion here
    }

    @Test
    public void testPaintComponent() {
        // Create a graphics object
        Graphics g = new JPanel().getGraphics();
        // Add assertion here
    }
}