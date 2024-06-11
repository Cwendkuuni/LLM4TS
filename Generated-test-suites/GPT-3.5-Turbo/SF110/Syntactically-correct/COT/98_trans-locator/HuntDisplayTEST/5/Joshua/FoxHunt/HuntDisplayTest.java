
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        display.addPoint(point);
        // Add assertion here
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandMark(landmark);
        // Add assertion here
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new JPanel().getGraphics();
        display.paintComponent(g);
        // Add assertion here
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testAddPointWithNullTrigPoint() {
        HuntDisplay display = new HuntDisplay();
        display.addPoint(null);
        // Add assertion here
    }

    @Test
    public void testAddLandMarkWithNullLandmark() {
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(null);
        // Add assertion here
    }

    @Test
    public void testPaintComponentWithNullGraphics() {
        HuntDisplay display = new HuntDisplay();
        display.paintComponent(null);
        // Add assertion here
    }

    // Test for getPreferredSize()

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expectedSize = new Dimension(200, 200);
        Dimension actualSize = display.getPreferredSize();
        assertEquals(expectedSize, actualSize);
    }

    // Test for getMinimumSize()

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expectedSize = new Dimension(50, 50);
        Dimension actualSize = display.getMinimumSize();
        assertEquals(expectedSize, actualSize);
    }

}