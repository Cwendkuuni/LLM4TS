import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new NoOpCompiler(); // Replace with a valid compiler
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node rootNode = Node.newCompilationUnit("function test() { var x = 1; }");
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeNullParent() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node rootNode = Node.newCompilationUnit("function test() { var x = 1; }");
    scopeCreator.createScope(rootNode, null);
  }

  @Test
  public void testScanRootFunction() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node rootNode = Node.newCompilationUnit("function test(x) { var x = 1; function foo() {}; }");
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(3, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
    assertEquals("x", scope.getVars().get(1).getName());
    assertEquals("foo", scope.getVars().get(2).getName());
  }

  @Test
  public void testScanRootScript() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node rootNode = Node.newCompilationUnit("var x = 1;");
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
  }

  @Test
  public void testScanVarsFunction() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node rootNode = Node.newCompilationUnit("function test() { var x = 1; var x = 2; }");
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
  }

  @Test
  public void testScanVarsScript() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node rootNode = Node.newCompilationUnit("var x = 1; var x = 2;");
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScanVarsNullParent() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node rootNode = Node.newCompilationUnit("function test() { var x = 1; }");
    scopeCreator.createScope(rootNode, null);
  }

  @Test
  public void testOnRedeclarationGlobal() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.RedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    Node rootNode = Node.newCompilationUnit("var x = 1; var x = 2;");
    scopeCreator.setRedeclarationHandler(redeclarationHandler);
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.getVars().size());
    assertTrue(redeclarationHandler.wasCalled);
  }

  @Test
  public void testOnRedeclarationLocal() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.RedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    Node rootNode = Node.newCompilationUnit("function test() { var x = 1; var x = 2; }");
    scopeCreator.setRedeclarationHandler(redeclarationHandler);
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.getVars().size());
    assertTrue(redeclarationHandler.wasCalled);
  }

  @Test
  public void testOnRedeclarationArgument() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.RedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    Node rootNode = Node.newCompilationUnit("function test(x) { arg x; }");
    scopeCreator.setRedeclarationHandler(redeclarationHandler);
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.getVars().size());
    assertTrue(redeclarationHandler.wasCalled);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnRedeclarationNullParent() {
    AbstractCompiler compiler = new NoOpCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    ScopeCreator.RedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    Node rootNode = Node.newCompilationUnit("function test() { }");
    scopeCreator.setRedeclarationHandler(redeclarationHandler);
    scopeCreator.createScope(rootNode, null);
  }

  private static class NoOpCompiler implements AbstractCompiler {
    @Override
    public void report(DiagnosticType type, Node node, String message,
                       Object... args) {}

    @Override
    public void report(DiagnosticType type, Node node, String message) {
      // empty
    }

    @Override
    public void report(Node node, String message) {
      // empty
    }

    @Override
    public void report(String message) {
      // empty
    }

    @Override
    public void reportError(Node node, String message) {
      // empty
    }

    @Override
    public void reportError(Node node, DiagnosticType type, String message) {
      // empty
    }

    @Override
    public void reportError(Node node, DiagnosticType type, String message,
                            Object... args) {
      // empty
    }

    @Override
    public void reportWarning(Node node, String message) {
      // empty
    }

    @Override
    public void reportWarning(Node node, DiagnosticType type, String message) {
      // empty
    }

    @Override
    public void reportWarning(Node node, DiagnosticType type, String message,
                              Object... args) {
      // empty
    }

    @Override
    public void reportError(String message) {
      // empty
    }

    @Override
    public void reportWarning(String message) {
      // empty
    }

    @Override
    public void reportError(String message, Object... args) {
      // empty
    }

    @Override
    public void reportWarning(String message, Object... args) {
      // empty
    }

    @Override
    public void reportError(DiagnosticType type, String message, Object... args) {
      // empty
    }

    @Override
    public void reportWarning(DiagnosticType type, String message, Object... args) {
      // empty
    }

    @Override
    public void reportError(Node node, DiagnosticType type) {
      // empty
    }

    @Override
    public void reportWarning(Node node, DiagnosticType type) {
      // empty
    }

    @Override
    public void reportError(Node node, String message, Object... args) {
      // empty
    }

    @Override
    public void reportWarning(Node node, String message, Object... args) {
      // empty
    }

    @Override
    public void reportError(DiagnosticType type, Node node) {
      // empty
    }

    @Override
    public void reportWarning(DiagnosticType type, Node node) {
      // empty
    }

    @Override
    public void reportError(DiagnosticType type, Node node, Object... args) {
      // empty
    }

    @Override
    public void reportWarning(DiagnosticType type, Node node, Object... args) {
      // empty
    }
  }
}