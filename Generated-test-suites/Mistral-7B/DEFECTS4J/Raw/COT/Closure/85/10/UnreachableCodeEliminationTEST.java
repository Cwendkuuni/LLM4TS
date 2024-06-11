import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeUtil;

import java.util.Deque;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private UnreachableCodeElimination elimination;
  private NodeTraversal traversal;
  private DiGraphNode<Node, Branch> node1;
  private DiGraphNode<Node, Branch> node2;
  private DiGraphNode<Node, Branch> node3;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = mock(NodeTraversal.class);

    node1 = createNodeMock("node1");
    node2 = createNodeMock("node2");
    node3 = createNodeMock("node3");

    when(traversal.getScope()).thenReturn(createScopeMock());
    when(traversal.getRootNode()).thenReturn(createRootNodeMock());
  }

  @Test
  public void testEnterScope() {
    elimination.enterScope(traversal);
    assertSame(curCfg, elimination.curCfg);
  }

  @Test
  public void testExitScope() {
    elimination.exitScope(traversal);
    assertNull(elimination.curCfg);
  }

  @Test
  public void testProcess() {
    elimination.process(null, null);
    NodeTraversal.traverse(compiler, null, elimination);
  }

  @Test
  public void testVisitNull() {
    elimination.visit(traversal, null, null);
  }

  @Test
  public void testVisitFunctionOrScript() {
    elimination.visit(traversal, createFunctionNodeMock(), null);
    elimination.visit(traversal, createScriptNodeMock(), null);
  }

  @Test
  public void testVisitWithParent() {
    elimination.visit(traversal, node1, node2);
  }

  @Test
  public void testVisitWithRemoveNoOpStatementsTrue() {
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.visit(traversal, node1, node2);
  }

  @Test
  public void testVisitWithNodeWithNoSideEffects() {
    when(NodeUtil.mayHaveSideEffects(node1)).thenReturn(false);
    elimination.visit(traversal, node1, node2);
  }

  @Test
  public void testVisitWithNodeNotInCFG() {
    when(curCfg.getDirectedGraphNode(node1)).thenReturn(null);
    elimination.visit(traversal, node1, node2);
  }

  @Test
  public void testVisitWithNodeNotReachable() {
    DiGraphNode<Node, Branch> notReachableNode = createNodeMock("notReachableNode");
    when(curCfg.getDirectedGraphNode(node1)).thenReturn(notReachableNode);
    when(notReachableNode.getAnnotation()).thenReturn(GraphReachability.UNREACHABLE);
    elimination.visit(traversal, node1, node2);
  }

  @Test
  public void testVisitWithNodeWithConditionalBranch() {
    when(curCfg.getDirectedGraphNode(node1)).thenReturn(node1);
    when(node1.getOutEdges()).thenReturn(createEdgeListMock());
    when(node1.getOutEdges().get(0).getDestination()).thenReturn(node2);
    elimination.visit(traversal, node1, node2);
  }

  @Test
  public void testVisitWithNodeWithUnconditionalBranch() {
    when(curCfg.getDirectedGraphNode(node1)).thenReturn(node1);
    when(node1.getOutEdges()).thenReturn(createEdgeListMock());
    when(node1.getOutEdges().get(0).getDestination()).thenReturn(node3);
    when(node1.getType()).thenReturn(Token.BREAK);
    elimination.visit(traversal, node1, node2);

    Node followingNode = createNodeMock("followingNode");
    when(computeFollowing(node1)).thenReturn(followingNode);
    elimination.tryRemoveUnconditionalBranching(node1);

    assertSame(node3, elimination.tryRemoveUnconditionalBranching(followingNode));
  }

  @Test
  public void testVisitWithNodeWithReturnStatement() {
    when(curCfg.getDirectedGraphNode(node1)).thenReturn(node1);
    when(node1.getOutEdges()).thenReturn(createEdgeListMock());
    when(node1.getOutEdges().get(0).getDestination()).thenReturn(node2);
    when(node1.getType()).thenReturn(Token.RETURN);
    elimination.visit(traversal, node1, node2);
    assertNull(node1.getNext());
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithFunctionNode() {
    when(curCfg.getDirectedGraphNode(node1)).thenReturn(node1);
    elimination.tryRemoveUnconditionalBranching(node1);
  }

  private Node createNodeMock(String name) {
    Node node = mock(Node.class);
    when(node.getName()).thenReturn(name);
    return node;
  }

  private Node createRootNodeMock() {
    Node root = mock(Node.class);
    when(root.getType()).thenReturn(Token.SCRIPT);
    return root;
  }

  private Scope createScopeMock() {
    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(createRootNodeMock());
    return scope;
  }

  private List<DiGraphEdge<Node, Branch>> createEdgeListMock() {
    Deque<DiGraphEdge<Node, Branch>> edges = new LinkedList<>();
    return (List<DiGraphEdge<Node, Branch>>) edges;
  }

  private Node createFunctionNodeMock() {
    Node function = mock(Node.class);
    when(function.getType()).thenReturn(Token.FUNCTION);
    return function;
  }

  private Node createScriptNodeMock() {
    Node script = mock(Node.class);
    when(script.getType()).thenReturn(Token.SCRIPT);
    return script;
  }

  private Node computeFollowing(Node n) {
    return node2;
  }
}