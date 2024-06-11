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
        TrigPoint point = new TrigPoint(60, 120, 45.0, true);
        display.addPoint(point);
        // Need to visually verify if point is added properly
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(30, 60, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        // Need to visually verify if landmark is added properly
    }
    
    // Edge cases and exceptions
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddPointWithInvalidIndex() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(150, 300, 90.0, false);
        display.addPoint(point);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddLandMarkWithNullIcon() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, null);
        display.addLandMark(landmark);
    }
    
    // Additional edge cases and exceptions can be added here
    
}