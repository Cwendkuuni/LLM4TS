
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Basic test case
        String instruction = "halt";
        Main.processInstruction(instruction);
        // Add more test cases for other instructions
        //...
    }

    @Test
    public void testLoadInstructions() {
        // Test loading instructions from a file
        String fileName = "test.txt";
        boolean result = Main.loadInstructions(fileName);
        // Assert the result
        //...
    }

    @Test
    public void testRemoveComment() {
        // Test removing comments from a line
        String line = "mov variable1, 10 ; this is a comment";
        String result = Main.removeComment(line);
        // Assert the result
        //...
    }

    // Add more test cases for other public methods

}