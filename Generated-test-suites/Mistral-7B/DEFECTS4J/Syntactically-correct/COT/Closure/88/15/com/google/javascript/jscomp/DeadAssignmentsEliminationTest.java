package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private DeadAssignmentsElimination deadAssignmentsElimination;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = new Compiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testProcess() {
    Node root = Node.newProgram();
    deadAssignmentsElimination.process(null, root);
  }

  @Test
  public void testEnterScope() {
    // Test empty scope
    NodeTraversal traverser = new TraversingRootVisitor(null, null);
    deadAssignmentsElimination.enterScope(traverser);

    // Test global scope
    traverser = new TraversingRootVisitor(null, new Scope());
    deadAssignmentsElimination.enterScope(traverser);

    // Test inner function scope
    traverser = new TraversingRootVisitor(null, new Scope().addChild(Node.newFunction("f", null, Node.newBlock(null))));
    deadAssignmentsElimination.enterScope(traverser);
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    Node root = Node.newProgram();
    root.addChild(Node.newName("x"));
    root.addChild(Node.newAssignment(Token.ASSIGN, Node.newName("x"), Node.newNumberLiteral(1)));
    root.addChild(Node.newName("x"));

    NodeTraversal traverser = new TraversingRootVisitor(null, new Scope());
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverser, null);

    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testTryRemoveAssignment() {
    Node root = Node.newProgram();
    root.addChild(Node.newName("x"));
    root.addChild(Node.newAssignment(Token.ASSIGN, Node.newName("x"), Node.newNumberLiteral(1)));

    NodeTraversal traverser = new TraversingRootVisitor(null, new Scope());
    deadAssignmentsElimination.tryRemoveAssignment(traverser, root.getLastChild(), null);

    assertEquals(1, root.getChildCount());
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    Node root = Node.newProgram();
    root.addChild(Node.newName("x"));
    root.addChild(Node.newAssignment(Token.ASSIGN, Node.newName("x"), Node.newNumberLiteral(1)));
    root.addChild(Node.newName("x"));
    root.addChild(Node.newName("y"));
    root.addChild(Node.newAssignment(Token.ASSIGN, Node.newName("y"), Node.newName("x")));

    NodeTraversal traverser = new TraversingRootVisitor(null, new Scope());
    boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(
        traverser.getScopeRoot().getLastChild(), traverser.getScopeRoot().getLastChild(), "x");

    assertTrue(result);

    root.removeChild(3);
    root.addChild(Node.newAssignment(Token.ASSIGN, Node.newName("x"), Node.newNumberLiteral(2)));
    result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(
        traverser.getScopeRoot().getLastChild(), traverser.getScopeRoot().getLastChild(), "x");

    assertFalse(result);
  }
}

class TraversingRootVisitor implements NodeTraversal {

  private Scope scope;
  private Node node;

  public TraversingRootVisitor(Scope scope, Node node) {
    this.scope = scope;
    this.node = node;
  }

  @Override
  public Scope getScope() {
    return scope;
  }

  @Override
  public Node getScopeRoot() {
    return node;
  }

  @Override
  public void enterScope(NodeTraversal t) {
  }

  @Override
  public void exitScope(NodeTraversal t) {
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
  }
}