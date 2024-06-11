import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testProcess() {
    // Test normal assignment operation
    String input = "x = 5;\n";
    String expected = "";
    assertEquals(expected, process(input));

    // Test increment assignment operation
    input = "x++;\n";
    expected = "";
    assertEquals(expected, process(input));

    // Test decrement assignment operation
    input = "x--;\n";
    expected = "";
    assertEquals(expected, process(input));

    // Test assignment operation with dead variable
    input = "int x;\n" +
            "x = 5;\n" +
            "int y = x;";
    expected = "int x;\n" +
               "int y = x;";
    assertEquals(expected, process(input));
  }

  /**
   * Helper method to process the Java class and return the resulting code
   */
  private String process(String input) {
    // Create the compiler and DeadAssignmentsElimination instance
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

    // Set up the test case
    String externs = "";
    String sourceCode = "package com.google.javascript.jscomp;\n" +
                        "public class TestClass {\n" +
                        "  " + input + "\n" +
                        "}";

    // Process the code
    elimination.process(externs, sourceCode);

    // Get the resulting code
    String result = compiler.toSource();

    return result;
  }
}