package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExits() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);

    Node ast = Node.objectLiteral();

    Node if1 = Node.newIfStatement(
        Node.booleanLiteral(true),
        Node.returnStatement(Node.stringLiteral("exit1")),
        Node.block());

    Node if2 = Node.newIfStatement(
        Node.booleanLiteral(false),
        Node.returnStatement(Node.stringLiteral("exit2")),
        Node.block());

    Node if3 = Node.newIfStatement(
        Node.booleanLiteral(false),
        Node.returnStatement(Node.stringLiteral("exit3")),
        Node.block());

    Node function = Node.newFunctionStatement("test", Node.block(), null);
    Node functionBody = function.getLastChild();

    functionBody.addChildToBack(if1);
    functionBody.addChildToBack(if2);
    functionBody.addChildToBack(if3);
    ast.addChildToBack(function);

    NodeTraversal.traverse(compiler, ast, minimizer);
    Node minimized = minimizer.compiler.getOutput();

    // check if1 was removed and its exit statement moved to if2
    assertTrue(functionBody.getFirstChild() instanceof Node.EmptyStatement);
    assertTrue(if2.getFirstChild() instanceof Node.ReturnStatement);
    assertTrue(if2.getFirstChild().getString().equals("exit1"));

    // check if3 was removed and its exit statement moved to if2
    assertTrue(if2.getNext() instanceof Node.EmptyStatement);
    assertTrue(if2.getNext().getFirstChild() instanceof Node.ReturnStatement);
    assertTrue(if2.getNext().getFirstChild().getString().equals("exit3"));

    // check if no function declarations were removed
    for (Node node : functionBody.getChildren()) {
      if (isFunctionDeclaration(node)) {
        assertFalse("Function declarations should not be removed", true);
      }
    }

    // check that there are no more exits in the function
    Node exitPoint = NodeUtil.getExitPoint(functionBody);
    assertNull(exitPoint);
  }
}