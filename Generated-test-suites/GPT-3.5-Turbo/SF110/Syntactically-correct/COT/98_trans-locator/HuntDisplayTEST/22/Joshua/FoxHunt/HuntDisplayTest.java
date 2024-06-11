
import org.junit.Assert;
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
        Assert.assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        Assert.assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1.0, 2.0, 45.0, false);
        huntDisplay.addPoint(point);
        
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        
        // Add assertions to check the functionality of the method
    }

    @Test
    public void testPaintComponent() {
        // Since paintComponent() is a void method and it directly interacts with the graphics, 
        // it is difficult to write a direct test case for it.
        // It can be visually tested or indirectly tested by testing the effects of its execution.
    }

    // Additional test cases for edge cases and exception handling scenarios

}
