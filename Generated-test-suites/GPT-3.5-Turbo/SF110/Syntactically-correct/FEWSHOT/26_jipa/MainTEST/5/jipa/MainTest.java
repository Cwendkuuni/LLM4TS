package jipa;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {
    @Before
    public void setUp() throws Exception {
        // Initialize test setup
        Main.TOTAL_INSTRUCTIONS = 0;
        Main.instruction = new String[0];
        Main.iPtr = 0;
    }

    @Test
    public void testProcessInstruction_halt() {
        // Test with halt instruction
        Main.instruction[0] = "halt";
        Main.processInstruction(Main.instruction[0]);
        // Test that the program exits
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_out() {
        // Test with out instruction
        Main.instruction[0] = "out tmpVar1";
        Variable.setVariableValue("tmpVar1", "Hello, World!");
        Main.processInstruction(Main.instruction[0]);
        // Test that the string value of tmpVar1 is printed
        assertEquals("Hello, World!", getConsoleOutput());
    }

    @Test
    public void testProcessInstruction_in() {
        // Test with in instruction
        Main.instruction[0] = "in tmpVar1";
        setConsoleInput("42");
        Main.processInstruction(Main.instruction[0]);
        // Test that the value read from console is stored in tmpVar1
        assertEquals(42, Variable.getVariableValue("tmpVar1"));
    }

    @Test
    public void testProcessInstruction_mov() {
        // Test with mov instruction
        Main.instruction[0] = "mov tmpVar1, tmpVar2";
        Variable.setVariableValue("tmpVar2", 42);
        Main.processInstruction(Main.instruction[0]);
        // Test that the value of tmpVar2 is moved to tmpVar1
        assertEquals(42, Variable.getVariableValue("tmpVar1"));
        assertEquals(0, Variable.getVariableValue("tmpVar2"));
    }

    @Test
    public void testProcessInstruction_cpy() {
        // Test with cpy instruction
        Main.instruction[0] = "cpy tmpVar1, tmpVar2";
        Variable.setVariableValue("tmpVar2", 42);
        Main.processInstruction(Main.instruction[0]);
        // Test that the value of tmpVar2 is copied to tmpVar1
        assertEquals(42, Variable.getVariableValue("tmpVar1"));
        assertEquals(42, Variable.getVariableValue("tmpVar2"));
    }

    // Add tests for other instructions

    // Helper method to simulate console input
    private void setConsoleInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    // Helper method to capture console output
    private String getConsoleOutput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the method that prints to console

        return outContent.toString().trim();
    }
}