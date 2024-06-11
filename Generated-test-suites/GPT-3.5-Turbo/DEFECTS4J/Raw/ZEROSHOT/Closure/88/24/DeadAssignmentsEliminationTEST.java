import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testTryRemoveAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    
    // Create a simple assignment node
    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, new Node(Token.STRING, "x")), new Node(Token.NUMBER, 5));
    
    // Create the expected result node after removing dead assignment
    Node expected = new Node(Token.NUMBER, 5);
    
    // Test tryRemoveAssignment method
    elimination.tryRemoveAssignment(null, node, null);
    assertEquals(expected.toStringTree(), node.toStringTree());
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    
    // Create a simple control flow graph node
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    DiGraphNode<Node, ControlFlowGraph.Branch> node = cfg.createNode(new Node());
    
    // Create the liveness information for the node
    FlowState<LiveVariablesAnalysis.LiveVariableLattice> state = new FlowState<>(null, null, null, null);
    
    // Call tryRemoveDeadAssignments method
    elimination.tryRemoveDeadAssignments(null, cfg);
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    
    // Create a simple expression root node
    Node exprRoot = new Node();
    
    // Create a variable node
    Node variable = new Node(Token.NAME, new Node(Token.STRING, "x"));
    
    // Create a node with read of the variable
    Node readNode = new Node(Token.EXPR_RESULT, new Node(Token.GETPROP, variable, new Node(Token.STRING, "length")));
    
    // Create a node with assignment of the variable
    Node assignNode = new Node(Token.EXPR_RESULT, new Node(Token.ASSIGN, variable, new Node(Token.NUMBER, 5)));
    
    // Create a node with write of the variable
    Node writeNode = new Node(Token.EXPR_RESULT, new Node(Token.SETPROP, variable, new Node(Token.STRING, "length"), new Node(Token.NUMBER, 10)));
    
    // Test isVariableStillLiveWithinExpression method
    boolean result1 = elimination.isVariableStillLiveWithinExpression(readNode, exprRoot, "x");
    boolean result2 = elimination.isVariableStillLiveWithinExpression(assignNode, exprRoot, "x");
    boolean result3 = elimination.isVariableStillLiveWithinExpression(writeNode, exprRoot, "x");
    
    assertFalse(result1);
    assertTrue(result2);
    assertFalse(result3);
  }
  
  @Test
  public void testIsVariableReadBeforeKill() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    
    // Create a variable node
    Node variable = new Node(Token.NAME, new Node(Token.STRING, "x"));
    
    // Create a node with read of the variable
    Node readNode = new Node(Token.EXPR_RESULT, new Node(Token.GETPROP, variable, new Node(Token.STRING, "length")));
    
    // Create a node with assignment of the variable
    Node assignNode = new Node(Token.EXPR_RESULT, new Node(Token.ASSIGN, variable, new Node(Token.NUMBER, 5)));
    
    // Create a node with write of the variable
    Node writeNode = new Node(Token.EXPR_RESULT, new Node(Token.SETPROP, variable, new Node(Token.STRING, "length"), new Node(Token.NUMBER, 10)));
    
    // Test isVariableReadBeforeKill method
    DeadAssignmentsElimination.VariableLiveness result1 = elimination.isVariableReadBeforeKill(readNode, "x");
    DeadAssignmentsElimination.VariableLiveness result2 = elimination.isVariableReadBeforeKill(assignNode, "x");
    DeadAssignmentsElimination.VariableLiveness result3 = elimination.isVariableReadBeforeKill(writeNode, "x");
    
    assertEquals(DeadAssignmentsElimination.VariableLiveness.READ, result1);
    assertEquals(DeadAssignmentsElimination.VariableLiveness.KILL, result2);
    assertEquals(DeadAssignmentsElimination.VariableLiveness.MAYBE_LIVE, result3);
  }
}