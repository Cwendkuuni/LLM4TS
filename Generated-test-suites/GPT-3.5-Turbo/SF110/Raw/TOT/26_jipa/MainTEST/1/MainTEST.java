import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {
    @Test
    public void testProcessInstruction_Halt() {
        // Test when opcode is 'halt'
        // Expected: System exits with code 0
        // This is a typical use case
        Main.processInstruction("halt");
        assertEquals(0, System.exit(0));
    }
    
    @Test
    public void testProcessInstruction_Out() {
        // Test when opcode is 'out'
        // Expected: The value of the operand is printed
        // This is a typical use case
        Main.processInstruction("out Hello, World!");
        assertEquals("Hello, World!", outContent.toString().trim());
    }
    
    @Test
    public void testProcessInstruction_In() {
        // Test when opcode is 'in'
        // Expected: The value from user input is stored in the operand variable
        // This is a typical use case
        Main.processInstruction("in input");
        // Assuming user inputs "5"
        assertEquals(5, Variable.getVariableValue("input"));
    }
    
    // More test cases for each method...

    @Test
    public void testGetFileName() {
        // Test when user enters a valid file name
        // Expected: The entered file name is returned
        // This is a typical use case
        String fileName = "src/test/resources/input.txt";
        System.setIn(new ByteArrayInputStream(fileName.getBytes()));
        assertEquals(fileName, Main.getFileName());
    }

    @Test
    public void testLoadInstructions_Success() {
        // Test when file name is provided and the file exists
        // Expected: Instructions are loaded successfully and true is returned
        // This is a typical use case
        String fileName = "src/test/resources/instructions.txt";
        boolean result = Main.loadInstructions(fileName);
        assertEquals(true, result);
        assertEquals(7, Main.TOTAL_INSTRUCTIONS);
        assertEquals("mov variable2, variable1", Main.instruction[2]);
    }

    @Test
    public void testLoadInstructions_FileNameEmpty() {
        // Test when file name is empty and user enters a valid file name
        // Expected: Instructions are loaded successfully and true is returned
        // This is a typical use case
        String fileName = "";
        System.setIn(new ByteArrayInputStream("src/test/resources/instructions.txt".getBytes()));
        boolean result = Main.loadInstructions(fileName);
        assertEquals(true, result);
        assertEquals(7, Main.TOTAL_INSTRUCTIONS);
        assertEquals("mov variable2, variable1", Main.instruction[2]);
    }

    // More test cases for each method...

}