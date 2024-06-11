import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expectedSize = new Dimension(200, 200);
        Dimension actualSize = display.getPreferredSize();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expectedSize = new Dimension(50, 50);
        Dimension actualSize = display.getMinimumSize();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        display.addPoint(point);

        assertTrue(display.points.contains(point));
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon("image.jpg"));
        display.addLandMark(landmark);

        assertTrue(display.landmarks.contains(landmark));
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new JPanel().getGraphics();
        display.paintComponent(g);

        // Assert paint logic here, e.g. assert colors of points and landmarks are correct
    }

}