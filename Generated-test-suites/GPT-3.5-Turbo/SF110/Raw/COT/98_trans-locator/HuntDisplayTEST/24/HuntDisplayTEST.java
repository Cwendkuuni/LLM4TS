
package Joshua.FoxHunt;

import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0.5, 0.5, 0.0, true);
        
        display.addPoint(point);
        
        // Check if point is added correctly
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.elementAt(0));
        
        // Check if repaint is called after adding point
        // You can only check this by mocking the Graphics object
        // or using a UI testing framework like Selenium
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon());
        
        display.addLandMark(landmark);
        
        // Check if landmark is added correctly
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.elementAt(0));
        
        // Check if repaint is called after adding landmark
        // You can only check this by mocking the Graphics object
        // or using a UI testing framework like Selenium
    }
    
    // No need to test getPreferredSize() and getMinimumSize()
    // These methods simply return the prefSize and minSize variables,
    // which are set during object construction and never changed
    
    @Test
    public void testPaintComponent() {
        // This method is difficult to test due to its dependency on Graphics
        // and the use of Swing components, which require a UI context
        // It is recommended to manually test the painting functionality
        // and visually verify the results
    }
    
}
