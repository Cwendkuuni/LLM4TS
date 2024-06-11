import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension prefSize = huntDisplay.getPreferredSize();
        assertEquals(new Dimension(200, 200), prefSize);
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension minSize = huntDisplay.getMinimumSize();
        assertEquals(new Dimension(50, 50), minSize);
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, false);
        huntDisplay.addPoint(point);
        Vector points = huntDisplay.points;
        assertEquals(1, points.size());
        assertEquals(point, points.elementAt(0));
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector landmarks = huntDisplay.landmarks;
        assertEquals(1, landmarks.size());
        assertEquals(landmark, landmarks.elementAt(0));
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics g = new JPanel().getGraphics();
        huntDisplay.paintComponent(g);
        // Assert paintComponent behavior
    }
}