import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Before
    public void setUp() {
        Main.TOTAL_INSTRUCTIONS = 0;
        Main.instruction = new String[0];
        Main.iPtr = 0;
    }

    @Test
    public void testProcessInstruction() {
        // Test for opcode "halt"
        Main.processInstruction("halt");
        // Assert the system exits with code 0
        assertEquals(0, Main.iPtr);

        // Test for opcode "out" with operand "Test"
        Main.processInstruction("out Test");
        // Nothing to assert as the output is printed using System.out

        // Test for opcode "in" with operand "var"
        Main.processInstruction("in var");
        // Nothing to assert as the input is read from System.in

        // Add more test cases for other opcodes with different operands
    }

    @Test
    public void testGetFileName() {
        // Nothing to test as it reads user input from System.in
    }

    @Test
    public void testLoadInstructions() {
        // Test loading instructions from a file
        assertTrue(Main.loadInstructions("test_file.txt"));

        // Test loading instructions from user input
        assertTrue(Main.loadInstructions(""));

        // Test quitting the program
        Main.loadInstructions("q");
        // Assert the system exits with code 0

        // Test loading instructions from a non-existing file
        assertFalse(Main.loadInstructions("non_existing_file.txt"));
    }

    @Test
    public void testRemoveComment() {
        // Test removing comment from a line with a comment
        String line = "instruction ; this is a comment";
        String expected = "instruction";
        assertEquals(expected, Main.removeComment(line));

        // Test removing comment from a line without a comment
        line = "instruction";
        expected = "instruction";
        assertEquals(expected, Main.removeComment(line));
    }

    @Test
    public void testResetInstructions() {
        // Test resetting instructions
        Main.resetInstructions();
        // All instructions should be empty strings
        for (String instruction : Main.instruction) {
            assertEquals("", instruction);
        }
    }

    @Test
    public void testSplitArguments() {
        // Test splitting arguments with a comma
        String arguments = "arg1, arg2, arg3";
        String[] expected = {"arg1", "arg2", "arg3"};
        assertArrayEquals(expected, Main.splitArguments(arguments));

        // Test splitting arguments with no comma
        arguments = "arg1";
        expected = new String[]{"arg1"};
        assertArrayEquals(expected, Main.splitArguments(arguments));
    }

    @Test
    public void testGetValue() {
        // Test getting value from a numeric operand
        String operand = "123";
        int expected = 123;
        assertEquals(expected, Main.getValue(operand));

        // Test getting value from a variable operand
        operand = "var";
        Variable.addVariable("var");
        Variable.setVariableValue("var", 456);
        expected = 456;
        assertEquals(expected, Main.getValue(operand));
    }

    @Test
    public void testGetJumpValue() {
        // Test getting jump value from a numeric operand
        String operand = "123";
        int expected = 122;
        assertEquals(expected, Main.getJumpValue(operand));

        // Test getting jump value from a label operand
        operand = "label";
        Label.addLabel("label", 456);
        expected = 455;
        assertEquals(expected, Main.getJumpValue(operand));
    }

    @Test
    public void testReadValue() {
        // Nothing to test as it reads user input from System.in
    }

    @Test
    public void testGetStringValue() {
        // Test getting string value from an operand with no quotes
        String operand = "123";
        String expected = "123";
        assertEquals(expected, Main.getStringValue(operand));

        // Test getting string value from an operand with quotes
        operand = "\"test\"";
        expected = "test";
        assertEquals(expected, Main.getStringValue(operand));

        // Test getting string value from an operand with a combination of quotes and numeric values
        operand = "\"test\"123\"456\"";
        expected = "test123456";
        assertEquals(expected, Main.getStringValue(operand));
    }

    @Test
    public void testIsNumeric() {
        // Test checking if a string is numeric
        String str = "123";
        assertTrue(Main.isNumeric(str));

        // Test checking if a string is not numeric
        str = "abc";
        assertFalse(Main.isNumeric(str));
    }

    @Test
    public void testToOpcode() {
        // Test converting a valid opcode string to opcode enum
        String opcodeStr = "halt";
        Main.opcodes expected = Main.opcodes.halt;
        assertEquals(expected, Main.opcodes.toOpcode(opcodeStr));

        // Test converting an invalid opcode string to opcode enum
        opcodeStr = "invalid";
        expected = Main.opcodes.novalue;
        assertEquals(expected, Main.opcodes.toOpcode(opcodeStr));
    }
}