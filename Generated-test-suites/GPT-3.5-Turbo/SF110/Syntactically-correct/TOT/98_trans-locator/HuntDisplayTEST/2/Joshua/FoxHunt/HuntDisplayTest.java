import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class HuntDisplayTest {

    private HuntDisplay display;
    private TrigPoint point;
    private Landmark landmark;
    private Graphics g;

    @Before
    public void setUp() {
        display = new HuntDisplay();
        point = new TrigPoint(30, 45, 60, true);
        landmark = new Landmark(30, 45, new ImageIcon("icon.png"));
        g = new Graphics();
    }

    @Test
    public void testAddPoint() {
        display.addPoint(point);
        assertEquals(1, display.points.size());
        // Test if the point coordinates are transformed correctly
        assertEquals(0.25, display.points.elementAt(0).getX(), 0.001);
        assertEquals(0.333, display.points.elementAt(0).getY(), 0.001);
        // Test if the repaint method is called
        assertTrue(display.needsRepaint);
    }

    @Test
    public void testAddLandMark() {
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
        // Test if the landmark coordinates are transformed correctly
        assertEquals(0.25, display.landmarks.elementAt(0).getLon(), 0.001);
        assertEquals(0.333, display.landmarks.elementAt(0).getLat(), 0.001);
        // Test if the repaint method is called
        assertTrue(display.needsRepaint);
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        TrigPoint point1 = new TrigPoint(30, 45, 60, false);
        TrigPoint point2 = new TrigPoint(60, 90, 120, true);
        Landmark landmark1 = new Landmark(30, 45, new ImageIcon("landmark1.png"));
        Landmark landmark2 = new Landmark(60, 90, new ImageIcon("landmark2.png"));
        display.addPoint(point1);
        display.addPoint(point2);
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);

        // Test if the correct colors are set for points and foxes
        assertEquals(Color.BLACK, display.getPointColor(point1));
        assertEquals(Color.GREEN, display.getPointColor(point2));
        // Test if the correct coordinates are calculated for points and landmarks
        assertEquals(0.25, display.calculateXCoordinate(point1), 0.001);
        assertEquals(0.333, display.calculateYCoordinate(point1), 0.001);
        assertEquals(0.5, display.calculateXCoordinate(point2), 0.001);
        assertEquals(0.666, display.calculateYCoordinate(point2), 0.001);
        assertEquals(0.25, display.calculateXCoordinate(landmark1), 0.001);
        assertEquals(0.333, display.calculateYCoordinate(landmark1), 0.001);
        assertEquals(0.5, display.calculateXCoordinate(landmark2), 0.001);
        assertEquals(0.666, display.calculateYCoordinate(landmark2), 0.001);
        // Test if the correct image is drawn for landmarks
        assertEquals(landmark1.getIcon().getImage(), display.drawLandmark(landmark1));
        assertEquals(landmark2.getIcon().getImage(), display.drawLandmark(landmark2));
    }
    
    // Additional test cases for getPreferredSize and getMinimumSize can be added as needed

}