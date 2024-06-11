package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private Compiler compiler;
  private MinimizeExitPoints minimizer;

  @Before
  public void setUp() {
    compiler = new Compiler();
    minimizer = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testBasicExitPoints() {
    Node sourceNode = NodeOperations.parseString(
        "function testFunction() {"
            + "if (true) return; "
            + "if (false) return; "
            + "return; "
            + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node resultFunction = rootNode.getLastChild();
    Node lastChild = resultFunction.getLastChild();

    // Test for return without values
    assertTrue(lastChild.getType() == Token.RETURN);
    assertTrue(lastChild.hasChildren() == false);

    // Test for continue
    Node forNode = resultFunction.getPrevious();
    assertTrue(forNode.getType() == Token.FOR);
    Node continueNode = NodeUtil.getFirstChildOfType(forNode, Token.CONTINUE);
    assertTrue(continueNode != null);
    assertTrue(continueNode.hasChildren() == false);

    // Test for break
    Node ifNode = NodeUtil.getPreviousOfType(forNode, Token.IF);
    Node labelNode = NodeUtil.getFirstChildOfType(ifNode, Token.LABEL);
    Node breakNode = NodeUtil.getFirstChildOfType(ifNode, Token.BREAK);
    assertTrue(labelNode != null);
    assertTrue(labelNode.getString().equals("loopLabel"));
    assertTrue(breakNode != null);
    assertTrue(breakNode.hasChildren() == false);
    assertTrue(breakNode.getFirstChild() != null);
    assertTrue(breakNode.getFirstChild().getString().equals("loopLabel"));
  }

  @Test
  public void testNoExitPoints() {
    Node sourceNode = NodeOperations.parseString("function testFunction() {}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node resultFunction = rootNode.getLastChild();
    Node lastChild = resultFunction.getLastChild();

    // Test for no exits
    assertFalse(lastChild.getType() == Token.RETURN);
    assertFalse(lastChild.isIf());
    assertFalse(lastChild.isLabel());
    assertFalse(lastChild.isFor());
    assertFalse(lastChild.isWhile());
    assertFalse(lastChild.isDo());
  }

  @Test
  public void testNestedLoops() {
    Node sourceNode = NodeOperations.parseString(
        "function testFunction() {"
            + "for (var i = 0; i < 10; i++) {"
            + "  for (var j = 0; j < 10; j++) {"
            + "    if (i == 5 && j == 5) break; "
            + "  }"
            + "}"
            + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node resultFunction = rootNode.getLastChild();
    Node forNode1 = NodeUtil.getFirstChildOfType(resultFunction, Token.FOR);
    Node forNode2 = NodeUtil.getFirstChildOfType(forNode1, Token.FOR);
    Node breakNode = NodeUtil.getFirstChildOfType(forNode2, Token.BREAK);

    // Test for nested breaks
    assertTrue(breakNode != null);
    assertTrue(breakNode.hasChildren() == false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    new MinimizeExitPoints(null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullNode() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullParentNode() {
    Node rootNode = NodeOperations.createRoot(NodeOperations.parseString(""));
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, rootNode);
    Node ifNode = NodeUtil.getFirstChildOfType(rootNode, Token.IF);
    minimizer.visit(null, ifNode, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidNodeType() {
    Node rootNode = NodeOperations.createRoot(NodeOperations.parseString(""));
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, rootNode);
    Node firstChild = rootNode.getFirstChild();
    minimizer.visit(null, firstChild, null);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testMissingChildNode() {
    Node rootNode = NodeOperations.parseStringToRoot("function testFunction() {}", "testFunction");
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, rootNode);
    Node firstChild = rootNode.getFirstChild();
    minimizer.visit(null, firstChild, null);
    Node missingChild = NodeOperations.parseStringToNode("return;", "return");
    firstChild.addChildToBack(missingChild);
    minimizer.visit(null, firstChild, null);
  }

  @Test
  public void testEmptyIfBlock() {
    Node sourceNode = NodeOperations.parseString(
        "function testFunction() {"
            + "if (true) {"
            + "}"
            + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node ifNode = NodeUtil.getFirstChildOfType(rootNode, Token.IF);
    Node ifBlock = ifNode.getLastChild();

    // Test for empty if block
    assertTrue(ifBlock.isEmpty());
  }

  @Test
  public void testEmptyIfElseBlock() {
    Node sourceNode = NodeOperations.parseString(
        "function testFunction() {"
            + "if (true) {"
            + "}"
            + " else {"
            + "}"
            + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node ifNode = NodeUtil.getFirstChildOfType(rootNode, Token.IF);
    Node ifBlock = ifNode.getLastChild();
    Node elseBlock = NodeUtil.getFirstChildOfType(ifNode, Token.ELSE);

    // Test for empty if and else blocks
    assertTrue(ifBlock.isEmpty());
    assertTrue(elseBlock.isEmpty());
  }

  @Test
  public void testEmptyTryCatchBlock() {
    Node sourceNode = NodeOperations.parseString(
        "try {"
            + "}"
        + " catch (e) {}", "testFunction");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node tryNode = NodeUtil.getFirstChildOfType(rootNode, Token.TRY);
    Node catchNode = NodeUtil.getFirstChildOfType(tryNode, Token.CATCH);

    // Test for empty try and catch blocks
    assertTrue(tryNode.getLastChild().isEmpty());
    assertTrue(catchNode.getLastChild().isEmpty());
  }

  @Test
  public void testEmptyTryCatchFinallyBlock() {
    Node sourceNode = NodeOperations.parseString(
        "try {"
            + "}"
        + " catch (e) {"
            + "}"
        + " finally {}", "testFunction");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node tryNode = NodeUtil.getFirstChildOfType(rootNode, Token.TRY);
    Node catchNode = NodeUtil.getFirstChildOfType(tryNode, Token.CATCH);
    Node finallyNode = NodeUtil.getFirstChildOfType(tryNode, Token.FINALLY);

    // Test for empty try, catch, and finally blocks
    assertTrue(tryNode.getLastChild().isEmpty());
    assertTrue(catchNode.getLastChild().isEmpty());
    assertTrue(finallyNode.getLastChild().isEmpty());
  }

  @Test
  public void testLabelWithBreak() {
    Node sourceNode = NodeOperations.parseString(
        "testFunction:"
            + "for (var i = 0; i < 10; i++) {"
            + "  for (var j = 0; j < 10; j++) {"
            + "    if (i == 5 && j == 5) break testFunction;"
            + "  }"
            + "}"
            + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node forNode1 = NodeUtil.getFirstChildOfType(rootNode, Token.FOR);
    Node forNode2 = NodeUtil.getFirstChildOfType(forNode1, Token.FOR);
    Node breakNode = NodeUtil.getFirstChildOfType(forNode2, Token.BREAK);
    Node labelNode = NodeUtil.getFirstChildOfType(forNode1, Token.LABEL);

    // Test for label with break
    assertTrue(labelNode.getString().equals("testFunction"));
  }

  @Test
  public void testReturnWithValue() {
    Node sourceNode = NodeOperations.parseString(
        "function testFunction() {"
            + "return 10;"
            + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node resultFunction = rootNode.getLastChild();
    Node lastChild = resultFunction.getLastChild();

    // Test for return with value
    assertFalse(lastChild.getType() == Token.RETURN);
    assertTrue(lastChild.isIntLiteral());
    assertEquals(10, lastChild.getInt());
  }

  @Test
  public void testReturnWithExpression() {
    Node sourceNode = NodeOperations.parseString(
        "function testFunction() {"
            + "return 1 + 1;"
            + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node resultFunction = rootNode.getLastChild();
    Node lastChild = resultFunction.getLastChild();

    // Test for return with expression
    assertFalse(lastChild.getType() == Token.RETURN);
    assertTrue(lastChild.isAdd());
    assertEquals(2, lastChild.getFirstChild().getInt());
    assertEquals(1, lastChild.getLastChild().getInt());
  }

  @Test
  public void testNestedTryCatch() {
    Node sourceNode = NodeOperations.parseString(
        "try {"
            + "try {"
            + "  throw new Error(\"Inner error\");"
            + "}"
            + " catch (e) {"
            + "  console.log(\"Caught inner error: \" + e.message);"
            + "}"
            + "}"
            + " catch (e) {"
            + "  console.log(\"Caught outer error: \" + e.message);"
            + "}"
        + "}");

    Node rootNode = NodeOperations.createRoot(sourceNode);
    minimizer.process(null, rootNode);

    Node tryBlock = NodeUtil.getFirstChildOfType(rootNode, Token.TRY);
    Node innerTryBlock = NodeUtil.getFirstChildOfType(tryBlock, Token.TRY);
    Node innerCatchBlock = NodeUtil.getFirstChildOfType(innerTryBlock, Token.CATCH);

    // Test for nested try-catch
    assertTrue(innerCatchBlock != null);
    assertTrue(innerCatchBlock.isBlock());
    assertTrue(innerCatchBlock.getLastChild().isLogCall());
  }
}

