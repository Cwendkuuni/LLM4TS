import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expected = new Dimension(200, 200);
        assertEquals(expected, display.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expected = new Dimension(50, 50);
        assertEquals(expected, display.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(10.0, 20.0, 30.0, true);
        display.addPoint(point);
        // Add assertion here based on expected behavior
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(40.0, 50.0, new ImageIcon());
        display.addLandMark(landmark);
        // Add assertion here based on expected behavior
    }

    // Add more test cases for other public methods as needed

}