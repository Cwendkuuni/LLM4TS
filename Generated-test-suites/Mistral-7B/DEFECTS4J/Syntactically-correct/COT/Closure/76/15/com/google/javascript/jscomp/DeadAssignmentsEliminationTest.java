package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case 1: Dead assignment elimination for simple assignment
    String sourceCode1 = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options, null);
    compiler.addPass(deadAssignmentsElimination);
    RewrittenSourceFile out = compiler.compile(sourceCode1, null, null);
    String resultCode1 = out.getCode();
    assertTrue(resultCode1.equals("function foo() {" +
        "  return 2;" +
        "}"));

    // Test case 2: Dead assignment elimination for increment operator
    String sourceCode2 = "function foo() {" +
        "  var x = 0;" +
        "  x++;" +
        "  x++;" +
        "  return x;" +
        "}";
    DeadAssignmentsElimination deadAssignmentsElimination2 = new DeadAssignmentsElimination(null);
    Compiler compiler2 = new Compiler(options, null);
    compiler2.addPass(deadAssignmentsElimination2);
    RewrittenSourceFile out2 = compiler2.compile(sourceCode2, null, null);
    String resultCode2 = out2.getCode();
    assertTrue(resultCode2.equals("function foo() {" +
        "  return 2;" +
        "}"));

    // Test case 3: Dead assignment elimination for decrement operator
    String sourceCode3 = "function foo() {" +
        "  var x = 3;" +
        "  x--;" +
        "  x--;" +
        "  return x;" +
        "}";
    DeadAssignmentsElimination deadAssignmentsElimination3 = new DeadAssignmentsElimination(null);
    Compiler compiler3 = new Compiler(options, null);
    compiler3.addPass(deadAssignmentsElimination3);
    RewrittenSourceFile out3 = compiler3.compile(sourceCode3, null, null);
    String resultCode3 = out3.getCode();
    assertTrue(resultCode3.equals("function foo() {" +
        "  return 1;" +
        "}"));

    // Test case 4: Dead assignment elimination for assignment within condition
    String sourceCode4 = "function foo() {" +
        "  var x = 0;" +
        "  if (x = 1) {" +
        "    return 1;" +
        "  } else {" +
        "    return 0;" +
        "  }" +
        "}";
    DeadAssignmentsElimination deadAssignmentsElimination4 = new DeadAssignmentsElimination(null);
    Compiler compiler4 = new Compiler(options, null);
    compiler4.addPass(deadAssignmentsElimination4);
    RewrittenSourceFile out4 = compiler4.compile(sourceCode4, null, null);
    String resultCode4 = out4.getCode();
    assertTrue(resultCode4.equals("function foo() {" +
        "  if (1) {" +
        "    return 1;" +
        "  } else {" +
        "    return 0;" +
        "  }" +
        "}"));

    // Test case 5: Dead assignment elimination for assignment within loop
    String sourceCode5 = "function foo() {" +
        "  var x = 0;" +
        "  for (; x < 10; x++) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";
    DeadAssignmentsElimination deadAssignmentsElimination5 = new DeadAssignmentsElimination(null);
    Compiler compiler5 = new Compiler(options, null);
    compiler5.addPass(deadAssignmentsElimination5);
    RewrittenSourceFile out5 = compiler5.compile(sourceCode5, null, null);
    String resultCode5 = out5.getCode();
    assertTrue(resultCode5.equals("function foo() {" +
        "  for (; x < 10; x++) {" +
        "  }" +
        "  return 10;" +
        "}"));

    // Test case 6: Dead assignment elimination for dead variable assignment
    String sourceCode6 = "function foo() {" +
        "  var x = 0;" +
        "  var y = 1;" +
        "  x = y;" +
        "  y = 3;" +
        "  return x;" +
        "}";
    DeadAssignmentsElimination deadAssignmentsElimination6 = new DeadAssignmentsElimination(null);
    Compiler compiler6 = new Compiler(options, null);
    compiler6.addPass(deadAssignmentsElimination6);
    RewrittenSourceFile out6 = compiler6.compile(sourceCode6, null, null);
    String resultCode6 = out6.getCode();
    assertTrue(resultCode6.equals("function foo() {" +
        "  var y = 3;" +
        "  return y;" +
        "}"));

    // Test case 7: Dead assignment elimination for dead variable assignment within condition
    String sourceCode7 = "function foo() {" +
        "  var x = 0;" +
        "  var y = 1;" +
        "  if (x = y) {" +
        "    return 1;" +
        "  } else {" +
        "    return 0;" +
        "  }" +
        "}";
    DeadAssignmentsElimination deadAssignmentsElimination7 = new DeadAssignmentsElimination(null);
    Compiler compiler7 = new Compiler(options, null);
    compiler7.addPass(deadAssignmentsElimination7);
    RewrittenSourceFile out7 = compiler7.compile(sourceCode7, null, null);
    String resultCode7 = out7.getCode();
    assertTrue(resultCode7.equals("function foo() {" +
        "  if (1) {" +
        "    return 1;" +
        "  } else {" +
        "    return 0;" +
        "  }" +
        "}"));
  }
}