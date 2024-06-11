import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint_typicalCase() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(30.0, 45.0, 0.5, false);
        display.addPoint(point);
        Vector points = display.getPoints();
        
        assertEquals(1, points.size());
        assertEquals(point, points.get(0));
    }

    @Test
    public void testAddPoint_edgeCase() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(80.0, 0.0, 1.0, true);
        display.addPoint(point);
        Vector points = display.getPoints();
        
        assertEquals(1, points.size());
        assertEquals(point, points.get(0));
    }

    @Test
    public void testAddPoint_errorCase() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = null;
        display.addPoint(point);
        Vector points = display.getPoints();
        
        assertEquals(0, points.size());
    }

    @Test
    public void testAddLandmark_typicalCase() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(10.0, 20.0, new ImageIcon());
        display.addLandmark(landmark);
        Vector landmarks = display.getLandmarks();
        
        assertEquals(1, landmarks.size());
        assertEquals(landmark, landmarks.get(0));
    }

    @Test
    public void testAddLandmark_edgeCase() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(-90.0, -180.0, new ImageIcon());
        display.addLandmark(landmark);
        Vector landmarks = display.getLandmarks();
        
        assertEquals(1, landmarks.size());
        assertEquals(landmark, landmarks.get(0));
    }

    @Test
    public void testAddLandmark_errorCase() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = null;
        display.addLandmark(landmark);
        Vector landmarks = display.getLandmarks();
        
        assertEquals(0, landmarks.size());
    }

    @Test
    public void testPaintComponent_typicalCase() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(30.0, 45.0, 0.5, false);
        TrigPoint point2 = new TrigPoint(-10.0, 10.0, 0.2, true);
        display.addPoint(point1);
        display.addPoint(point2);
        
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        display.paintComponent(g2d);
        
        // Add assertions to verify the correctness of the painted component
    }

    @Test
    public void testPaintComponent_edgeCase() {
        HuntDisplay display = new HuntDisplay();
        
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        display.paintComponent(g2d);
        
        // Add assertions to verify the correctness of the painted component
    }

    @Test
    public void testPaintComponent_errorCase() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(30.0, 45.0, 0.5, false);
        display.addPoint(point);
        
        BufferedImage image = null;
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        display.paintComponent(g2d);
        
        // Add assertions to handle the null pointer exception
    }
}