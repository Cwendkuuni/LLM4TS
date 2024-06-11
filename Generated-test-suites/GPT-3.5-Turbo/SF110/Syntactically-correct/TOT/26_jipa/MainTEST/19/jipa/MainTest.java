import org.junit.*;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Basic test case with halt opcode
        String instruction = "halt";
        Main.processInstruction(instruction);
        assertEquals(0, Main.iPtr);
        
        // Test case with out opcode
        instruction = "out, \"Hello World!\"";
        Main.processInstruction(instruction);
        // verify the output message using println
        // ...
        assertEquals(1, Main.iPtr);
        
        // Test case with in opcode
        instruction = "in, variable1";
        // Set up a mock input stream and pass the value of variable1
        // ...
        Main.processInstruction(instruction);
        assertEquals(2, Main.iPtr);
        // Verify that variable1 has been set to the value provided from input stream
        // ...
        
        // ... continue with more test cases for other opcodes
        
    }
    
    @Test
    public void testGetFileName() {
        // Test case for valid file name input
        String fileName = "test.txt";
        // Set up a mock input stream to pass the file name
        // ...
        assertEquals(fileName, Main.getFileName());
        
        // Test case for quitting the program
        fileName = "q";
        // Set up a mock input stream to pass the file name
        // ...
        assertEquals(fileName, Main.getFileName());
    }
    
    // ... continue with more test cases for other methods
    
}