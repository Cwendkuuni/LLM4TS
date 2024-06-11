package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    // Given
    Node root = new IR().makeString("function test() { if (true) return; }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    assertTrue(processedNode.getLastChild().isIf());
    assertFalse(processedNode.hasChildWithType(Token.RETURN));
  }

  @Test
  public void testMinimizeExitPointsWithLabel() {
    // Given
    Node root = new IR().makeString("label1: function test() { if (true) break label1; }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    assertTrue(processedNode.getLastChild().isLabel());
    assertTrue(processedNode.getLastChild().getPrevious().isEmpty());
  }

  @Test
  public void testMinimizeExitPointsWithLoop() {
    // Given
    Node root = new IR().makeString("function test() { for (var i = 0; i < 10; i++) {} }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    Node loopNode = processedNode.getFirstChild();
    assertTrue(loopNode.isFor());
    assertTrue(processedNode.getLastChild().isBlock());
    assertFalse(processedNode.hasChildWithType(Token.BREAK));
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsWithInvalidNodeType() {
    // Given
    Node root = new IR().makeString("function test() { 1 + 1; }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    // This test case should fail since the given Node is not a valid type for the MinimizeExitPoints class
  }

  @Test
  public void testMinimizeExitPointsWithNestedIfBlocks() {
    // Given
    Node root = new IR().makeString("function test() { if (true) { if (false) return; } else if (true) return; }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    assertTrue(processedNode.getLastChild().isIf());
    Node ifBlock = processedNode.getLastChild().getFirstChild();
    assertTrue(ifBlock.isIf());
    Node innerIfBlock = ifBlock.getLastChild();
    assertTrue(innerIfBlock.isIf());
    assertFalse(processedNode.hasChildWithType(Token.RETURN));
  }

  @Test
  public void testMinimizeExitPointsWithTryCatchBlock() {
    // Given
    Node root = new IR().makeString("function test() { try { if (true) return; } catch (e) {} }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    assertTrue(processedNode.getLastChild().isTry());
    Node tryNode = processedNode.getLastChild();
    assertTrue(tryNode.isTry());
    Node catchNode = tryNode.getLastChild();
    assertTrue(catchNode.isBlock());
    assertTrue(processedNode.getLastChild().isBlock());
    assertFalse(processedNode.hasChildWithType(Token.RETURN));
  }

  @Test
  public void testMinimizeExitPointsWithTryCatchFinallyBlock() {
    // Given
    Node root = new IR().makeString("function test() { try { if (true) return; } catch (e) {} finally {} }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    assertTrue(processedNode.getLastChild().isTry());
    Node tryNode = processedNode.getLastChild();
    assertTrue(tryNode.isTry());
    Node catchNode = tryNode.getLastChild();
    assertTrue(catchNode.isBlock());
    Node finallyNode = tryNode.getLastChild().getLastChild();
    assertTrue(finallyNode.isBlock());
    assertTrue(processedNode.getLastChild().isBlock());
    assertFalse(processedNode.hasChildWithType(Token.RETURN));
  }

  @Test
  public void testMinimizeExitPointsWithEmptyStatement() {
    // Given
    Node root = new IR().makeString("function test() { }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    assertTrue(processedNode.isEmpty());
  }

  @Test
  public void testMinimizeExitPointsWithFunctionDeclaration() {
    // Given
    Node root = new IR().makeString("function test() { function inner() {} }");
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // When
    minimizeExitPoints.process(null, root);

    // Then
    Node processedNode = root.getFirstChild();
    assertTrue(processedNode.isBlock());
    Node functionDeclaration = processedNode.getLastChild();
    assertTrue(functionDeclaration.isFunctionDeclaration());
    assertTrue(processedNode.getLastChild().isBlock());
    assertFalse(processedNode.hasChildWithType(Token.RETURN));
  }
}