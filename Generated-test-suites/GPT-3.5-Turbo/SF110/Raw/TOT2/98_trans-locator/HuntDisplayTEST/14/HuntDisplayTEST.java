import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;
import java.awt.Color;

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
        TrigPoint point = new TrigPoint(45, 90, 30, false);
        display.addPoint(point);
        assertTrue(display.points.contains(point));
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        assertTrue(display.landmarks.contains(landmark));
    }
    
    // Edge cases and exceptions testing can be added here
    
}