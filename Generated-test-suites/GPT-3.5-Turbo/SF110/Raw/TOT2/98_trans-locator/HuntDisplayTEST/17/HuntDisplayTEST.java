import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(200, 200), display.getPreferredSize());
    }
    
    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(50, 50), display.getMinimumSize());
    }
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(30, 60, 45, true);
        display.addPoint(point);
        // Add assertion here
        
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(40, 80, new ImageIcon("image.png"));
        display.addLandMark(landmark);
        // Add assertion here
        
    }
    
    // Additional test cases for edge cases and error scenarios can be added here

}