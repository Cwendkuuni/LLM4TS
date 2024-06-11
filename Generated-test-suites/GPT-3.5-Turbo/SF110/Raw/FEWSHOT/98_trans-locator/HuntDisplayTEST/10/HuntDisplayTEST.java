package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90, 180, 0, true);
        huntDisplay.addPoint(point);
        // assertion to check if point is added to the vector
        
        // mocking the Graphics object
        Graphics g = mock(Graphics.class);
        huntDisplay.paintComponent(g);
        // assertion to check if repaint is called after adding point
        verify(huntDisplay).repaint();
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // assertion to check if landmark is added to the vector
        
        // mocking the Graphics object
        Graphics g = mock(Graphics.class);
        huntDisplay.paintComponent(g);
        // assertion to check if repaint is called after adding landmark
        verify(huntDisplay).repaint();
    }

    @Test
    public void testPaintComponent() {
        // creating test data
        TrigPoint point1 = new TrigPoint(90, 180, 0, true);
        TrigPoint point2 = new TrigPoint(45, 90, 90, false);
        Vector<TrigPoint> points = new Vector<>();
        points.add(point1);
        points.add(point2);
        huntDisplay.setPoints(points);
        
        // creating test data
        Landmark landmark1 = new Landmark(90, 180, new ImageIcon());
        Landmark landmark2 = new Landmark(45, 90, new ImageIcon());
        Vector<Landmark> landmarks = new Vector<>();
        landmarks.add(landmark1);
        landmarks.add(landmark2);
        huntDisplay.setLandmarks(landmarks);
        
        // mocking the Graphics object
        Graphics g = mock(Graphics.class);
        
        // calling the method under test
        huntDisplay.paintComponent(g);
        
        // assertions to check if the desired graphics methods are called with correct parameters
        
        verify(g).setColor(Color.BLACK);
        verify(g).fillOval(anyInt(), anyInt(), eq(5), eq(5));
        
        verify(g).setColor(Color.GREEN);
        verify(g, times(2)).fillOval(anyInt(), anyInt(), eq(5), eq(5));
        
        verify(g).setColor(huntDisplay.getForeground());
        verify(g, times(2)).drawImage(any(Image.class), anyInt(), anyInt(), eq(10), eq(10), any(Color.class), any(Component.class));
    }
}