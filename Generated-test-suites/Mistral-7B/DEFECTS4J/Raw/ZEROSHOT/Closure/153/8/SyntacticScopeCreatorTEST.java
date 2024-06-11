import static org.junit.Assert.assertNotNull;
import static com.google.javascript.jscomp.NodeUtil.isFunctionExpression;
import static com.google.javascript.jscomp.NodeUtil.isVarDeclaration;
import static com.google.javascript.jscomp.Scope.Var.INPUT;
import static com.google.javascript.jscomp.Scope.Var.NAME;
import static com.google.javascript.jscomp.Scope.Var.NODE;
import static com.google.javascript.jscomp.Scope.Var.TYPE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

@RunWith(JUnit4.class)
public class SyntacticScopeCreatorTest {

  private static final DiagnosticType VAR_MULTIPLE_DECLARED_ERROR =
      DiagnosticType.error(
          "JSC_VAR_MULTIPLE_DECLARED_ERROR",
          "Variable {0} first declared in {1}");

  private static final DiagnosticType VAR_ARGUMENTS_SHADOWED_ERROR =
      DiagnosticType.error(
          "JSC_VAR_ARGUMENTS_SHADOWED_ERROR",
          "Shadowing \"arguments\" is not allowed");

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo() {}");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);
  }

  @Test
  public void testCreateGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node scriptNode = createScriptNode("var x = 5;");
    Scope scope = creator.createScope(scriptNode, null);
    assertNotNull(scope);

    Var variable = scope.getVar("x");
    assertNotNull(variable);
    assertEquals(INPUT, variable.getInput());
    assertEquals("x", variable.getName());
    assertEquals(Node.class, variable.getNode().getClass());
    assertEquals(JSType.number(), variable.getType());
  }

  @Test
  public void testCreateFunctionScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo(x) {}");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    Var argument = scope.getVar("x");
    assertNotNull(argument);
    assertEquals(INPUT, argument.getInput());
    assertEquals("x", argument.getName());
    assertEquals(Node.class, argument.getNode().getClass());
    assertEquals(JSType.undefined(), argument.getType());
  }

  @Test
  public void testCreateNestedFunctionScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node outerFunctionNode = createFunctionNode("function foo(x) {}");
    Node innerFunctionNode = createFunctionNode("function bar() {}");
    Scope parentScope = creator.createScope(outerFunctionNode, null);
    Scope scope = creator.createScope(innerFunctionNode, parentScope);
    assertNotNull(scope);

    Var outerArgument = parentScope.getVar("x");
    Var innerArgument = scope.getVar("x");
    assertNotNull(outerArgument);
    assertNotNull(innerArgument);
    assertNotNull(outerArgument.getNode());
    assertNotNull(innerArgument.getNode());
    assertEquals(outerArgument.getNode(), innerArgument.getNode());
  }

  @Test
  public void testCreateFunctionWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo(x, y) {}");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    assertNotNull(x);
    assertNotNull(y);
    assertEquals(INPUT, x.getInput());
    assertEquals("x", x.getName());
    assertEquals(INPUT, y.getInput());
    assertEquals("y", y.getName());
  }

  @Test
  public void testCreateFunctionWithArgumentsAndDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo(x, y) { var y = 5; }");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    assertNotNull(x);
    assertNotNull(y);
    assertEquals(INPUT, x.getInput());
    assertEquals("x", x.getName());
    assertEquals(INPUT, y.getInput());
    assertEquals("y", y.getName());
    assertEquals(JSType.undefined(), x.getType());
    assertEquals(JSType.number(), y.getType());
  }

  @Test
  public void testCreateFunctionWithArgumentsAndVarDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo(x, y) { var y = 5; var x; }");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    assertNotNull(x);
    assertNotNull(y);
    assertEquals(INPUT, x.getInput());
    assertEquals("x", x.getName());
    assertEquals(INPUT, y.getInput());
    assertEquals("y", y.getName());
    assertEquals(JSType.undefined(), x.getType());
    assertEquals(JSType.number(), y.getType());

    Preconditions.checkState(x.getNode() == null);
    Preconditions.checkState(y.getNode() != null);
  }

  @Test
  public void testCreateFunctionWithVarDeclarationWithoutArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo() { var x = 5; }");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    Var x = scope.getVar("x");
    assertNotNull(x);
    assertEquals(INPUT, x.getInput());
    assertEquals("x", x.getName());
    assertEquals(JSType.number(), x.getType());
    Preconditions.checkState(x.getNode() != null);
  }

  @Test
  public void testCreateFunctionWithVarDeclarationAndArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo(x, y) { var x = 5; }");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    Var x = scope.getVar("x");
    assertNotNull(x);
    assertEquals(INPUT, x.getInput());
    assertEquals("x", x.getName());
    assertEquals(JSType.number(), x.getType());
    Preconditions.checkState(x.getNode() == null);
  }

  @Test
  public void testCreateFunctionWithVarDeclarationAndArgumentsShadowingThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo() { var arguments = 5; }");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    compiler.reportCollector().clear();
    compiler.compile(functionNode, null, null, null);

    Preconditions.checkState(compiler.reportCollector().getDiagnostics().size() > 0);
    JSError error = compiler.reportCollector().getDiagnostics().get(0);
    assertEquals(VAR_ARGUMENTS_SHADOWED_ERROR, error.getType());
  }

  @Test
  public void testCreateFunctionWithVarDeclarationAndArgumentsWithDuplicateName() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo(x, y) { var x = 5; }");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    compiler.reportCollector().clear();
    compiler.compile(functionNode, null, null, null);

    Preconditions.checkState(compiler.reportCollector().getDiagnostics().size() > 0);
    JSError error = compiler.reportCollector().getDiagnostics().get(0);
    assertEquals(VAR_MULTIPLE_DECLARED_ERROR, error.getType());
  }

  @Test
  public void testCreateFunctionWithVarDeclarationAndArgumentsWithDuplicateNameInParentScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node parentFunctionNode = createFunctionNode("function parent() { var x = 5; }");
    Node functionNode = createFunctionNode("function foo() { var x = 10; }");
    Scope parentScope = creator.createScope(parentFunctionNode, null);
    Scope scope = creator.createScope(functionNode, parentScope);
    assertNotNull(scope);

    compiler.reportCollector().clear();
    compiler.compile(functionNode, null, null, null);

    Preconditions.checkState(compiler.reportCollector().getDiagnostics().size() > 0);
    JSError error = compiler.reportCollector().getDiagnostics().get(0);
    assertEquals(VAR_MULTIPLE_DECLARED_ERROR, error.getType());
  }

  @Test
  public void testCreateFunctionWithDuplicateArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node functionNode = createFunctionNode("function foo(x, x) {}");
    Scope scope = creator.createScope(functionNode, null);
    assertNotNull(scope);

    compiler.reportCollector().clear();
    compiler.compile(functionNode, null, null, null);

    Preconditions.checkState(compiler.reportCollector().getDiagnostics().size() > 0);
    JSError error = compiler.reportCollector().getDiagnostics().get(0);
    assertEquals(VAR_MULTIPLE_DECLARED_ERROR, error.getType());
  }

  @Test
  public void testCreateFunctionWithDuplicateArgumentsInParentScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node parentFunctionNode = createFunctionNode("function parent(x, x) {}");
    Node functionNode = createFunctionNode("function foo() {}");
    Scope parentScope = creator.createScope(parentFunctionNode, null);
    Scope scope = creator.createScope(functionNode, parentScope);
    assertNotNull(scope);

    compiler.reportCollector().clear();
    compiler.compile(functionNode, null, null, null);

    Preconditions.checkState(compiler.reportCollector().getDiagnostics().size() > 0);
    JSError error = compiler.reportCollector().getDiagnostics().get(0);
    assertEquals(VAR_MULTIPLE_DECLARED_ERROR, error.getType());
  }

  @Test
  public void testCreateGlobalWithDuplicateVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node scriptNode = createScriptNode("var x = 5; var x = 10;");
    Scope scope = creator.createScope(scriptNode, null);
    assertNotNull(scope);

    compiler.reportCollector().clear();
    compiler.compile(scriptNode, null, null, null);

    Preconditions.checkState(compiler.reportCollector().getDiagnostics().size() > 0);
    JSError error = compiler.reportCollector().getDiagnostics().get(0);
    assertEquals(VAR_MULTIPLE_DECLARED_ERROR, error.getType());
  }

  @Test
  public void testCreateGlobalWithDuplicateVariableInGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler);

    Node scriptNode = createScriptNode("var x = 5; var x = 10;");
    Scope scope = creator.createScope(scriptNode, null);
    assertNotNull(scope);

    compiler.reportCollector().clear();
    compiler.compile(scriptNode, null, null, null);

    Preconditions.checkState(compiler.reportCollector().getDiagnostics().size() > 0);
    JSError error = compiler.reportCollector().getDiagnostics().get(0);
    assertEquals(VAR_MULTIPLE_DECLARED_ERROR, error.getType());
  }

  private Node createFunctionNode(String nodeText) {
    Node node = new Node();
    node.setToken(new Token(Token.FUNCTION, 0, nodeText.length()));
    node.setSourcePosition(1, 1);
    NODE_UTILS.addChildren(node, new Node(new Token(Token.LP, 0, 1)), createFunctionNameNode(nodeText), createFunctionBodyNode(nodeText));
    return node;
  }

  private Node createFunctionNameNode(String nodeText) {
    Node node = new Node();
    node.setToken(new Token(Token.IDENT, 0, nodeText.indexOf("(") > 0 ? nodeText.indexOf("(") : nodeText.length()));
    node.setSourcePosition(1, 1 + nodeText.indexOf("(") > 0 ? nodeText.indexOf("(") + 1 : 1);
    return node;
  }

  private Node createFunctionBodyNode(String nodeText) {
    Node node = new Node();
    node.setToken(new Token(Token.RP, nodeText.indexOf("(") > 0 ? nodeText.indexOf("(") + nodeText.indexOf("(") : 0));
    node.setSourcePosition(1, nodeText.indexOf("(") > 0 ? nodeText.indexOf("(") + 1 + nodeText.lastIndexOf(")") + 1 : 1);
    return node;
  }

  private Node createScriptNode(String nodeText) {
    Node node = new Node();
    node.setToken(new Token(Token.SCRIPT, 0, nodeText.length()));
    node.setSourcePosition(1, 1);
    NODE_UTILS.addChildren(node, NodeUtil.parseScript(nodeText));
    return node;
  }
}