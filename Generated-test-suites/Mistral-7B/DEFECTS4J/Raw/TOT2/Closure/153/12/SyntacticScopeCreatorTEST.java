import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SpyScope;

public class SyntacticScopeCreatorTest {

  @org.junit.Test
  public void testCreateScope() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = new Node(Token.SCRIPT, null);
    Scope parentScope = new SpyScope();

    Scope scope = scopeCreator.createScope(rootNode, parentScope);

    assertEquals(parentScope, scope.getParent());
    verify(compiler, times(1)).getInput(eq("testSourceName"));
  }

  // ... other test cases for other methods
}