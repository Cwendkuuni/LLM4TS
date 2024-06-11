import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;
    
    @Test
    public void testGetPreferredSize() {
        huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }
    
    @Test
    public void testGetMinimumSize() {
        huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }
    
    @Test
    public void testAddPoint() {
        huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(30, 45, 60, true);
        huntDisplay.addPoint(point);
        assertEquals(1, huntDisplay.points.size());
    }
    
    @Test
    public void testAddLandMark() {
        huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(30, 45, new ImageIcon("icon.png"));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
    }
    
    @Test
    public void testPaintComponent() {
        // The paintComponent method is complex and may need visual inspection
        // We can add tests for checking the positioning of points and landmarks
        // For edge cases, we can test when points or landmarks are empty
        // And for error scenarios, we can test by passing null values to addPoint and addLandMark methods
    }
}