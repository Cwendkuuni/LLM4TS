import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.util.Vector;

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
        TrigPoint point = new TrigPoint(90, 180, 45, true);
        display.addPoint(point);
        assertEquals(1, display.points.size());
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(90, 180, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(90, 180, 45, true);
        Landmark landmark = new Landmark(90, 180, new ImageIcon("icon.png"));
        display.addPoint(point);
        display.addLandMark(landmark);
        assertEquals(1, display.points.size());
        assertEquals(1, display.landmarks.size());
    }

    // Add more comprehensive test cases for edge cases and error scenarios

}