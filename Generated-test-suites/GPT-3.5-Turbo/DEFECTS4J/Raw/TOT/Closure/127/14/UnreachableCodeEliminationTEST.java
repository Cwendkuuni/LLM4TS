import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;
import com.google.javascript.jscomp.graph.*;
import com.google.javascript.jscomp.ControlFlowGraph.*;

public class UnreachableCodeEliminationTest {

  // Test case for process() method
  @Test
  public void testProcess() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);

    // Create sample externs and toplevel Nodes
    Node externs = new Node(Token.EXTERNSTR, "externs");
    Node toplevel = new Node(Token.BLOCK, "toplevel");

    // Call process() method and check if some changes have occurred
    uce.process(externs, toplevel);
    assertTrue(uce.codeChanged);
  }

  // Test case for visit() method
  @Test
  public void testVisit() {
    Compiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node n = new Node(Token.SCRIPT, "script");
    NodeTraversal t = new NodeTraversal(compiler, new MyCallback());
    UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination.EliminationPass(cfg);

    // Create a sample parent node and check expected behavior of visit()
    Node parent = new Node(Token.BLOCK, "block");
    t.visit(n, parent);
    assertFalse(ep.codeChanged);
  }

  // Custom callback class for testing purposes
  private class MyCallback extends AbstractShallowCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // Do nothing
    }
  }

  // Test case for tryRemoveUnconditionalBranching() method
  @Test
  public void testTryRemoveUnconditionalBranching() {
    Compiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node n = new Node(Token.BREAK, "break");
    UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination.EliminationPass(cfg);

    // Set up the control flow graph with a valid break statement
    DiGraphEdge<Node, Branch> edge = new DiGraphEdge<>();
    DiGraphNode<Node, Branch> sourceNode = new DiGraphNode<>(null);
    DiGraphNode<Node, Branch> destinationNode = new DiGraphNode<>(null);
    sourceNode.addOutEdge(edge, destinationNode);
    cfg.addNodes(sourceNode, destinationNode);
    edge.setDestination(destinationNode);
    edge.setValue(Branch.UNCOND);
    n.setNext(destinationNode.getValue());

    // Call tryRemoveUnconditionalBranching() and check if the node is removed
    ep.tryRemoveUnconditionalBranching(n);
    assertNull(cfg.getDirectedGraphNode(n));
  }

  // Test case for computeFollowing() method
  @Test
  public void testComputeFollowing() {
    Compiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node n = new Node(Token.RETURN, "return");
    UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination.EliminationPass(cfg);

    // Set up the control flow graph with a return statement
    DiGraphNode<Node, Branch> node = new DiGraphNode<>(n);
    cfg.addNodes(node);
    
    // Call computeFollowing() and check if correct node is returned
    Node result = ep.computeFollowing(n);
    assertEquals(result, n);
  }

  // Test case for removeDeadExprStatementSafely() method
  @Test
  public void testRemoveDeadExprStatementSafely() {
    Compiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node n = new Node(Token.EXPR_RESULT, "expr");
    UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination.EliminationPass(cfg);

    // Set up the control flow graph with an expression statement
    DiGraphNode<Node, Branch> node = new DiGraphNode<>(n);
    cfg.addNodes(node);

    // Call removeDeadExprStatementSafely() and check if the node is removed
    ep.removeDeadExprStatementSafely(n);
    assertNull(cfg.getDirectedGraphNode(n));
  }

  // Test case for removeNode() method
  @Test
  public void testRemoveNode() {
    Compiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node n = new Node(Token.VAR, "var");
    UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination.EliminationPass(cfg);

    // Set up the control flow graph with a variable declaration
    DiGraphNode<Node, Branch> node = new DiGraphNode<>(n);
    cfg.addNodes(node);

    // Call removeNode() and check if the node is removed
    ep.removeNode(n);
    assertNull(cfg.getDirectedGraphNode(n));
  }
}