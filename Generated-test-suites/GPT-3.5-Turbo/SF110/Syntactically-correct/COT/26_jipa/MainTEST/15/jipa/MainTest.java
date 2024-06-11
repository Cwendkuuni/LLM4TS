
import org.junit.Assert;
import org.junit.Test;

public class MainTest {
  
  // Test for processInstruction() method
  @Test
  public void testProcessInstruction() {
    String instruction = "halt";
    Main.processInstruction(instruction);
    // Check if the system exits after the 'halt' instruction
    Assert.assertEquals(0, Main.iPtr);

    instruction = "out Hello";
    Main.processInstruction(instruction);
    // Check if the system prints correctly
    Assert.assertEquals("Hello", // Expected output
                        outContent.toString().trim()); // Actual output is captured in 'outContent'
  }
  
  // Test for getFileName() method
  @Test
  public void testGetFileName() {
    // Manual testing required as the method takes user input
  }
  
  // Test for loadInstructions() method
  @Test
  public void testLoadInstructions() {
    // Manual testing required as the method loads instructions from a file
  }
  
  // Test for removeComment() method
  @Test
  public void testRemoveComment() {
    String line = "var x; ; This is a comment";
    String result = Main.removeComment(line);
    // Check if the comment is removed
    Assert.assertEquals("var x;", result);
  }
  
  // Test for resetInstructions() method (Not necessary as it is a void method with no return value)
  
  // Test for splitArguments() method
  @Test
  public void testSplitArguments() {
    String original = "mov x, y";
    String[] result = Main.splitArguments(original);
    // Check if the arguments are split correctly
    Assert.assertArrayEquals(new String[]{"x", "y"}, result);
  }
  
  // Test for getValue() method
  @Test
  public void testGetValue() {
    String operand = "10";
    int result = Main.getValue(operand);
    // Check if the numeric operand is returned correctly
    Assert.assertEquals(10, result);
    
    operand = "x";
    Variable.addVariable(operand);
    Variable.setVariableValue(operand, 20);
    result = Main.getValue(operand);
    // Check if the variable operand is returned correctly
    Assert.assertEquals(20, result);
  }
  
  // Test for getJumpValue() method
  @Test
  public void testGetJumpValue() {
    String operand = "10";
    int result = Main.getJumpValue(operand);
    // Check if the numeric operand is returned correctly
    Assert.assertEquals(9, result); // Subtracted by 1 as the instruction pointer is incremented after each instruction
    
    operand = "label";
    Label.addLabel(operand, 5);
    result = Main.getJumpValue(operand);
    // Check if the label operand is returned correctly
    Assert.assertEquals(4, result); // Subtracted by 1 as the instruction pointer is incremented after each instruction
  }
  
  // Test for readValue() method (Manual testing required as it takes user input)
  
  // Test for getStringValue() method
  @Test
  public void testGetStringValue() {
    String operand = "10";
    String result = Main.getStringValue(operand);
    // Check if the numeric operand is returned correctly
    Assert.assertEquals("10", result);
    
    operand = "\"Hello\"";
    result = Main.getStringValue(operand);
    // Check if the string operand is returned correctly
    Assert.assertEquals("Hello", result);
    
    operand = "\"Hello\" + 10";
    result = Main.getStringValue(operand);
    // Check if the expression is evaluated correctly
    Assert.assertEquals("Hello10", result);
  }
  
  // Test for isNumeric() method
  @Test
  public void testIsNumeric() {
    String str = "10";
    boolean result = Main.isNumeric(str);
    // Check if the numeric string returns true
    Assert.assertTrue(result);
    
    str = "x";
    result = Main.isNumeric(str);
    // Check if the non-numeric string returns false
    Assert.assertFalse(result);
  }
  
  // Test for toOpcode() method (Not necessary as it is part of enum)
  
}
