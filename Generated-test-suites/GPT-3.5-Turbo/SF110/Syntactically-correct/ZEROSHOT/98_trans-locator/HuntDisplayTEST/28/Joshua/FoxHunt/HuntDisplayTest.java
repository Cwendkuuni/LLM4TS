import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {
    
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testGetPreferredSize() {
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }
    
    @Test
    public void testGetMinimumSize() {
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, true);
        huntDisplay.addPoint(point);
        // Add assertion statements
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Add assertion statements
    }
    
    @Test
    public void testPaintComponent() {
        // Add test case for paintComponent
    }
}