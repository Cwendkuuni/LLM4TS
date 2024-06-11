
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import java.util.Vector;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    
    // Test addPoint() method
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        huntDisplay.addPoint(point);
        // Check if the point is added to the points vector
        assertEquals(1, huntDisplay.points.size());
        // Check if the point coordinates are correctly converted and added to the points vector
        assertEquals(0.0, huntDisplay.points.get(0).getX(), 0.001);
        assertEquals(0.0, huntDisplay.points.get(0).getY(), 0.001);
        // Check if repaint is called after adding a point
        // Not feasible to test repaint() directly here
    }
    
    
    // Test addLandMark() method
    @Test
    public void testAddLandMark() {
        ImageIcon icon = new ImageIcon("icon.png");
        Landmark landmark = new Landmark(0, 0, icon);
        huntDisplay.addLandMark(landmark);
        // Check if the landmark is added to the landmarks vector
        assertEquals(1, huntDisplay.landmarks.size());
        // Check if the landmark coordinates are correctly converted and added to the landmarks vector
        assertEquals(0.0, huntDisplay.landmarks.get(0).getLon(), 0.001);
        assertEquals(0.0, huntDisplay.landmarks.get(0).getLat(), 0.001);
        // Check if repaint is called after adding a landmark
        // Not feasible to test repaint() directly here
    }
    
    
    // Test paintComponent() method
    @Test
    public void testPaintComponent() {
        // Create mock Graphics object
        Graphics g = new JComponent().getGraphics();
        
        // Set up test case with point and landmark
        TrigPoint point = new TrigPoint(0.5, 0.5, 0, false);
        Landmark landmark = new Landmark(0.25, 0.25, new ImageIcon("icon.png"));
        huntDisplay.addPoint(point);
        huntDisplay.addLandMark(landmark);
        
        // Call paintComponent() method
        huntDisplay.paintComponent(g);
        
        // Cannot test the graphical output directly, but can check internal calculations and function calls
        
        // Check if the average x and y coordinates are correctly calculated
        assertEquals(0.5, huntDisplay.trigPointAvgX, 0.001);
        assertEquals(0.5, huntDisplay.trigPointAvgY, 0.001);
        
        // Check if the colors are set correctly for point and fox
        assertEquals(Color.BLACK, g.getColor());
        assertEquals(Color.GREEN, g.getColor());
        
        // Check if the fillOval() method is called for the point
        // Not feasible to test actual oval drawing
        
        // Check if the drawImage() method is called for the landmark
        // Not feasible to test actual image drawing
    }
    
    
    // Test getPreferredSize() method
    @Test
    public void testGetPreferredSize() {
        Dimension prefSize = huntDisplay.getPreferredSize();
        // Check if the preferred size is returned correctly
        assertEquals(200, prefSize.width);
        assertEquals(200, prefSize.height);
    }
    
    
    // Test getMinimumSize() method
    @Test
    public void testGetMinimumSize() {
        Dimension minSize = huntDisplay.getMinimumSize();
        // Check if the minimum size is returned correctly
        assertEquals(50, minSize.width);
        assertEquals(50, minSize.height);
    }
    
}
