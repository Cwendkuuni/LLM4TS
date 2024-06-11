import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testLoadInstructions() {
        // Typical use case - valid file name
        String validFileName = "testFile.txt";
        assertTrue(Main.loadInstructions(validFileName));
        
        // Error scenario - empty file name
        String emptyFileName = "";
        assertFalse(Main.loadInstructions(emptyFileName));
        
        // Error scenario - file not found
        String nonExistentFileName = "nonexistent.txt";
        assertFalse(Main.loadInstructions(nonExistentFileName));
    }
    
    @Test
    void testRemoveComment() {
        // Typical use case - comment at end of line
        String line1 = "instruction ; comment";
        String expectedLine1 = "instruction";
        assertEquals(expectedLine1, Main.removeComment(line1));
        
        // Typical use case - comment in middle of line
        String line2 = "instruction ; comment ; more comment";
        String expectedLine2 = "instruction ; comment";
        assertEquals(expectedLine2, Main.removeComment(line2));
        
        // Typical use case - no comment
        String line3 = "instruction";
        String expectedLine3 = "instruction";
        assertEquals(expectedLine3, Main.removeComment(line3));
        
        // Edge case - multiple comments
        String line4 = "instruction ; comment1 ; comment2 ; comment3";
        String expectedLine4 = "instruction ; comment1 ; comment2 ; comment3";
        assertEquals(expectedLine4, Main.removeComment(line4));
    }
    
    @Test
    void testSplitArguments() {
        // Typical use case - multiple arguments
        String original1 = "arg1, arg2, arg3";
        String[] expectedArgs1 = {"arg1", "arg2", "arg3"};
        assertArrayEquals(expectedArgs1, Main.splitArguments(original1));
        
        // Typical use case - single argument
        String original2 = "arg1";
        String[] expectedArgs2 = {"arg1"};
        assertArrayEquals(expectedArgs2, Main.splitArguments(original2));
        
        // Edge case - no arguments
        String original3 = "";
        String[] expectedArgs3 = {""};
        assertArrayEquals(expectedArgs3, Main.splitArguments(original3));
    }
    
    @Test
    void testGetValue() {
        // Typical use case - numeric
        String operand1 = "10";
        int expectedValue1 = 10;
        assertEquals(expectedValue1, Main.getValue(operand1));
        
        // Typical use case - variable
        String operand2 = "variable";
        int expectedValue2 = Variable.getVariableValue(operand2);
        assertEquals(expectedValue2, Main.getValue(operand2));
        
        // Error scenario - invalid operand
        String operand3 = "invalid";
        int expectedValue3 = 0;
        assertEquals(expectedValue3, Main.getValue(operand3));
    }
    
    @Test
    void testGetJumpValue() {
        // Typical use case - numeric
        String operand1 = "10";
        int expectedValue1 = 9;
        assertEquals(expectedValue1, Main.getJumpValue(operand1));
        
        // Typical use case - label
        String operand2 = "label";
        int expectedValue2 = Label.findLabel(operand2) - 1;
        assertEquals(expectedValue2, Main.getJumpValue(operand2));
        
        // Error scenario - invalid operand
        String operand3 = "invalid";
        int expectedValue3 = 0;
        assertEquals(expectedValue3, Main.getJumpValue(operand3));
    }
    
    @Test
    void testReadValue() {
        // Typical use case - numeric input
        String input1 = "10";
        System.setIn(new ByteArrayInputStream(input1.getBytes()));
        int expectedValue1 = 10;
        assertEquals(expectedValue1, Main.readValue());
        
        // Typical use case - non-numeric input
        String input2 = "abcd";
        System.setIn(new ByteArrayInputStream(input2.getBytes()));
        int expectedValue2 = 0;
        assertEquals(expectedValue2, Main.readValue());
    }
    
    @Test
    void testGetStringValue() {
        // Typical use case - numeric
        String original1 = "10";
        String expectedValue1 = "10";
        assertEquals(expectedValue1, Main.getStringValue(original1));
        
        // Typical use case - variable
        String original2 = "variable";
        String expectedValue2 = Integer.toString(Main.getValue(original2));
        assertEquals(expectedValue2, Main.getStringValue(original2));
        
        // Typical use case - string with quotes
        String original3 = "\"hello\"";
        String expectedValue3 = "hello";
        assertEquals(expectedValue3, Main.getStringValue(original3));
        
        // Typical use case - string with variable and quotes
        String original4 = "variable \"hello\" variable";
        String expectedValue4 = Integer.toString(Main.getValue("variable")) + "hello" + 
                Integer.toString(Main.getValue("variable"));
        assertEquals(expectedValue4, Main.getStringValue(original4));
    }
    
    @Test
    void testIsNumeric() {
        // Typical use case - numeric
        String str1 = "10";
        assertTrue(Main.isNumeric(str1));
        
        // Error scenario - non-numeric
        String str2 = "abcd";
        assertFalse(Main.isNumeric(str2));
    }
    
    // Add tests for other methods in Main class
    
}