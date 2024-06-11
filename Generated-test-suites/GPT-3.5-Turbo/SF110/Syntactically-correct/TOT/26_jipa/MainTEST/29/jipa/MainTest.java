import org.junit.*;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testLoadInstructions_validFile() {
        String fileName = "instructions.txt";
        assertTrue(Main.loadInstructions(fileName));
        // Assert that total instructions should be greater than 0
        assertTrue(Main.TOTAL_INSTRUCTIONS > 0);
        // Assert that instruction array should not be null
        assertNotNull(Main.instruction);
    }

    @Test
    public void testLoadInstructions_invalidFile() {
        String fileName = "invalid.txt";
        assertFalse(Main.loadInstructions(fileName));
    }

    @Test
    public void testRemoveComment_noComment() {
        String line = "mov a, b";
        assertEquals(line, Main.removeComment(line));
    }

    @Test
    public void testRemoveComment_withComment() {
        String line = "jmp label ; jump to label";
        assertEquals("jmp label", Main.removeComment(line));
    }

    @Test
    public void testRemoveComment_withinQuotes() {
        String line = "out \"Hello, world! ; This is a comment\"";
        assertEquals(line, Main.removeComment(line));
    }

    @Test
    public void testResetInstructions() {
        Main.TOTAL_INSTRUCTIONS = 5;
        Main.instruction = new String[5];
        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; i++) {
            Main.instruction[i] = "instruction " + (i+1);
        }
        
        Main.resetInstructions();
        
        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; i++) {
            assertEquals("", Main.instruction[i]);
        }
    }

    @Test
    public void testSplitArguments_singleArgument() {
        String original = "out a";
        String[] expected = {"a"};
        assertArrayEquals(expected, Main.splitArguments(original));
    }

    @Test
    public void testSplitArguments_multipleArguments() {
        String original = "add a, b, 3";
        String[] expected = {"a", "b", "3"};
        assertArrayEquals(expected, Main.splitArguments(original));
    }

    @Test
    public void testGetValue_numericOperand() {
        String operand = "5";
        assertEquals(5, Main.getValue(operand));
    }

    @Test
    public void testGetValue_variableOperand() {
        String operand = "a";
        Variable.addVariable(operand);
        Variable.setVariableValue(operand, 10);
        assertEquals(10, Main.getValue(operand));
    }

    @Test
    public void testGetJumpValue_numericOperand() {
        String operand = "10";
        assertEquals(9, Main.getJumpValue(operand));
    }

    @Test
    public void testGetJumpValue_labelOperand() {
        String operand = "label";
        Label.addLabel(operand, 5);
        assertEquals(4, Main.getJumpValue(operand));
    }

    @Test
    public void testReadValue_numericInput() {
        String input = "20";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals(20, Main.readValue());
        System.setIn(System.in);
    }

    @Test
    public void testReadValue_invalidInput() {
        String input = "abc";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals(0, Main.readValue());
        System.setIn(System.in);
    }

    @Test
    public void testGetStringValue_numericOperand() {
        String operand = "5";
        assertEquals("5", Main.getStringValue(operand));
    }

    @Test
    public void testGetStringValue_variableOperand() {
        String operand = "a";
        Variable.addVariable(operand);
        Variable.setVariableValue(operand, 10);
        assertEquals("10", Main.getStringValue(operand));
    }

    @Test
    public void testGetStringValue_stringOperand() {
        String operand = "\"Hello, world!\"";
        assertEquals("Hello, world!", Main.getStringValue(operand));
    }

    @Test
    public void testIsNumeric_numericString() {
        String str = "123";
        assertTrue(Main.isNumeric(str));
    }

    @Test
    public void testIsNumeric_nonNumericString() {
        String str = "abc";
        assertFalse(Main.isNumeric(str));
    }
}