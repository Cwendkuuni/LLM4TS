import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {
  @Test
  public void testUnreachableCodeElimination() {
    // Given: A Node object representing a JavaScript code snippet
    Node code = new Node("return; alert('unreachable');");

    // When: The UnreachableCodeElimination pass is applied to the Node
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
    elimination.process(null, null);
    Node result = elimination.getCompiler().getRootNode();

    // Then: The alert statement should be removed
    assertFalse(result.containsStatement(code.getFirstChild()));
  }
}