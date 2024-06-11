package Joshua.FoxHunt;

import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1, 2, 3, true);
        
        display.addPoint(point);
        
        // Assert that the point is added to the display
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.elementAt(0));
        
        // Assert that the display is repainted
        // ... (add assertion here)
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1, 2, new ImageIcon());
        
        display.addLandMark(landmark);
        
        // Assert that the landmark is added to the display
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.elementAt(0));
        
        // Assert that the display is repainted
        // ... (add assertion here)
    }
    
    @Test
    public void testPaintComponent() {
        // Create test data
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(0.5, 0.5, 0, true);
        TrigPoint point2 = new TrigPoint(0.6, 0.6, 0, false);
        display.addPoint(point1);
        display.addPoint(point2);
        
        Landmark landmark1 = new Landmark(0.5, 0.5, new ImageIcon());
        Landmark landmark2 = new Landmark(0.6, 0.6, new ImageIcon());
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        
        // Call paintComponent
        // ... (add code here to call paintComponent and capture the graphics)
        
        // Assert that the graphics are correctly updated
        // ... (add assertion here)
    }
    
}