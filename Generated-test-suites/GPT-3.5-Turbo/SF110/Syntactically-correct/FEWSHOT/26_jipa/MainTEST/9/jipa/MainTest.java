import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class MainTest {

    @Before
    public void setUp() {
        Main.TOTAL_INSTRUCTIONS = 0;
        Main.instruction = new String[0];
        Main.iPtr = 0;
    }

    @Test
    public void testProcessInstruction_halt() {
        Main.instruction = new String[]{"halt"};
        Main.processInstruction(Main.instruction[Main.iPtr]);
        // Verify code behavior here
    }

    @Test
    public void testProcessInstruction_out() {
        Main.instruction = new String[]{"out Hello World!"};
        Main.processInstruction(Main.instruction[Main.iPtr]);
        // Verify code behavior here
    }

    // Create test methods for the other processInstruction cases

    @Test
    public void testGetFileName() {
        // Set up necessary data for the test
        assertEquals("testPath", Main.getFileName());
        // Verify the expected behavior here
    }
    
    // Create test methods for the other methods in the Main class

}