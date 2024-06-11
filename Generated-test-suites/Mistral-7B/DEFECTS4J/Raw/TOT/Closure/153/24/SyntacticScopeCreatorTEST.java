import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSError.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test.TestSourceFile;

public class SyntacticScopeCreatorTest {

  private static AbstractCompiler compiler;
  private static ScopeCreator scopeCreator;

  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void setUpOnce() {
    compiler = new TestCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testEmptyFunction() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo() {}",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(0, scope.getVars().size());
  }

  @Test
  public void testVarDeclaration() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo() { var x = 1; }",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
  }

  @Test
  public void testVarDeclarationWithType() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo() { var x: number = 1; }",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
    assertEquals(JSType.NUMBER, scope.getVars().get(0).getType());
  }

  @Test
  public void testMultipleVarDeclarations() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo() { var x = 1, y = 2; }",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(2, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
    assertEquals("y", scope.getVars().get(1).getName());
  }

  @Test
  public void testFunctionDeclaration() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo() { function bar() {} }",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("bar", scope.getVars().get(0).getName());
  }

  @Test
  public void testVarArgumentDeclaration() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo(x, y) {}",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(2, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
    assertEquals("y", scope.getVars().get(1).getName());
  }

  @Test
  public void testVarArgumentDeclarationWithType() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo(x: number, y: number) {}",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(2, scope.getVars().size());
    assertEquals("x", scope.getVars().get(0).getName());
    assertEquals(JSType.NUMBER, scope.getVars().get(0).getType());
    assertEquals("y", scope.getVars().get(1).getName());
    assertEquals(JSType.NUMBER, scope.getVars().get(1).getType());
  }

  @Test
  public void testVarArgumentDeclarationAndLocalVar() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo(x) { var y = 1; }",
        null).getRootNode();

    Scope scope = scopeCreator.createScope(root, null);
    assertEquals(1, scope.getVars().size());
    assertEquals("y", scope.getVars().get(0).getName());
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateDeclaration() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo() { var x = 1; var x = 2; }",
        null).getRootNode();

    scopeCreator.createScope(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateArgumentDeclaration() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo(x, x) {}",
        null).getRootNode();

    scopeCreator.createScope(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateGlobalDeclaration() {
    Node root = new TestSourceFile(
        "Test.js",
        "var x = 1; var x = 2;",
        null).getRootNode();

    scopeCreator.createScope(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testArgumentsVariableDeclaration() {
    Node root = new TestSourceFile(
        "Test.js",
        "function foo() { var arguments = 1; }",
        null).getRootNode();

    scopeCreator.createScope(root, null);
  }
}