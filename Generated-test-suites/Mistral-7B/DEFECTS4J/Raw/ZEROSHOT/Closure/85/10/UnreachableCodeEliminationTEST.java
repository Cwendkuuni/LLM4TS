import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class UnreachableCodeEliminationTest {

 @Mock
 private AbstractCompiler compiler;
 @Mock
 private ControlFlowAnalysis controlFlowAnalysis;
 @Mock
 private GraphReachability<Node, ControlFlowGraph.Branch> graphReachability;
 @Mock
 private Node node;
 @Mock
 private Node rootNode;
 @Mock
 private Scope scope;
 @Mock
 private NodeUtil nodeUtil;

 private NodeTraversal nodeTraversal;
 private DiGraphNode<Node, Branch> gNode;
 private UnreachableCodeElimination unreachableCodeElimination;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 nodeTraversal = new NodeTraversal(compiler, null, null, null, null, null, null, nodeUtil);
 unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
 }

 @Test
 public void testEnterScope() {
 unreachableCodeElimination.enterScope(nodeTraversal);
 assertSame(controlFlowAnalysis, unreachableCodeElimination.curCfg);
 }

 @Test
 public void testExitScope() {
 unreachableCodeElimination.exitScope(nodeTraversal);
 assertNull(unreachableCodeElimination.curCfg);
 }

 @Test
 public void testProcess() {
 unreachableCodeElimination.process(rootNode, rootNode);
 }

 @Test
 public void testVisitNull() {
 unreachableCodeElimination.visit(nodeTraversal, null, null);
 }

 @Test
 public void testVisitFunctionOrScript() {
 unreachableCodeElimination.visit(nodeTraversal, node, node);
 }

 @Test
 public void testVisitNonReachableStatement() {
 gNode = createGraphNode();
 unreachableCodeElimination.visit(nodeTraversal, node, null);
 assertFalse(unreachableCodeElimination.curCfg.contains(node));
 }

 @Test
 public void testVisitNonReachableStatementWithSideEffects() {
 gNode = createGraphNodeWithSideEffects();
 unreachableCodeElimination.visit(nodeTraversal, node, null);
 assertSame(node, unreachableCodeElimination.curCfg.getDirectedGraphNode(node).getValue());
 }

 @Test
 public void testVisitReachableStatementWithNoSideEffects() {
 gNode = createGraphNode();
 unreachableCodeElimination.visit(nodeTraversal, node, null);
 assertSame(node, unreachableCodeElimination.curCfg.getDirectedGraphNode(node).getValue());
 }

 @Test
 public void testVisitReachableStatementWithSideEffects() {
 gNode = createGraphNodeWithSideEffects();
 unreachableCodeElimination.visit(nodeTraversal, node, null);
 assertSame(node, unreachableCodeElimination.curCfg.getDirectedGraphNode(node).getValue());
 }

 @Test
 public void testTryRemoveUnconditionalBranchingNull() {
 assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(null));
 }

 @Test
 public void testTryRemoveUnconditionalBranchingFunction() {
 assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(createFunctionNode()));
 }

 @Test
 public void testTryRemoveUnconditionalBranchingBlock() {
 Node blockNode = createBlockNode();
 assertSame(blockNode, unreachableCodeElimination.tryRemoveUnconditionalBranching(blockNode));
 }

 @Test
 public void testTryRemoveUnconditionalBranchingReturn() {
 Node returnNode = createReturnNode();
 assertSame(returnNode, unreachableCodeElimination.tryRemoveUnconditionalBranching(returnNode));
 }

 @Test
 public void testTryRemoveUnconditionalBranchingBreak() {
 Node breakNode = createBreakNode();
 assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(breakNode));
 }

 @Test
 public void testTryRemoveUnconditionalBranchingContinue() {
 Node continueNode = createContinueNode();
 assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(continueNode));
 }

 @Test
 public void testRemoveDeadExprStatementSafelyNull() {
 unreachableCodeElimination.removeDeadExprStatementSafely(null);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyEmptyOrBlockWithoutChildren() {
 Node emptyNode = createEmptyNode();
 Node blockNode = createBlockNode();
 unreachableCodeElimination.removeDeadExprStatementSafely(emptyNode);
 unreachableCodeElimination.removeDeadExprStatementSafely(blockNode);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyTryCatchNode() {
 Node tryNode = createTryNode();
 Node catchNode = createCatchNode();
 NodeUtil.maybeAddFinally(tryNode);
 unreachableCodeElimination.removeDeadExprStatementSafely(catchNode);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyVariableDeclaration() {
 Node variableDeclarationNode = createVariableDeclarationNode();
 unreachableCodeElimination.removeDeadExprStatementSafely(variableDeclarationNode);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyFunctionWithChildren() {
 Node functionNode = createFunctionNode();
 unreachableCodeElimination.removeDeadExprStatementSafely(functionNode);
 }

 private DiGraphNode<Node, Branch> createGraphNode() {
 return new DiGraphNode<Node, Branch>(node, null);
 }

 private DiGraphNode<Node, Branch> createGraphNodeWithSideEffects() {
 return new DiGraphNode<Node, Branch>(node, null, true);
 }

 private Node createBlockNode() {
 return new Node(Token.BLOCK, null);
 }

 private Node createBreakNode() {
 return new Node(Token.BREAK, null);
 }

 private Node createContinueNode() {
 return new Node(Token.CONTINUE, null);
 }

 private Node createEmptyNode() {
 return new Node(Token.EMPTY, null);
 }

 private Node createFunctionNode() {
 return new Node(Token.FUNCTION, null);
 }

 private Node createReturnNode() {
 return new Node(Token.RETURN, null);
 }

 private Node createTryNode() {
 return new Node(Token.TRY, null);
 }

 private Node createCatchNode() {
 return new Node(Token.CATCH, null);
 }

 private Node createVariableDeclarationNode() {
 return new Node(Token.VAR, null);
 }
}