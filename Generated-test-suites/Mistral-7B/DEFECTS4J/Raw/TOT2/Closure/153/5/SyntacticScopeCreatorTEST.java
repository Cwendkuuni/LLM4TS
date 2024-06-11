import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  @Test
  public void testCreateScope_FunctionWithArguments() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(null, null);
    Node n = createFunctionNode("function test(x, y) { }");

    Scope scope = sc.createScope(n, parent);
    assertEquals(2, scope.getVariables().size());
    assertEquals("x", scope.getVariables().get(0).getName());
    assertEquals("y", scope.getVariables().get(1).getName());
  }

  @Test
  public void testCreateScope_FunctionWithoutArguments() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(null, null);
    Node n = createFunctionNode("function test() { }");

    Scope scope = sc.createScope(n, parent);
    assertTrue(scope.getVariables().isEmpty());
  }

  @Test
  public void testCreateScope_GlobalScope() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = null;
    Node n = createScriptNode("var x = 1;");

    Scope scope = sc.createScope(n, parent);
    assertEquals("x", scope.getVariable("x").getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScope_NullNode() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(null, null);
    Node n = null;

    Scope scope = sc.createScope(n, parent);
  }

  @Test
  public void testDeclareVar_DuplicateDeclaration() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(null, null);
    Node n = createScriptNode("var x = 1; var x = 2;");

    try {
      sc.createScope(n, parent);
      fail("Expected a JSError to be reported.");
    } catch (JSError e) {
      assertEquals(DiagnosticType.VAR_MULTIPLY_DECLARED_ERROR, e.getType());
    }
  }

  @Test
  public void testDeclareVar_ArgumentShadowing() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(null, null);
    Node n = createScriptNode("function test(x) { var arguments = 1; }");

    try {
      sc.createScope(n, parent);
      fail("Expected a JSError to be reported.");
    } catch (JSError e) {
      assertEquals(DiagnosticType.VAR_ARGUMENTS_SHADOWED_ERROR, e.getType());
    }
  }

  private Node createFunctionNode(String code) {
    // TODO: Implement Node creation
    return null;
  }

  private Node createScriptNode(String code) {
    // TODO: Implement Node creation
    return null;
  }
}