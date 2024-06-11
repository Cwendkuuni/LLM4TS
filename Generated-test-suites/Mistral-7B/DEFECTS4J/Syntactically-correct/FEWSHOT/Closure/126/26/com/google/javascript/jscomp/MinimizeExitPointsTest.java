package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeUtil.getFunctionNode;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    nodeFactory = new NodeFactory();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints() {
    Node root = NodeUtil.parseStatement("function foo() { if (1) return; }");
    NodeUtil.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        t.removeNode(n);
      }
    }, root);
    minimizeExitPoints.process(null, root);

    assertTrue(isFunctionDeclaration(root.getFirstChild()));
    assertFalse(root.hasChildWithType(Token.RETURN));
  }

  @Test
  public void testMinimizeExitPointsReturn() {
    Node root = NodeUtil.parseStatement("function foo() { return; }");
    NodeUtil.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        t.removeNode(n);
      }
    }, root);
    minimizeExitPoints.process(null, root);

    assertTrue(isFunctionDeclaration(root.getFirstChild()));
    assertFalse(root.hasChildWithType(Token.RETURN));
  }

  @Test
  public void testMinimizeExitPointsLabel() {
    Node root = NodeUtil.parseStatement(
        "label1: function foo() { break label1; } label2: {} label3: { return; }");
    minimizeExitPoints.process(null, root);

    Node label3 = getFunctionNode(root, "foo").getLastChild();
    assertFalse(label3.hasChildWithType(Token.RETURN));

    Node label2 = root.getNextSibling();
    assertNull(label2.getLastChild());
  }

  @Test
  public void testMinimizeExitPointsNestedIf() {
    Node root = NodeUtil.parseStatement(
        "function foo() { if (true) { if (false) break; } }");
    minimizeExitPoints.process(null, root);

    Node ifNode1 = getFunctionNode(root, "foo").getChild(0);
    Node ifNode2 = getFunctionNode(root, "foo").getChild(1);
    assertFalse(ifNode1.hasChildWithType(Token.BREAK));
    assertFalse(ifNode2.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsNestedIfElse() {
    Node root = NodeUtil.parseStatement(
        "function foo() { if (true) { if (false) break; } else break; }");
    minimizeExitPoints.process(null, root);

    Node ifNode1 = getFunctionNode(root, "foo").getChild(0);
    Node ifNode2 = getFunctionNode(root, "foo").getChild(1);
    Node elseBlock = getFunctionNode(root, "foo").getChild(2);
    assertFalse(ifNode1.hasChildWithType(Token.BREAK));
    assertFalse(ifNode2.hasChildWithType(Token.BREAK));
    assertFalse(elseBlock.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsNestedIfElseIf() {
    Node root = NodeUtil.parseStatement(
        "function foo() { if (true) { if (false) { if (true) break; } } else if (true) break; }");
    minimizeExitPoints.process(null, root);

    Node ifNode1 = getFunctionNode(root, "foo").getChild(0);
    Node ifNode2 = getFunctionNode(root, "foo").getChild(1);
    Node ifNode3 = getFunctionNode(root, "foo").getChild(2);
    Node elseIfBlock = getFunctionNode(root, "foo").getChild(3);
    assertFalse(ifNode1.hasChildWithType(Token.BREAK));
    assertFalse(ifNode2.hasChildWithType(Token.BREAK));
    assertFalse(ifNode3.hasChildWithType(Token.BREAK));
    assertFalse(elseIfBlock.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsNestedIfElseIfElse() {
    Node root = NodeUtil.parseStatement(
        "function foo() { if (true) { if (false) { if (true) break; } } else if (true) break; } else { } }");
    minimizeExitPoints.process(null, root);

    Node ifNode1 = getFunctionNode(root, "foo").getChild(0);
    Node ifNode2 = getFunctionNode(root, "foo").getChild(1);
    Node ifNode3 = getFunctionNode(root, "foo").getChild(2);
    Node elseIfBlock = getFunctionNode(root, "foo").getChild(3);
    Node elseBlock = getFunctionNode(root, "foo").getChild(4);
    assertFalse(ifNode1.hasChildWithType(Token.BREAK));
    assertFalse(ifNode2.hasChildWithType(Token.BREAK));
    assertFalse(ifNode3.hasChildWithType(Token.BREAK));
    assertFalse(elseIfBlock.hasChildWithType(Token.BREAK));
    assertFalse(elseBlock.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsEmptyFunction() {
    Node root = NodeUtil.parseStatement("function foo() {}");
    minimizeExitPoints.process(null, root);

    assertTrue(isFunctionDeclaration(root.getFirstChild()));
    assertFalse(root.hasChildWithType(Token.RETURN));
  }

  @Test
  public void testMinimizeExitPointsFor() {
    Node root = NodeUtil.parseStatement(
        "for (var i = 0; i < 10; i++) { break; }");
    minimizeExitPoints.process(null, root);

    Node forNode = getFunctionNode(root, "Script").getChild(0);
    Node body = forNode.getLastChild();
    assertFalse(body.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsWhile() {
    Node root = NodeUtil.parseStatement(
        "while (true) { break; }");
    minimizeExitPoints.process(null, root);

    Node whileNode = getFunctionNode(root, "Script").getChild(0);
    Node body = whileNode.getLastChild();
    assertFalse(body.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsDoWhile() {
    Node root = NodeUtil.parseStatement(
        "do { break; } while (false);");
    minimizeExitPoints.process(null, root);

    Node doNode = getFunctionNode(root, "Script").getChild(0);
    Node body = doNode.getLastChild();
    assertFalse(body.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsTryCatch() {
    Node root = NodeUtil.parseStatement(
        "try { break; } catch (e) {}");
    minimizeExitPoints.process(null, root);

    Node tryNode = getFunctionNode(root, "Script").getChild(0);
    Node catchBlock = tryNode.getLastChild();
    assertFalse(catchBlock.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsTryCatchFinally() {
    Node root = NodeUtil.parseStatement(
        "try { break; } catch (e) {} finally {}");
    minimizeExitPoints.process(null, root);

    Node tryNode = getFunctionNode(root, "Script").getChild(0);
    Node catchBlock = tryNode.getLastChild();
    Node finallyBlock = tryNode.getLastChild().getNextSibling();
    assertFalse(catchBlock.hasChildWithType(Token.BREAK));
    assertFalse(finallyBlock.hasChildWithType(Token.BREAK));
  }

  @Test
  public void testMinimizeExitPointsLabeledBreak() {
    Node root = NodeUtil.parseStatement(
        "label1: function foo() { break label1; } label2: { return; }");
    minimizeExitPoints.process(null, root);

    Node label2 = getFunctionNode(root, "foo").getChild(1);
    assertFalse(label2.hasChildWithType(Token.RETURN));

    Node label1 = root.getNextSibling();
    assertNull(label1.getLastChild());
  }

  @Test
  public void testMinimizeExitPointsLabeledContinue() {
    Node root = NodeUtil.parseStatement(
        "label1: function foo() { continue label1; } label2: {} label3: { return; }");
    minimizeExitPoints.process(null, root);

    Node label3 = getFunctionNode(root, "foo").getChild(2);
    assertFalse(label3.hasChildWithType(Token.RETURN));

    Node label1 = root.getNextSibling();
    assertNull(label1.getLastChild());
  }
}