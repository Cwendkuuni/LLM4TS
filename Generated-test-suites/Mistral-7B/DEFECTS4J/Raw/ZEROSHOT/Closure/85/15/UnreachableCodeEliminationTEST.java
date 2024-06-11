package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

/**
 * Test class for UnreachableCodeElimination class.
 */
public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowAnalysis controlFlowAnalysis;
  private GraphReachability<Node, ControlFlowGraph.Branch> graphReachability;
  private UnreachableCodeElimination elimination;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    controlFlowAnalysis = mock(ControlFlowAnalysis.class);
    graphReachability = mock(GraphReachability.class);
    elimination = new UnreachableCodeElimination(compiler, false);
    MockitoAnnotations.initMocks(this);

    when(compiler.createControlFlowAnalysis()).thenReturn(controlFlowAnalysis);
    when(controlFlowAnalysis.getCfg()).thenReturn(createMockControlFlowGraph());
    when(controlFlowAnalysis.getGraphReachability()).thenReturn(graphReachability);
  }

  @Test
  public void testUnreachableCodeElmination() {
    Node rootNode = createTestRootNode();
    elimination.process(null, rootNode);

    assertEquals(1, elimination.cfgStack.size());
    ControlFlowGraph<Node> cfg = elimination.cfgStack.peek();
    List<Node> nodes = new ArrayList<>();
    cfg.getNodes().values().forEach(nodes::add);

    Node returnStatement = nodes.get(nodes.size() - 2);
    Node alertStatement = nodes.get(nodes.size() - 3);
    Node ifStatement = nodes.get(nodes.size() - 4);
    Node root = nodes.get(0);

    assertEquals(3, nodes.size());
    assertEquals(Token.IF, ifStatement.getType());
    assertEquals(Token.RETURN, returnStatement.getType());
    assertEquals(Token.ALERT, alertStatement.getType());
    assertEquals(Token.VOID, root.getType());

    assertEquals(0, ifStatement.getChildCount());
    assertEquals(1, returnStatement.getChildCount());
    assertEquals(0, alertStatement.getChildCount());

    assertEquals(null, ifStatement.getNext());
    assertEquals(returnStatement, ifStatement.getParent());
    assertEquals(returnStatement, alertStatement.getParent());
    assertEquals(alertStatement, returnStatement.getNext());

    assertEquals(Branch.UNCOND, ifStatement.getAnnotation());
    assertEquals(Branch.UNCOND, returnStatement.getAnnotation());
    assertEquals(Branch.UNCOND, alertStatement.getAnnotation());

    assertEquals(GraphReachability.REACHABLE, graphReachability.getAnnotation(ifStatement));
    assertEquals(GraphReachability.REACHABLE, graphReachability.getAnnotation(returnStatement));
    assertEquals(GraphReachability.UNREACHABLE, graphReachability.getAnnotation(alertStatement));
  }

  private ControlFlowGraph<Node> createMockControlFlowGraph() {
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    when(cfg.getCfgNodes()).thenReturn(createMockControlFlowGraphNodes());
    when(cfg.getDirectedGraphNode(ArgumentMatchers.any(Node.class))).then(n -> createMockNode(n));
    when(cfg.getEntry()).thenReturn(createMockControlFlowGraphEntry());
    when(cfg.getNodes()).thenReturn(createMockControlFlowGraphNodes());
    return cfg;
  }

  private List<DiGraphNode<Node, Branch>> createMockControlFlowGraphNodes() {
    List<DiGraphNode<Node, Branch>> nodes = new ArrayList<>();
    nodes.add(createMockControlFlowGraphNode(Token.IF, null));
    nodes.add(createMockControlFlowGraphNode(Token.RETURN, null));
    nodes.add(createMockControlFlowGraphNode(Token.ALERT, null));
    nodes.add(createMockControlFlowGraphNode(Token.VOID, null));
    return nodes;
  }

  private DiGraphNode<Node, Branch> createMockControlFlowGraphNode(Token type, Node value) {
    DiGraphNode<Node, Branch> node = mock(DiGraphNode.class);
    when(node.getType()).thenReturn(type);
    when(node.getValue()).thenReturn(value);
    return node;
  }

  private DiGraphEdge<Node, Branch> createMockControlFlowGraphEdge(Node source, Node target) {
    DiGraphEdge<Node, Branch> edge = mock(DiGraphEdge.class);
    when(edge.getDestination()).thenReturn(createMockControlFlowGraphNode(Token.VOID, null));
    when(edge.getDestination().getValue()).thenReturn(null);
    when(edge.getSource()).thenReturn(source);
    when(edge.getValue()).thenReturn(Branch.UNCOND);
    return edge;
  }

  private Node createMockControlFlowGraphEntry() {
    Node node = mock(Node.class);
    when(node.getFirstChild()).thenReturn(null);
    when(node.getParent()).thenReturn(null);
    when(node.getType()).thenReturn(Token.VOID);
    when(node.getValue()).thenReturn(null);
    return node;
  }

  private Node createTestRootNode() {
    Node rootNode = new Node(Token.SCRIPT);
    rootNode.setSourceName("test.js");

    Node ifStatement = new Node(Token.IF);
    Node returnStatement = new Node(Token.RETURN);
    Node alertStatement = new Node(Token.ALERT);

    ifStatement.setSourcePosition(1, 1);
    returnStatement.setSourcePosition(2, 1);
    alertStatement.setSourcePosition(3, 1);

    ifStatement.setParent(rootNode);
    returnStatement.setParent(ifStatement);
    alertStatement.setParent(returnStatement);

    rootNode.setFirstChild(ifStatement);

    return rootNode;
  }
}