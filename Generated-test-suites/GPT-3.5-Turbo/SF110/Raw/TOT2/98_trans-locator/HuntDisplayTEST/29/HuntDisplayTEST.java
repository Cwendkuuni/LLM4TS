import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.awt.image.BufferedImage;
import java.awt.Image;

public class HuntDisplayTest
{
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
        TrigPoint point = new TrigPoint(45, 90, 0.0, false);
        huntDisplay.addPoint(point);
        // Add assertions here to test the functionality
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)));
        huntDisplay.addLandMark(landmark);
        // Add assertions here to test the functionality
    }
    
    // Add more test methods for other public methods in the HuntDisplay class
    
    @Test
    public void testPaintComponent() {
        // Add test cases to cover the functionality and edge cases of paintComponent method
    }
}