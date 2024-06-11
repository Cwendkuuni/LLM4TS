import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, 0, false);
        huntDisplay.addPoint(point);
        Vector<TrigPoint> expected = new Vector<>();
        expected.add(new TrigPoint(point.getY() / 90.0, point.getX() / 180.0, point.getTheta(), point.isFox()));
        assertEquals(expected, huntDisplay.points);
    }
    
    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> expected = new Vector<>();
        expected.add(new Landmark(landmark.getLat() / 90.0, landmark.getLon() / 180.0, landmark.getIcon()));
        assertEquals(expected, huntDisplay.landmarks);
    }
    
    @Test
    public void testPaintComponent() {
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        huntDisplay.paintComponent(g);
        // Check that the paintComponent method does not throw an exception
    }
}