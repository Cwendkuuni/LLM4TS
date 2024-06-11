package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PeepholeOptimizationsPassTest {

  private Compiler compiler;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    pass = new PeepholeOptimizationsPass(compiler,
        new ConstantFoldingOptimization(),
        new DeadCodeEliminationOptimization(),
        new MinimizationOptimization());
  }

  @Test
  public void testPeepholeOptimizations() {
    String input = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = a + b;" +
        "  if (c > 3) {" +
        "    return 'greater than three';" +
        "  } else {" +
        "    return 42;" +
        "  }" +
        "}" +
        "function bar() {" +
        "  return foo();" +
        "}" +
        "bar();";

    Node root = NodeOperations.parseJsonString(input);
    pass.process(null, root);

    Node expectedOutput = NodeOperations.parseJsonString("" +
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = 1 + 2;" +
        "  if (c > 3) {" +
        "    return 'greater than three';" +
        "  } else {" +
        "    return 42;" +
        "  }" +
        "}" +
        "function bar() {" +
        "  return 'greater than three';" +
        "}" +
        "bar();");

    assertEquals(expectedOutput.toString(), root.toString());
  }

  @Test
  public void testDeadCodeElimination() {
    String input = "function foo() {" +
        "  var a = 1;" +
        "  if (false) {" +
        "    var b = 2;" +
        "  }" +
        "  return a;" +
        "}" +
        "foo();";

    Node root = NodeOperations.parseJsonString(input);
    pass.process(null, root);

    Node expectedOutput = NodeOperations.parseJsonString("function foo() {" +
        "  return 1;" +
        "}foo();");

    assertEquals(expectedOutput.toString(), root.toString());
  }

  @Test
  public void testMinimization() {
    String input = "function foo(a, b, c) {" +
        "  var d = Math.sqrt(a * a + b * b);" +
        "  return d * c;" +
        "}" +
        "foo(1, 2, 3);";

    Node root = NodeOperations.parseJsonString(input);
    pass.process(null, root);

    Node expectedOutput = NodeOperations.parseJsonString("function foo(a, b, c) {" +
        "  return Math.sqrt(a * a + b * b) * c;" +
        "}" +
        "foo(1, 2, 3);");

    assertEquals(expectedOutput.toString(), root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testCompilationError() {
    String input = "function foo() {" +
        "  return 1 / 0;" +
        "}" +
        "foo();";

    pass.process(null, NodeOperations.parseJsonString(input));
  }
}