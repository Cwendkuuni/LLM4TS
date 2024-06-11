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
        TrigPoint point = new TrigPoint(90.0, 180.0, 45.0, false);
        display.addPoint(point);
        //TODO: Add assertions to check if point was added correctly
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(90.0, 180.0, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        //TODO: Add assertions to check if landmark was added correctly
    }
    
    @Test
    public void testPaintComponent() {
        //TODO: Test the paintComponent method with different scenarios including edge cases
    }
}