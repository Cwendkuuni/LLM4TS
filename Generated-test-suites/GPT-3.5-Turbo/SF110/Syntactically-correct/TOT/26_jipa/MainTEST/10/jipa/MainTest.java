import org.junit.*;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Typical use case - opcode is "halt"
        Main.processInstruction("halt");
        // Verify that the program exits with code 0
        assertEquals(0, System.exitValue());

        // Edge case - opcode is "out"
        Main.processInstruction("out Hello World!");
        // Verify that the output is correct
        assertEquals("Hello World!\n", outContent.toString());

        // Error scenario - opcode is invalid
        Main.processInstruction("invalid");
        // Verify that the program does nothing
        // In this case, no assertions are needed, as the method does not have any side effects
    }

    @Test
    public void testLoadInstructions() {
        // Typical use case - loaded from a file
        assertTrue(Main.loadInstructions("input.txt"));
        // Verify that the instructions are loaded correctly
        assertEquals(5, Main.TOTAL_INSTRUCTIONS);
        assertEquals("halt", Main.instruction[0]);
        assertEquals("out Hello", Main.instruction[1]);
        assertEquals("out World", Main.instruction[2]);
        assertEquals("add x, y", Main.instruction[3]);
        assertEquals("jmp label", Main.instruction[4]);

        // Error scenario - file not found
        assertFalse(Main.loadInstructions("nonexistent.txt"));
        // Verify that the program does not crash and returns false
        // In this case, no assertions are needed, as the method already returns a boolean

        // Error scenario - invalid instructions in file
        assertTrue(Main.loadInstructions("invalid.txt"));
        // Verify that the program does not crash and returns true
        // In this case, no assertions are needed, as the method already returns a boolean
    }

    @Test
    public void testRemoveComment() {
        // Typical use case - line with a comment
        assertEquals("move x, y", Main.removeComment("move x, y ; This is a comment"));
        // Verify that the comment is removed correctly

        // Typical use case - line without a comment
        assertEquals("move x, y", Main.removeComment("move x, y"));
        // Verify that the line is returned as is

        // Edge case - empty line
        assertEquals("", Main.removeComment(""));
        // Verify that an empty string is returned

        // Edge case - line with only a comment
        assertEquals("", Main.removeComment("; This is a comment"));
        // Verify that the comment is removed and an empty string is returned
    }

    @Test
    public void testResetInstructions() {
        // Set up - initialize the array with values
        Main.instruction = new String[]{"halt", "out Hello"};
        // Call the method
        Main.resetInstructions();
        // Verify that the array is reset to empty strings
        assertArrayEquals(new String[]{"", ""}, Main.instruction);
    }

    @Test
    public void testSplitArguments() {
        // Typical use case - multiple arguments
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, Main.splitArguments("arg1, arg2, arg3"));
        // Verify that the arguments are split correctly

        // Typical use case - single argument
        assertArrayEquals(new String[]{"arg1"}, Main.splitArguments("arg1"));
        // Verify that the argument is returned as is

        // Edge case - empty argument
        assertArrayEquals(new String[]{""}, Main.splitArguments(""));
        // Verify that an empty string is returned

        // Edge case - no arguments
        assertArrayEquals(new String[]{""}, Main.splitArguments(" "));
        // Verify that an empty string is returned

        // Error scenario - missing comma
        assertArrayEquals(new String[]{"arg1", " arg2"}, Main.splitArguments("arg1 arg2"));
        // Verify that the arguments are split correctly
    }

    @Test
    public void testGetValue() {
        // Typical use case - operand is a variable
        Variable.addVariable("x");
        Variable.setVariableValue("x", 5);
        assertEquals(5, Main.getValue("x"));
        // Verify that the value of the variable is returned

        // Typical use case - operand is an integer
        assertEquals(10, Main.getValue("10"));
        // Verify that the integer value is returned

        // Error scenario - operand is not a valid variable or integer
        assertEquals(0, Main.getValue("invalid"));
        // Verify that the default value of 0 is returned
    }

    @Test
    public void testGetJumpValue() {
        // Typical use case - operand is a label
        Label.addLabel("label", 10);
        assertEquals(9, Main.getJumpValue("label"));
        // Verify that the jump value is correct

        // Typical use case - operand is an integer
        assertEquals(4, Main.getJumpValue("4"));
        // Verify that the jump value is correct

        // Error scenario - operand is not a valid label or integer
        assertEquals(-1, Main.getJumpValue("invalid"));
        // Verify that -1 is returned
    }

    @Test
    public void testReadValue() {
        // Typical use case - read an integer value
        System.setIn(new ByteArrayInputStream("10".getBytes()));
        assertEquals(10, Main.readValue());
        // Verify that the integer value is read correctly

        // Error scenario - read a non-integer value
        System.setIn(new ByteArrayInputStream("invalid".getBytes()));
        assertEquals(0, Main.readValue());
        // Verify that the default value of 0 is returned
    }

    @Test
    public void testGetStringValue() {
        // Typical use case - operand is a variable
        Variable.addVariable("x");
        Variable.setVariableValue("x", 5);
        assertEquals("5", Main.getStringValue("x"));
        // Verify that the string representation of the variable value is returned

        // Typical use case - operand is an integer
        assertEquals("10", Main.getStringValue("10"));
        // Verify that the string representation of the integer is returned

        // Typical use case - operand is a string with quotes
        assertEquals("Hello World!", Main.getStringValue("\"Hello World!\""));
        // Verify that the quotes are removed and the value inside is returned

        // Error scenario - operand is not a valid variable, integer, or string with quotes
        assertEquals("0", Main.getStringValue("invalid"));
        // Verify that the default value of 0 is returned
    }

    @Test
    public void testIsNumeric() {
        // Typical use case - string is a valid integer
        assertTrue(Main.isNumeric("10"));
        // Verify that true is returned

        // Typical use case - string is not a valid integer
        assertFalse(Main.isNumeric("invalid"));
        // Verify that false is returned
    }
}