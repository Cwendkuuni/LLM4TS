import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90.0, 180.0, 45.0, true);
        huntDisplay.addPoint(point);
        assertEquals(huntDisplay.points.elementAt(0), point);
    }
    
    @Test
    public void testAddLandMark() {
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(90.0, 180.0, icon);
        huntDisplay.addLandMark(landmark);
        assertEquals(huntDisplay.landmarks.elementAt(0), landmark);
    }
    
    @Test
    public void testPaintComponent() {
        Graphics g = new MyGraphics();
        huntDisplay.paintComponent(g);
        // Assert statements for validating the behavior of paintComponent method
    }
}

class MyGraphics extends Graphics {
    // Implement other abstract methods of Graphics class
}