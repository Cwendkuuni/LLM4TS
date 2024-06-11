import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mockito.Mockito.mock;

public class DeadAssignmentsEliminationTest {

 private AbstractCompiler compilerMock;
 private ControlFlowGraph<Node> controlFlowGraphMock;
 private DataFlowAnalysis dataFlowAnalysisMock;
 private Node nodeMock;
 private Scope scopeMock;
 private DeadAssignmentsElimination deadAssignmentsElimination;

 @Before
 public void setUp() {
 compilerMock = mock(AbstractCompiler.class);
 controlFlowGraphMock = mock(ControlFlowGraph.class);
 dataFlowAnalysisMock = mock(DataFlowAnalysis.class);
 scopeMock = mock(Scope.class);
 deadAssignmentsElimination = new DeadAssignmentsElimination(compilerMock);
 }

 @Test
 public void testProcessNullExternsAndRoot() {
 deadAssignmentsElimination.process(null, null);
 verify(compilerMock).reportError(anyString());
 }

 @Test
 public void testProcessValidExternsAndRoot() {
 Node externsMock = mock(Node.class);
 Node rootMock = mock(Node.class);
 deadAssignmentsElimination.process(externsMock, rootMock);
 verify(compilerMock).runPassesInOrder(eq(DeadAssignmentsElimination.class));
 }

 @Test
 public void testEnterScopeGlobalScope() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 when(nodeTraversalMock.getScope()).thenReturn(scopeMock);
 when(nodeTraversalMock.getControlFlowGraph()).thenReturn(controlFlowGraphMock);
 when(dataFlowAnalysisMock.getAnalysis(any())).thenReturn(mock(FlowState.class));

