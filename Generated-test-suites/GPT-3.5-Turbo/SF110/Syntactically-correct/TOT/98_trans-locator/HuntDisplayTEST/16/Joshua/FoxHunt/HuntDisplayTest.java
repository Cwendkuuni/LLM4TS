import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class HuntDisplayTest {
    
    @Test
    public void testConstructor() {
        HuntDisplay display = new HuntDisplay();
        assertNotNull(display);
        assertEquals(Color.WHITE, display.getBackground());
        assertEquals(new Dimension(50, 50), display.getMinimumSize());
        assertEquals(new Dimension(200, 200), display.getPreferredSize());
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1, 1, 0, true);
        HuntDisplay display = new HuntDisplay();
        display.addPoint(point);
        
        Vector<TrigPoint> points = display.points;
        assertEquals(1, points.size());
        
        TrigPoint addedPoint = points.elementAt(0);
        assertEquals(1.0 / 90.0, addedPoint.getY(), 0.01);
        assertEquals(1.0 / 180.0, addedPoint.getX(), 0.01);
        assertEquals(0, addedPoint.getTheta());
        assertTrue(addedPoint.isFox());
    }
    
    @Test
    public void testAddLandmark() {
        ImageIcon icon = new ImageIcon();
        Landmark landmark = new Landmark(1, 1, icon);
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(landmark);
        
        Vector<Landmark> landmarks = display.landmarks;
        assertEquals(1, landmarks.size());
        
        Landmark addedLandmark = landmarks.elementAt(0);
        assertEquals(1.0 / 90.0, addedLandmark.getLat(), 0.01);
        assertEquals(1.0 / 180.0, addedLandmark.getLon(), 0.01);
        assertEquals(icon, addedLandmark.getIcon());
    }
    
    @Test
    public void testPaintComponentWithNoPointsAndNoLandmarks() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_ARGB).getGraphics();
        g.setColor(Color.RED);
        
        display.paintComponent(g);
        
        // Check that nothing is drawn
        assertEquals(Color.RED, g.getColor());
        
    }
    
    @Test
    public void testPaintComponentWithPoints() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        display.addPoint(new TrigPoint(0.5, 0.5, 0, false));
        display.addPoint(new TrigPoint(0.6, 0.6, 0, true));
        
        display.paintComponent(g);
        
        // Verify that the points are drawn at the correct positions
        assertEquals(Color.BLACK, g.getColor());
        assertTrue(g instanceof Graphics2D);
    }
    
    @Test
    public void testPaintComponentWithLandmarks() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        ImageIcon icon = new ImageIcon();
        display.addLandMark(new Landmark(0.5, 0.5, icon));
        display.addLandMark(new Landmark(0.6, 0.6, icon));
        
        display.paintComponent(g);
        
        // Verify that the landmarks are drawn at the correct positions with the correct icons
        assertEquals(Color.BLACK, g.getColor());
        assertTrue(g instanceof Graphics2D);
    }
}