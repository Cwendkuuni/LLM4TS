import org.junit.*;
import static org.junit.Assert.*;

public class MainTest {
    
    @Test
    public void testProcessInstruction() {
        // Typical use case: opcode is "halt"
        Main.processInstruction("halt");
        // Assert that system has exited with code 0
        assertEquals(0, System.exit(0));

        // Edge case: opcode is "out" with operand as null
        Main.processInstruction("out");
        // No assertion as the output is printed to the console
        
        // Error scenario: opcode is an invalid value
        Main.processInstruction("invalid");
        // Assert that the opcode is not recognized and returns the default value opcodes.novalue
        assertEquals(Main.opcodes.novalue, Main.opcodes.toOpcode("invalid"));
    }
    
    @Test
    public void testGetFileName() {
        // Typical use case: user inputs a valid file path
        // Manually input a file path and see if the fileName returned matches
        assertTrue(Main.getFileName().trim().endsWith(".txt"));
        
        // Edge case: user inputs "q" to quit the program
        // Manually input "q" and see if the system exits with code 0
        assertEquals(0, System.exit(0));
    }
    
    @Test
    public void testLoadInstructions() {
        // Typical use case: loading instructions from a valid file
        String fileName = "valid_instructions.txt";
        assertTrue(Main.loadInstructions(fileName));
        // Assert that the instructions array is initialized with non-empty strings
        for (String instr : Main.instruction) {
            assertFalse(instr.isEmpty());
        }
        
        // Error scenario: loading instructions from an invalid file
        fileName = "invalid_instructions.txt";
        assertFalse(Main.loadInstructions(fileName));
        // Assert that the method returns false when an exception occurs during loading instructions
        
    }
    
    @Test
    public void testRemoveComment() {
        // Typical use case: removing comment from a line with comment
        String line = "add 10, 20    ; This is an addition instruction";
        String expectedLine = "add 10, 20";
        assertEquals(expectedLine, Main.removeComment(line));
        
        // Edge case: removing comment from a line without comment
        line = "halt";
        // The method should return the same line as input since there is no comment to remove
        assertEquals(line, Main.removeComment(line));
    }
    
    @Test
    public void testResetInstructions() {
        // Edge case: resetting instructions array when it is empty
        Main.resetInstructions();
        // Assert that all elements in the instructions array are empty strings
        for (String instr : Main.instruction) {
            assertTrue(instr.isEmpty());
        }
    }
    
    @Test
    public void testSplitArguments() {
        // Typical use case: splitting arguments with multiple arguments
        String args = "var, x, 10";
        String[] expectedArgs = {"var", "x", "10"};
        assertArrayEquals(expectedArgs, Main.splitArguments(args));
        
        // Edge case: splitting arguments with only one argument
        args = "halt";
        // The method should return an array with a single element which is the input string
        String[] expectedArgs2 = {args};
        assertArrayEquals(expectedArgs2, Main.splitArguments(args));
    }
    
    @Test
    public void testGetValue() {
        // Typical use case: getting value of a variable
        String operand = "x";
        // Set the value of variable "x" to 5
        Variable.setVariableValue(operand, 5);
        assertEquals(5, Main.getValue(operand));
        
        // Edge case: getting value of a numeric operand
        operand = "10";
        // The method should return the same numeric value as an int
        assertEquals(Integer.parseInt(operand), Main.getValue(operand));
        
        // Error scenario: getting value of an undefined variable
        operand = "y";
        // The method should return 0 since the variable is undefined and returns a default value
        assertEquals(0, Main.getValue(operand));
    }
    
    @Test
    public void testGetJumpValue() {
        // Typical use case: getting jump value from numeric operand
        String operand = "5";
        // The method should return the operand value minus 1
        assertEquals(Integer.parseInt(operand) - 1, Main.getJumpValue(operand));
        
        // Edge case: getting jump value from label
        operand = "label1";
        // Manually create a label "label1" and store its line number
        Label.addLabel(operand, 10);
        // The method should return the line number of the label minus 1
        assertEquals(Label.findLabel(operand) - 1, Main.getJumpValue(operand));
    }
    
    @Test
    public void testReadValue() {
        // Typical use case: reading a valid integer value from console
        // Manually input a valid integer value and see if the method returns the same value
        assertEquals(5, Main.readValue());
        
        // Edge case: reading an invalid non-numeric value from console
        // Manually input a non-numeric value, the method should return 0 as default value
        assertEquals(0, Main.readValue());
    }
    
    @Test
    public void testGetStringValue() {
        // Typical use case: getting string value from operand with quotes
        String operand = "\"Hello World\"";
        // The method should return the value inside quotes
        assertEquals(operand.replaceAll("\"", ""), Main.getStringValue(operand));
        
        // Edge case: getting string value from operand without quotes
        operand = "10";
        // The method should return the string value of the operand
        assertEquals(operand, Main.getStringValue(operand));
    }
    
    @Test
    public void testIsNumeric() {
        // Typical use case: checking if a string is numeric
        String str = "10";
        // The method should return true since the string is numeric
        assertTrue(Main.isNumeric(str));
        
        // Edge case: checking if a string is non-numeric
        str = "abc";
        // The method should return false since the string is non-numeric
        assertFalse(Main.isNumeric(str));
    }
}