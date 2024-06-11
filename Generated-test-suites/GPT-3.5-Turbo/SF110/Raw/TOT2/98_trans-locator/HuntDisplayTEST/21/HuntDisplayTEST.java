import org.junit.*;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;

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
        TrigPoint point = new TrigPoint(10.0, 20.0, 30.0, true);
        huntDisplay.addPoint(point);
        // Check if the TrigPoint was added successfully
        assertEquals(1, huntDisplay.points.size());
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(30.0, 40.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
        huntDisplay.addLandMark(landmark);
        // Check if the Landmark was added successfully
        assertEquals(1, huntDisplay.landmarks.size());
    }
    
    // Additional test cases for edge cases and error scenarios can be added here
    
}