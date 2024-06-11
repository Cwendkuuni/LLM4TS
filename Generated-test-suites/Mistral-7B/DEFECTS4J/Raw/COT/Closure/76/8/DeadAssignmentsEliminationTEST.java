package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeadAssignmentsEliminationTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private ControlFlowGraph<Node> cfg;
  @Mock
  private Node root;
  @Mock
  private Node externs;
  @Mock
  private NodeTraversal nodeTraversal;
  @Mock
  private Scope scope;
  @Mock
  private DiGraphNode<Node, Branch> cfgNode;
  @Mock
  private LiveVariablesAnalysis liveVariablesAnalysis;
  @Mock
  private FlowState<LiveVariableLattice> state;

  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testProcess() {
    // TODO: Add test cases for the process method.
  }

  @Test
  public void testEnterScope() {
    // TODO: Add test cases for the enterScope method.
  }

  @Test
  public void testExitScope() {
    // TODO: Add test cases for the exitScope method.
  }

  @Test
  public void testVisit() {
    // TODO: Add test cases for the visit method.
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    // Given
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(cfg.getDirectedGraphNodes()).thenReturn(iterable(cfgNode));
    when(cfgNode.getValue()).thenReturn(root);

    // When
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    // Then
    // TODO: Add assertions for the expected behavior of tryRemoveDeadAssignments.
  }

  @Test
  public void testTryRemoveAssignment() {
    // Given
    when(root.getType()).thenReturn(Token.ASSIGN);
    when(scope.getVar(anyString())).thenReturn(mock(Var.class));
    when(liveVariablesAnalysis.getEscapedLocals()).thenReturn(emptyList());

    // When
    deadAssignmentsElimination.tryRemoveAssignment(nodeTraversal, root, state);

    // Then
    // TODO: Add assertions for the expected behavior of tryRemoveAssignment.
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // Given
    Node n = mock(Node.class);
    Node exprRoot = mock(Node.class);

    // When
    boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(n, exprRoot, "variable");

    // Then
    // TODO: Add assertions for the expected behavior of isVariableStillLiveWithinExpression.
  }

  /**
   * Helper methods to create mock objects and collections.
   */

  private <T> Iterable<T> iterable(T... elements) {
    return new Iterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return new Iterator<T>() {
          int index = 0;

          @Override
          public boolean hasNext() {
            return index < elements.length;
          }

          @Override
          public T next() {
            return elements[index++];
          }
        };
      }
    };
  }

  private <T> T mock(Class<T> clazz) {
    return mock(clazz, false);
  }

  private <T> T mock(Class<T> clazz, boolean isFinal) {
    return mockito.mock(clazz, isMockitoFinal(isFinal));
  }

  private <T> Node mock(Node node) {
    return (Node) mock(Node.class);
  }

  private <T> Var mock(Var var) {
    return mock(Var.class);
  }

  private static <T> boolean isMockitoFinal(boolean isFinal) {
    return isFinal;
  }
}