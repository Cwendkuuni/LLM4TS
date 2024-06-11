package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SpyCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private ScopeCreator scopeCreator;

  @Before
  public void setUp() {
    compiler = new SpyCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope() {
    Scope parent = new Scope(null, null);
    Scope scope = scopeCreator.createScope(createNode(Token.LP), parent);

    assertThat(scope.getParent()).isEqualTo(parent);
    assertThat(scope.getVariables()).isEmpty();
  }

  @Test
  public void testCreateScopeWithFunction() {
    Scope parent = new Scope(null, null);
    String functionName = "myFunction";
    Node functionNode = createNode(Token.FUNCTION, functionName);
    Scope scope = scopeCreator.createScope(functionNode, parent);

    assertThat(scope.getParent()).isEqualTo(parent);
    assertThat(scope.getVariables()).isEmpty();
    assertThat(scope.getName()).isEqualTo(functionName);
  }

  @Test
  public void testCreateScopeWithFunctionAndArguments() {
    Scope parent = new Scope(null, null);
    String functionName = "myFunction";
    Node functionNode = createNode(Token.FUNCTION, functionName);
    Node argumentsNode = createNode(Token.LP);
    Node bodyNode = createNode(Token.RP);

    scopeCreator.createScope(functionNode, parent);
    Scope scope = scopeCreator.createScope(functionNode, argumentsNode, bodyNode, parent);

    assertThat(scope.getParent()).isEqualTo(parent);
    assertThat(scope.getName()).isEqualTo(functionName + "(...args)");

    Scope argumentsScope = scope.getChildScope();
    assertThat(argumentsScope.getParent()).isEqualTo(scope);
    assertThat(argumentsScope.getName()).isEqualTo("arguments");
    assertThat(argumentsScope.getVariables()).isEmpty();
  }

  @Test
  public void testCreateGlobalScope() {
    String sourceName = "global.js";
    Scope parent = null;
    Scope scope = scopeCreator.createScope(createNode(Token.SCRIPT, sourceName), parent);

    assertThat(scope.getParent()).isNull();
    assertThat(scope.getName()).isEqualTo(sourceName);
  }

  @Test
  public void testDeclareVariable() {
    String name = "foo";
    Node node = createNode(Token.NAME, name);
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    scope.declare(name, node, null, null);

    Scope.Var variable = scope.getVar(name);
    assertThat(variable).isNotNull();
    assertThat(variable.getNode()).isSameInstanceAs(node);
  }

  @Test
  public void testDeclareVariableWithType() {
    String name = "foo";
    String type = "number";
    Node node = createNode(Token.NAME, name);
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    scope.declare(name, node, JSType.forName(type), null);

    Scope.Var variable = scope.getVar(name);
    assertThat(variable).isNotNull();
    assertThat(variable.getType()).isEqualTo(JSType.forName(type));
  }

  @Test
  public void testDeclareVariableWithDuplicateName() {
    String name = "foo";
    Node node = createNode(Token.NAME, name);
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);
    scope.declare(name, node, null, null);

    RedeclarationHandler handler = new DefaultRedeclarationHandler();
    scopeCreator = new SyntacticScopeCreator(compiler, handler);
    Scope newScope = scopeCreator.createScope(node, scope);

    assertThat(newScope.getParent()).isEqualTo(scope);
    assertNull(newScope.getVar(name));
    assertThat(handler.wasCalled()).isTrue();
  }

  @Test
  public void testDeclareVariableWithDuplicateNameInParentScope() {
    String name = "foo";
    Node node = createNode(Token.NAME, name);
    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, null);
    childScope.declare(name, node, null, null);

    RedeclarationHandler handler = new DefaultRedeclarationHandler();
    scopeCreator = new SyntacticScopeCreator(compiler, handler);
    Scope newScope = scopeCreator.createScope(node, null);

    assertThat(newScope.getParent()).isNull();
    assertNull(newScope.getVar(name));
    assertThat(handler.wasCalled()).isTrue();
  }

  @Test
  public void testDeclareVariableWithDuplicateNameInGlobalScope() {
    String name = "foo";
    Node node = createNode(Token.NAME, name);
    Scope globalScope = new Scope(null, null);
    globalScope.declare(name, node, null, null);

    RedeclarationHandler handler = new DefaultRedeclarationHandler();
    scopeCreator = new SyntacticScopeCreator(compiler, handler);
    Scope newScope = scopeCreator.createScope(node, null);

    assertThat(newScope.getParent()).isNull();
    assertNull(newScope.getVar(name));
    assertThat(handler.wasCalled()).isTrue();
  }

  @Test
  public void testDeclareFunction() {
    String name = "myFunction";
    Node functionNode = createNode(Token.FUNCTION, name);
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    scope.declare(name, functionNode, null, null);

    Scope.Var variable = scope.getVar(name);
    assertThat(variable).isNotNull();
    assertThat(variable.getType()).isInstanceOf(FunctionType.class);
  }

  @Test
  public void testDeclareCatchVariable() {
    String name = "exception";
    Node catchNode = createNode(Token.CATCH);
    Node variableNode = createNode(Token.NAME, name);
    Node bodyNode = createNode(Token.RP);
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    scope.declare(name, variableNode, null, null);

    Scope.Var variable = scope.getVar(name);
    assertThat(variable).isNotNull();
    assertThat(variable.getNode()).isSameInstanceAs(variableNode);

    Scope catchScope = scope.getChildScope();
    assertThat(catchScope.getParent()).isEqualTo(scope);
    assertThat(catchScope.getName()).isEqualTo("catch (" + name + ")");

    Node functionNode = createNode(Token.FUNCTION, "myFunction");
    Node argumentsNode = createNode(Token.LP);
    Node bodyNode = createNode(Token.RP);
    Scope functionScope = new Scope(null, null);
    Scope functionParentScope = new Scope(parentScope, null);

    scopeCreator.createScope(functionNode, argumentsNode, bodyNode, functionParentScope);

    Scope catchFunctionScope = functionScope.getChildScope();
    assertThat(catchFunctionScope.getParent()).isEqualTo(functionScope);
    assertThat(catchFunctionScope.getName()).isEqualTo("catch (" + name + ")");
  }

  @Test
  public void testDuplicateDeclarationWithSuppressDuplicateWarning() {
    String name = "foo";
    Node node = createNode(Token.NAME, name);
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    JSDocInfo docInfo = createJSDocInfo("@suppress {duplicate}");
    Scope newScope = scopeCreator.createScope(node, scope, docInfo);

    Scope.Var variable = newScope.getVar(name);
    assertThat(variable).isNotNull();
  }

  @Test
  public void testDuplicateDeclarationWithArguments() {
    String name = "arguments";
    Node node = createNode(Token.NAME, name);
    Scope parent = new Scope(null, null);

    RedeclarationHandler handler = new DefaultRedeclarationHandler();
    scopeCreator = new SyntacticScopeCreator(compiler, handler);
    Scope newScope = scopeCreator.createScope(node, null, null);

    assertNull(newScope.getVar(name));
    assertThat(handler.wasCalled()).isTrue();
  }

  private Node createNode(int type, String... children) {
    Node node = new Node();
    node.setType(type);
    node.setLineNumber(1);
    node.setColumnNumber(1);

    if (children != null) {
      for (String child : children) {
        Node childNode = createNode(Token.NAME, child);
        node.addChild(childNode);
      }
    }

    return node;
  }

  private Node createNode(int type, String name, Node... children) {
    Node node = new Node();
    node.setType(type);
    node.setName(name);
    node.setLineNumber(1);
    node.setColumnNumber(1);

    if (children != null) {
      for (Node child : children) {
        node.addChild(child);
      }
    }

    return node;
  }

  private JSDocInfo createJSDocInfo(String... suppressions) {
    JSDocInfo docInfo = new JSDocInfo();
    docInfo.setSuppressions(suppressions);
    return docInfo;
  }
}

class FunctionType extends JSType {
}

class SpyCompiler implements AbstractCompiler {
  @Override
  public Input getInput(String name) {
    return new Input("", name);
  }
}

class DefaultRedeclarationHandler implements RedeclarationHandler {
  private boolean wasCalled = false;

  @Override
  public void onRedeclaration(
      Scope s, String name,
      Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
    wasCalled = true;
  }

  public boolean wasCalled() {
    return wasCalled;
  }
}