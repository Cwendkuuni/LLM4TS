package ipac;
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whoIS = new WhoIS();
        assertNotNull(whoIS);
        // Add more assertions here as needed
    }
    
    @Test
    public void testActionPerformed_queryButton() {
        WhoIS whoIS = new WhoIS();
        // Simulate the query button being clicked
        whoIS.queryButton.doClick();
        // Check if the startQuery method is called
        // Add more assertions here as needed
    }
    
    @Test
    public void testActionPerformed_resetButton() {
        WhoIS whoIS = new WhoIS();
        // Simulate the reset button being clicked
        whoIS.resetButton.doClick();
        // Check if the reset method is called
        // Add more assertions here as needed
    }

    // Add more test cases for other public methods as needed
    
}