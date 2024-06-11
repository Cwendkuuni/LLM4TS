package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() throws Exception {
    String jsCode = "function test() {"
        + "  if (x) return;"
        + "  if (y) return;"
        + "  otherFunction();"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseTree(jsCode);
    NodeTraversal.traverse(compiler, root, new MinimizeExitPoints(compiler));
    Node result = compiler.getCompiledCode();

    Node ifBlock1 = NodeUtil.getFirstChild(NodeUtil.findFirstChildOfType(root, Token.FUNCTION));
    Node ifBlock2 = ifBlock1.getNext();
    Node functionCall = ifBlock2.getNext();

    // ifBlock1 and ifBlock2 should now be siblings in the same block
    assertEquals(ifBlock1.getParent(), ifBlock2.getParent());

    // functionCall should now be the last child of the ifBlock2's parent
    assertEquals(functionCall.getParent(), ifBlock2.getParent());
    assertNull(ifBlock2.getNext());
  }

  @Test
  public void testMinimizeExitPointsWithLabel() throws Exception {
    String jsCode = "label1:\n"
        + "function test() {"
        + "  if (x) break label1;"
        + "  if (y) break label1;"
        + "  otherFunction();"
        + "}"
        + "label2:\n"
        + "function otherFunction() {"
        + "  if (z) return;"
        + "  label3:\n"
        + "  if (a) break label3;"
        + "  b();"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseTree(jsCode);
    NodeTraversal.traverse(compiler, root, new MinimizeExitPoints(compiler));
    Node result = compiler.getCompiledCode();

    // label1 should be the parent of ifBlock1 and ifBlock2
    Node label1 = NodeUtil.findFirstChildOfType(root, Token.LABEL);
    Node ifBlock1 = NodeUtil.getFirstChild(NodeUtil.findFirstChildOfType(label1, Token.FUNCTION));
    Node ifBlock2 = ifBlock1.getNext();

    // ifBlock1 and ifBlock2 should now be siblings in the same block
    assertEquals(ifBlock1.getParent(), ifBlock2.getParent());

    // otherFunction should be the parent of ifBlock3 and b
    Node otherFunction = NodeUtil.findFirstChildOfType(label1, Token.FUNCTION, 1);
    Node ifBlock3 = otherFunction.getLastChild();
    Node functionB = ifBlock3.getNext();

    // ifBlock3 and b should now be siblings in the same block
    assertEquals(ifBlock3.getParent(), functionB.getParent());

    // b should be the last child of otherFunction
    assertEquals(functionB.getParent(), otherFunction);
    assertNull(otherFunction.getNext());
  }

  @Test
  public void testMinimizeExitPointsWithEmptyBlock() throws Exception {
    String jsCode = "function test() {"
        + "  if (x) return;"
        + "  if (y) return;"
        + "  {}"
        + "  otherFunction();"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseTree(jsCode);
    NodeTraversal.traverse(compiler, root, new MinimizeExitPoints(compiler));
    Node result = compiler.getCompiledCode();

    Node ifBlock1 = NodeUtil.getFirstChild(NodeUtil.findFirstChildOfType(root, Token.FUNCTION));
    Node ifBlock2 = ifBlock1.getNext();
    Node emptyBlock = ifBlock2.getNext();
    Node functionCall = emptyBlock.getNext();

    // emptyBlock should now be removed
    assertNull(emptyBlock.getParent());

    // ifBlock1 and ifBlock2 should now be siblings in the same block
    assertEquals(ifBlock1.getParent(), ifBlock2.getParent());

    // functionCall should now be the last child of ifBlock2's parent
    assertEquals(functionCall.getParent(), ifBlock2.getParent());
    assertNull(ifBlock2.getNext());
  }
}