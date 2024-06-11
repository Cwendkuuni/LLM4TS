import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;

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
        TrigPoint point = new TrigPoint(45.0, 90.0, 0, true);

        display.addPoint(point);
        // Add assertions here for expected behavior
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45.0, 90.0, new ImageIcon("icon.png"));

        display.addLandMark(landmark);
        // Add assertions here for expected behavior
    }

    // Add more test cases for other methods as needed

    @Test
    public void testPaintComponent() {
        // Add test cases for painting component
    }
}