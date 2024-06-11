package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.Scope.GLOBAL;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.NodeTraversal.FunctionCallback;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tests for {@link UnreachableCodeElimination} class.
 */
public class UnreachableCodeEliminationTest {

  private static final Logger logger =
      Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testUnreachableCodeElimination() {
    String testCode =
        "function foo() {" +
            "var x = 1;" +
            "return;" +
            "alert('unreachable');" +
            "var y = 2;" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, true);

    unreachableCodeElimination.process(null, node);

    // Check that unreachable code has been removed.
    assertEquals(0, node.getLastChildIndexOf(node.getChildAtIndex(1)));

    // Check that variable y is still present in the AST.
    Node yNode = node.getChildAtIndex(2);
    assertEquals("y", yNode.getIdentifier().getName());
  }

  @Test
  public void testRemoveNoOpStatements() {
    String testCode =
        "function foo() {" +
            "var x = 1;" +
            "a.b.MyClass.prototype.propertyName;" +
            "return;" +
            "alert('unreachable');" +
            "var y = 2;" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, true);

    unreachableCodeElimination.process(null, node);

    // Check that no-op statement has been removed.
    Node noOpStatement = node.getChildAtIndex(1);
    assertNull(noOpStatement);
  }

  @Test
  public void testBreakStatement() {
    String testCode =
        "function foo() {" +
            "for (var i = 0; i < 10; i++) {" +
                "if (i == 5) {" +
                    "break;" +
                "}" +
            "}" +
            "return;" +
            "alert('unreachable');" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, false);

    unreachableCodeElimination.process(null, node);

    // Check that break statement and following code have been removed.
    Node breakStatement = node.getChildAtIndex(2);
    Node followingCode = node.getChildAtIndex(3);
    assertNull(breakStatement);
    assertNull(followingCode);
  }

  @Test
  public void testContinueStatement() {
    String testCode =
        "function foo() {" +
            "for (var i = 0; i < 10; i++) {" +
                "if (i % 2 == 0) {" +
                    "continue;" +
                "}" +
                "log(i);" +
            "}" +
            "return;" +
            "alert('unreachable');" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, false);

    unreachableCodeElimination.process(null, node);

    // Check that continue statement and following code have been removed.
    Node continueStatement = node.getChildAtIndex(3);
    List<Node> logStatements = new ArrayList<>();
    for (int i = 4; i < node.getChildCount(); i++) {
      logStatements.add(node.getChildAtIndex(i));
    }
    assertEquals(9, logStatements.size());
  }

  @Test
  public void testReturnStatement() {
    String testCode =
        "function foo() {" +
            "return;" +
            "return;" +
            "return;" +
            "return;" +
            "alert('unreachable');" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, false);

    unreachableCodeElimination.process(null, node);

    // Check that all return statements have been removed, except the first one.
    assertEquals(1, node.getChildCount());
    Node firstReturnStatement = node.getChildAtIndex(0);
    assertEquals(Token.RETURN, firstReturnStatement.getType());
  }

  @Test
  public void testNestedFunction() {
    String testCode =
        "function foo() {" +
            "function bar() {" +
                "return;" +
                "alert('unreachable');" +
            "}" +
            "return bar();" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, false);

    unreachableCodeElimination.process(null, node);

    // Check that inner function return statement and following code have been removed.
    Node innerFunction = node.getChildAtIndex(1);
    Node innerFunctionReturnStatement = innerFunction.getLastChild();
    assertNull(innerFunctionReturnStatement);
  }

  @Test
  public void testTryCatch() {
    String testCode =
        "function foo() {" +
            "try {" +
                "throw new Error('error');" +
            "}" +
            "catch (e) {" +
                "return;" +
                "alert('unreachable');" +
            "}" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, false);

    unreachableCodeElimination.process(null, node);

    // Check that catch block code has been removed.
    Node catchBlock = node.getChildAtIndex(1);
    Node catchBlockReturnStatement = catchBlock.getLastChild();
    assertNull(catchBlockReturnStatement);
  }

  @Test
  public void testFinallyBlock() {
    String testCode =
        "function foo() {" +
            "try {" +
                "return;" +
            "}" +
            "finally {" +
                "return;" +
                "alert('unreachable');" +
            "}" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, false);

    unreachableCodeElimination.process(null, node);

    // Check that finally block code has been removed.
    Node finallyBlock = node.getChildAtIndex(1);
    Node finallyBlockReturnStatement = finallyBlock.getLastChild();
    assertNull(finallyBlockReturnStatement);
  }

  @Test
  public void testCaseInsensitiveSearch() {
    String testCode =
        "function foo() {" +
            "return;" +
            "ALERT('unreachable');" +
            "}";

    Node node = AbstractSyntaxTree.parseJs(testCode, null);

    AbstractCompiler compiler = new AbstractCompiler();

    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, false);

    unreachableCodeElimination.process(null, node);

    // Check that ALERT statement has been removed.
    assertNull(node.getLastChild());
  }
}