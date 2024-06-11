package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableLiveness;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testTryRemoveAssignmentWithNoLiveness() {
    AbstractCompiler compiler = new TestCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(compiler, null);
    Node root = Node.newProgram(0);

    Node n = Node.newAssignment(1, Node.newName("x"), Node.newName("y"));
    root.addChild(n);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.tryRemoveDeadAssignments(
        new NodeTraversal(root), cfg);

    assertTrue(n.isPresentInTree());
  }

  @Test
  public void testTryRemoveAssignmentWithDeadVariable() {
    AbstractCompiler compiler = new TestCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(compiler, null);
    Node root = Node.newProgram(0);

    Node condition = Node.newComparison(2, Node.newName("x"), Node.newNumber(0));
    Node assign = Node.newAssignment(1, Node.newName("x"), Node.newNumber(1));
    Node body = Node.newBlock();
    body.addChild(assign);

    Node ifStatement = Node.newIfStatement(condition, body, null);
    root.addChild(ifStatement);

    Scope scope = new Scope(root);
    scope.declareVariable("x", Scope.VarType.VAR, Node.newName("x"));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    assertFalse(assign.isPresentInTree());
  }

  @Test
  public void testTryRemoveAssignmentWithStillLiveVariable() {
    AbstractCompiler compiler = new TestCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(compiler, null);
    Node root = Node.newProgram(0);

    Node condition = Node.newComparison(2, Node.newName("x"), Node.newNumber(0));
    Node assign = Node.newAssignment(1, Node.newName("x"), Node.newNumber(1));
    Node body = Node.newBlock();
    body.addChild(assign);
    Node print = Node.newCall("print", Node.newList(Node.newName("x")));
    body.addChild(print);

    Node ifStatement = Node.newIfStatement(condition, body, null);
    root.addChild(ifStatement);

    Scope scope = new Scope(root);
    scope.declareVariable("x", Scope.VarType.VAR, Node.newName("x"));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    assertTrue(assign.isPresentInTree());
  }

  @Test
  public void testTryRemoveAssignmentWithDeadVariableInExpression() {
    AbstractCompiler compiler = new TestCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(compiler, null);
    Node root = Node.newProgram(0);

    Node condition = Node.newComparison(2, Node.newName("x"), Node.newNumber(0));
    Node assign1 = Node.newAssignment(1, Node.newName("x"), Node.newNumber(1));
    Node body = Node.newBlock();
    body.addChild(assign1);

    Node assign2 = Node.newAssignment(1, Node.newName("y"), Node.newName("x"));
    body.addChild(assign2);

    Node print = Node.newCall("print", Node.newList(Node.newName("y")));
    body.addChild(print);

    Node ifStatement = Node.newIfStatement(condition, body, null);
    root.addChild(ifStatement);

    Scope scope = new Scope(root);
    scope.declareVariable("x", Scope.VarType.VAR, Node.newName("x"));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    assertTrue(assign1.isPresentInTree());
    assertTrue(assign2.isPresentInTree());
    assertFalse(print.isPresentInTree());
  }

  @Test
  public void testTryRemoveAssignmentWithDeadVariableInDeepExpression() {
    AbstractCompiler compiler = new TestCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(compiler, null);
    Node root = Node.newProgram(0);

    Node assign = Node.newAssignment(1, Node.newName("x"), Node.newNumber(1));
    Node condition = Node.newComparison(2, Node.newName("x"), Node.newNumber(0));
    Node body = Node.newBlock();
    Node assign2 = Node.newAssignment(1, Node.newName("y"), Node.newName("x"));
    body.addChild(assign2);
    Node print = Node.newCall("print", Node.newList(Node.newName("y")));
    body.addChild(print);

    Node ifStatement = Node.newIfStatement(condition, body, null);
    root.addChild(ifStatement);

    Scope rootScope = new Scope(root);
    Scope scope = new Scope(Node.newBlock());
    scope.setParent(rootScope);
    rootScope.addChild(scope);

    scope.declareVariable("x", Scope.VarType.VAR, Node.newName("x"));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    assertTrue(assign.isPresentInTree());
    assertFalse(assign2.isPresentInTree());
    assertFalse(print.isPresentInTree());
  }

  @Test
  public void testTryRemoveAssignmentWithDeadVariableInDeepExpression2() {
    AbstractCompiler compiler = new TestCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(compiler, null);
    Node root = Node.newProgram(0);

    Node assign = Node.newAssignment(1, Node.newName("x"), Node.newNumber(1));
    Node condition = Node.newComparison(2, Node.newName("x"), Node.newNumber(0));
    Node body = Node.newBlock();
    Node assign2 = Node.newAssignment(1, Node.newName("y"), Node.newName("x"));
    Node assign3 = Node.newAssignment(1, Node.newName("z"), Node.newName("y"));
    body.addChild(assign3);
    Node print = Node.newCall("print", Node.newList(Node.newName("z")));
    body.addChild(print);

    Node ifStatement = Node.newIfStatement(condition, body, null);
    root.addChild(ifStatement);

    Scope rootScope = new Scope(root);
    Scope scope = new Scope(Node.newBlock());
    scope.setParent(rootScope);
    rootScope.addChild(scope);

    scope.declareVariable("x", Scope.VarType.VAR, Node.newName("x"));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    assertTrue(assign.isPresentInTree());
    assertFalse(assign2.isPresentInTree());
    assertFalse(assign3.isPresentInTree());
    assertFalse(print.isPresentInTree());
  }

  @Test
  public void testTryRemoveAssignmentWithDeadVariableInDeepExpression3() {
    AbstractCompiler compiler = new TestCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(compiler, null);
    Node root = Node.newProgram(0);

    Node assign = Node.newAssignment(1, Node.newName("x"), Node.newNumber(1));
    Node condition = Node.newComparison(2, Node.newName("x"), Node.newNumber(0));
    Node body = Node.newBlock();
    Node assign2 = Node.newAssignment(1, Node.newName("y"), Node.newName("x"));
    Node assign3 = Node.newAssignment(1, Node.newName("z"), Node.newName("y"));
    Node assign4 = Node.newAssignment(1, Node.newName("w"), Node.newName("z"));
    body.addChild(assign4);
    Node print = Node.newCall("print", Node.newList(Node.newName("w")));
    body.addChild(print);

    Node ifStatement = Node.newIfStatement(condition, body, null);
    root.addChild(ifStatement);

    Scope rootScope = new Scope(root);
    Scope scope = new Scope(Node.newBlock());
    scope.setParent(rootScope);
    rootScope.addChild(scope);

    scope.declareVariable("x", Scope.VarType.VAR, Node.newName("x"));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    assertTrue(assign.isPresentInTree());
    assertFalse(assign2.isPresentInTree());
    assertFalse(assign3.isPresentInTree());
    assertFalse(assign4.isPresentInTree());
    assertFalse(print.isPresentInTree());
  }

  private static class TestCompiler implements Compiler {
    @Override
    public Node compile(Node externs, Node root) throws CompilerException {
      return null;
    }
  }
}