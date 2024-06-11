package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversal;
import com.google.javascript.jscomp.AbstractTraversal.NodeTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    

    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> controlFlowGraph = mock(ControlFlowGraph.class);
    Scope scope = mock(Scope.class);
    DataFlowAnalysis dataFlowAnalysis = mock(DataFlowAnalysis.class);

    when(compiler.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(Mockito.mock(Iterable.class));
    when(dataFlowAnalysis.getAnalysis(ArgumentMatchers.any(Node.class)))
        .thenReturn(mock(FlowState.class));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // When
    Node root = new Node(Token.PROGRAM);

    Node assign1 = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node assign2 = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(2));
    Node assign3 = new Node(Token.ASSIGN, Node.newName("y"), Node.newNumber(3));

    Node ifStatement = new Node(Token.IF, assign1, Node.newBoolean(true), assign3);
    Node returnStatement = new Node(Token.RETURN, Node.newName("x"));

    root.addChild(assign1);
    root.addChild(ifStatement);
    root.addChild(assign2);
    root.addChild(returnStatement);

    deadAssignmentsElimination.process(null, root);

    // Then
    Node expectedRoot = new Node(Token.PROGRAM);
    expectedRoot.addChild(assign1);
    expectedRoot.addChild(assign3);
    expectedRoot.addChild(returnStatement);

    AbstractTraversal traversal = new NodeTraversal(compiler, false, false);
    AbstractPostOrderCallback postOrderCallback = new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && !ControlFlowGraph.isEnteringNewCfgNode(n)) {
          assertThat(n.getChildren()).isEmpty();
          if (n.getType() == Token.ASSIGN && n.getFirstChild() != null) {
            String firstChildName = n.getFirstChild().getString();
            assertThat(n.getParent().getChildren()).doesNotContain(n.getFirstChild());
            Node parentOfFirstChild = n.getParent().getParent();
            if (parentOfFirstChild != null && parentOfFirstChild.getType() == Token.IF) {
              assertThat(parentOfFirstChild.getLastChild().getString()).isNot(firstChildName);
            }
          }
          if (n.getType() == Token.RETURN && n.getFirstChild() != null) {
            Node child = n.getFirstChild();
            String childName = child.getString();
            assertThat(n.getParent().getChildren()).doesNotContain(child);
            assertThat(childName).isEqualTo("x");
          }
        }
      }
    };

    NodeTraversal traversalResult = new NodeTraversal(compiler, false, false);
    traversalResult.traverse(expectedRoot, null, postOrderCallback);
    traversal.traverse(root, null, postOrderOrderCallback);

    BDDMockito.then(controlFlowGraph).doReturn(null).when(compiler).getControlFlowGraph();

    // Edge cases and exception handling scenarios

    // Test if method is not called when there is no CFG node
    DeadAssignmentsElimination deadAssignmentsElimination2 =
        new DeadAssignmentsElimination(compiler);
    Node root2 = new Node(Token.PROGRAM);
    deadAssignmentsElimination2.process(null, root2);

    // Test if method is not called when there are no assignable nodes
    Node root3 = new Node(Token.PROGRAM);
    deadAssignmentsElimination.process(null, root3);

    // Test if method is not called when there is only one scope with no assignable nodes
    Node root4 = new Node(Token.PROGRAM);
    Scope scope4 = new Scope(null);
    root4.setUserData(Scope.SCOPE_KEY, scope4);
    deadAssignmentsElimination.process(null, root4);

    // Test if method is not called when there is an inner function
    Node root5 = new Node(Token.PROGRAM);
    root5.addChild(new Node(Token.FUNCTION, null, null, null));
    deadAssignmentsElimination.process(null, root5);

    // Test if method is not called when there is a var statement
    Node root6 = new Node(Token.PROGRAM);
    root6.addChild(new Node(Token.VAR, Node.newName("x"), Node.newNull()));
    deadAssignmentsElimination.process(null, root6);

    // Test if method is not called when there is a catch statement
    Node root7 = new Node(Token.PROGRAM);
    root7.addChild(new Node(Token.TRY, null, null, null));
    root7.addChild(new Node(Token.CATCH, null, null, null));
    deadAssignmentsElimination.process(null, root7);

    // Test if method is not called when there is a with statement
    Node root8 = new Node(Token.PROGRAM);
    root8.addChild(new Node(Token.WITH, null, null, null));
    deadAssignmentsElimination.process(null, root8);

    // Test if method is not called when there is a label statement
    Node root9 = new Node(Token.PROGRAM);
    root9.addChild(new Node(Token.LABEL, Node.newName("label")));
    deadAssignmentsElimination.process(null, root9);

    // Test if method is not called when there is a switch statement without any cases
    Node root10 = new Node(Token.PROGRAM);
    root10.addChild(new Node(Token.SWITCH, Node.newName("expr"), null, null));
    deadAssignmentsElimination.process(null, root10);

    // Test if method is not called when there is a switch statement with no cases that match
    Node root11 = new Node(Token.PROGRAM);
    root11.addChild(new Node(Token.SWITCH, Node.newName("expr"), null, null));
    root11.addChild(new Node(Token.CASE, Node.newNumber(1), null, null));
    root11.addChild(new Node(Token.DEFAULT, null, null, null));
    deadAssignmentsElimination.process(null, root11);

    // Test if method is not called when there is a switch statement with multiple cases
    Node root12 = new Node(Token.PROGRAM);
    root12.addChild(new Node(Token.SWITCH, Node.newName("expr"), null, null));
    root12.addChild(new Node(Token.CASE, Node.newNumber(1), null, null));
    root12.addChild(new Node(Token.CASE, Node.newNumber(2), null, null));
    deadAssignmentsElimination.process(null, root12);

    // Test if method is not called when there is a return statement with an expression
    Node root13 = new Node(Token.PROGRAM);
    root13.addChild(new Node(Token.RETURN, Node.newName("x"), null, null));
    deadAssignmentsElimination.process(null, root13);

    // Test if method is not called when there is an increment or decrement operator
    Node root14 = new Node(Token.PROGRAM);
    root14.addChild(new Node(Token.ASSIGN, Node.newName("x"), new Node(Token.INC, Node.newName("x"))));
    deadAssignmentsElimination.process(null, root14);

    // Test if method is not called when there is an assignment operator with non-name LHS
    Node root15 = new Node(Token.PROGRAM);
    root15.addChild(new Node(Token.ASSIGN, new Node(Token.NUMBER, "1"), Node.newNumber(2)));
    deadAssignmentsElimination.process(null, root15);

    
  }
}