import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(10, 20, 30, true);
        display.addPoint(point);
        
        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(new TrigPoint(point.getY() / 90.0, point.getX() / 180.0, point.getTheta(), point.isFox()));
        
        assertEquals(expectedPoints, display.points);
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(10, 20, new ImageIcon());
        display.addLandMark(landmark);
        
        Vector<Landmark> expectedLandmarks = new Vector<>();
        expectedLandmarks.add(new Landmark(landmark.getLat() / 90.0, landmark.getLon() / 180.0, landmark.getIcon()));
        
        assertEquals(expectedLandmarks, display.landmarks);
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics graphics = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        display.paintComponent(graphics);
        
        // No assertions as this method draws on graphics object
        
        // TODO: Add assertions to verify expected outcome of paintComponent()
    }
}