 deadAssignmentsElimination.enterScope(nodeTraversalMock);
 verify(nodeTraversalMock, times(1)).getScope();
 verify(nodeTraversalMock, times(1)).getControlFlowGraph();
 }

 @Test
 public void testEnterScopeNonGlobalScope() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node fnBlockMock = mock(Node.class);
 when(fnBlockMock.hasChildren()).thenReturn(false);
 when(fnBlockMock.getLastChild()).thenReturn(null);
 when(nodeTraversalMock.getScopeRoot()).thenReturn(fnBlockMock);
 when(nodeTraversalMock.getControlFlowGraph()).thenReturn(controlFlowGraphMock);
 when(dataFlowAnalysisMock.getAnalysis(any())).thenReturn(mock(FlowState.class));

 deadAssignmentsElimination.enterScope(nodeTraversalMock);
 verify(nodeTraversalMock, times(1)).getScope();
 verify(nodeTraversalMock, times(1)).getControlFlowGraph();
 }

 @Test
 public void testEnterScopeWithFunction() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node fnBlockMock = mock(Node.class);
 when(fnBlockMock.hasChildren()).thenReturn(true);
 when(fnBlockMock.getLastChild()).thenReturn(null);
 when(nodeTraversalMock.getScopeRoot()).thenReturn(fnBlockMock);
 when(nodeTraversalMock.getControlFlowGraph()).thenReturn(controlFlowGraphMock);
 when(dataFlowAnalysisMock.getAnalysis(any())).thenReturn(mock(FlowState.class));

 deadAssignmentsElimination.enterScope(nodeTraversalMock);
 verify(nodeTraversalMock, never()).getScope();
 }

 @Test
 public void testEnterScopeWithoutAssignments() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node fnBlockMock = mock(Node.class);
 when(fnBlockMock.hasChildren()).thenReturn(false);
 when(fnBlockMock.getLastChild()).thenReturn(null);
 when(nodeTraversalMock.getScopeRoot()).thenReturn(fnBlockMock);
 when(nodeTraversalMock.getControlFlowGraph()).thenReturn(controlFlowGraphMock);
 when(dataFlowAnalysisMock.getAnalysis(any())).thenReturn(mock(FlowState.class));

 deadAssignmentsElimination.enterScope(nodeTraversalMock);
 verify(nodeTraversalMock, never()).getScope();
 }

 @Test
 public void testVisitNullParent() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node nMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, null, nMock);
 verify(nodeTraversalMock, never()).visit(any(), any(), any());
 }

 @Test
 public void testVisitNoAssignments() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node nMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, nMock, null);
 verify(nodeTraversalMock, never()).visit(any(), any(), any());
 }

 @Test
 public void testVisitAssignmentOperator() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node lhsMock = mock(Node.class);
 Node rhsMock = mock(Node.class);
 Node nMock = new Node(Token.ASSIGN, lhsMock, rhsMock);
 deadAssignmentsElimination.visit(nodeTraversalMock, nMock, null);
 verify(nodeTraversalMock, times(1)).visit(eq(lhsMock), any(), any());
 verify(nodeTraversalMock, times(1)).visit(eq(rhsMock), any(), any());
 }

 @Test
 public void testVisitAssignmentOperatorWithDeadVariable() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node lhsMock = mock(Node.class);
 Node rhsMock = mock(Node.class);
 Node nMock = new Node(Token.ASSIGN, lhsMock, rhsMock);
 when(lhsMock.getString()).thenReturn("deadVariable");

 NodeTraversal nodeTraversalSpy = spy(nodeTraversalMock);
 when(compilerMock.getScope()).thenReturn(scopeMock);
 when(scopeMock.getVar("deadVariable")).thenReturn(mock(Var.class));
 when(scopeMock.getVar("deadVariable").isDead()).thenReturn(true);

 deadAssignmentsElimination.visit(nodeTraversalSpy, nMock, null);
 verify(nodeTraversalSpy, never()).visit(lhsMock, any(), any());
 verify(nodeTraversalSpy, never()).visit(rhsMock, any(), any());
 verify(compilerMock, times(1)).reportCodeChange();
 }

 @Test
 public void testVisitAssignmentOperatorWithLiveVariable() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node lhsMock = mock(Node.class);
 Node rhsMock = mock(Node.class);
 Node nMock = new Node(Token.ASSIGN, lhsMock, rhsMock);
 when(lhsMock.getString()).thenReturn("liveVariable");
 when(scopeMock.getVar("liveVariable")).thenReturn(mock(Var.class));
 when(scopeMock.getVar("liveVariable").isDead()).thenReturn(false);

 NodeTraversal nodeTraversalSpy = spy(nodeTraversalMock);
 when(compilerMock.getScope()).thenReturn(scopeMock);

 deadAssignmentsElimination.visit(nodeTraversalSpy, nMock, null);
 verify(nodeTraversalSpy, times(1)).visit(lhsMock, any(), any());
 verify(nodeTraversalSpy, times(1)).visit(rhsMock, any(), any());
 }

 @Test
 public void testVisitDecrementOperator() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node nMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, nMock, null);
 verify(nodeTraversalMock, times(1)).visitPredecessors(nMock, any());
 }

 @Test
 public void testVisitIncrementOperator() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node nMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, nMock, null);
 verify(nodeTraversalMock, times(1)).visitPredecessors(nMock, any());
 }

 @Test
 public void testVisitIfStatement() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node conditionMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, null, null);
 verify(nodeTraversalMock, times(1)).visit(conditionMock, any(), any());
 }

 @Test
 public void testVisitWhileStatement() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node conditionMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, null, null);
 verify(nodeTraversalMock, times(1)).visit(conditionMock, any(), any());
 }

 @Test
 public void testVisitReturnStatement() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node returnStatementMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, null, null);
 if (returnStatementMock.getFirstChild() != null) {
 verify(nodeTraversalMock, times(1)).visit(returnStatementMock.getFirstChild(), any(), any());
 }
 }

 @Test
 public void testVisitForStatementWithoutInitialization() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node forStatementMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, null, null);
 verify(nodeTraversalMock, times(1)).visit(forStatementMock, any(), any());
 }

 @Test
 public void testVisitForStatementWithInitialization() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node forStatementMock = mock(Node.class);
 Node initializationMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, null, null);
 verify(nodeTraversalMock, times(1)).visit(initializationMock, any(), any());
 }

 @Test
 public void testVisitForInStatement() {
 NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
 Node forInStatementMock = mock(Node.class);
 deadAssignmentsElimination.visit(nodeTraversalMock, null, null);
 verify(nodeTraversalMock, never()).visit(forInStatementMock, any(), any());
 }

 @Test
 public void testIsVariableStillLiveWithinExpression() {
 Node n1Mock = mock(Node.class);
 Node n2Mock = mock(Node.class);
 Node exprRootMock = mock(Node.class);
 when(n1Mock.getType()).thenReturn(Token.ASSIGN);
 when(n1Mock.getParent()).thenReturn(null);
 when(n2Mock.getType()).thenReturn(Token.ASSIGN);
 when(n2Mock.getParent()).thenReturn(n1Mock);
 when(n2Mock.getString()).thenReturn("x");
 when(exprRootMock.getType()).thenReturn(Token.ASSIGN);
 when(exprRootMock.getParent()).thenReturn(null);

 boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(n1Mock, exprRootMock, "x");
 verify(nodeTraversalMock, never()).getScope();
 assertTrue(result);

 when(n1Mock.getType()).thenReturn(Token.ASSIGN);
 when(n1Mock.getParent()).thenReturn(null);
 when(n2Mock.getType()).thenReturn(Token.ASSIGN);
 when(n2Mock.getParent()).thenReturn(n1Mock);
 when(n2Mock.getString()).thenReturn("x");
 when(exprRootMock.getType()).thenReturn(Token.ASSIGN);
 when(exprRootMock.getParent()).thenReturn(n2Mock);

 result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(n1Mock, exprRootMock, "x");
 verify(nodeTraversalMock, never()).getScope();
 assertFalse(result);
 }
}