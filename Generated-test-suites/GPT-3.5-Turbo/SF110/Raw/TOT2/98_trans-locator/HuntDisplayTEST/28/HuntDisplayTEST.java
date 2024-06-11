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
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        display.addPoint(point);
        // Add assertion here once the functionality of addPoint is clarified
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        // Add assertion here once the functionality of addLandMark is clarified
    }

    // Add more test cases for edge cases and error scenarios here

}