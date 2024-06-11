import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {
  
  @Test
  public void testTryRemoveUnconditionalBranching() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination();
    
    // Test case 1: n is null
    Node result1 = elimination.tryRemoveUnconditionalBranching(null);
    assertNull(result1);
    
    // Test case 2: n is a block with children
    Node n2 = createBlock();
    Node child2 = createEmptyStatement();
    n2.addChildToBack(child2);
    Node result2 = elimination.tryRemoveUnconditionalBranching(n2);
    assertEquals(result2, child2);
    
    // Test case 3: n is a block without children
    Node n3 = createBlock();
    Node result3 = elimination.tryRemoveUnconditionalBranching(n3);
    assertEquals(result3, ControlFlowAnalysis.computeFollowNode(n3));
    
    // Test case 4: n is a return statement with children
    Node n4 = createReturnStatement();
    Node child4 = createEmptyStatement();
    n4.addChildToBack(child4);
    Node result4 = elimination.tryRemoveUnconditionalBranching(n4);
    assertEquals(result4, n4);
    
    // Test case 5: n is a return statement without children
    Node n5 = createReturnStatement();
    Node result5 = elimination.tryRemoveUnconditionalBranching(n5);
    assertNull(result5);
    
    // Test case 6: n is a break statement
    Node n6 = createBreakStatement();
    Node result6 = elimination.tryRemoveUnconditionalBranching(n6);
    assertEquals(result6, null);
    
    // Test case 7: n is a continue statement
    Node n7 = createContinueStatement();
    Node result7 = elimination.tryRemoveUnconditionalBranching(n7);
    assertEquals(result7, null);
  }
  
  @Test
  public void testComputeFollowing() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination();
    
    // Test case 1: n is null
    Node result1 = elimination.computeFollowing(null);
    assertNull(result1);
    
    // Test case 2: n is a block with children
    Node n2 = createBlock();
    Node child2 = createEmptyStatement();
    n2.addChildToBack(child2);
    Node result2 = elimination.computeFollowing(n2);
    assertEquals(result2, child2);
    
    // Test case 3: n is a block without children
    Node n3 = createBlock();
    Node result3 = elimination.computeFollowing(n3);
    assertEquals(result3, ControlFlowAnalysis.computeFollowNode(n3));
  }
  
  @Test
  public void testRemoveDeadExprStatementSafely() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination();
    
    // Test case 1: n is an empty statement
    Node n1 = createEmptyStatement();
    elimination.removeDeadExprStatementSafely(n1);
    assertNull(n1.getParent());
    
    // Test case 2: n is a block with children
    Node n2 = createBlock();
    Node child2 = createEmptyStatement();
    n2.addChildToBack(child2);
    elimination.removeDeadExprStatementSafely(child2);
    assertFalse(n2.hasChildren());
    
    // Test case 3: n is a block without children
    Node n3 = createBlock();
    elimination.removeDeadExprStatementSafely(n3);
    assertNull(n3.getParent());
  }
  
  // Helper methods to create test Nodes
  
  private Node createBlock() {
    Node block = new Node(Token.BLOCK);
    return block;
  }
  
  private Node createEmptyStatement() {
    Node statement = new Node(Token.EMPTY);
    return statement;
  }
  
  private Node createReturnStatement() {
    Node statement = new Node(Token.RETURN);
    return statement;
  }
  
  private Node createBreakStatement() {
    Node statement = new Node(Token.BREAK);
    return statement;
  }
  
  private Node createContinueStatement() {
    Node statement = new Node(Token.CONTINUE);
    return statement;
  }
}