package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {

    String jsCode = "function foo() {\n" +
        "  if (x) return; \n" +
        "  if (y) return; \n" +
        "  blah(); \n" +
        "  if (z) return; \n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node root = AbstractCompiler.parseTree(jsCode, null, null, null);
    minimizer.process(null, root);

    Node result = root;
    while (result.isBlock()) {
      result = result.getLastChild();
    }
    Node ifNode = result.getPrevious();

    // Check if all if statements have been merged
    assertEquals(1, ifNode.getChildren().size());

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node elseBlock = ifBlock.getNext();

    // Check if else block exists
    if (elseBlock != null) {
      assertEquals(2, elseBlock.getChildren().size());
      // Check if statements in else block have been moved
      for (Node statement : elseBlock.getChildren()) {
        if (statement.isLabel()) {
          Node labelBlock = statement.getLastChild();
          Node lastStatement = labelBlock.getLastChild();
          tryMinimizeIfBlockExits(lastStatement, null, ifNode, Token.RETURN, null);
          assertEquals(0, labelBlock.getChildren().size());
        } else {
          tryMinimizeIfBlockExits(statement, null, ifNode, Token.RETURN, null);
        }
      }
    } else {
      // Check if there's only one statement in if block
      assertEquals(1, ifBlock.getChildren().size());
      Node ifStatement = ifBlock.getFirstChild();
      tryMinimizeIfBlockExits(ifStatement, null, ifNode, Token.RETURN, null);
      assertEquals(0, ifBlock.getChildren().size());
    }

  }

  @Test
  public void testMinimizeExitPointsWithLabel() {

    String jsCode = "function foo() {\n" +
        "  bar: {\n" +
        "    if (x) return; \n" +
        "    if (y) break bar; \n" +
        "    blah(); \n" +
        "    if (z) return; \n" +
        "  }\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node root = AbstractCompiler.parseTree(jsCode, null, null, null);
    minimizer.process(null, root);

    Node result = root;
    while (result.isBlock()) {
      result = result.getLastChild();
    }
    Node ifNode = result.getPrevious();

    // Check if all if statements have been merged
    assertEquals(1, ifNode.getChildren().size());

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node firstLabelBlock = ifBlock.getFirstChild();
    Node lastLabelBlock = ifBlock.getLastChild();
    Node lastStatement = lastLabelBlock.getLastChild();

    // Check if statements in label blocks have been moved
    tryMinimizeIfBlockExits(lastStatement, firstLabelBlock, ifNode, Token.BREAK, "bar");
    assertEquals(1, lastLabelBlock.getChildren().size());
    Node label = lastLabelBlock.getFirstChild();
    assertEquals("", label.getString());

    // Check if there's only one statement in if block
    assertEquals(1, ifBlock.getChildren().size());
    Node ifStatement = ifBlock.getFirstChild();
    tryMinimizeIfBlockExits(ifStatement, null, ifNode, Token.BREAK, "bar");
    assertEquals(0, ifBlock.getChildren().size());

  }

  @Test
  public void testMinimizeExitPointsWithLabelAndElse()  {

    String jsCode = "function foo() {\n" +
        "  bar: {\n" +
        "    if (x) return; \n" +
        "    if (y) break bar;\n" +
        "    blah(); \n" +
        "    if (z) return; \n" +
        "    else blah2(); \n" +
        "  }\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node root = AbstractCompiler.parseTree(jsCode, null, null, null);
    minimizer.process(null, root);

    Node result = root;
    while (result.isBlock()) {
      result = result.getLastChild();
    }
    Node ifNode = result.getPrevious();

    // Check if all if statements have been merged
    assertEquals(1, ifNode.getChildren().size());

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node firstLabelBlock = ifBlock.getFirstChild();
    Node lastLabelBlock = ifBlock.getLastChild();
    Node lastStatement = lastLabelBlock.getLastChild();

    // Check if statements in label blocks have been moved
    tryMinimizeIfBlockExits(lastStatement, firstLabelBlock, ifNode, Token.BREAK, "bar");
    assertEquals(1, lastLabelBlock.getChildren().size());
    Node label = lastLabelBlock.getFirstChild();
    assertEquals("", label.getString());

    // Check if else block exists
    Node elseBlock = lastLabelBlock.getNext();
    assertEquals(1, elseBlock.getChildren().size());
    Node elseStatement = elseBlock.getFirstChild();

    // Check if statements in else block have been moved
    tryMinimizeIfBlockExits(elseStatement, null, ifNode, Token.RETURN, null);
    assertEquals(0, elseBlock.getChildren().size());

    // Check if there's only one statement in if block
    assertEquals(1, ifBlock.getChildren().size());
    Node ifStatement = ifBlock.getFirstChild();
    tryMinimizeIfBlockExits(ifStatement, null, ifNode, Token.BREAK, "bar");
    assertEquals(0, ifBlock.getChildren().size());

  }

  @Test
  public void testMinimizeExitPointsWithNestedLabel() {

    String jsCode = "function foo() {\n" +
        "  outer: {\n" +
        "    inner: {\n" +
        "      if (x) return; \n" +
        "      if (y) break inner;\n" +
        "      blah(); \n" +
        "      if (z) break outer;\n" +
        "    }\n" +
        "  }\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node root = AbstractCompiler.parseTree(jsCode, null, null, null);
    minimizer.process(null, root);

    Node result = root;
    while (result.isBlock()) {
      result = result.getLastChild();
    }
    Node ifNode = result.getPrevious();

    // Check if all if statements have been merged
    assertEquals(1, ifNode.getChildren().size());

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node innerIfBlock = ifBlock.getFirstChild();
    Node outerIfBlock = innerIfBlock.getFirstChild();
    Node firstLabelBlock = outerIfBlock.getFirstChild();
    Node lastLabelBlock = innerIfBlock.getLastChild();
    Node lastStatement = lastLabelBlock.getLastChild();

    // Check if statements in label blocks have been moved
    tryMinimizeIfBlockExits(lastStatement, firstLabelBlock, ifNode, Token.BREAK, "outer");
    assertEquals(1, lastLabelBlock.getChildren().size());
    Node label = lastLabelBlock.getFirstChild();
    assertEquals("", label.getString());

    // Check if there's only one statement in inner if block
    assertEquals(1, innerIfBlock.getChildren().size());
    Node innerIfStatement = innerIfBlock.getFirstChild();
    tryMinimizeIfBlockExits(innerIfStatement, null, ifNode, Token.BREAK, "outer");
    assertEquals(0, innerIfBlock.getChildren().size());

    // Check if there's only one statement in outer if block
    assertEquals(1, ifBlock.getChildren().size());
    Node outerIfStatement = ifBlock.getFirstChild();
    tryMinimizeIfBlockExits(outerIfStatement, null, ifNode, Token.BREAK, "outer");
    assertEquals(0, ifBlock.getChildren().size());

  }

  @Test
  public void testMinimizeExitPointsWithFunctionDeclaration() {

    String jsCode = "function foo() {\n" +
        "  function bar() {\n" +
        "    if (x) return; \n" +
        "  }\n" +
        "  if (y) return; \n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node root = AbstractCompiler.parseTree(jsCode, null, null, null);
    minimizer.process(null, root);

    Node result = root;
    while (result.isBlock()) {
      result = result.getLastChild();
    }
    Node ifNode = result.getPrevious();

    // Check if all if statements have been merged
    assertEquals(1, ifNode.getChildren().size());

    Node ifBlock = ifNode.getFirstChild().getNext();
    Node functionDeclaration = ifBlock.getFirstChild();

    // Check if function declaration has been moved
    tryMinimizeIfBlockExits(functionDeclaration, null, ifNode, Token.RETURN, null);
    assertEquals(0, functionDeclaration.getChildren().size());

    // Check if there's only one statement in if block
    assertEquals(1, ifBlock.getChildren().size());
    Node ifStatement = ifBlock.getFirstChild();
    tryMinimizeIfBlockExits(ifStatement, null, ifNode, Token.RETURN, null);
    assertEquals(0, ifBlock.getChildren().size());

  }

  private void tryMinimizeIfBlockExits(Node node, Node destNode, Node ifNode, int exitType, String labelName) {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal nodeTraversal = new NodeTraversal(minimizer);
    minimizer.tryMinimizeExits(node, exitType, labelName);
    nodeTraversal.traverse(minimizer, node.getParent(), node);
    assertEquals(destNode, node.getParent());
  }
}