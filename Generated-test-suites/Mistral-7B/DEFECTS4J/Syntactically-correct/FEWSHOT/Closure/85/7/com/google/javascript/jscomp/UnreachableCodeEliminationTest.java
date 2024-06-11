package org.apache.commons.cli.test;

import static com.google.javascript.jscomp.UnreachableCodeElimination.removeNoOpStatements;
import static com.google.javascript.jscomp.UnreachableCodeElimination.removeUnreachableCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {
  Compiler compiler;

  @Before
  public void setup() {
    compiler = new Compiler();
  }

  @Test
  public void testRemoveUnreachableCode() {
    Node root = new Node(Token.SCRIPT);
    // Unreachable code
    root.addChild(new Node(Token.RETURN, null));
    root.addChild(new Node(Token.TEXT, "unreachable"));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    new UnreachableCodeElimination(compiler, removeNoOpStatements).process(null, root);
    NodeUtil.traverseAndPrint(root);

    assertEquals(Token.SCRIPT, root.getType());
    assertNull(root.getFirstChild());
  }

  @Test
  public void testRemoveConditionalBranching() {
    Node root = new Node(Token.SCRIPT);
    Node ifNode = new Node(Token.IF, null);
    Node trueBranch = new Node(Token.TEXT, "true");
    Node falseBranch = new Node(Token.TEXT, "false");

    ifNode.addChild(new Node(Token.IF, null));
    ifNode.addChild(new Node(Token.ELSE));
    ifNode.addChild(trueBranch);
    ifNode.getLastChild().addChild(new Node(Token.TEXT, "unreachable"));
    ifNode.getLastChild().addChild(falseBranch);

    root.addChild(ifNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    new UnreachableCodeElimination(compiler, removeNoOpStatements).process(null, root);
    NodeUtil.traverseAndPrint(root);

    assertEquals(Token.SCRIPT, root.getType());
    assertEquals(Token.IF, ifNode.getType());
    assertEquals(Token.TEXT, ifNode.getFirstChild().getType());
    assertNull(ifNode.getLastChild().getFirstChild());
    assertEquals(Token.TEXT, ifNode.getLastChild().getType());
  }

  @Test
  public void testRemoveReturnStatement() {
    Node root = new Node(Token.SCRIPT);
    Node ifNode = new Node(Token.IF, null);
    Node returnNode = new Node(Token.RETURN, null);
    Node trueBranch = new Node(Token.TEXT, "true");
    Node falseBranch = new Node(Token.TEXT, "false");

    ifNode.addChild(returnNode);
    ifNode.addChild(new Node(Token.ELSE));
    ifNode.addChild(trueBranch);
    ifNode.getLastChild().addChild(falseBranch);

    root.addChild(ifNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    new UnreachableCodeElimination(compiler, removeNoOpStatements).process(null, root);
    NodeUtil.traverseAndPrint(root);

    assertEquals(Token.SCRIPT, root.getType());
    assertNull(ifNode.getFirstChild());
    assertEquals(Token.TEXT, ifNode.getType());
    assertEquals(Token.TEXT, ifNode.getFirstChild().getType());
    assertEquals(Token.TEXT, ifNode.getLastChild().getType());
  }

  @Test
  public void testRemoveFunctionCall() {
    Node root = new Node(Token.SCRIPT);
    Node ifNode = new Node(Token.IF, null);
    Node functionCall = new Node(Token.CALL, null);
    Node function = new Node(Token.FUNCTION, null);
    Node trueBranch = new Node(Token.TEXT, "true");
    Node falseBranch = new Node(Token.TEXT, "false");

    function.addChild(new Node(Token.TEXT, "function"));
    functionCall.addChild(function);
    functionCall.addChild(new Node(Token.NUMBER, 42));

    ifNode.addChild(functionCall);
    ifNode.addChild(new Node(Token.ELSE));
    ifNode.addChild(trueBranch);
    ifNode.getLastChild().addChild(falseBranch);

    root.addChild(ifNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    new UnreachableCodeElimination(compiler, removeNoOpStatements).process(null, root);
    NodeUtil.traverseAndPrint(root);

    assertEquals(Token.SCRIPT, root.getType());
    assertNull(ifNode.getFirstChild());
    assertEquals(Token.TEXT, ifNode.getType());
    assertEquals(Token.TEXT, ifNode.getFirstChild().getType());
    assertEquals(Token.TEXT, ifNode.getLastChild().getType());
  }

  @Test
  public void testRemoveVariableDeclaration() {
    Node root = new Node(Token.SCRIPT);
    Node ifNode = new Node(Token.IF, null);
    Node varDeclaration = new Node(Token.VAR, null);
    Node trueBranch = new Node(Token.TEXT, "true");
    Node falseBranch = new Node(Token.TEXT, "false");

    ifNode.addChild(varDeclaration);
    ifNode.addChild(new Node(Token.ELSE));
    ifNode.addChild(trueBranch);
    ifNode.getLastChild().addChild(falseBranch);

    root.addChild(ifNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    new UnreachableCodeElimination(compiler, removeNoOpStatements).process(null, root);
    NodeUtil.traverseAndPrint(root);

    assertEquals(Token.SCRIPT, root.getType());
    assertNull(ifNode.getFirstChild());
    assertEquals(Token.TEXT, ifNode.getType());
    assertEquals(Token.TEXT, ifNode.getFirstChild().getType());
    assertEquals(Token.TEXT, ifNode.getLastChild().getType());
  }
}