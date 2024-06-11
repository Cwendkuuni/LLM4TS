import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    
    @Test
    public void testLoadInstructions() {
        // Typical case
        assertTrue(Main.loadInstructions("test_instructions.txt"));
        
        // Edge case
        assertFalse(Main.loadInstructions(""));
    }
    
    @Test
    public void testRemoveComment() {
        // Typical case
        assertEquals("mov operand1, operand2", Main.removeComment("mov operand1, operand2; comment"));
        
        // Edge case
        assertEquals("jmp operand", Main.removeComment("jmp operand;"));
    }
    
    @Test
    public void testSplitArguments() {
        // Typical case
        String[] expected = {"operand1", "operand2"};
        assertArrayEquals(expected, Main.splitArguments("operand1, operand2"));
        
        // Edge case
        String[] singleValueExpected = {"operand"};
        assertArrayEquals(singleValueExpected, Main.splitArguments("operand"));
    }
    
    // Add more test cases for other methods
    
}