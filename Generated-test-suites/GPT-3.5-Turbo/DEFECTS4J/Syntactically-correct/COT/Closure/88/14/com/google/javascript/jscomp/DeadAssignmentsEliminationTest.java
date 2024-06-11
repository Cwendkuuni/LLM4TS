
import static org.junit.Assert.*;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  
  @Test
  public void testTryRemoveDeadAssignments() {
    // Test case 1: no assignments to remove
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(new AbstractCompiler());
    NodeTraversal traversal = new NodeTraversal(eliminator, new NodeUtilTest.TestScopeCreator());
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.EMPTY));
    eliminator.tryRemoveDeadAssignments(traversal, cfg);
    // Assert here
    
    // Test case 2: assignments to remove
    // ...
  }
  
  @Test
  public void testEnterScope() {
    // Test case 1: scope is global
    // ...
    
    // Test case 2: scope contains inner functions
    // ...
    
    // Test case 3: scope has no removable assignments
    // ...
    
    // Test case 4: scope has removable assignments
    // ...
  }
  
  @Test
  public void testExitScope() {
    // Test case 1: do nothing
    // ...
  }
  
  @Test
  public void testVisit() {
    // Test case 1: do nothing
    // ...
  }
  
  @Test
  public void testTryRemoveAssignment() {
    // Test case 1: variable is not dead/escaped
    // ...
    
    // Test case 2: variable is escaped due to closures
    // ...
    
    // Test case 3: variable is identity assignment
    // ...
    
    // Test case 4: variable is dead and assignment is removed
    // ...
    
    // Test case 5: variable is killed but also live before it
    // ...
  }
  
  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // Test case 1: no read or write of variable
    // ...
    
    // Test case 2: read right of n
    // ...
    
    // Test case 3: read right of n but after a write
    // ...
    
    // Test case 4: variable is not found before reaching exprRoot
    // ...
  }
  
  @Test
  public void testIsVariableReadBeforeKill() {
    // Test case 1: first reference is a read
    // ...
    
    // Test case 2: first reference is an assignment
    // ...
    
    // Test case 3: variable is not found in expression
    // ...
  }
}
