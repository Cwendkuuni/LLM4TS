import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {
    @Test
    public void testProcessInstruction() {
        // Test cases for halt opcode
        assertEquals(0, Main.iPtr);
        Main.processInstruction("halt");
        assertEquals(0, Main.iPtr);
        
        // Test cases for out opcode
        assertEquals(0, Main.iPtr);
        assertEquals("foo", Main.getStringValue("foo"));
        Main.processInstruction("out foo");
        assertEquals(1, Main.iPtr);
        
        // Test cases for in opcode
        assertEquals(1, Main.iPtr);
        Main.processInstruction("in var");
        assertNull(Variable.getVariableValue("var"));
        
        // Test cases for mov opcode
        assertEquals(1, Main.iPtr);
        Variable.setVariableValue("var1", 5);
        Variable.setVariableValue("var2", 0);
        Main.processInstruction("mov var1,var2");
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(5, Variable.getVariableValue("var2"));
        
        // Test cases for cpy opcode
        assertEquals(1, Main.iPtr);
        Variable.setVariableValue("var1", 5);
        Variable.setVariableValue("var2", 0);
        Main.processInstruction("cpy var1,var2");
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(5, Variable.getVariableValue("var2"));   
    }

    @Test
    public void testLoadInstructions() {
        // Test case for valid file
        String fileName = "instructions.txt";
        assertTrue(Main.loadInstructions(fileName));

        // Test case for invalid file
        fileName = "invalid.txt";
        assertFalse(Main.loadInstructions(fileName));
    }

    @Test
    public void testRemoveComment() {
        // Test case for line without comment
        String line = "mov var1,var2";
        assertEquals(line, Main.removeComment(line));

        // Test case for line with comment
        line = "mov var1,var2 ; comment";
        assertEquals("mov var1,var2", Main.removeComment(line));

        // Test case for line with double quote in comment
        line = "mov var1,var2 ; comment ; comment\"\"";
        assertEquals("mov var1,var2", Main.removeComment(line));
    }

    @Test
    public void testResetInstructions() {
        // Set up instructions
        Main.TOTAL_INSTRUCTIONS = 3;
        Main.instruction = new String[Main.TOTAL_INSTRUCTIONS];
        Main.instruction[0] = "mov var1,var2";
        Main.instruction[1] = "add var1,var2";
        Main.instruction[2] = "sub var1,var2";
        
        // Reset instructions
        Main.resetInstructions();
        
        // Check if instructions are reset
        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; i++) {
            assertEquals("", Main.instruction[i]);
        }
    }

    @Test
    public void testSplitArguments() {
        // Test case for single argument
        String argument = "var1";
        String[] expected = {"var1"};
        assertArrayEquals(expected, Main.splitArguments(argument));

        // Test case for multiple arguments
        argument = "var1, var2, var3";
        expected = new String[] {"var1", "var2", "var3"};
        assertArrayEquals(expected, Main.splitArguments(argument));
    }

    @Test
    public void testGetValue() {
        // Test case for non-numeric operand
        String operand = "var1";
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Main.getValue(operand));

        // Test case for numeric operand
        operand = "10";
        assertEquals(10, Main.getValue(operand));
    }

    @Test
    public void testGetJumpValue() {
        // Test case for numeric operand
        String operand = "10";
        assertEquals(9, Main.getJumpValue(operand));

        // Test case for non-numeric operand
        operand = "label1";
        Label.addLabel("label1", 5);
        assertEquals(4, Main.getJumpValue(operand));
    }

    @Test
    public void testReadValue() {
        // Test case for numeric input
        System.setIn(new ByteArrayInputStream("10".getBytes()));
        assertEquals(10, Main.readValue());

        // Test case for non-numeric input
        System.setIn(new ByteArrayInputStream("abc".getBytes()));
        assertEquals(0, Main.readValue());
    }

    @Test
    public void testGetStringValue() {
        // Test case for single quoted string
        String argument = "\"hello\"";
        assertEquals("hello", Main.getStringValue(argument));

        // Test case for numeric value in string
        argument = "5";
        assertEquals("5", Main.getStringValue(argument));

        // Test case for string with numeric component
        argument = "\"abc\"5";
        assertEquals("abc5", Main.getStringValue(argument));
    }

    @Test
    public void testIsNumeric() {
        // Test case for numeric string
        String str = "10";
        assertTrue(Main.isNumeric(str));

        // Test case for non-numeric string
        str = "abc";
        assertFalse(Main.isNumeric(str));
    }

    @Test
    public void testOpcodesToOpcode() {
        // Test case for valid opcode
        String str = "mov";
        assertEquals(Main.opcodes.mov, Main.opcodes.toOpcode(str));

        // Test case for invalid opcode
        str = "invalid";
        assertEquals(Main.opcodes.novalue, Main.opcodes.toOpcode(str));
    }
}