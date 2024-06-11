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
        TrigPoint point = new TrigPoint(1.0, 1.0, 0.0, true);
        display.addPoint(point);
        // Add more assertions if needed
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 1.0, new ImageIcon());
        display.addLandMark(landmark);
        // Add more assertions if needed
    }

    // Add more test cases for other methods if needed

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        // Create test scenarios for painting component
        // Add assertions based on the expected behavior
    }

    // Additional test cases for edge cases, error scenarios, etc.

}