import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {
    @Test
    public void testProcessInstruction() {
        String[] instruction = {"halt", "out A", "in A", "mov A B", "cpy A B", "set A 5", "del A", "arr A 3", "add A B", "sub A B", "mul A B", "div A B", "mod A B", "inc A", "dec A", "in A", "out A", "jmp A", "jnz A B", "jz A", "je A B", "ja A B", "jae A B", "jb A B", "jbe A B", "jne A B", "xor A B", "or A B", "and A B", "cmp A B"};
        for (String instr : instruction) {
            Main.processInstruction(instr);
        }
    }
    
    @Test
    public void testGetFileName() {
        // Not testable due to reliance on input from user
    }
    
    @Test
    public void testLoadInstructions() {
        assertTrue(Main.loadInstructions("testfile.txt"));
        assertFalse(Main.loadInstructions(""));
    }
    
    @Test
    public void testRemoveComment() {
        assertEquals("processInstruction(Main.instruction[Main.iPtr]);", Main.removeComment("processInstruction(Main.instruction[Main.iPtr]); // Process the instruction"));
        assertEquals("opcode = instruction.substring(0, spaceLoc);", Main.removeComment("opcode = instruction.substring(0, spaceLoc); // Get the opcode"));
    }
    
    @Test
    public void testResetInstructions() {
        Main.resetInstructions();
        for (String instr : Main.instruction) {
            assertTrue(instr.isEmpty());
        }
    }
    
    @Test
    public void testSplitArguments() {
        String[] args = {"arg1", "arg2"};
        assertArrayEquals(args, Main.splitArguments("arg1, arg2"));
        args = new String[]{"arg1", "arg2", "arg3"};
        assertArrayEquals(args, Main.splitArguments("arg1, arg2, arg3"));
        args = new String[]{"arg"};
        assertArrayEquals(args, Main.splitArguments("arg"));
    }
    
    @Test
    public void testGetValue() {
        Main.opcodes.opcodes haltOpcode = Main.opcodes.toOpcode("halt");
        Main.opcodes.opcodes novalueOpcode = Main.opcodes.toOpcode("novalue");
        assertEquals(0, Main.getValue("0"));
        assertEquals(5, Main.getValue("5"));
        assertEquals(haltOpcode.ordinal(), Main.getValue("halt"));
        assertEquals(novalueOpcode.ordinal(), Main.getValue("invalid"));
    }
    
    @Test
    public void testGetJumpValue() {
        Main.setVariableValue("label", 5);
        assertEquals(4, Main.getJumpValue("4"));
        assertEquals(5, Main.getJumpValue("label"));
        assertEquals(0, Main.getJumpValue("invalid"));
    }
    
    @Test 
    public void testReadValue() {
        // Not testable due to reliance on input from user
    }
    
    @Test
    public void testGetStringValue() {
        Main.setVariableValue("var", 5);
        assertEquals("5", Main.getStringValue("var"));
        assertEquals("ABC123", Main.getStringValue("\"ABC\" + 123"));
    }
    
    @Test
    public void testIsNumeric() {
        assertTrue(Main.isNumeric("0"));
        assertTrue(Main.isNumeric("5"));
        assertFalse(Main.isNumeric("ABC"));
    }
}