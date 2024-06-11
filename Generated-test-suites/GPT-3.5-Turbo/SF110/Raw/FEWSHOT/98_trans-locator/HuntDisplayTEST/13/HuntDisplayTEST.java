package Joshua.FoxHunt;

import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {
    
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        //Assert if point is added correctly
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        //Assert if landmark is added correctly
    }
    
    @Test
    public void testPaintComponent() {
        //Cannot test the paintComponent method directly
    }
}