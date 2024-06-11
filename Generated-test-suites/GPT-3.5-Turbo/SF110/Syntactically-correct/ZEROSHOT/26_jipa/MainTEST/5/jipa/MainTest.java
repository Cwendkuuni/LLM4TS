import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        // Arrange
        String instruction = "halt";
        
        // Act
        Main.processInstruction(instruction);
        
        // Assert - verify that the program exits
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_out() {
        // Arrange
        String instruction = "out Hello";
        
        // Act
        Main.processInstruction(instruction);
        
        // Assert - verify that the output is printed
        // Implement appropriate assertion based on the actual output
    }

    @Test
    public void testProcessInstruction_in() {
        // Arrange
        String instruction = "in variable";
        
        // Act
        // Pass user input to simulate reading a value
        Main.processInstruction(instruction);
        
        // Assert - verify that the variable is set with the correct value
        // Implement appropriate assertion based on the variable value
    }
    
    // Repeat the above pattern for testing all the other methods in the Main class

}