package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testLoadInstructions_emptyFileName() {
        assertFalse(Main.loadInstructions(""));
    }

    @Test
    public void testLoadInstructions_validFileName() {
        assertTrue(Main.loadInstructions("test.txt"));
    }

    @Test
    public void testRemoveComment_noComment() {
        assertEquals("mov abc, 123", Main.removeComment("mov abc, 123"));
    }

    @Test
    public void testRemoveComment_withComment() {
        assertEquals("mov abc, 123", Main.removeComment("mov abc, 123; this is a comment"));
    }

    @Test
    public void testSplitArguments_singleArgument() {
        String[] expected = {"abc"};
        assertArrayEquals(expected, Main.splitArguments("abc"));
    }

    @Test
    public void testSplitArguments_multipleArguments() {
        String[] expected = {"abc", "123"};
        assertArrayEquals(expected, Main.splitArguments("abc, 123"));
    }

    @Test
    public void testGetValue_numericOperand() {
        assertEquals(123, Main.getValue("123"));
    }

    @Test
    public void testGetValue_variableOperand() {
        Variable.setVariableValue("abc", 123);
        assertEquals(123, Main.getValue("abc"));
    }

    @Test
    public void testGetJumpValue_numericOperand() {
        assertEquals(5, Main.getJumpValue("5"));
    }

    @Test
    public void testGetJumpValue_labelOperand() {
        Label.addLabel("testLabel", 10);
        assertEquals(9, Main.getJumpValue("testLabel"));
    }

    @Test
    public void testIsNumeric_numericString() {
        assertTrue(Main.isNumeric("123"));
    }

    @Test
    public void testIsNumeric_nonNumericString() {
        assertFalse(Main.isNumeric("abc"));
    }

    @Test
    public void testGetStringValue_numericOperand() {
        assertEquals("123", Main.getStringValue("123"));
    }

    @Test
    public void testGetStringValue_variableOperand() {
        Variable.setVariableValue("abc", 123);
        assertEquals("123", Main.getStringValue("abc"));
    }

}