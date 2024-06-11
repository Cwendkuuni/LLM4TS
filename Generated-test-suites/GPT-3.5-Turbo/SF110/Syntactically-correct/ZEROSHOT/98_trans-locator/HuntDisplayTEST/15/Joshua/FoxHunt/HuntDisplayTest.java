import org.junit.Test;
import org.junit.Before;

import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(0.5, 0.5, 45, true);
        huntDisplay.addPoint(point);
        
        //Assert statements
        // add your assert statements here
    }
    
    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon("icon.png"));
        huntDisplay.addLandMark(landmark);
        
        //Assert statements
        // add your assert statements here
    }
    
    //Write more test methods for other public methods in the HuntDisplay class
    
}