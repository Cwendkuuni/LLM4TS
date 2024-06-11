import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;
import java.util.Vector;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expected = new Dimension(200, 200);
        Dimension actual = display.getPreferredSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expected = new Dimension(50, 50);
        Dimension actual = display.getMinimumSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(90, 180, 45, true);
        display.addPoint(point);
        assertEquals(1, display.points.size());
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(90, 180, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
    }

    // Additional edge cases and exception handling tests can be added here
    
}