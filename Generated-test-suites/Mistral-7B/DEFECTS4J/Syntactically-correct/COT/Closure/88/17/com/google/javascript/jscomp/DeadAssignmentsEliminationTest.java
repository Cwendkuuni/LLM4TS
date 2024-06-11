package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private ControlFlowGraph<Node> controlFlowGraph;

  @Before
  public void setup() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testSimpleAssignment() {
    Node root = createNode("x = 1;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(Token.VOID, root.getType());
  }

  @Test
  public void testSimpleAssignmentWithDeadVariable() {
    Node root = createNode("x = y;");
    VariableAssignments variableAssignments =
        new VariableAssignments(compiler, new Scope());
    variableAssignments.process(root);
    Node deadVariable = findNode("y", root);
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertNull(findNode("y", root.getParent()));
  }

  @Test
  public void testAssignmentWithMultipleAssignments() {
    Node root = createNode("x = 1; x = 2;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(Token.VOID, root.getType());
  }

  @Test
  public void testAssignmentWithDeadVariableInConditional() {
    Node root = createNode("if (y) { x = 1; } x = 2;");
    VariableAssignments variableAssignments =
        new VariableAssignments(compiler, new Scope());
    variableAssignments.process(root);
    Node deadVariable = findNode("y", root);
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertNull(findNode("y", root.getParent()));
  }

  @Test
  public void testAssignmentWithLiveVariableInConditional() {
    Node root = createNode("if (x) { y = 1; } x = 2;");
    VariableAssignments variableAssignments =
        new VariableAssignments(compiler, new Scope());
    variableAssignments.process(root);
    Node liveVariable = findNode("x", root);
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertNotNull(findNode("x", root.getParent()));
  }

  @Test
  public void testAssignmentWithDeadVariableInLoop() {
    Node root = createNode("for (var y = 0; y < 10; y++) { x = 1; } x = 2;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(Token.VOID, root.getType());
  }

  @Test
  public void testAssignmentWithDeadVariableInFunctionCall() {
    Node root = createNode("f(x = 1); x = 2;");
    VariableAssignments variableAssignments =
        new VariableAssignments(compiler, new Scope());
    variableAssignments.process(root);
    Node argument = findNode("x", root);
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertNull(argument);
  }

  @Test
  public void testAssignmentWithLiveVariableInFunctionCall() {
    Node root = createNode("f(x = 1, x = 2);");
    VariableAssignments variableAssignments =
        new VariableAssignments(compiler, new Scope());
    variableAssignments.process(root);
    Node argument1 = findNode("x", root.getFirstChild());
    Node argument2 = findNode("x", root.getLastChild());
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertNotNull(argument1);
    assertNotNull(argument2);
  }

  @Test
  public void testAssignmentWithDeadVariableInSwitch() {
    Node root = createNode("switch (y) { case 1: x = 1; case 2: x = 2; } x = 3;");
    VariableAssignments variableAssignments =
        new VariableAssignments(compiler, new Scope());
    variableAssignments.process(root);
    Node deadVariable = findNode("y", root);
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertNull(findNode("y", root.getParent()));
  }

  @Test
  public void testAssignmentWithLiveVariableInSwitch() {
    Node root = createNode("switch (x) { case 1: y = 1; case 2: y = 2; } x = 3;");
    VariableAssignments variableAssignments =
        new VariableAssignments(compiler, new Scope());
    variableAssignments.process(root);
    Node liveVariable = findNode("x", root);
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertNotNull(findNode("x", root.getParent()));
  }

  private Node createNode(String code) {
    return new CompilerOptions().getParser(compiler).parseProgram(code, null)
        .getRootNode();
  }

  private Node findNode(String name, Node node) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (NodeUtil.isName(child) && name.equals(child.getString())) {
        return child;
      }
    }
    return null;
  }

  private static class VariableAssignments extends AbstractPostOrderCallback {

    private final AbstractCompiler compiler;
    private final Scope scope;

    public VariableAssignments(AbstractCompiler compiler, Scope scope) {
      this.compiler = compiler;
      this.scope = scope;
    }

    @Override
    public void process(Node externs, Node root) {
      NodeTraversal nodeTraversal = new NodeTraversal(root, this);
      NodeTraversal.traverse(compiler, root, nodeTraversal);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (NodeUtil.isAssignmentOp(n) ||
          n.getType() == Token.INC || n.getType() == Token.DEC) {
        Node lhs = n.getFirstChild();
        if (!NodeUtil.isName(lhs)) {
          return;
        }
        String name = lhs.getString();
        Var var = scope.getVar(name);
        if (var != null) {
          scope.setVariableValue(name, n.getLastChild());
          deleteVariable(scope, name);
        }
      }
    }

    private void deleteVariable(Scope scope, String name) {
      if (scope.isGlobal()) {
        return;
      }
      Scope parentScope = scope.getParent();
      if (parentScope != null) {
        parentScope.deleteVariable(name);
        deleteVariable(parentScope, name);
      }
    }
  }

  
  @org.junit.Test
  public void testAll() {
    Node root = createNode("x = 1; y = 1; x = 2; y = 2;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(Token.VOID, root.getType());
    assertNull(findNode("x", root.getParent()));
    assertNull(findNode("y", root.getParent()));
  }
  
}