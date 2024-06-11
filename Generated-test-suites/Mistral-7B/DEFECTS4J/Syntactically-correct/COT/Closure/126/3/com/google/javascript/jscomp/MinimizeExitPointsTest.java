package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private Node rootNode;
  private MinimizeExitPoints minimizer;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    AbstractCompiler compiler = new AbstractCompiler();
    minimizer = new MinimizeExitPoints(compiler);
    traversal = new NodeTraversal(compiler);

    rootNode = NodeUtil.parseJs("function test() {"
        + "  if (true) return; "
        + "  if (false) return; "
        + "  if (true) break test; "
        + "  if (false) break test; "
        + "  if (true) continue test; "
        + "  if (false) continue test; "
        + "}");
  }

  @Test
  public void testMinimizeExitPoints() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler);

    minimizer.process(NullNode.INSTANCE, rootNode);
    NodeTraversal.traverse(minimizer, rootNode, traversal);

    assertExitPointsMinimized(rootNode);
  }

  @Test
  public void testMinimizeIfBlockExits() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler);

    Node ifNode = NodeUtil.parseJs("if (true) { break test; } if (false) { return; }");

    Node srcBlock = ifNode.getFirstChild().getNext();
    Node destBlock = ifNode.getFirstChild();

    minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.BREAK, null);

    assertExitPointsMinimized(ifNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMinimizeIfBlockExitsWithNullIfNode() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node srcBlock = Node.newBlock();
    Node destBlock = Node.newBlock();

    minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, null, Token.BREAK, null);
  }

  @Test
  public void testMinimizeIfBlockExitsWithNullSrcBlock() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node ifNode = NodeUtil.parseJs("if (true) { return; } if (false) { break test; }");
    Node srcBlock = null;
    Node destBlock = ifNode.getFirstChild();

    try {
      minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.BREAK, null);
      assert False : "Should have thrown an IllegalArgumentException.";
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testMinimizeIfBlockExitsWithNullDestBlock() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node ifNode = NodeUtil.parseJs("if (true) { break test; } if (false) { return; }");
    Node srcBlock = ifNode.getFirstChild();
    Node destBlock = null;

    try {
      minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.BREAK, null);
      assert False : "Should have thrown an IllegalArgumentException.";
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testMinimizeIfBlockExitsWithEmptyDestBlock() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node ifNode = NodeUtil.parseJs("if (true) { break test; } if (false) { }");
    Node srcBlock = ifNode.getFirstChild();
    Node destBlock = Node.newBlock();

    try {
      minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.BREAK, null);
      assert False : "Should have thrown an IllegalArgumentException.";
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testMinimizeIfBlockExitsWithFunctionDeclarationInDestBlock() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node ifNode = NodeUtil.parseJs("function test() {"
        + "  if (true) { return; } "
        + "  if (false) { function foo() {} }"
        + "}");
    Node srcBlock = ifNode.getFirstChild().getNext();
    Node destBlock = ifNode.getFirstChild();

    try {
      minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.RETURN, null);
      assert False : "Should have thrown an IllegalArgumentException.";
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testMinimizeIfBlockExitsWithNullLabelName() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node ifNode = NodeUtil.parseJs("if (true) { break; } if (false) { return; }");
    Node srcBlock = ifNode.getFirstChild().getNext();
    Node destBlock = ifNode.getFirstChild();

    try {
      minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.BREAK, null);
      assert False : "Should have thrown an IllegalArgumentException.";
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testMinimizeIfBlockExitsWithInvalidLabelName() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    Node ifNode = NodeUtil.parseJs("if (true) { break test2; } if (false) { return; }");
    Node srcBlock = ifNode.getFirstChild().getNext();
    Node destBlock = ifNode.getFirstChild();

    try {
      minimizer.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.BREAK, "test");
      assert False : "Should have thrown an IllegalArgumentException.";
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  private void assertExitPointsMinimized(Node node) {
    assertTrue("Expected no exits in 'if (true) { return; } if (false) { return; }'",
        NodeUtil.getExitCount(node) == 0);

    assertTrue("Expected no exits in 'if (true) break test; if (false) break test;'",
        NodeUtil.getExitCount(node) == 0);

    assertTrue("Expected no exits in 'if (true) continue test; if (false) continue test;'",
        NodeUtil.getExitCount(node) == 0);

    assertTrue("Expected 'if (true) { break test; } if (false) { return; }' to be converted to 'if (true) { }; if (false) { return; }'",
        NodeUtil.isIfElseBlock(node) && NodeUtil.getCondition(node.getFirstChild())
            .getBooleanValue().getValue() == TernaryValue.TRUE);

    assertTrue("Expected 'if (false) { return; } if (true) { break test; }' to be converted to 'if (false) { return; } if (true) {}'",
        NodeUtil.isIfElseBlock(node) && NodeUtil.getCondition(node.getFirstChild().getNext())
            .getBooleanValue().getValue() == TernaryValue.FALSE);

    assertTrue("Expected 'if (true) { continue test; } if (false) { return; }' to be converted to 'if (true) {} if (false) { return; }'",
        NodeUtil.isIfElseBlock(node) && NodeUtil.isBlock(node.getFirstChild().getNext())
            && NodeUtil.isBlock(node.getFirstChild().getNext().getNext()));

    assertTrue("Expected 'if (true) { break test; } if (false) { break test; }' to be converted to 'if (true) {} if (false) {}'",
        NodeUtil.isIfElseBlock(node) && NodeUtil.isEmptyBlock(node.getFirstChild().getNext())
            && NodeUtil.isEmptyBlock(node.getFirstChild().getNext().getNext()));

    assertTrue("Expected 'if (true) { continue test; } if (false) { continue test; }' to be converted to 'if (true) {} if (false) {}'",
        NodeUtil.isIfElseBlock(node) && NodeUtil.isEmptyBlock(node.getFirstChild().getNext())
            && NodeUtil.isEmptyBlock(node.getFirstChild().getNext().getNext()));
  }
}