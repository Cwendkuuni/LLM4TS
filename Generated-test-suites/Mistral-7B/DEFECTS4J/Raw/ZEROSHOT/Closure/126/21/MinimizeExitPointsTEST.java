package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

/**
 * Test class for MinimizeExitPoints class.
 */
public class MinimizeExitPointsTest {

  /** Compiler instance for testing. */
  private Compiler compiler;

  /**
   * Sets up the test environment by creating a new instance of the
   * compiler.
   */
  @Before
   public void setUp() {
    compiler = new Compiler();
  }

  /**
   * Test case for MinimizeExitPoints class.
   */
  @Test
  public void testMinimizeExitPoints() {
    String testCode = "function test() {" +
            "  if (true) return; " +
            "  else if (false) return; " +
            "  else if (1 < 2) return; " +
            "  else return; " +
            "}";

    Node root = NodeUtil.parseJsSource(testCode);
    Node externs = Node.newExterns();

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    // Check that there is only one exit point.
    Node testFunction = NodeUtil.getFunctionDeclaration(root, "test");
    Node testFunctionBody = testFunction.getFirstChild();
    assertEquals("Unexpected number of exit points.", 1, testFunctionBody.getChildCount());
  }

  /**
   * Test case for MinimizeExitPoints when there are multiple exit points.
   */
  @Test
  public void testMinimizeExitPointsWithMultipleExits() {
    String testCode = "function test() {" +
            "  if (true) return; " +
            "  else if (false) return; " +
            "  else if (1 < 2) return; " +
            "  else if (2 > 1) return; " +
            "  else return; " +
            "}";

    Node root = NodeUtil.parseJsSource(testCode);
    Node externs = Node.newExterns();

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    // Check that there is only one exit point.
    Node testFunction = NodeUtil.getFunctionDeclaration(root, "test");
    Node testFunctionBody = testFunction.getFirstChild();
    assertEquals("Unexpected number of exit points.", 1, testFunctionBody.getChildCount());
  }

  /**
   * Test case for MinimizeExitPoints when there is a break statement.
   */
  @Test
  public void testMinimizeExitPointsWithBreak() {
    String testCode = "function test() {" +
            "  for (var i = 0; i < 10; i++) {" +
            "    if (i === 5) break;" +
            "  }" +
            "}";

    Node root = NodeUtil.parseJsSource(testCode);
    Node externs = Node.newExterns();

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    // Check that there is only one exit point.
    Node testFunction = NodeUtil.getFunctionDeclaration(root, "test");
    Node testFunctionBody = testFunction.getFirstChild();
    assertEquals("Unexpected number of exit points.", 1, testFunctionBody.getChildCount());

    // Check that the break statement has been removed.
    Node forStatement = NodeUtil.getForStatement(testFunctionBody, 0);
    Node loopBlock = NodeUtil.getLoopCodeBlock(forStatement);
    assertTrue("Break statement not removed.", loopBlock.isEmpty());
  }

  /**
   * Test case for MinimizeExitPoints when there is a labeled break statement.
   */
  @Test
  public void testMinimizeExitPointsWithLabeledBreak() {
    String testCode = "function test() {" +
            "  outer:" +
            "  for (var i = 0; i < 10; i++) {" +
            "    if (i === 5) break outer;" +
            "  }" +
            "}";

    Node root = NodeUtil.parseJsSource(testCode);
    Node externs = Node.newExterns();

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    // Check that there is only one exit point.
    Node testFunction = NodeUtil.getFunctionDeclaration(root, "test");
    Node testFunctionBody = testFunction.getFirstChild();
    assertEquals("Unexpected number of exit points.", 1, testFunctionBody.getChildCount());

    // Check that the labeled break statement has been removed.
    Node forStatement = NodeUtil.getForStatement(testFunctionBody, 0);
    Node loopBlock = NodeUtil.getLoopCodeBlock(forStatement);
    Node breakNode = NodeUtil.getLabeledStatement(testFunctionBody, "outer");
    assertTrue("Labeled break statement not removed.", breakNode == null);
  }

  /**
   * Test case for MinimizeExitPoints when there is a continue statement.
   */
  @Test
  public void testMinimizeExitPointsWithContinue() {
    String testCode = "function test() {" +
            "  for (var i = 0; i < 10; i++) {" +
            "    if (i % 2 === 0) continue;" +
            "    console.log(i);" +
            "  }" +
            "}";

    Node root = NodeUtil.parseJsSource(testCode);
    Node externs = Node.newExterns();

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    // Check that there is only one exit point.
    Node testFunction = NodeUtil.getFunctionDeclaration(root, "test");
    Node testFunctionBody = testFunction.getFirstChild();
    assertEquals("Unexpected number of exit points.", 1, testFunctionBody.getChildCount());

    // Check that the continue statement has been removed.
    Node forStatement = NodeUtil.getForStatement(testFunctionBody, 0);
    Node loopBlock = NodeUtil.getLoopCodeBlock(forStatement);
    Node continueNode = NodeUtil.getStatement(loopBlock, 1);
    assertTrue("Continue statement not removed.", continueNode == null);
  }

  /**
   * Test case for MinimizeExitPoints when there is no exit point.
   */
  @Test
  public void testMinimizeExitPointsWithoutExits() {
    String testCode = "function test() {" +
            "  for (var i = 0; i < 10; i++) {" +
            "    console.log(i);" +
            "  }" +
            "}";

    Node root = NodeUtil.parseJsSource(testCode);
    Node externs = Node.newExterns();

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    // Check that there is still only one exit point.
    Node testFunction = NodeUtil.getFunctionDeclaration(root, "test");
    Node testFunctionBody = testFunction.getFirstChild();
    assertEquals("Unexpected number of exit points.", 1, testFunctionBody.getChildCount());

    // Check that the code hasn't been changed.
    Node forStatement = NodeUtil.getForStatement(testFunctionBody, 0);
    Node loopBlock = NodeUtil.getLoopCodeBlock(forStatement);
    Node consoleLogNode = NodeUtil.getStatement(loopBlock, 0);
    assertEquals("Unexpected statement found.", "console.log", consoleLogNode.getString());
  }
}