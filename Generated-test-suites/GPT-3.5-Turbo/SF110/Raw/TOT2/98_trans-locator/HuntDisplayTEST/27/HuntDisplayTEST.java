import org.junit.Test;
import static org.junit.Assert.*;

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
        // Add assertion here for expected result
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(30, 60, new ImageIcon());
        display.addLandmark(landmark);
        // Add assertion here for expected result
    }

    // Add more test cases for other methods and edge cases

}