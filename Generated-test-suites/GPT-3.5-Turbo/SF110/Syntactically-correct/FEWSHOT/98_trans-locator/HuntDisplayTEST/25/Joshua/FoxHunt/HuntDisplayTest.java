package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {
    private HuntDisplay display;
    
    @Before
    public void setUp() {
        display = new HuntDisplay();
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(display.points);
        assertNotNull(display.landmarks);
        assertNotNull(display.minSize);
        assertNotNull(display.prefSize);
        assertEquals(display.getBackground(), Color.WHITE);
        assertEquals(display.minSize, new Dimension(50, 50));
        assertEquals(display.prefSize, new Dimension(200, 200));
    }
    
    @Test
    public void testGetPreferredSize() {
        assertEquals(display.getPreferredSize(), new Dimension(200, 200));
    }
    
    @Test
    public void testGetMinimumSize() {
        assertEquals(display.getMinimumSize(), new Dimension(50, 50));
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90, 180, 0, false);
        display.addPoint(point);
        assertEquals(display.points.size(), 1);
        assertEquals(display.points.elementAt(0), point);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.add(display);
            frame.pack();
            frame.setVisible(true);
        });
        // Verify that the point is painted on the display
    }
    
    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        display.addLandMark(landmark);
        assertEquals(display.landmarks.size(), 1);
        assertEquals(display.landmarks.elementAt(0), landmark);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.add(display);
            frame.pack();
            frame.setVisible(true);
        });
        // Verify that the landmark is painted on the display
    }
    
    @Test
    public void testPaintComponent() {
        TrigPoint point1 = new TrigPoint(90, 180, 0, false);
        TrigPoint point2 = new TrigPoint(-90, 0, 90, true);
        display.addPoint(point1);
        display.addPoint(point2);
        Landmark landmark1 = new Landmark(90, 180, new ImageIcon());
        Landmark landmark2 = new Landmark(-90, 0, new ImageIcon());
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.add(display);
            frame.pack();
            frame.setVisible(true);
        });
        // Verify that all the points and landmarks are correctly painted on the display
    }
}