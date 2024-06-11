import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;

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
        TrigPoint point = new TrigPoint(1.0, 1.0, 45, true);
        display.addPoint(point);
        // Add assertion here to verify the point was added successfully
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 1.0, new ImageIcon());
        display.addLandMark(landmark);
        // Add assertion here to verify the landmark was added successfully
    }

    @Test
    public void testPaintComponent() {
        // This method is difficult to test directly due to its reliance on graphics
        // Consider testing the calculations involved in the method instead
    }
    
    // Add more test cases for edge cases and error scenarios here

}