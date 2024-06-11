import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Typical use case
        String instruction = "mov 10, 20";
        Main.processInstruction(instruction);
        assertEquals(20, Variable.getVariableValue("10"));
        
        // Edge case
        instruction = "set 30, 40";
        Main.processInstruction(instruction);
        assertEquals(40, Variable.getVariableValue("30"));
        
        // Error scenario
        instruction = "invalid instruction";
        Main.processInstruction(instruction);
        // Verify if there's an error handling mechanism in place
    }
    
    @Test
    public void testGetFileName() {
        // Typical use case
        // Provide an input file and check if the correct filename is returned
        // Mock the System.in for input stream
        
        // Edge case
        // Enter 'q' as the input and verify if the program exits
        
        // Error scenario
        // Provide a non-existing file and see how it's handled
    }
    
    // More test cases for other methods

}