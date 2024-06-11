import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {
  @Test
  public void testCreateScope() {
    // Given
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator sc = new SyntacticScopeCreator(compiler);

    // When
    Scope scope = sc.createScope(new Node(), null);

    // Then
    assertNotNull(scope);
  }

  @Test(expected = NullPointerException.class)
  public void testCreateScopeWithNullCompiler() {
    // Given
    ScopeCreator sc = new SyntacticScopeCreator(null);

    // When
    Scope scope = sc.createScope(new Node(), null);

    // Then
    fail("Should throw NullPointerException");
  }

  // Add more test cases here for each method and identified edge cases and exceptions
}