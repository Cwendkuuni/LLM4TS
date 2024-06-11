package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphTestUtils;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    String sourceCode = "function foo() {" +
                       "  var x = 1, y = 2;" +
                       "  if (true) {" +
                       "    x = 3;" +
                       "  } else {" +
                       "    y = 4;" +
                       "  }" +
                       "  return x + y;" +
                       "}";

    Node root = NodeFactory.createProgram(NodeFactory.SCOPE_GLOBAL, null,
        NodeFactory.createStringLiteral(sourceCode));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Node expectedResult = NodeUtils.createString("function foo() {" +
                                                "  return 3 + 2;" +
                                                "}");

    String result = GraphTestUtils.getSourceCodeFromTree(root);
    assertTrue("Unexpected result", result.equals(expectedResult.toString()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithConditionFalse() {
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    String sourceCode = "function foo() {" +
                       "  var x = 1, y = 2;" +
                       "  if (false) {" +
                       "    x = 3;" +
                       "  } else {" +
                       "    y = 4;" +
                       "  }" +
                       "  return x + y;" +
                       "}";

    Node root = NodeFactory.createProgram(NodeFactory.SCOPE_GLOBAL, null,
        NodeFactory.createStringLiteral(sourceCode));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Node expectedResult = NodeUtils.createString("function foo() {" +
                                                "  return 1 + 2;" +
                                                "}");

    String result = GraphTestUtils.getSourceCodeFromTree(root);
    assertTrue("Unexpected result", result.equals(expectedResult.toString()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunctionCall() {
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    String sourceCode = "function foo() {" +
                       "  var x = 1, y = 2;" +
                       "  if (true) {" +
                       "    x = someFunction();" +
                       "  } else {" +
                       "    y = 4;" +
                       "  }" +
                       "  return x + y;" +
                       "}" +
                       "function someFunction() {" +
                       "  return 3;" +
                       "}";

    Node root = NodeFactory.createProgram(NodeFactory.SCOPE_GLOBAL, null,
        NodeFactory.createStringLiteral(sourceCode));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Node expectedResult = NodeUtils.createString("function foo() {" +
                                                "  return 3 + 2;" +
                                                "}");

    String result = GraphTestUtils.getSourceCodeFromTree(root);
    assertTrue("Unexpected result", result.equals(expectedResult.toString()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithLoop() {
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    String sourceCode = "function foo() {" +
                       "  var x = 1, i = 0;" +
                       "  for (i = 0; i < 10; i++) {" +
                       "    x = i;" +
                       "  }" +
                       "  return x;" +
                       "}";

    Node root = NodeFactory.createProgram(NodeFactory.SCOPE_GLOBAL, null,
        NodeFactory.createStringLiteral(sourceCode));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Node expectedResult = NodeUtils.createString("function foo() {" +
                                                "  return 10;" +
                                                "}");

    String result = GraphTestUtils.getSourceCodeFromTree(root);
    assertTrue("Unexpected result", result.equals(expectedResult.toString()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithNestedConditions() {
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    String sourceCode = "function foo() {" +
                       "  var x = 1, y = 2;" +
                       "  if (true) {" +
                       "    if (false) {" +
                       "      x = 3;" +
                       "    } else {" +
                       "      y = 4;" +
                       "    }" +
                       "  }" +
                       "  return x + y;" +
                       "}";

    Node root = NodeFactory.createProgram(NodeFactory.SCOPE_GLOBAL, null,
        NodeFactory.createStringLiteral(sourceCode));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Node expectedResult = NodeUtils.createString("function foo() {" +
                                                "  return 1 + 2;" +
                                                "}");

    String result = GraphTestUtils.getSourceCodeFromTree(root);
    assertTrue("Unexpected result", result.equals(expectedResult.toString()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithMultipleAssignments() {
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    String sourceCode = "function foo() {" +
                       "  var x = 1, y = 2, z = 3;" +
                       "  if (true) {" +
                       "    x = 4;" +
                       "    y = 5;" +
                       "  } else {" +
                       "    z = 6;" +
                       "  }" +
                       "  return x + y + z;" +
                       "}";

    Node root = NodeFactory.createProgram(NodeFactory.SCOPE_GLOBAL, null,
        NodeFactory.createStringLiteral(sourceCode));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Node expectedResult = NodeUtils.createString("function foo() {" +
                                                "  return 4 + 5 + 3;" +
                                                "}");

    String result = GraphTestUtils.getSourceCodeFromTree(root);
    assertTrue("Unexpected result", result.equals(expectedResult.toString()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithRecursiveFunctionCall() {
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    String sourceCode = "function foo(x) {" +
                       "  if (x > 0) {" +
                       "    return foo(x - 1) + 1;" +
                       "  } else {" +
                       "    return 0;" +
                       "  }" +
                       "}" +
                       "function bar() {" +
                       "  var result = 0;" +
                       "  for (var i = 0; i < 10; i++) {" +
                       "    result += foo(i);" +
                       "  }" +
                       "  return result;" +
                       "}";

    Node root = NodeFactory.createProgram(NodeFactory.SCOPE_GLOBAL, null,
        NodeFactory.createStringLiteral(sourceCode));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Node expectedResult = NodeUtils.createString("function bar() {" +
                                                "  var result = 0;" +
                                                "  for (var i = 0; i < 10; i++) {" +
                                                "    result += i;" +
                                                "  }" +
                                                "  return result;" +
                                                "}");

    String result = GraphTestUtils.getSourceCodeFromTree(root);
    assertTrue("Unexpected result", result.equals(expectedResult.toString()));
  }
}