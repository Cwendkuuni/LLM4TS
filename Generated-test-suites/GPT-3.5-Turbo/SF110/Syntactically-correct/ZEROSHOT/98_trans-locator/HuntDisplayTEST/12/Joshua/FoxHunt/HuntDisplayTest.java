import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import static org.junit.Assert.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setup() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        // Create TrigPoint object
        TrigPoint point = new TrigPoint(30, 60, 45, true);

        // Add TrigPoint to HuntDisplay
        huntDisplay.addPoint(point);

        // Get vector of points in HuntDisplay
        Vector points = huntDisplay.points;

        // Check if the added point is present in the vector
        assertTrue(points.contains(point));
    }

    @Test
    public void testAddLandMark() {
        // Create Landmark object
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(30, 60, icon);

        // Add Landmark to HuntDisplay
        huntDisplay.addLandMark(landmark);

        // Get vector of landmarks in HuntDisplay
        Vector landmarks = huntDisplay.landmarks;

        // Check if the added landmark is present in the vector
        assertTrue(landmarks.contains(landmark));
    }

    @Test
    public void testGetPreferredSize() {
        // Set preferred size for HuntDisplay
        Dimension preferredSize = new Dimension(200, 200);
        huntDisplay.prefSize = preferredSize;

        // Get the preferred size of the HuntDisplay object
        Dimension returnedSize = huntDisplay.getPreferredSize();

        // Check if the returned size matches the set preferred size
        assertEquals(preferredSize, returnedSize);
    }

    @Test
    public void testGetMinimumSize() {
        // Set minimum size for HuntDisplay
        Dimension minimumSize = new Dimension(50, 50);
        huntDisplay.minSize = minimumSize;

        // Get the minimum size of the HuntDisplay object
        Dimension returnedSize = huntDisplay.getMinimumSize();

        // Check if the returned size matches the set minimum size
        assertEquals(minimumSize, returnedSize);
    }

    @Test
    public void testPaintComponent() {
        // Create graphics object and buffered image for testing
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();

        // Call the paintComponent method with the graphics object
        huntDisplay.paintComponent(g);

        // No assertions as this method only performs drawing operations
        // and cannot be directly tested
    }
}