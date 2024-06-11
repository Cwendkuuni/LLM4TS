import org.junit.Test;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import static org.junit.Assert.assertNotNull;

public class SyntacticScopeCreatorTest {
  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new NoOpCompiler(); // Replace with a real compiler
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node rootNode = new Node(Token.SCRIPT); // Replace with a valid script node
    Scope parentScope = new Scope(null, null); // Replace with a valid parent scope
    Scope scope = creator.createScope(rootNode, parentScope);
    assertNotNull(scope);
  }

  // Add test cases for each method and potential edge cases here
}

class NoOpCompiler implements AbstractCompiler {
  @Override
  public void report(DiagnosticType diagnosticType, Node node, String message,
                    Object... args) {
    // Do nothing
  }

  @Override
  public Input getInput(String name) {
    return new Input(); // Replace with a valid input
  }
}

class Input {}