import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class MainTest {
    
    @Test
    public void testProcessInstruction_halt() {
        // Test when the instruction is halt
        String instruction = "halt";
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if the system has exited with code 0
        assertEquals(0, System.exitValue());
    }
    
    @Test
    public void testProcessInstruction_out() {
        // Test when the instruction is out
        String instruction = "out Hello World!";
        
        // Redirect system output to a new OutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if the expected output is printed
        assertEquals("Hello World!\n", outContent.toString());
    }
    
    @Test
    public void testProcessInstruction_in() {
        // Test when the instruction is in
        String instruction = "in variable";
        
        // Create a mock InputStream with desired input
        ByteArrayInputStream inContent = new ByteArrayInputStream("5\n".getBytes());
        System.setIn(inContent);
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if the variable has been set to the expected value
        assertEquals(5, Variable.getVariableValue("variable"));
    }
    
    @Test
    public void testProcessInstruction_mov() {
        // Test when the instruction is mov
        String instruction = "mov variable1 variable2";
        
        // Set the initial value of variable1
        Variable.setVariableValue("variable1", 10);
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if variable2 has been set to the value of variable1
        assertEquals(10, Variable.getVariableValue("variable2"));
        // Check if variable1 has been set to 0
        assertEquals(0, Variable.getVariableValue("variable1"));
    }
    
    @Test
    public void testProcessInstruction_cpy() {
        // Test when the instruction is cpy
        String instruction = "cpy variable1 variable2";
        
        // Set the initial value of variable1
        Variable.setVariableValue("variable1", 5);
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if variable2 has been set to the value of variable1
        assertEquals(5, Variable.getVariableValue("variable2"));
    }
    
    @Test
    public void testProcessInstruction_set() {
        // Test when the instruction is set
        String instruction = "set variable 7";
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if variable has been set to the expected value
        assertEquals(7, Variable.getVariableValue("variable"));
    }
    
    @Test
    public void testProcessInstruction_del() {
        // Test when the instruction is del
        String instruction = "del variable";
        
        // Set the initial value of variable
        Variable.setVariableValue("variable", 10);
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if variable has been removed
        assertNull(Variable.getVariableValue("variable"));
    }
    
    @Test
    public void testProcessInstruction_arr() {
        // Test when the instruction is arr
        String instruction = "arr array 5";
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if the array variable and its elements have been created
        assertNotNull(Variable.getVariableValue("array"));
        for (int i = 1; i < 5; i++) {
            assertNotNull(Variable.getVariableValue("array" + i));
        }
    }
    
    @Test
    public void testProcessInstruction_var() {
        // Test when the instruction is var
        String instruction = "var variable";
        
        // Call the method to be tested
        Main.processInstruction(instruction);
        
        // Add assertions here
        // Check if the variable has been created
        assertNotNull(Variable.getVariableValue("variable"));
    }
    
    // Add more test methods for other instructions
    
    // ...
    
    // End of test methods
    
}