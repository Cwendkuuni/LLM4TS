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
        TrigPoint point = new TrigPoint(90, 180, 45, false);
        display.addPoint(point);
        // Ensure point is added correctly
        assertEquals(1, display.points.size());
    }
    
    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(90, 180, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        // Ensure landmark is added correctly
        assertEquals(1, display.landmarks.size());
    }
    
    @Test
    public void testPaintComponent() {
        // Not easily testable due to graphical output, can be manually verified
    }
}