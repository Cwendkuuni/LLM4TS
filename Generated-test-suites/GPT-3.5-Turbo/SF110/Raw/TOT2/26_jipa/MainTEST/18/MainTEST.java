import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    
    @Test
    public void testProcessInstruction() {
        // Typical use case
        String instruction = "mov 2, var1";
        Main.processInstruction(instruction);
        assertEquals(2, Variable.getVariableValue("var1"));
        
        // Edge case: Space location is -1
        instruction = "halt";
        Main.processInstruction(instruction);
        assertEquals(0, System.exit(0));
        
        // Exception handling: Invalid opcode
        instruction = "invalidOp 2, var1";
        Main.processInstruction(instruction);
        assertEquals(0, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testGetFileName() {
        // Typical use case
        String fileName = "test.txt";
        String expected = fileName;
        assertEquals(expected, Main.getFileName());
    }
    
    // Add more test cases for other public methods
    
}