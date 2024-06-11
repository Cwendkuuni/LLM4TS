
import org.junit.*;
import java.util.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        
        display.addPoint(point);
        
        Vector expectedPoints = new Vector();
        expectedPoints.add(new TrigPoint(point.getY() / 90.0, point.getX() / 180.0, point.getTheta(), point.isFox()));
        
        Assert.assertEquals(expectedPoints, display.points);
    }
    
    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon("icon.png"));
        
        display.addLandMark(landmark);
        
        Vector expectedLandmarks = new Vector();
        expectedLandmarks.add(new Landmark(landmark.getLat() / 90.0, landmark.getLon() / 180.0, landmark.getIcon()));
        
        Assert.assertEquals(expectedLandmarks, display.landmarks);
    }
    
    // Add additional test cases for other methods if needed
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        // Mock data for points and landmarks
        TrigPoint[] points = new TrigPoint[2];
        points[0] = new TrigPoint(0.5, 0.5, 0, true);
        points[1] = new TrigPoint(0.6, 0.6, 0, false);
        
        Landmark[] landmarks = new Landmark[2];
        landmarks[0] = new Landmark(0.5, 0.5, new ImageIcon("icon1.png"));
        landmarks[1] = new Landmark(0.6, 0.6, new ImageIcon("icon2.png"));
        
        display.points = new Vector(Arrays.asList(points));
        display.landmarks = new Vector(Arrays.asList(landmarks));
        
        // Mock expected values
        Color foxColor = Color.GREEN;
        Color pointColor = Color.BLACK;
        
        display.paintComponent(g);
        
        // Verify the expected colors used for painting points and landmarks
        g.setColor(foxColor);
        int expectedXFox = (int)((points[0].getX() + 0.5 - (points[0].getX() + 0.5)) * 200);
        int expectedYFox = (int)((points[0].getY() + 0.5 - (points[0].getY() + 0.5)) * 200);
        Assert.assertTrue(g.getColor() == foxColor && g.fillRect(expectedXFox, expectedYFox, 5, 5));
        
        g.setColor(pointColor);
        int expectedXPoint = (int)((points[1].getX() + 0.5 - (points[0].getX() + 0.5)) * 200);
        int expectedYPoint = (int)((points[1].getY() + 0.5 - (points[0].getY() + 0.5)) * 200);
        Assert.assertTrue(g.getColor() == pointColor && g.fillRect(expectedXPoint, expectedYPoint, 5, 5));
        
      
    }
}
