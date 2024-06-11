import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticContext;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.ScopeType;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private ScopeCreator scopeCreator;
  private Scope rootScope;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    nodeFactory = compiler.getFactory();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateGlobalScope() {
    rootScope = scopeCreator.createScope(nodeFactory.createScript(), null);
    assertEquals(ScopeType.GLOBAL, rootScope.getType());
    assertNull(rootScope.getParent());
  }

  @Test
  public void testCreateFunctionScope() {
    Node functionNode = nodeFactory.createFunction("test", null, null, null);
    Scope functionScope = scopeCreator.createScope(functionNode, rootScope);
    assertEquals(ScopeType.FUNCTION, functionScope.getType());
    assertEquals(rootScope, functionScope.getParent());

    Node functionNameNode = functionNode.getFirstChild();
    String functionName = functionNameNode.getString();
    declareFunction(functionScope, functionName, functionNode);

    assertNull(functionScope.getVar(ARGUMENTS));
  }

  @Test
  public void testCreateFunctionWithArguments() {
    Node functionNode = nodeFactory.createFunction("test", null, null, null);
    Node argsNode = functionNode.getFirstChild().getNext();
    Node bodyNode = argsNode.getNext();

    Scope functionScope = scopeCreator.createScope(functionNode, rootScope);

    assertEquals(ScopeType.FUNCTION, functionScope.getType());
    assertEquals(rootScope, functionScope.getParent());

    String functionName = functionNode.getFirstChild().getString();
    declareFunction(functionScope, functionName, functionNode);

    // Arguments
    Node argumentNode = argsNode.getFirstChild();
    String argumentName = argumentNode.getString();

    declareVariable(functionScope, argumentName, argumentNode, null);

    Node functionBodyNode = bodyNode;

    // Test empty body
    Scope emptyBodyScope = scopeCreator.createScope(functionBodyNode, functionScope);
    assertEquals(ScopeType.BLOCK, emptyBodyScope.getType());
    assertEquals(functionScope, emptyBodyScope.getParent());

    // Test variable declaration
    Node variableNode = nodeFactory.createName("x");
    Node variableDeclarationNode = nodeFactory.createVar("", variableNode);

    Node variableStatementNode = nodeFactory.createStatement(variableDeclarationNode);
    Node variableBlockNode = nodeFactory.createBlock(variableStatementNode);

    Scope variableBlockScope = scopeCreator.createScope(variableBlockNode, functionScope);
    assertEquals(ScopeType.BLOCK, variableBlockScope.getType());
    assertEquals(functionScope, variableBlockScope.getParent());

    declareVariable(variableBlockScope, "x", variableNode, variableBlockNode);

    // Test function declaration
    Node functionDeclarationNode = nodeFactory.createFunction(
        "foo", null, null, null);

    Node functionDeclarationStatementNode = nodeFactory.createStatement(functionDeclarationNode);
    Node functionDeclarationBlockNode = nodeFactory.createBlock(
        functionDeclarationStatementNode);

    Scope functionDeclarationBlockScope = scopeCreator.createScope(
        functionDeclarationBlockNode, functionScope);

    assertEquals(ScopeType.BLOCK, functionDeclarationBlockScope.getType());
    assertEquals(functionScope, functionDeclarationBlockScope.getParent());

    String functionName = functionDeclarationNode.getFirstChild().getString();
    declareFunction(functionDeclarationBlockScope, functionName, functionDeclarationNode);
  }

  @Test
  public void testCreateFunctionWithRedeclaredVariable() {
    Node functionNode = nodeFactory.createFunction("test", null, null, null);
    Node argsNode = functionNode.getFirstChild().getNext();
    Node bodyNode = argsNode.getNext();

    Scope functionScope = scopeCreator.createScope(functionNode, rootScope);

    assertEquals(ScopeType.FUNCTION, functionScope.getType());
    assertEquals(rootScope, functionScope.getParent());

    String functionName = functionNode.getFirstChild().getString();
    declareFunction(functionScope, functionName, functionNode);

    // Arguments
    Node argumentNode = argsNode.getFirstChild();
    String argumentName = argumentNode.getString();

    declareVariable(functionScope, argumentName, argumentNode, null);

    Node functionBodyNode = bodyNode;

    // Test redeclared variable
    Node variableNode = nodeFactory.createName("x");
    Node variableDeclarationNode = nodeFactory.createVar("", variableNode);

    Node variableStatementNode = nodeFactory.createStatement(variableDeclarationNode);
    Node variableBlockNode = nodeFactory.createBlock(variableStatementNode);

    Scope variableBlockScope = scopeCreator.createScope(variableBlockNode, functionScope);

    assertEquals(ScopeType.BLOCK, variableBlockScope.getType());
    assertEquals(functionScope, variableBlockScope.getParent());

    String variableName = variableNode.getString();
    declareVariable(variableBlockScope, variableName, variableNode, variableBlockNode);

    Node variableDeclarationStatementNode = variableStatementNode;
    diagnosticExpected("JSC_VAR_MULTIPLY_DECLARED_ERROR", variableDeclarationStatementNode,
        rootScope, variableName, functionName);
  }

  @Test
  public void testCreateFunctionWithArgumentsShadowingArguments() {
    Node functionNode = nodeFactory.createFunction("test", null, null, null);
    Node argsNode = functionNode.getFirstChild().getNext();
    Node bodyNode = argsNode.getNext();

    Scope functionScope = scopeCreator.createScope(functionNode, rootScope);

    assertEquals(ScopeType.FUNCTION, functionScope.getType());
    assertEquals(rootScope, functionScope.getParent());

    String functionName = functionNode.getFirstChild().getString();
    declareFunction(functionScope, functionName, functionNode);

    // Arguments
    Node argumentNode = argsNode.getFirstChild();
    String argumentName = argumentNode.getString();

    declareVariable(functionScope, argumentName, argumentNode, null);

    Node argumentDeclarationNode = nodeFactory.createVar(
        "arg", nodeFactory.createName("arg"), null, null);
    Node argumentDeclarationStatementNode = nodeFactory.createStatement(argumentDeclarationNode);
    Node argumentDeclarationBlockNode = nodeFactory.createBlock(
        argumentDeclarationStatementNode);

    Scope argumentDeclarationBlockScope = scopeCreator.createScope(
        argumentDeclarationBlockNode, functionScope);

    assertEquals(ScopeType.BLOCK, argumentDeclarationBlockScope.getType());
    assertEquals(functionScope, argumentDeclarationBlockScope.getParent());

    String argumentVariableName = "arg";
    declareVariable(argumentDeclarationBlockScope, argumentVariableName,
        argumentDeclarationNode.getFirstChild(), argumentDeclarationBlockNode);

    Node functionBodyNode = bodyNode;

    // Test shadowed arguments
    Node variableNode = nodeFactory.createName("arguments");
    Node variableDeclarationNode = nodeFactory.createVar("", variableNode);

    Node variableStatementNode = nodeFactory.createStatement(variableDeclarationNode);
    Node variableBlockNode = nodeFactory.createBlock(variableStatementNode);

    Scope variableBlockScope = scopeCreator.createScope(variableBlockNode, functionScope);

    assertEquals(ScopeType.BLOCK, variableBlockScope.getType());
    assertEquals(functionScope, variableBlockScope.getParent());

    declareVariable(variableBlockScope, "arguments", variableNode, variableBlockNode);

    Node variableDeclarationStatementNode = variableStatementNode;
    diagnosticExpected("JSC_VAR_ARGUMENTS_SHADOWED_ERROR", variableDeclarationStatementNode,
        rootScope, "arguments");
  }

  @Test
  public void testCreateFunctionWithRedeclaredGlobalVariable() {
    Node functionNode = nodeFactory.createFunction("test", null, null, null);
    Node argsNode = functionNode.getFirstChild().getNext();
    Node bodyNode = argsNode.getNext();

    String globalVariableName = "x";

    // Set up global variable
    rootScope.declare(globalVariableName, null, JSType.NULL_TYPE, null);

    Scope functionScope = scopeCreator.createScope(functionNode, rootScope);

    assertEquals(ScopeType.FUNCTION, functionScope.getType());
    assertEquals(rootScope, functionScope.getParent());

    String functionName = functionNode.getFirstChild().getString();
    declareFunction(functionScope, functionName, functionNode);

    // Arguments
    Node argumentNode = argsNode.getFirstChild();
    String argumentName = argumentNode.getString();

    declareVariable(functionScope, argumentName, argumentNode, null);

    Node functionBodyNode = bodyNode;

    // Test redeclared global variable
    Node variableNode = nodeFactory.createName(globalVariableName);
    Node variableDeclarationNode = nodeFactory.createVar("", variableNode);

    Node variableStatementNode = nodeFactory.createStatement(variableDeclarationNode);
    Node variableBlockNode = nodeFactory.createBlock(variableStatementNode);

    Scope variableBlockScope = scopeCreator.createScope(variableBlockNode, functionScope);

    assertEquals(ScopeType.BLOCK, variableBlockScope.getType());
    assertEquals(functionScope, variableBlockScope.getParent());

    String variableName = variableNode.getString();
    declareVariable(variableBlockScope, variableName, variableNode, variableBlockNode);

    Node variableDeclarationStatementNode = variableStatementNode;
    diagnosticExpected("JSC_VAR_MULTIPLY_DECLARED_ERROR", variableDeclarationStatementNode,
        rootScope, globalVariableName, functionName);
  }

  @Test
  public void testCreateFunctionWithRedeclaredLocalVariable() {
    Node functionNode = nodeFactory.createFunction("test", null, null, null);
    Node argsNode = functionNode.getFirstChild().getNext();
    Node bodyNode = argsNode.getNext();

    String localVariableName = "x";

    Scope functionScope = scopeCreator.createScope(functionNode, rootScope);

    assertEquals(ScopeType.FUNCTION, functionScope.getType());
    assertEquals(rootScope, functionScope.getParent());

    String functionName = functionNode.getFirstChild().getString();
    declareFunction(functionScope, functionName, functionNode);

    // Arguments
    Node argumentNode = argsNode.getFirstChild();
    String argumentName = argumentNode.getString();

    declareVariable(functionScope, argumentName, argumentNode, null);

    Node functionBodyNode = bodyNode;

    // Test redeclared local variable
    Node variableNode = nodeFactory.createName(localVariableName);
    Node variableDeclarationNode = nodeFactory.createVar("", variableNode);

    Node variableStatementNode = nodeFactory.createStatement(variableDeclarationNode);
    Node variableBlockNode = nodeFactory.createBlock(variableStatementNode);

    Scope variableBlockScope = scopeCreator.createScope(variableBlockNode, functionScope);

    assertEquals(ScopeType.BLOCK, variableBlockScope.getType());
    assertEquals(functionScope, variableBlockScope.getParent());

    String variableName = variableNode.getString();
    declareVariable(variableBlockScope, variableName, variableNode, variableBlockNode);

    Node variableDeclarationStatementNode = variableStatementNode;
    diagnosticExpected("JSC_VAR_MULTIPLY_DECLARED_ERROR", variableDeclarationStatementNode,
        rootScope, localVariableName, functionName);
  }

  private void declareFunction(Scope scope, String name, Node node) {
    Preconditions.checkArgument(scope.getVar(name) == null, "Function %s already declared", name);

    JSDocInfo functionJSDocInfo = node.getJSDocInfo();
    JSType functionType = functionJSDocInfo != null ? functionJSDocInfo.getFunctionType() : null;

    scope.declare(name, node, functionType, null);
  }

  private void declareVariable(Scope scope, String name, Node node, Node parent) {
    Preconditions.checkArgument(scope.getVar(name) == null, "Variable %s already declared", name);

    JSType variableType = null;
    JSDocInfo variableJSDocInfo = node.getJSDocInfo();
    if (variableJSDocInfo != null) {
      variableType = variableJSDocInfo.getVariableType();
    }

    scope.declare(name, node, variableType, parent);
  }

  private void diagnosticExpected(DiagnosticType diagnosticType, Node node, Scope rootScope,
      String variableName) {
    DiagnosticContext diagnosticContext = new DiagnosticContext(compiler, rootScope, node);
    diagnosticContext.report(diagnosticType);
  }

  private void diagnosticExpected(DiagnosticType diagnosticType, Node node, Scope rootScope,
      String variableName, String functionName) {
    DiagnosticContext diagnosticContext = new DiagnosticContext(compiler, rootScope, node);
    diagnosticContext.report(diagnosticType, variableName, functionName);
  }
}