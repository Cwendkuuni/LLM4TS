import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testEnterScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal t = new NodeTraversal(compiler, null);
    
    // Test global scope
    elimination.enterScope(t);
    assertNull(elimination.liveness);
    assertFalse(elimination.compiler.hasCodeChanged());
    
    // Test scope with inner function
    Node fnBlock = new Node(Token.BLOCK);
    Node fnNode = new Node(Token.FUNCTION);
    fnNode.addChildToFront(fnBlock);
    
    t.getScopeRoot().addChildToBack(fnNode);
    elimination.enterScope(t);
    assertNull(elimination.liveness);
    assertFalse(elimination.compiler.hasCodeChanged());
    
    // Test scope with no removable assignments
    Node noAssignments = new Node(Token.BLOCK);
    Node assignment = new Node(Token.EXPR_RESULT);
    assignment.addChildToFront(new Node(Token.NAME, "a"));
    assignment.addChildToFront(new Node(Token.NUMBER, "1"));
    noAssignments.addChildToBack(assignment);
    
    t.getScopeRoot().addChildToBack(noAssignments);
    elimination.enterScope(t);
    assertNull(elimination.liveness);
    assertFalse(elimination.compiler.hasCodeChanged());
    
    // Test scope with removable assignments
    Node removableAssignments = new Node(Token.BLOCK);
    Node assignment1 = new Node(Token.EXPR_RESULT);
    assignment1.addChildToFront(new Node(Token.NAME, "a"));
    assignment1.addChildToFront(new Node(Token.NUMBER, "1"));
    Node assignment2 = new Node(Token.EXPR_RESULT);
    assignment2.addChildToFront(new Node(Token.NAME, "b"));
    assignment2.addChildToFront(new Node(Token.NUMBER, "2"));
    removableAssignments.addChildToBack(assignment1);
    removableAssignments.addChildToBack(assignment2);
    
    t.getScopeRoot().addChildToBack(removableAssignments);
    elimination.enterScope(t);
    assertNotNull(elimination.liveness);
    assertTrue(elimination.compiler.hasCodeChanged());
  }
  
  @Test
  public void testExitScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal t = new NodeTraversal(compiler, null);
    
    // Test exitScope does nothing
    elimination.exitScope(t);
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal t = new NodeTraversal(compiler, null);
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    
    // Test visit does nothing
    elimination.visit(t, n, parent);
  }
  
  @Test
  public void testTryRemoveDeadAssignments() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal t = new NodeTraversal(compiler, null);
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    elimination.liveness = new LiveVariablesAnalysis.LiveVariableLattice();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    cfg.createDirectedGraph();
    
    // Test tryRemoveDeadAssignments does nothing
    elimination.tryRemoveDeadAssignments(t, cfg);
  }
  
  @Test
  public void testTryRemoveAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal t = new NodeTraversal(compiler, null);
    Node n = new Node(Token.NAME, "a");
    Node exprRoot = new Node(Token.BLOCK);
    LiveVariablesAnalysis.LiveVariableLattice state = new LiveVariablesAnalysis.LiveVariableLattice();
    
    // Test tryRemoveAssignment does nothing
    elimination.tryRemoveAssignment(t, n, exprRoot, state);
  }
  
  @Test
  public void testIsVariableStillLiveWithinExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node n = new Node(Token.NAME, "a");
    Node exprRoot = new Node(Token.BLOCK);
    String variable = "a";
    
    // Test isVariableStillLiveWithinExpression returns false
    assertFalse(elimination.isVariableStillLiveWithinExpression(n, exprRoot, variable));
  }
  
  @Test
  public void testIsVariableReadBeforeKill() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node n = new Node(Token.NAME, "a");
    String variable = "a";
    
    // Test isVariableReadBeforeKill returns MAYBE_LIVE
    assertEquals(DeadAssignmentsElimination.VariableLiveness.MAYBE_LIVE, elimination.isVariableReadBeforeKill(n, variable));
  }
}