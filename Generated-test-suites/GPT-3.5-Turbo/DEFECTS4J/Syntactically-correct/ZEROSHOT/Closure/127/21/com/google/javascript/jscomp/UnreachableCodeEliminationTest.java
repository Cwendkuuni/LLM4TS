import org.junit.*;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testProcess() {
    // Create a Compiler instance
    AbstractCompiler compiler = new AbstractCompiler();
    
    // Create an instance of UnreachableCodeElimination
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    
    // Create dummy nodes for externs and toplevel
    Node externs = new Node(Token.EXTERN, null);
    Node toplevel = new Node(Token.TOPLEVEL, null);
    
    // Call the process method and assert that it does not throw any exceptions
    try {
      elimination.process(externs, toplevel);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}