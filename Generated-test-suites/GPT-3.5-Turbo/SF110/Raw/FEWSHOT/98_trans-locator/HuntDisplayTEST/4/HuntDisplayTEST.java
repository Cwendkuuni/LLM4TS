package Joshua.FoxHunt;

import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        assertEquals(expectedSize, huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        Dimension expectedSize = new Dimension(50, 50);
        assertEquals(expectedSize, huntDisplay.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90.0, 180.0, 0.0, true);
        huntDisplay.addPoint(point);
        // Assert something about huntDisplay.points and its size
        // Assert something about repaint method of huntDisplay
    }

    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(90.0, 180.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Assert something about huntDisplay.landmarks and its size
        // Assert something about repaint method of huntDisplay
    }

    @Test
    public void testPaintComponent() {
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        huntDisplay.paintComponent(g);
        // Assert something about g and its operations
    }
}