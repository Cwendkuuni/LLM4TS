import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import org.junit.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        // Test Case 1
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1, 1, 1, false);
        
        display.addPoint(point);
        
        // Assert the point has been added correctly
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.get(0));
    }

    @Test
    public void testAddLandMark() {
        // Test Case 2
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1, 1, new ImageIcon());
        
        display.addLandMark(landmark);
        
        // Assert the landmark has been added correctly
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.get(0));
    }

    @Test
    public void testPaintComponent() {
        // Test Case 3
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        display.paintComponent(g);
        
        // Assertion for the expected result
    }

    @Test
    public void testAddMultiplePoints() {
        // Test Case 4
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1, 1, 1, false);
        TrigPoint point2 = new TrigPoint(2, 2, 2, true);
        
        display.addPoint(point1);
        display.addPoint(point2);
        
        // Assert both points have been added correctly
        assertEquals(2, display.points.size());
        assertEquals(point1, display.points.get(0));
        assertEquals(point2, display.points.get(1));
    }

    @Test
    public void testAddPointsWithMaxAndMinValues() {
        // Test Case 5
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(90, 180, 1, false);
        TrigPoint point2 = new TrigPoint(-90, -180, 2, true);
        
        display.addPoint(point1);
        display.addPoint(point2);
        
        // Assert both points have been added correctly
        assertEquals(2, display.points.size());
        assertEquals(point1, display.points.get(0));
        assertEquals(point2, display.points.get(1));
    }

    @Test
    public void testAddMultipleLandMarks() {
        // Test Case 6
        HuntDisplay display = new HuntDisplay();
        Landmark landmark1 = new Landmark(1, 1, new ImageIcon());
        Landmark landmark2 = new Landmark(2, 2, new ImageIcon());
        
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        
        // Assert both landmarks have been added correctly
        assertEquals(2, display.landmarks.size());
        assertEquals(landmark1, display.landmarks.get(0));
        assertEquals(landmark2, display.landmarks.get(1));
    }

    @Test
    public void testPaintComponentNoTrigpointsAndLandmarks() {
        // Test Case 7
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        
        display.paintComponent(g);
        
        // Assertion for the expected result
    }

    @Test
    public void testPaintComponentOneTrigpointNoLandmarks() {
        // Test Case 8
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        TrigPoint point = new TrigPoint(1, 1, 1, false);
        display.addPoint(point);
        
        display.paintComponent(g);
        
        // Assertion for the expected result
    }

    @Test
    public void testPaintComponentNoTrigpointsOneLandmark() {
        // Test Case 9
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Landmark landmark = new Landmark(1, 1, new ImageIcon());
        display.addLandMark(landmark);
        
        display.paintComponent(g);
        
        // Assertion for the expected result
    }

    @Test(expected = NullPointerException.class)
    public void testAddPointNull() {
        // Test Case 10
        HuntDisplay display = new HuntDisplay();
        
        display.addPoint(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddLandMarkNull() {
        // Test Case 11
        HuntDisplay display = new HuntDisplay();
        
        display.addLandMark(null);
    }

}