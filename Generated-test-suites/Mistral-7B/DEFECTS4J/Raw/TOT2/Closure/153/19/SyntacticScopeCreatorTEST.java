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
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ScopeCreator getScopeCreator() {
        return new SyntacticScopeCreator(compiler);
      }
    };
    String sourceName = "test.js";
    ScopeCreator sc = new SyntacticScopeCreator(compiler);

    // When
    Scope scope = sc.createScope(new Node("testFunction() {}", sourceName, null), null);

    // Then
    assertNotNull(scope);
  }

  @Test
  public void testCreateScopeWithFunctionName() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ScopeCreator getScopeCreator() {
        return new SyntacticScopeCreator(compiler);
      }
    };
    String sourceName = "test.js";
    ScopeCreator sc = new SyntacticScopeCreator(compiler);

    // When
    Scope scope = sc.createScope(new Node("function testFunction() {}", sourceName, null), null);

    // Then
    assertNotNull(scope);
    assertEquals("testFunction", scope.getParent().getVar(0).getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullScopeParent() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ScopeCreator getScopeCreator() {
        return new SyntacticScopeCreator(compiler);
      }
    };
    String sourceName = "test.js";
    ScopeCreator sc = new SyntacticScopeCreator(compiler);

    // When
    Scope scope = sc.createScope(new Node("testFunction() {}", sourceName, null), null);

    // Then
    fail("Expected an IllegalArgumentException");
  }

  // Add test cases for edge cases and error scenarios here...
}