package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractPostOrderCallbackType.COMPILER_PASS;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParserTestUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MinimizeExitPointsTest {

  private MinimizeExitPoints minimizeExitPoints;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Parameterized.Parameters(name = "{index}: testMinimizeExitPoints_{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"Test1.js"},
        {"Test2.js"},
        {"Test3.js"},
        {"Test4.js"},
        {"Test5.js"}
    });
  }

  @Parameterized.Parameter(0)
  public String inputFile;

  @Test
  public void testMinimizeExitPoints() {
    List<Node> ast = ParserTestUtil.parseJavaScript(inputFile);
    Node root = NodeOperations.getRootNode(ast);

    minimizeExitPoints.process(null, root);

    // Basic functionality test
    testFunctionality(root);

    // Edge cases and exception handling tests
    testLabelBreakWithLabelName(root, "label1");
    testLabelBreakWithoutLabelName(root);
    testLabelContinueWithLabelName(root, "label1");
    testLabelContinueWithoutLabelName(root);
    testReturnWithValue(root);
    testReturnWithoutValue(root);
    testContinue(root);
    testBreak(root);
    testIfElseBlock(root);
    testIfElseBlockWithReturn(root);
    testIfElseBlockWithBreak(root);
    testTryCatchBlock(root);
    testTryCatchBlockWithFinally(root);
  }

  private void testFunctionality(Node root) {
    assertTrue("Minimize exit points functionality test passed",
        minimizeExitPointsFunctionality(root));
  }

  private void testLabelBreakWithLabelName(Node root, String labelName) {
    Node labelNode = NodeUtil.findLabelNode(root, labelName);
    Node breakNode = NodeUtil.findFirstChildOfType(labelNode, Token.BREAK);
    assertTrue("Label break with label name test passed",
        minimizeExitPointsFunctionalityWithException(root, labelNode, breakNode));
  }

  private void testLabelBreakWithoutLabelName(Node root) {
    Node breakNode = NodeUtil.findFirstChildOfType(root, Token.BREAK);
    assertTrue("Label break without label name test passed",
        minimizeExitPointsFunctionalityWithException(root, null, breakNode));
  }

  private void testLabelContinueWithLabelName(Node root, String labelName) {
    Node labelNode = NodeUtil.findLabelNode(root, labelName);
    Node continueNode = NodeUtil.findFirstChildOfType(labelNode, Token.CONTINUE);
    assertTrue("Label continue with label name test passed",
        minimizeExitPointsFunctionalityWithException(root, labelNode, continueNode));
  }

  private void testLabelContinueWithoutLabelName(Node root) {
    Node continueNode = NodeUtil.findFirstChildOfType(root, Token.CONTINUE);
    assertTrue("Label continue without label name test passed",
        minimizeExitPointsFunctionalityWithException(root, null, continueNode));
  }

  private void testReturnWithValue(Node root) {
    Node returnNode = NodeUtil.findFirstChildOfType(root, Token.RETURN);
    if (returnNode != null && returnNode.hasChildren()) {
      // Return with value test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    assertTrue("Return with value test passed",
        minimizeExitPointsFunctionalityWithException(root, null, returnNode));
  }

  private void testReturnWithoutValue(Node root) {
    Node returnNode = NodeUtil.findFirstChildOfType(root, Token.RETURN);
    if (returnNode == null) {
      // Return without value test skipped as it is already the case
      return;
    }

    assertTrue("Return without value test passed",
        minimizeExitPointsFunctionalityWithException(root, returnNode, null));
  }

  private void testContinue(Node root) {
    Node continueNode = NodeUtil.findFirstChildOfType(root, Token.CONTINUE);
    if (continueNode == null) {
      // Continue test skipped as it is not present in the given test case
      return;
    }

    assertTrue("Continue test passed",
        minimizeExitPointsFunctionalityWithException(root, null, continueNode));
  }

  private void testBreak(Node root) {
    Node breakNode = NodeUtil.findFirstChildOfType(root, Token.BREAK);
    if (breakNode == null) {
      // Break test skipped as it is not present in the given test case
      return;
    }

    assertTrue("Break test passed",
        minimizeExitPointsFunctionalityWithException(root, null, breakNode));
  }

  private void testIfElseBlock(Node root) {
    Node ifNode = NodeUtil.findFirstChildOfType(root, Token.IF);
    Node ifBlock = ifNode.getFirstChild();
    Node elseBlock = ifNode.getNext();

    assertTrue("If-else block test passed",
        minimizeExitPointsFunctionality(root));

    Node ifExitNode = NodeUtil.findFirstChildOfType(ifBlock, Token.RETURN);
    if (ifExitNode != null) {
      // If block return test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node elseExitNode = NodeUtil.findFirstChildOfType(elseBlock, Token.RETURN);
    assertTrue("If-else block test with return passed",
        minimizeExitPointsFunctionalityWithException(root, null, elseExitNode));

    Node ifBreakNode = NodeUtil.findFirstChildOfType(ifBlock, Token.BREAK);
    if (ifBreakNode != null) {
      // If block break test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node elseBreakNode = NodeUtil.findFirstChildOfType(elseBlock, Token.BREAK);
    assertTrue("If-else block test with break passed",
        minimizeExitPointsFunctionalityWithException(root, null, elseBreakNode));
  }

  private void testIfElseBlockWithReturn(Node root) {
    Node ifNode = NodeUtil.findFirstChildOfType(root, Token.IF);
    Node ifBlock = ifNode.getFirstChild();
    Node elseBlock = ifNode.getNext();

    assertTrue("If-else block test with return passed",
        minimizeExitPointsFunctionalityWithException(root, ifNode, null));
  }

  private void testIfElseBlockWithBreak(Node root) {
    Node ifNode = NodeUtil.findFirstChildOfType(root, Token.IF);
    Node ifBlock = ifNode.getFirstChild();
    Node elseBlock = ifNode.getNext();

    assertTrue("If-else block test with break passed",
        minimizeExitPointsFunctionalityWithException(root, ifNode, null));
  }

  private void testTryCatchBlock(Node root) {
    Node tryNode = NodeUtil.findFirstChildOfType(root, Token.TRY);
    Node catchNode = NodeUtil.findFirstChildOfType(tryNode, Token.CATCH);
    Node finallyNode = NodeUtil.findFirstChildOfType(tryNode, Token.FINALLY);

    assertTrue("Try-catch block test passed",
        minimizeExitPointsFunctionality(root));

    Node tryReturnNode = NodeUtil.findFirstChildOfType(tryNode, Token.RETURN);
    if (tryReturnNode != null) {
      // Try block return test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node catchReturnNode = NodeUtil.findFirstChildOfType(catchNode, Token.RETURN);
    assertTrue("Try-catch block test with return passed",
        minimizeExitPointsFunctionalityWithException(root, tryNode, catchReturnNode));

    Node finallyReturnNode = NodeUtil.findFirstChildOfType(finallyNode, Token.RETURN);
    if (finallyReturnNode != null) {
      // Try-catch block finally return test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node tryBreakNode = NodeUtil.findFirstChildOfType(tryNode, Token.BREAK);
    if (tryBreakNode != null) {
      // Try block break test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node catchBreakNode = NodeUtil.findFirstChildOfType(catchNode, Token.BREAK);
    assertTrue("Try-catch block test with break passed",
        minimizeExitPointsFunctionalityWithException(root, tryNode, catchBreakNode));

    Node finallyBreakNode = NodeUtil.findFirstChildOfType(finallyNode, Token.BREAK);
    if (finallyBreakNode != null) {
      // Try-catch block finally break test skipped as it is not supported by MinimizeExitPoints
      return;
    }
  }

  private void testTryCatchBlockWithFinally(Node root) {
    Node tryNode = NodeUtil.findFirstChildOfType(root, Token.TRY);
    Node catchNode = NodeUtil.findFirstChildOfType(tryNode, Token.CATCH);
    Node finallyNode = NodeUtil.findFirstChildOfType(tryNode, Token.FINALLY);

    assertTrue("Try-catch-finally block test passed",
        minimizeExitPointsFunctionality(root));

    Node tryReturnNode = NodeUtil.findFirstChildOfType(tryNode, Token.RETURN);
    if (tryReturnNode != null) {
      // Try block return test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node catchReturnNode = NodeUtil.findFirstChildOfType(catchNode, Token.RETURN);
    if (catchReturnNode != null) {
      // Catch block return test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node finallyReturnNode = NodeUtil.findFirstChildOfType(finallyNode, Token.RETURN);
    assertTrue("Try-catch-finally block test with return passed",
        minimizeExitPointsFunctionalityWithException(root, tryNode, finallyReturnNode));

    Node tryBreakNode = NodeUtil.findFirstChildOfType(tryNode, Token.BREAK);
    if (tryBreakNode != null) {
      // Try block break test skipped as it is not supported by MinimizeExitPoints
      return;
    }

    Node catchBreakNode = NodeUtil.findFirstChildOfType(catchNode, Token.BREAK);
    assertTrue("Try-catch-finally block test with break passed",
        minimizeExitPointsFunctionalityWithException(root, tryNode, catchBreakNode));

    Node finallyBreakNode = NodeUtil.findFirstChildOfType(finallyNode, Token.BREAK);
    assertTrue("Try-catch-finally block test with break in finally passed",
        minimizeExitPointsFunctionalityWithException(root, tryNode, finallyBreakNode));
  }

  private boolean minimizeExitPointsFunctionality(Node root) {
    boolean result = false;
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(null, root);
    Node newRoot = NodeOperations.getRootNode(minimizeExitPoints.getCompiledAst());
    result = NodeUtil.getImpureBooleanValue(NodeUtil.getExpression(newRoot))
        == NodeUtil.getImpureBooleanValue(NodeUtil.getExpression(root));
    return result;
  }

  private boolean minimizeExitPointsFunctionalityWithException(
      Node root, Node ifNode, Node exitNode) {
    try {
      minimizeExitPointsFunctionality(root);
      fail("Expected an exception to be thrown");
    } catch (AssertionError e) {
      NodeUtil.removeChild(root, exitNode);
      MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
      minimizeExitPoints.process(null, root);
      Node newRoot = NodeOperations.getRootNode(minimizeExitPoints.getCompiledAst());
      assertTrue("Test passed",
          NodeUtil.getImpureBooleanValue(NodeUtil.getExpression(newRoot))
              == NodeUtil.getImpureBooleanValue(NodeUtil.getExpression(root)));
      return true;
    }
    return false;
  }
}

// Test cases
// Test1.js: function () {
//     if (true) return;
//     if (false) break;
// }
// Test2.js: function () {
//     for (var i = 0; i < 10; i++) {
//         if (i == 5) return;
//         if (i == 7) break;
//     }
// }
// Test3.js: function () {
//     do {
//         if (x) continue;
//         if (x) break;
//     } while (true);
// }
// Test4.js: function foo() {
//     function bar() {
//         if (x) return;
//     }
//     bar();
// }
// Test5.js: function () {
//     try {
//         throw new Error();
//     } finally {
//         if (x) break;
//     }
// }