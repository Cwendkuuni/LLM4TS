import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Test for halt opcode
        Main.TOTAL_INSTRUCTIONS = 1;
        Main.instruction = new String[]{"halt"};
        Main.iPtr = 0;
        Main.processInstruction("halt");
        assertEquals(0, Main.iPtr);

        // Test for out opcode
        Main.TOTAL_INSTRUCTIONS = 1;
        Main.instruction = new String[]{"out 5"};
        Main.iPtr = 0;
        Main.processInstruction("out 5");
        assertEquals("5", outContent.toString().trim());

        // Add more test cases for other opcodes
    }

    @Test
    public void testGetFileName() {
        // Test when a valid file name is entered
        // Need to mock System.in and verify the expected file name is returned

        // Test when "q" is entered
        // Need to mock System.in and verify System.exit(0) is called
    }

    @Test
    public void testLoadInstructions() {
        // Test loading instructions from a valid file
        assertTrue(Main.loadInstructions("testfile.txt"));

        // Test loading instructions from a non-existing file
        assertFalse(Main.loadInstructions("nonexistingfile.txt"));
    }

    // Add more test cases for other methods

}