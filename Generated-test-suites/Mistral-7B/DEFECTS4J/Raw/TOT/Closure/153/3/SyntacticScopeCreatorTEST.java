import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.TestUtils;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private ScopeCreator sc;
  private Scope rootScope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    Var globalVar = new Scope.Var("globalVar", null, null, null);
    rootScope = new Scope(null, compiler);
    rootScope.declare("globalVar", null, null, null);
    sc = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testGlobalScope() {
    Node n = TestUtils.createNode("var globalVar = 1;");
    Scope scope = sc.createScope(n, rootScope);
    assertEquals(rootScope, scope);
    Var declaredVar = scope.getVar("globalVar");
    assertEquals("globalVar", declaredVar.getName());
    assertEquals(JSType.NUMBER, declaredVar.getType());
  }

  @Test
  public void testFunctionScope() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node functionNode = TestUtil.createNode("function test(){}");
    Scope parentScope = new Scope(null, compiler);
    Scope scope = sc.createScope(functionNode, parentScope);
    assertEquals(parentScope, scope.getParent());
    assertNull(scope.getVar("test")); // not declared yet
  }

  @Test
  public void testFunctionScopeWithArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node functionNode = TestUtil.createNode("function test(x) {}");
    Scope parentScope = new Scope(null, compiler);
    Scope scope = sc.createScope(functionNode, parentScope);
    assertEquals(parentScope, scope.getParent());
    Var argumentsVar = scope.getVar("arguments");
    assertEquals("arguments", argumentsVar.getName());
    assertEquals(JSType.OBJECT, argumentsVar.getType());
  }

  @Test
  public void testFunctionScopeWithArgumentsAndVariables() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node functionNode = TestUtil.createNode("function test(x) { var y = 1; }");
    Scope parentScope = new Scope(null, compiler);
    Scope scope = sc.createScope(functionNode, parentScope);
    assertEquals(parentScope, scope.getParent());
    Var argumentsVar = scope.getVar("arguments");
    assertEquals("arguments", argumentsVar.getName());
    assertEquals(JSType.OBJECT, argumentsVar.getType());
    Var yVar = scope.getVar("y");
    assertEquals("y", yVar.getName());
    assertEquals(JSType.NUMBER, yVar.getType());
  }

  @Test
  public void testFunctionScopeWithDuplicateDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node functionNode = TestUtil.createNode("function test() { var x = 1; var x = 2; }");
    Scope parentScope = new Scope(null, compiler);
    Scope scope = sc.createScope(functionNode, parentScope);
    assertEquals(parentScope, scope.getParent());
    assertNull(scope.getVar("x"));

    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNull(scope.getVar("x"));
    softAssert.assertFalse(scope.isDeclared("x", false));
    softAssert.assertTrue(compiler.hasError());
    softAssert.assertErrorMessage("Variable x first declared in test", DiagnosticType.ERROR);
    softAssert.assertErrorMessage("Variable x first declared in test", DiagnosticType.ERROR);
    softAssert.assertAll();
  }

  @Test
  public void testRedeclarationHandler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator sc = new SyntacticScopeCreator(compiler, new RedeclarationHandler() {
      @Override
      public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        assertEquals(s, scope);
        assertEquals("test", name);
        assertEquals(n, TestUtil.createNode("var test = 1;"));
        assertEquals(parent, null);
        assertEquals(gramps, null);
        assertEquals(nodeWithLineNumber, TestUtil.createNode("var test = 2;"));
      }
    });
    Node functionNode = TestUtil.createNode("function test() { var test = 1; var test = 2; }");
    Scope parentScope = new Scope(null, compiler);
    Scope scope = sc.createScope(functionNode, parentScope);
    assertEquals(parentScope, scope.getParent());
    // Test that the redeclarationHandler is triggered
    assertNull(scope.getVar("test"));

    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNull(scope.getVar("test"));
    softAssert.assertFalse(scope.isDeclared("test", false));
    softAssert.assertTrue(compiler.hasError());
    softAssert.assertErrorMessage("Variable test first declared in test", DiagnosticType.ERROR);
    softAssert.assertErrorMessage("Variable test first declared in test", DiagnosticType.ERROR);
    softAssert.assertAll();
  }

  @Test
  public void testGlobalScopeWithDuplicateDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node globalNode = TestUtil.createNode("var x = 1; var x = 2;");
    Scope scope = sc.createScope(globalNode, null);
    assertEquals(null, scope.getParent());
    Var xVar = scope.getVar("x");
    assertNull(xVar);

    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNull(xVar);
    softAssert.assertFalse(scope.isDeclared("x", false));
    softAssert.assertTrue(compiler.hasError());
    softAssert.assertErrorMessage("Variable x first declared in", DiagnosticType.ERROR);
    softAssert.assertErrorMessage("Variable x first declared in", DiagnosticType.ERROR);
    softAssert.assertAll();
  }

  @Test
  public void testGlobalScopeWithArgumentsShadowingDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ScopeCreator sc = new SyntacticScopeCreator(compiler);
    Node globalNode = TestUtil.createNode("var x = 1; function test(x) {}");
    Scope scope = sc.createScope(globalNode, null);
    assertEquals(null, scope.getParent());

    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNull(scope.getVar("x"));
    softAssert.assertFalse(scope.isDeclared("x", false));
    softAssert.assertTrue(compiler.hasError());
    softAssert.assertErrorMessage("Shadowing \"arguments\" is not allowed", DiagnosticType.ERROR);
    softAssert.assertAll();
  }
}