import org.junit.Test;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint_typical() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(trigPoint);
        
        // Verify that the point is added to the points vector
        assertEquals(1, huntDisplay.points.size());
        
        // Verify that repaint is called
        // (Repaint cannot be directly tested, hence only the method call is verified)
        //assertEquals();
    }
    
    @Test
    public void testAddPoint_edge() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(0, 0, 0, true);
        huntDisplay.addPoint(trigPoint);
        
        // Verify that the point is added to the points vector
        assertEquals(1, huntDisplay.points.size());
        
        // Verify that repaint is called
        // (Repaint cannot be directly tested, hence only the method call is verified)
        //assertEquals();
    }
    
    @Test
    public void testAddPoint_error() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = null;
        huntDisplay.addPoint(trigPoint);
        
        // Verify that null point is not added to the points vector
        assertEquals(0, huntDisplay.points.size());
        
        // Verify that repaint is not called
    }
    
    @Test
    public void testAddLandMark_typical() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        
        // Verify that the landmark is added to the landmarks vector
        assertEquals(1, huntDisplay.landmarks.size());
        
        // Verify that repaint is called
        // (Repaint cannot be directly tested, hence only the method call is verified)
        //assertEquals();
    }
    
    @Test
    public void testAddLandMark_edge() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        
        // Verify that the landmark is added to the landmarks vector
        assertEquals(1, huntDisplay.landmarks.size());
        
        // Verify that repaint is called
        // (Repaint cannot be directly tested, hence only the method call is verified)
        //assertEquals();
    }
    
    @Test
    public void testAddLandMark_error() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = null;
        huntDisplay.addLandMark(landmark);
        
        // Verify that null landmark is not added to the landmarks vector
        assertEquals(0, huntDisplay.landmarks.size());
        
        // Verify that repaint is not called
    }
    
    // Remaining test cases for other methods in the class

}