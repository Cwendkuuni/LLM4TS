package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case for simple assignment elimination
    String sourceCode = "function foo() {" +
        "  var x = 1, y = x, z = y;" +
        "  x = 2;" +
        "  return x; // returns 2" +
        "}";

    CompilerOptions options = new CompilerOptions(new SourceFile("Test.java", sourceCode));
    Compiler compiler = new SimpleCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, options.getRootNode());
    Node result = options.getRootNode();

    String expectedSourceCode = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x; // returns 2" +
        "}";

    assertEquals(expectedSourceCode, result.toSource(options));

    // Test case for increment/decrement operator elimination
    sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  x++;" +
        "  x++;" +
        "  return x; // returns 4" +
        "}";

    options = new CompilerOptions(new SourceFile("Test.java", sourceCode));
    compiler = new SimpleCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, options.getRootNode());
    result = options.getRootNode();

    expectedSourceCode = "function foo() {" +
        "  var x = 1;" +
        "  x += 2;" +
        "  return x; // returns 4" +
        "}";

    assertEquals(expectedSourceCode, result.toSource(options));

    // Test case for dead assignment elimination in a condition
    sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x = 2) {" +
        "    x = 3;" +
        "  }" +
        "  return x; // returns 3" +
        "}";

    options = new CompilerOptions(new SourceFile("Test.java", sourceCode));
    compiler = new SimpleCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, options.getRootNode());
    result = options.getRootNode();

    expectedSourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (2) {" +
        "    x = 3;" +
        "  }" +
        "  return x; // returns 3" +
        "}";

    assertEquals(expectedSourceCode, result.toSource(options));

    // Test case for dead assignment elimination in a loop
    sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    x = i;" +
        "  }" +
        "  return x; // returns the last value of i, which is 10" +
        "}";

    options = new CompilerOptions(new SourceFile("Test.java", sourceCode));
    compiler = new SimpleCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, options.getRootNode());
    result = options.getRootNode();

    expectedSourceCode = "function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "  }" +
        "  return x; // returns the last value of i, which is 10" +
        "}";

    assertEquals(expectedSourceCode, result.toSource(options));

    // Test case for dead assignment elimination in a switch statement
    sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  switch (1) {" +
        "    case 1:" +
        "      x = 2;" +
        "      break;" +
        "    case 2:" +
        "      x = 3;" +
        "      break;" +
        "  }" +
        "  return x; // returns 3" +
        "}";

    options = new CompilerOptions(new SourceFile("Test.java", sourceCode));
    compiler = new SimpleCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, options.getRootNode());
    result = options.getRootNode();

    expectedSourceCode = "function foo() {" +
        "  var x = 1;" +
        "  switch (1) {" +
        "    case 1:" +
        "      break;" +
        "    case 2:" +
        "      break;" +
        "  }" +
        "  return x; // returns 1" +
        "}";

    assertEquals(expectedSourceCode, result.toSource(options));

    // Test case for dead assignment elimination in a return statement
    sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  return x = 2; // returns 2" +
        "}";

    options = new CompilerOptions(new SourceFile("Test.java", sourceCode));
    compiler = new SimpleCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, options.getRootNode());
    result = options.getRootNode();

    expectedSourceCode = "function foo() {" +
        "  return 2; // returns 2" +
        "}";

    assertEquals(expectedSourceCode, result.toSource(options));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInput() {
    String sourceCode = "function foo() {}";
    CompilerOptions options = new CompilerOptions(new SourceFile("Test.java", sourceCode));
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
    deadAssignmentsElimination.process(null, options.getRootNode());
  }
}