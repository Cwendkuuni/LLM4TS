import org.junit.*;
import static org.junit.Assert.*;

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
        TrigPoint point = new TrigPoint(45, 90, 0.0, true);
        display.addPoint(point);
        // Verify point is added to points vector
        assertEquals(1, display.points.size());
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        display.addLandMark(landmark);
        // Verify landmark is added to landmarks vector
        assertEquals(1, display.landmarks.size());
    }
    
    @Test
    public void testPaintComponent() {
        // TODO: implement test cases for paintComponent method
    }
}