import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;
  private Node testNode;

  @Before
  public void setup() {
    compiler = new Compiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
    testNode = Node.createTreeFromString(
        "function test() {" +
        "  var x = 1;" +
        "  var x = 2;" +
        "  var y = 3;" +
        "}"
    );
  }

  @Test
  public void testCreateScope() {
    Scope scope = scopeCreator.createScope(testNode, null);
    assertEquals(3, scope.size());
    assertEquals("x", scope.getVar("x").getName());
    assertEquals("y", scope.getVar("y").getName());
    assertEquals(null, scope.getVar("z"));
  }

  @Test
  public void testCreateScopeWithParent() {
    Scope parentScope = new Scope(null, null);
    parentScope.declare("x", Node.createString("x"), null, null);

    Scope scope = scopeCreator.createScope(testNode, parentScope);
    assertEquals(2, scope.size());
    assertEquals("x", scope.getVar("x").getName());
    assertEquals("y", scope.getVar("y").getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeNullNode() {
    scopeCreator.createScope(null, null);
  }

  @Test
  public void testCreateScopeNullParent() {
    Scope scope = scopeCreator.createScope(testNode, null);
    assertEquals(3, scope.size());
  }

  @Test
  public void testScanRootFunctionExpression() {
    Node functionNode = Node.createTreeFromString(
        "function test() {" +
        "}"
    );
    scopeCreator.scanRoot(functionNode, null);
    assertEquals(0, scope.size());
  }

  @Test
  public void testScanRootFunctionDeclaration() {
    Node functionNode = Node.createTreeFromString(
        "function test() {" +
        "  var x = 1;" +
        "}"
    );
    scopeCreator.scanRoot(functionNode, null);
    assertEquals(1, scope.size());
    assertEquals("x", scope.getVar("x").getName());
  }

  @Test
  public void testScanRootFunctionDeclarationWithArguments() {
    Node functionNode = Node.createTreeFromString(
        "function test(x) {" +
        "}"
    );
    scopeCreator.scanRoot(functionNode, null);
    assertEquals(1, scope.size());
    assertEquals("x", scope.getVar("x").getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScanRootInvalidNodeType() {
    scopeCreator.scanRoot(Node.createTreeFromString("1"), null);
  }

  @Test
  public void testScanRootScript() {
    Node scriptNode = Node.createTreeFromString(
        "var x = 1;" +
        "var y = 2;"
    );
    scopeCreator.scanRoot(scriptNode, null);
    assertEquals(2, scope.size());
    assertEquals("x", scope.getVar("x").getName());
    assertEquals("y", scope.getVar("y").getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScanRootInvalidNodeTypeScript() {
    scopeCreator.scanRoot(Node.createTreeFromString("1"), null);
  }

  @Test
  public void testDeclareVar() {
    String name = "x";
    Node node = Node.createTreeFromString("x");
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test
  public void testDeclareVarExistingVariable() {
    String name = "x";
    Node node = Node.createTreeFromString("x");
    scopeCreator.declareVar(name, node, null, null, null, null);
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVarNullInputs() {
    scopeCreator.declareVar(null, null, null, null, null, null);
  }

  @Test
  public void testDeclareVarNullParent() {
    String name = "x";
    Node node = Node.createTreeFromString("x");
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVarNullNode() {
    scopeCreator.declareVar(null, null, null, null, null, null);
  }

  @Test
  public void testDeclareVarEmptyName() {
    String name = "";
    Node node = Node.createTreeFromString("x");
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(0, scope.size());
  }

  @Test
  public void testDeclareVarVariableDeclarationStatement() {
    String name = "x";
    Node node = Node.createTreeFromString("var x = 1");
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test
  public void testDeclareVarFunctionDeclarationStatement() {
    String name = "test";
    Node node = Node.createTreeFromString(
        "function " + name + "() {" +
        "}"
    );
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test
  public void testDeclareVarFunctionExpressionStatement() {
    String name = "test";
    Node node = Node.createTreeFromString(
        "function " + name + "() {}"
    );
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test
  public void testDeclareVarVarStatementWithEmptyName() {
    String name = "";
    Node node = Node.createTreeFromString("var " + name + " = 1");
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(0, scope.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVarVarStatementWithNullName() {
    Node node = Node.createTreeFromString("var x = 1");
    scopeCreator.declareVar(null, node, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVarVarStatementWithNullNode() {
    String name = "x";
    scopeCreator.declareVar(name, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVarWithType() {
    String name = "x";
    Node node = Node.createTreeFromString("var x: number = 1");
    scopeCreator.declareVar(name, node, null, null, JSType.NUMBER, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVarWithInvalidType() {
    String name = "x";
    Node node = Node.createTreeFromString("var x = color");
    scopeCreator.declareVar(name, node, null, null, null, null);
  }

  @Test
  public void testDeclareVarWithJSDocInfo() {
    String name = "test";
    Node node = Node.createTreeFromString(
        "/** @type {number} */ " +
        "function " + name + "() {" +
        "}"
    );
    scopeCreator.declareVar(name, node, null, null, null, null);
    assertEquals(1, scope.size());
    assertEquals(name, scope.getVar(name).getName());
  }

  @Test
  public void testRedeclarationHandlerOnRedeclaration() {
    String name = "x";
    Node node = Node.createTreeFromString("var x = 1");
    Node parentNode = Node.createTreeFromString("function test() { var x = 2 }");
    Node grampsNode = null;
    Node nodeWithLineNumber = Node.createTreeFromString("function test() { var x = 2 }");

    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, node, parentNode, grampsNode, nodeWithLineNumber);

    assertEquals(1, scope.size());
    assertEquals("x", scope.getVar(name).getName());

    // Check error reporting
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR",
        scopeCreator.compiler.getErrors().get(0).getType().getName());
  }

  @Test
  public void testRedeclarationHandlerOnRedeclarationWithGlobalScope() {
    String name = "x";
    Node node = Node.createTreeFromString("var x = 1");
    Node parentNode = Node.createTreeFromString("function test() { var x = 2 }");
    Node grampsNode = null;
    Node nodeWithLineNumber = Node.createTreeFromString("function test() { var x = 2 }");

    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, node, parentNode, grampsNode, nodeWithLineNumber);

    assertEquals(1, scope.size());
    assertEquals("x", scope.getVar(name).getName());

    // Check error reporting
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR",
        scopeCreator.compiler.getErrors().get(0).getType().getName());
  }

  @Test
  public void testRedeclarationHandlerOnRedeclarationWithLocalScope() {
    String name = "x";
    Scope parentScope = new Scope(null, null);
    parentScope.declare(name, Node.createString(name), null, null);

    Node node = Node.createTreeFromString("var x = 2");
    Node parentNode = Node.createTreeFromString("function test() { var x = 2 }");
    Node grampsNode = null;
    Node nodeWithLineNumber = Node.createTreeFromString("function test() { var x = 2 }");

    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, node, parentNode, grampsNode, nodeWithLineNumber);

    assertEquals(1, scope.size());
    assertEquals("x", scope.getVar(name).getName());

    // Check error reporting
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR",
        scopeCreator.compiler.getErrors().get(0).getType().getName());
  }

  @Test
  public void testRedeclarationHandlerOnRedeclarationWithArgumentsShadowing() {
    String name = "arguments";
    Node node = Node.createTreeFromString("var arguments = 1");
    Node parentNode = Node.createTreeFromString("function test() { var arguments = 2 }");
    Node grampsNode = null;
    Node nodeWithLineNumber = Node.createTreeFromString("function test() { var arguments = 2 }");

    scopeCreator.redeclarationHandler.onRedeclaration(
        scope, name, node, parentNode, grampsNode, nodeWithLineNumber);

    assertEquals(0, scope.size());

    // Check error reporting
    assertEquals("JSC_VAR_ARGUMENTS_SHADOWED_ERROR",
        scopeCreator.compiler.getErrors().get(0).getType().getName());
  }
}