import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        this.huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testGetPreferredSize() {
        Dimension expected = new Dimension(200, 200);
        Dimension actual = huntDisplay.getPreferredSize();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMinimumSize() {
        Dimension expected = new Dimension(50, 50);
        Dimension actual = huntDisplay.getMinimumSize();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, true);
        huntDisplay.addPoint(point);
        Vector<TrigPoint> expected = new Vector<>();
        expected.add(new TrigPoint(0.011111111111111112, 0.022222222222222223, 3.0, true));
        Vector<TrigPoint> actual = huntDisplay.points;
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> expected = new Vector<>();
        expected.add(new Landmark(0.011111111111111112, 0.022222222222222223, new ImageIcon()));
        Vector<Landmark> actual = huntDisplay.landmarks;
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testPaintComponent() {
        JPanel panel = new JPanel();
        Graphics g = panel.getGraphics();
        
        huntDisplay.addPoint(new TrigPoint(0.0, 0.0, 0.0, false));
        huntDisplay.addPoint(new TrigPoint(0.25, 0.25, 0.5, true));
        huntDisplay.addLandMark(new Landmark(0.2, 0.1, new ImageIcon()));
        
        huntDisplay.paintComponent(g);
        
        // No assertions as this method only draws graphics
        
    }
}