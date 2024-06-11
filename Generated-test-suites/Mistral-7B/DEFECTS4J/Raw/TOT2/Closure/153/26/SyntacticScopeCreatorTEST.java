import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.TestSyntaxTree;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class SyntacticScopeCreatorTest {

  static ScopeCreator createScopeCreator(AbstractCompiler compiler) {
    return new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testEmptyFunction() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = createScopeCreator(compiler);

    Node root = new TestSyntaxTree().getFunctionDeclaration("test");
    Scope scope = scopeCreator.createScope(root, null);

    // No variables should be declared
    assertEquals(0, scope.size());
  }

  @Test
  public void testSimpleFunction() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = createScopeCreator(compiler);

    Node root = new TestSyntaxTree().getFunctionDeclaration("test", new Node("x"));
    Scope scope = scopeCreator.createScope(root, null);

    // One variable should be declared
    assertEquals(1, scope.size());
    Scope.Var var = scope.getVar("x");
    assertEquals("x", var.getName());
    assertEquals(null, var.getType());
  }

  @Test
  public void testFunctionWithArguments() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = createScopeCreator(compiler);

    Node root = new TestSyntaxTree().getFunctionDeclaration("test", new Node("x"), new Node("y"));
    Scope scope = scopeCreator.createScope(root, null);

    // Two variables should be declared
    assertEquals(2, scope.size());
    Scope.Var xVar = scope.getVar("x");
    assertEquals("x", xVar.getName());
    assertEquals(null, xVar.getType());

    Scope.Var yVar = scope.getVar("y");
    assertEquals("y", yVar.getName());
    assertEquals(null, yVar.getType());
  }

  @Test(expected = JSError.class)
  public void testDuplicateDeclaration() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = createScopeCreator(compiler);

    Node root = new TestSyntaxTree().getFunctionDeclaration("test", new Node("x"), new Node("x"));
    ScopeCreator.ScopeCreatorDelegate delegate = mock(ScopeCreator.ScopeCreatorDelegate.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, delegate);
    Scope scope = scopeCreator.createScope(root, null);

    // An error should be thrown
    failure();
  }

  @Test
  public void testGlobalDeclaration() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = createScopeCreator(compiler);

    Node root = new TestSyntaxTree().getVariableDeclaration("x");
    Scope scope = scopeCreator.createScope(root, null);

    // One variable should be declared
    assertEquals(1, scope.size());
    Scope.Var var = scope.getVar("x");
    assertEquals("x", var.getName());
    assertEquals(null, var.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullParent() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Scope scope = scopeCreator.createScope(null, null);

    // An error should be thrown
    failure();
  }

  @Test
  public void testCatchDeclaration() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = createScopeCreator(compiler);

    Node root = new TestSyntaxTree().getCatchDeclaration("test", new Node("x"));
    Scope scope = scopeCreator.createScope(root, null);

    // One variable should be declared
    assertEquals(1, scope.size());
    Scope.Var var = scope.getVar("x");
    assertEquals("x", var.getName());
    assertEquals(null, var.getType());
  }

  @Test(expected = JSError.class)
  public void testDuplicateCatchDeclaration() {
    AbstractCompiler compiler = new Compiler();
    ScopeCreator scopeCreator = createScopeCreator(compiler);

    Node root = new TestSyntaxTree().getCatchDeclaration("test", new Node("x"), new Node("x"));
    ScopeCreator.ScopeCreatorDelegate delegate = mock(ScopeCreator.ScopeCreatorDelegate.class);
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, delegate);
    Scope scope = scopeCreator.createScope(root, null);

    // An error should be thrown
    failure();
  }
}