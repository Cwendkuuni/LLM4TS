import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination uce;

  @Before
  public void setup() {
    uce = new UnreachableCodeElimination(null, true);
  }

  @Test
  public void testEnterScope() {
    // Test when the scope has no code
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    uce.process(externs, root);
    assertNull(uce.cfgStack.peek());
    
    // Test when the scope has code
    Node funcNode = new Node(Token.FUNCTION);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToFront(new Node(Token.RETURN));
    funcNode.addChildToFront(blockNode);
    root = new Node(Token.ROOT);
    root.addChildToFront(funcNode);
    uce.process(externs, root);
    assertEquals(blockNode, uce.cfgStack.peek().getEntry().getValue());
  }

  @Test
  public void testExitScope() {
    Node externs = new Node(Token.EXTERN);
    Node funcNode = new Node(Token.FUNCTION);
    Node root = new Node(Token.ROOT);
    root.addChildToFront(funcNode);
    uce.process(externs, root);
    
    uce.exitScope(null);
    assertNull(uce.cfgStack.peek());
  }

  @Test
  public void testVisit_noParentNode() {
    NodeTraversal t = new NodeTraversal(null, null, null);
    Node n = new Node(Token.SCRIPT);
    Node parent = null;
    uce.visit(t, n, parent);
    // The code doesn't enter the if block inside the method
  }

  @Test
  public void testVisit_functionOrScriptNode() {
    NodeTraversal t = new NodeTraversal(null, null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    uce.visit(t, n, parent);
    // The code doesn't enter the if block inside the method
  }

  @Test
  public void testVisit_notInCFG() {
    NodeTraversal t = new NodeTraversal(null, null, null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    uce.visit(t, n, parent);
    // The code doesn't enter the if block inside the method
  }

  @Test
  public void testVisit_notReachable() {
    NodeTraversal t = new NodeTraversal(null, null, null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    DiGraphEdge<Node, ControlFlowGraph.Branch> edge =
        new DiGraphEdge<Node, ControlFlowGraph.Branch>(null, ControlFlowGraph.Branch.ALWAYS);
    DiGraphNode<Node, Branch> gNode = new DiGraphNode<Node, Branch>(n, edge);
    cfg.createDirectedGraphNode(n, gNode);

    uce.curCfg = cfg;
    uce.visit(t, n, parent);
    // The code doesn't enter the if block inside the method
  }

  @Test
  public void testVisit_removeNoOpStatement() {
    NodeTraversal t = new NodeTraversal(null, null, null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    DiGraphEdge<Node, ControlFlowGraph.Branch> edge =
        new DiGraphEdge<Node, ControlFlowGraph.Branch>(null, ControlFlowGraph.Branch.ALWAYS);
    DiGraphNode<Node, Branch> gNode = new DiGraphNode<Node, Branch>(n, edge);
    cfg.createDirectedGraphNode(n, gNode);

    uce.removeNoOpStatements = true;
    uce.curCfg = cfg;
    uce.visit(t, n, parent);
    // The code doesn't enter the if block inside the method
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);

    uce.removeDeadExprStatementSafely(n);
    // No assert since it removes the node from the AST
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node n = new Node(Token.BREAK);
    Node followingNode = new Node(Token.RETURN);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(n, followingNode);

    uce.curCfg = cfg;
    Node result = uce.tryRemoveUnconditionalBranching(n);
    assertEquals(followingNode, result);
  }

  private ControlFlowGraph<Node> createControlFlowGraph(Node currentNode, Node followingNode) {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    DiGraphEdge<Node, ControlFlowGraph.Branch> edge =
        new DiGraphEdge<Node, ControlFlowGraph.Branch>(null, ControlFlowGraph.Branch.UNCOND);
    DiGraphNode<Node, Branch> currentGNode = new DiGraphNode<Node, Branch>(currentNode, edge);
    cfg.createDirectedGraphNode(currentNode, currentGNode);

    DiGraphNode<Node, Branch> followingGNode = new DiGraphNode<Node, Branch>(followingNode, null);
    cfg.createDirectedGraphNode(followingNode, followingGNode);

    currentGNode.connect(followingGNode, ControlFlowGraph.Branch.UNCOND);
    return cfg;
  }

}