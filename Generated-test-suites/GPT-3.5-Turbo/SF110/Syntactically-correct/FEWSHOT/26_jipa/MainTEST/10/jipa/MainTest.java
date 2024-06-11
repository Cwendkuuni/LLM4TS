package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testProcessInstruction() {
        // Testing halt opcode
        Main.processInstruction("halt");
        // Testing out opcode
        Main.processInstruction("out \"Hello\"");
        // Testing in opcode
        Main.processInstruction("in testVar");
        // Testing mov opcode
        Main.processInstruction("mov var1, var2");
        // Testing cpy opcode
        Main.processInstruction("cpy var3, var4");
        // Testing set opcode
        Main.processInstruction("set var5, 10");
        // Testing del opcode
        Main.processInstruction("del var6");
        // Testing arr opcode
        Main.processInstruction("arr arr1, 5");
        // Testing var opcode
        Main.processInstruction("var var7");
        // Testing inc opcode
        Main.processInstruction("inc var8");
        // Testing dec opcode
        Main.processInstruction("dec var9");
        // Testing add opcode
        Main.processInstruction("add var10, 5");
        // Testing sub opcode
        Main.processInstruction("sub var11, 10");
        // Testing mul opcode
        Main.processInstruction("mul var12, 3");
        // Testing div opcode
        Main.processInstruction("div var13, 2");
        // Testing mod opcode
        Main.processInstruction("mod var14, 7");
        // Testing xor opcode
        Main.processInstruction("xor var15, 3");
        // Testing or opcode
        Main.processInstruction("or var16, 5");
        // Testing and opcode
        Main.processInstruction("and var17, 7");
        // Testing jmp opcode
        Main.processInstruction("jmp 14");
        // Testing jnz opcode
        Main.processInstruction("jnz var18, 15");
        // Testing jz opcode
        Main.processInstruction("jz var19");
        // Testing je opcode
        Main.processInstruction("je var20, var21");
        // Testing ja opcode
        Main.processInstruction("ja var22, var23, 16");
        // Testing jae opcode
        Main.processInstruction("jae var24, var25, 17");
        // Testing jb opcode
        Main.processInstruction("jb var26, var27, 18");
        // Testing jbe opcode
        Main.processInstruction("jbe var28, var29, 19");
        // Testing jne opcode
        Main.processInstruction("jne var30, var31, 20");
        // Testing cmp opcode
        Main.processInstruction("cmp var32, var33, var34");
        
        // Assert statements here...
    }

    @Test
    public void testGetFileName() {
        // Test cases for get file name
        String[] args = { "file.txt" };
        String expected = "file.txt";
        assertEquals(expected, Main.getFileName());
        
        args[0] = "";
        expected = "";
        assertEquals(expected, Main.getFileName());
    }

    @Test
    public void testLoadInstructions() {
        // Test cases for load instructions
        String fileName = "file.txt";
        boolean expected = true;
        assertEquals(expected, Main.loadInstructions(fileName));
        
        fileName = "";
        expected = false;
        assertEquals(expected, Main.loadInstructions(fileName));
    }

    @Test
    public void testRemoveComment() {
        // Test cases for remove comment
        String line = "instruction # comment";
        String expected = "instruction";
        assertEquals(expected, Main.removeComment(line));
        
        line = "instruction";
        expected = "instruction";
        assertEquals(expected, Main.removeComment(line));
    }

    @Test
    public void testResetInstructions() {
        // Test case for reset instructions
        Main.resetInstructions();
        // Assert statements here...
    }
    
    @Test
    public void testSplitArguments() {
        // Test cases for split arguments
        String original = "arg1, arg2, arg3";
        String[] expected = {"arg1", "arg2", "arg3"};
        assertArrayEquals(expected, Main.splitArguments(original));
        
        original = "arg1";
        expected = new String[] { "arg1" };
        assertArrayEquals(expected, Main.splitArguments(original));
    }
    
    @Test
    public void testGetValue() {
        // Test cases for get value
        String operand = "arg1";
        int expected = 0;
        assertEquals(expected, Main.getValue(operand));
        
        operand = "10";
        expected = 10;
        assertEquals(expected, Main.getValue(operand));
    }
    
    @Test
    public void testGetJumpValue() {
        // Test case for get jump value
        String operand = "10";
        int expected = 9;
        assertEquals(expected, Main.getJumpValue(operand));
        
        operand = "label";
        expected = -1;
        assertEquals(expected, Main.getJumpValue(operand));
    }
    
    @Test
    public void testReadValue() {
        // Test case for read value
        int expected = 0;
        assertEquals(expected, Main.readValue());
        
        // Assert statements here...
    }
    
    @Test
    public void testGetStringValue() {
        // Test case for get string value
        String original = "10";
        String expected = "10";
        assertEquals(expected, Main.getStringValue(original));
        
        original = "\"hello\"";
        expected = "hello";
        assertEquals(expected, Main.getStringValue(original));
    }
    
    @Test
    public void testIsNumeric() {
        // Test cases for is numeric
        String str = "10";
        boolean expected = true;
        assertEquals(expected, Main.isNumeric(str));
        
        str = "hello";
        expected = false;
        assertEquals(expected, Main.isNumeric(str));
    }
}