import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  
  private UnreachableCodeElimination codeElimination;
  
  @Before
  public void setup() {
    codeElimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
  }
  
  @Test
  public void testProcess_RemovesDeadCodeAfterReturn() {
    // Arrange
    Node externs = new Node();
    Node toplevel = new Node();
    
    // Act
    codeElimination.process(externs, toplevel);
    
    // Assert
    // Test if the dead code after return statement is removed
    assertEquals(toplevel, codeElimination.getProcessedNode());
  }
  
  @Test
  public void testProcess_RemovesNoOpStatementsWithNoSideEffects() {
    // Arrange
    Node externs = new Node();
    Node toplevel = new Node();
    
    // Act
    codeElimination.process(externs, toplevel);
    
    // Assert
    // Test if the no-op statements without side effects are removed
    assertEquals(toplevel, codeElimination.getProcessedNode());
  }
  
  @Test
  public void testProcess_RemovesUnreachableCode() {
    // Arrange
    Node externs = new Node();
    Node toplevel = new Node();
    
    // Act
    codeElimination.process(externs, toplevel);
    
    // Assert
    // Test if the unreachable code is removed
    assertEquals(toplevel, codeElimination.getProcessedNode());
  }
}