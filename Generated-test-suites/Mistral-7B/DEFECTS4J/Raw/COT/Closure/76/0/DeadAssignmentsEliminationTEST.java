package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testEmptyProgram() {
    Node root = Node.EMPTY;
    deadAssignmentsElimination.process(Node.NULL, root);
    assertTrue(NodeUtil.isEqual(root, Node.EMPTY));
  }

  @Test
  public void testNoAssignments() {
    Node root = Node.newText("print(1)");;
    deadAssignmentsElimination.process(Node.NULL, root);
    assertTrue(NodeUtil.isEqual(root, Node.newText("print(1)")));
  }

  @Test
  public void testSimpleAssignment() {
    Node root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test
  public void testMultipleAssignments() {
    Node root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test
  public void testDeadAssignment() {
    Node root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    root = Node.newAssignment(Node.newName("x"), Node.newName("x"));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test
  public void testIfStatement() {
    Node root = Node.newIf(Node.newBooleanLiteral(false));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root.addChild(root);
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    root = Node.newIf(Node.newBooleanLiteral(true));
    root.addChild(root);
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newIf(Node.newBooleanLiteral(false));
    expected.addChild(Node.newAssignment(Node.newName("x"), Node.newNumber(1)));
    expected.addChild(Node.newAssignment(Node.newName("x"), Node.newNumber(2)));
    expected.addChild(Node.newIf(Node.newBooleanLiteral(true)));
    expected.addChild(Node.newAssignment(Node.newName("x"), Node.newNumber(2)));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentInsideIf() {
    Node root = Node.newIf(Node.newBooleanLiteral(false));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newIf(Node.newBooleanLiteral(false));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
  }

  @Test
  public void testWhileStatement() {
    Node root = Node.newWhile(Node.newBooleanLiteral(false));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root.addChild(root);
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newWhile(Node.newBooleanLiteral(false));
    expected.addChild(Node.newAssignment(Node.newName("x"), Node.newNumber(1)));
    expected.addChild(Node.newWhile(Node.newBooleanLiteral(false)));
    expected.addChild(Node.newAssignment(Node.newName("x"), Node.newNumber(2)));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentInsideWhile() {
    Node root = Node.newWhile(Node.newBooleanLiteral(false));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newWhile(Node.newBooleanLiteral(false));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
  }

  @Test
  public void testForStatement() {
    Node root = Node.newFor(Node.newName("i"), Node.newNumber(1), Node.newNumber(3));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newFor(Node.newName("i"), Node.newNumber(1), Node.newNumber(3));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newFor(Node.newName("i"), Node.newNumber(1), Node.newNumber(3));
    expected.addChild(Node.newAssignment(Node.newName("x"), Node.newNumber(1)));
    expected.addChild(Node.newFor(Node.newName("i"), Node.newNumber(1), Node.newNumber(3)));
    expected.addChild(Node.newAssignment(Node.newName("x"), Node.newNumber(2)));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentInsideFor() {
    Node root = Node.newFor(Node.newName("i"), Node.newNumber(1), Node.newNumber(3));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newFor(Node.newName("i"), Node.newNumber(1), Node.newNumber(3));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
  }

  @Test
  public void testSwitchStatement() {
    Node root = Node.newSwitch(Node.newName("x"));
    root = Node.newCase(Node.newNumber(1));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(1));
    root = Node.newCase(Node.newNumber(2));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newSwitch(Node.newName("x"));
    expected.addChild(Node.newCase(Node.newNumber(1)));
    expected.addChild(Node.newAssignment(Node.newName("y"), Node.newNumber(1)));
    expected.addChild(Node.newCase(Node.newNumber(2)));
    expected.addChild(Node.newAssignment(Node.newName("y"), Node.newNumber(2)));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test
  public void testDeadAssignmentInsideSwitch() {
    Node root = Node.newSwitch(Node.newName("x"));
    root = Node.newCase(Node.newNumber(1));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(1));
    root = Node.newCase(Node.newNumber(2));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(2));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(3));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newSwitch(Node.newName("x"));
    expected.addChild(Node.newCase(Node.newNumber(1)));
    expected.addChild(Node.newAssignment(Node.newName("y"), Node.newNumber(1)));
    expected.addChild(Node.newCase(Node.newNumber(2)));
    expected.addChild(Node.newAssignment(Node.newName("y"), Node.newNumber(2)));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test
  public void testReturnStatement() {
    Node root = Node.newReturn(Node.newNumber(1));
    deadAssignmentsElimination.process(Node.NULL, root);
    assertTrue(NodeUtil.isEqual(root, Node.newReturn(Node.newNumber(1))));
  }

  @Test
  public void testDeadAssignmentBeforeReturn() {
    Node root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newReturn(Node.newName("x"));
    deadAssignmentsElimination.process(Node.NULL, root);
    assertTrue(NodeUtil.isEqual(root, Node.newReturn(Node.newName("x"))));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentAfterReturn() {
    Node root = Node.newReturn(Node.newNumber(1));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    deadAssigmentsElimination.process(Node.NULL, root);
  }

  @Test
  public void testDeadAssignmentInsideFunction() {
    Node root = Node.newFunction(Node.newName("f"), Node.newName("x"), Node.newBlock());
    root = Node.newLocal(Node.newName("x"));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newLocal(Node.newName("y"));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(1));
    root = Node.newStatementList();
    root = Node.newReturn(Node.newName("y"));
    deadAssignmentsElimination.process(Node.NULL, root);
    Node expected = Node.newFunction(Node.newName("f"), Node.newName("x"), Node.newBlock());
    expected.addChild(Node.newLocal(Node.newName("x")));
    expected.addChild(Node.newLocal(Node.newName("y")));
    expected.addChild(Node.newStatementList());
    expected.addChild(Node.newReturn(Node.newName("y")));
    assertTrue(NodeUtil.isEqual(root, expected));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentInsideFunctionBeforeReturn() {
    Node root = Node.newFunction(Node.newName("f"), Node.newName("x"), Node.newBlock());
    root = Node.newLocal(Node.newName("x"));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newLocal(Node.newName("y"));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(1));
    root = Node.newStatementList();
    root = Node.newReturn(Node.newName("x"));
    deadAssignmentsElimination.process(Node.NULL, root);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentInsideFunctionAfterReturn() {
    Node root = Node.newFunction(Node.newName("f"), Node.newName("x"), Node.newBlock());
    root = Node.newLocal(Node.newName("x"));
    root = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    root = Node.newLocal(Node.newName("y"));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(1));
    root = Node.newStatementList();
    root = Node.newReturn(Node.newName("x"));
    root = Node.newAssignment(Node.newName("y"), Node.newNumber(2));
    deadAssignmentsElimination.process(Node.NULL, root);
  }
}