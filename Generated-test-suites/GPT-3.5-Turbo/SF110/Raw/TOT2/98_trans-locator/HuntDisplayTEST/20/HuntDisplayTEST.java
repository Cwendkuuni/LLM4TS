import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals("Incorrect preferred size", new Dimension(200, 200), display.getPreferredSize());
    }
    
    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals("Incorrect minimum size", new Dimension(50, 50), display.getMinimumSize());
    }
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        display.addPoint(point);
        // Add assertion here
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandMark(landmark);
        // Add assertion here
    }

    // Add more test cases for edge cases and exception handling scenarios
    
}