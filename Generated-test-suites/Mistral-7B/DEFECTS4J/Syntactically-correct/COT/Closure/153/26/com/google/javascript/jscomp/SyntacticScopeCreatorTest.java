import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.DefaultRedeclarationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeUtil;

public class SyntacticScopeCreatorTest {

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public void report(JSError error) {}
  }

  @Test
  public void testCreateScope() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Scope scope = creator.createScope(createNode("function foo() {}"), null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
  }

  @Test
  public void testCreateGlobalScope() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Scope scope = creator.createScope(createNode("(function() {})();"), null);

    assertEquals("", scope.getName());
    assertEquals(Scope.Type.GLOBAL, scope.getType());
    assertNull(scope.getParent());
  }

  @Test
  public void testFunctionDeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo() {}");
    Scope parentScope = createScope("var x = 1");
    Scope scope = creator.createScope(functionNode, parentScope);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertEquals(parentScope, scope.getParent());
    assertFalse(scope.isDeclared("foo"));
    assertFalse(scope.isDeclared("x"));
  }

  @Test
  public void testFunctionDeclarationWithName() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo() {}");
    Node nameNode = functionNode.getFirstChild();
    String name = nameNode.getString();
    Scope parentScope = createScope("var x = 1");
    Scope scope = creator.createScope(functionNode, parentScope);

    assertEquals(name, scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertEquals(parentScope, scope.getParent());
    assertTrue(scope.isDeclared(name));
    assertFalse(scope.isDeclared("x"));
  }

  @Test
  public void testVariableDeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node node = createNode("var x = 1, y, z;");
    Scope scope = creator.createScope(node, null);

    assertEquals("", scope.getName());
    assertEquals(Scope.Type.BLOCK, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertTrue(scope.isDeclared("y"));
    assertTrue(scope.isDeclared("z"));
  }

  @Test
  public void testVariableDeclarationWithSameName() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node node = createNode("var x = 1, x = 2;");
    Scope scope = creator.createScope(node, null);

    assertEquals("", scope.getName());
    assertEquals(Scope.Type.BLOCK, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(1, scope.getVar("x").getIndex());
    assertEquals(2, scope.getVar("x", 1).getIndex());
  }

  @Test
  public void testVariableDeclarationWithSameNameInFunction() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo() { var x = 1; var x = 2; }");
    Scope scope = creator.createScope(functionNode, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(1, scope.getVar("x").getIndex());
    assertEquals(2, scope.getVar("x", 1).getIndex());
  }

  @Test
  public void testVariableDeclarationWithSameNameInGlobalScope() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node node = createNode("var x = 1; var x = 2;");
    Scope scope = creator.createScope(node, null);

    assertEquals("", scope.getName());
    assertEquals(Scope.Type.GLOBAL, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(1, scope.getVar("x").getIndex());
    assertEquals(2, scope.getVar("x", 1).getIndex());
  }

  @Test(expected = AssertionError.class)
  public void testVariableDeclarationWithSameNameInParentScope() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node parentNode = createNode("var x = 1;");
    Node node = createNode("function foo() { var x = 2; }");
    Scope parentScope = creator.createScope(parentNode, null);
    Scope scope = creator.createScope(node, parentScope);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertEquals(parentScope, scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(2, scope.getVar("x").getIndex());
    assertThrows(AssertionError.class, () -> parentScope.getVar("x"));
  }

  @Test
  public void testFunctionWithArgumentsDeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo(x, y) {}");
    Scope scope = creator.createScope(functionNode, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertTrue(scope.isDeclared("y"));
  }

  @Test
  public void testFunctionWithEmptyArgumentsDeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo() {}");
    Scope scope = creator.createScope(functionNode, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("arguments"));
  }

  @Test
  public void testFunctionWithArgumentsDeclarationAndLocalVariable() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo(x) { var y = 1; }");
    Scope scope = creator.createScope(functionNode, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertTrue(scope.isDeclared("y"));
  }

  @Test
  public void testVariableDeclarationWithSameNameAsArguments() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo(x) { var x = 1; }");
    Scope scope = creator.createScope(functionNode, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(1, scope.getVar("x").getIndex());
    assertEquals(0, scope.getVar("arguments").getVar("x").getIndex());
  }

  @Test(expected = AssertionError.class)
  public void testVariableDeclarationWithSameNameAsArgumentsInParentScope() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node parentNode = createNode("function foo(x) { var x; }");
    Node node = createNode("function bar(y) { var x = 1; }");
    Scope parentScope = creator.createScope(parentNode, null);
    Scope scope = creator.createScope(node, parentScope);

    assertEquals("bar", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertEquals(parentScope, scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(1, scope.getVar("x").getIndex());
    assertThrows(AssertionError.class, () -> parentScope.getVar("x"));
  }

  @Test
  public void testVariableDeclarationWithSameNameAsArgumentsInFunctionScope() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo(x) { var x = 1; }");
    Scope scope = creator.createScope(functionNode, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(1, scope.getVar("x").getIndex());
    assertEquals(0, scope.getVar("arguments").getVar("x").getIndex());
  }

  @Test
  public void testVariableDeclarationWithSameNameAsGlobalVariable() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node node = createNode("var x = 1; function foo() { var x = 2; }");
    Scope scope = creator.createScope(node, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(2, scope.getVar("x").getIndex());
    assertTrue(scope.getParent().isDeclared("x"));
    assertEquals(1, scope.getParent().getVar("x").getIndex());
  }

  @Test
  public void testVariableDeclarationWithSameNameAsGlobalVariableInFunction() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node functionNode = createNode("function foo() { var x = 2; }");
    Scope scope = creator.createScope(functionNode, null);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertNull(scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(2, scope.getVar("x").getIndex());
    assertTrue(scope.getParent().isDeclared("x"));
    assertEquals(1, scope.getParent().getVar("x").getIndex());
  }

  @Test
  public void testVariableDeclarationWithSameNameAsGlobalVariableInParent() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node parentNode = createNode("var x = 1;");
    Node node = createNode("function foo() { var x = 2; }");
    Scope parentScope = creator.createScope(parentNode, null);
    Scope scope = creator.createScope(node, parentScope);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertEquals(parentScope, scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(2, scope.getVar("x").getIndex());
    assertTrue(parentScope.isDeclared("x"));
    assertEquals(1, parentScope.getVar("x").getIndex());
  }

  @Test
  public void testVariableDeclarationWithSameNameAsGlobalVariableInParentWithDuplicateHandling() {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler(), new AllowDuplicateDeclarationHandler());
    Node parentNode = createNode("var x = 1;");
    Node node = createNode("function foo() { var x = 2; }");
    Scope parentScope = creator.createScope(parentNode, null);
    Scope scope = creator.createScope(node, parentScope);

    assertEquals("foo", scope.getName());
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertEquals(parentScope, scope.getParent());
    assertTrue(scope.isDeclared("x"));
    assertEquals(2, scope.getVar("x").getIndex());
    assertTrue(parentScope.isDeclared("x"));
    assertEquals(1, parentScope.getVar("x").getIndex());
  }

  private static Node createNode(String code) {
    return com.google.javascript.rhino.JSDocInfo.getJsDocInfo(code).getAst();
  }

  private static Scope createScope(String code) {
    ScopeCreator creator = new SyntacticScopeCreator(new TestCompiler());
    Node node = createNode(code);
    Scope scope = creator.createScope(node, null);
    return scope;
  }

  private static class AllowDuplicateDeclarationHandler implements RedeclarationHandler {
    @Override
    public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
      // Allow duplicate declarations for testing
    }
  }
}