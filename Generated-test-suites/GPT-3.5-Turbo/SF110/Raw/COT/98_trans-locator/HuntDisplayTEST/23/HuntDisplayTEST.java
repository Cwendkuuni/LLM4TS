
import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        // Create a new HuntDisplay object
        HuntDisplay huntDisplay = new HuntDisplay();
        
        // Create a TrigPoint object
        TrigPoint trigPoint = new TrigPoint(0.5, 0.5, 45, false);
        
        // Add the TrigPoint to the HuntDisplay
        huntDisplay.addPoint(trigPoint);
        
        // TODO: Add assertions to verify the functionality of the method
    }
    
    @Test
    public void testAddLandMark() {
        // Create a new HuntDisplay object
        HuntDisplay huntDisplay = new HuntDisplay();
        
        // Create a Landmark object
        Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon());
        
        // Add the Landmark to the HuntDisplay
        huntDisplay.addLandMark(landmark);
        
        // TODO: Add assertions to verify the functionality of the method
    }
    
}
