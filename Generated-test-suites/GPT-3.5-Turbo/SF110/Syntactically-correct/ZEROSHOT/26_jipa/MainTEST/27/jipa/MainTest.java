import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        Main.processInstruction("halt");
        // TODO: Add assertion statements
    }

    @Test
    public void testGetFileName() {
        // TODO: Implement test case
    }

    @Test
    public void testLoadInstructions() {
        boolean result = Main.loadInstructions("");
        assertTrue(result);
    }

    @Test
    public void testRemoveComment() {
        String result = Main.removeComment("Hello World; This is a comment");
        assertEquals("Hello World", result);
    }

    @Test
    public void testResetInstructions() {
        // TODO: Implement test case
    }

    @Test
    public void testSplitArguments() {
        String[] result = Main.splitArguments("arg1, arg2, arg3");
        String[] expected = {"arg1", "arg2", "arg3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetValue() {
        int result = Main.getValue("5");
        assertEquals(5, result);
    }

    @Test
    public void testGetJumpValue() {
        int result = Main.getJumpValue("label");
        // TODO: Add assertion statement
    }

    @Test
    public void testReadValue() {
        // TODO: Implement test case
    }

    @Test
    public void testGetStringValue() {
        String result = Main.getStringValue("Hello \"World\"");
        assertEquals("Hello World", result);
    }

    @Test
    public void testIsNumeric() {
        boolean result = Main.isNumeric("123");
        assertTrue(result);
    }
}