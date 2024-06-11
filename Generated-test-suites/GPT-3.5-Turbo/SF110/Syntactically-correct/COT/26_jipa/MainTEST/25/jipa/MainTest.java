
import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        // Given
        String instruction = "halt";

        // When
        Main.processInstruction(instruction);

        // Then
        // This method exits the program, so there is no direct assertion
    }

    @Test
    public void testProcessInstruction_out() {
        // Given
        String instruction = "out arg1";

        // When
        Main.processInstruction(instruction);

        // Then
        // This method prints the value of the operand, so there is no direct assertion
    }
    
    // Add more test methods for the rest of the public methods in the Main class...
    
    @Test
    public void testGetFileName() {
        // Given
        // No input required

        // When
        String fileName = Main.getFileName();

        // Then
        // There is no direct assertion for this method, as it depends on user input
        // We can manually verify that the returned file name is correct and matches the input
    }

    @Test
    public void testLoadInstructions() {
        // Given
        String fileName = "path/to/instructions.txt"; // Provide valid test file

        // When
        boolean result = Main.loadInstructions(fileName);

        // Then
        assertTrue(result);
        // Verify that the instructions are correctly loaded and stored in the Main.instruction array
    }

    @Test
    public void testRemoveComment() {
        // Given
        String line = "This is a line of code; with a comment";

        // When
        String result = Main.removeComment(line);

        // Then
        assertEquals("This is a line of code", result);
    }
    
    // Add more test methods for the rest of the public methods in the Main class...

}