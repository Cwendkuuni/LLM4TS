import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jipa.Main;

public class MainTest {
    
    @Test
    public void testProcessInstruction() {
        // Test case 1: opcode = "halt"
        Main.processInstruction("halt");
        assertEquals(0, Main.iPtr);
        
        // Test case 2: opcode = "out", operand = {"Hello"}
        Main.processInstruction("out Hello");
        assertEquals("Hello", System.out.toString().trim());
        
        // Test case 3: opcode = "in", operand = {var1}
        Main.processInstruction("in var1");
        assertEquals(0, Variable.getVariableValue("var1"));
        
        // Test case 4: opcode = "mov", operand = {var1, var2}
        Variable.setVariableValue("var1", 5);
        Main.processInstruction("mov var1 var2");
        assertEquals(5, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var1"));
        
        // Test case 5: opcode = "cpy", operand = {var1, var2}
        Variable.setVariableValue("var1", 5);
        Main.processInstruction("cpy var1 var2");
        assertEquals(5, Variable.getVariableValue("var2"));
        
        // Add more test cases for all opcodes
        
    }
    
    @Test
    public void testRemoveComment() {
        // Test case 1: line = "mov var1 var2 ; This is a comment"
        String result = Main.removeComment("mov var1 var2 ; This is a comment");
        assertEquals("mov var1 var2", result);
        
        // Test case 2: line = "out Hello ; Comment ; Another Comment"
        String result = Main.removeComment("out Hello ; Comment ; Another Comment");
        assertEquals("out Hello", result);
        
        // Test case 3: line = "var var1 ; This is a variable"
        String result = Main.removeComment("var var1 ; This is a variable");
        assertEquals("var var1", result);
        
        // Add more test cases
    }
    
    // Add test cases for other methods in the Main class
    
}