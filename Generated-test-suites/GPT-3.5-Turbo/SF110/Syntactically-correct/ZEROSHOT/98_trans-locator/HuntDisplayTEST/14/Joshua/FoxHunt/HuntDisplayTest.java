import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expectedSize = new Dimension(200, 200);
        assertEquals(expectedSize, display.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expectedSize = new Dimension(50, 50);
        assertEquals(expectedSize, display.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(50, 50, 0, true);
        display.addPoint(point);

        Vector expectedPoints = new Vector();
        expectedPoints.add(new TrigPoint(0.5555555555555556, 0.2777777777777778, 0, true));
        assertEquals(expectedPoints, display.points);
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(50, 50, new ImageIcon());
        display.addLandMark(landmark);

        Vector expectedLandmarks = new Vector();
        expectedLandmarks.add(new Landmark(0.5555555555555556, 0.2777777777777778, new ImageIcon()));
        assertEquals(expectedLandmarks, display.landmarks);
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        g.setColor(Color.BLACK);
        g.fillOval(100, 100, 5, 5);

        display.points.add(new TrigPoint(0.5555555555555556, 0.2777777777777778, 0, false));
        display.paintComponent(g);
    }
}