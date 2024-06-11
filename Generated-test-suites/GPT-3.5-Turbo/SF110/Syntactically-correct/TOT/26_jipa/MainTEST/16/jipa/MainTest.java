import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Test case 1: Testing halt opcode
        Main.processInstruction("halt");
        assertEquals(0, Main.iPtr);
        // Test case 2: Testing out opcode
        Main.processInstruction("out Hello");
        assertEquals("Hello", outContent.toString().trim());
    }

    @Test
    public void testLoadInstructions() {
        // Test case 1: Testing empty fileName
        assertFalse(Main.loadInstructions(""));
        // Test case 2: Testing valid fileName
        assertTrue(Main.loadInstructions("testFile.txt"));
    }

    @Test
    public void testRemoveComment() {
        // Test case 1: Testing line without comment
        assertEquals("Instruction", Main.removeComment("Instruction"));
        // Test case 2: Testing line with comment at end
        assertEquals("Instruction", Main.removeComment("Instruction; Comment"));
    }

    @Test
    public void testResetInstructions() {
        // Test case 1: Testing resetting instructions
        Main.TOTAL_INSTRUCTIONS = 3;
        Main.instruction = new String[Main.TOTAL_INSTRUCTIONS];
        Main.resetInstructions();
        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; i++) {
            assertEquals("", Main.instruction[i]);
        }
    }

    @Test
    public void testSplitArguments() {
        // Test case 1: Testing single argument
        String[] expected1 = {"arg"};
        assertArrayEquals(expected1, Main.splitArguments("arg"));
        // Test case 2: Testing multiple arguments
        String[] expected2 = {"arg1", "arg2"};
        assertArrayEquals(expected2, Main.splitArguments("arg1, arg2"));
    }

    @Test
    public void testGetValue() {
        // Test case 1: Testing numeric operand
        int expected1 = 5;
        assertEquals(expected1, Main.getValue("5"));
        // Test case 2: Testing variable operand
        Variable.setVariableValue("var", 10);
        int expected2 = 10;
        assertEquals(expected2, Main.getValue("var"));
    }

    @Test
    public void testGetJumpValue() {
        // Test case 1: Testing numeric operand
        int expected1 = 3;
        assertEquals(expected1, Main.getJumpValue("4"));
        // Test case 2: Testing label operand
        Label.addLabel("label", 4);
        int expected2 = 3;
        assertEquals(expected2, Main.getJumpValue("label"));
    }

    @Test
    public void testReadValue() {
        // Test case 1: Testing numeric input
        ByteArrayInputStream inStream1 = new ByteArrayInputStream("5".getBytes());
        System.setIn(inStream1);
        int expected1 = 5;
        assertEquals(expected1, Main.readValue());
        // Test case 2: Testing non-numeric input
        ByteArrayInputStream inStream2 = new ByteArrayInputStream("test".getBytes());
        System.setIn(inStream2);
        int expected2 = 0;
        assertEquals(expected2, Main.readValue());
    }

    @Test
    public void testGetStringValue() {
        // Test case 1: Testing simple value
        String expected1 = "5";
        assertEquals(expected1, Main.getStringValue("5"));
        // Test case 2: Testing string value with variable
        Variable.setVariableValue("var", 10);
        String expected2 = "Hello 10";
        assertEquals(expected2, Main.getStringValue("Hello \"var\""));
    }

    @Test
    public void testIsNumeric() {
        // Test case 1: Testing numeric string
        assertTrue(Main.isNumeric("123"));
        // Test case 2: Testing non-numeric string
        assertFalse(Main.isNumeric("test"));
    }

}