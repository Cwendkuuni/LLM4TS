package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.util.DiagnosticType;
import com.google.javascript.rhino.JSDocInfoImpl;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;

  @Test
  public void testCreateScope() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Scope rootScope = scopeCreator.createScope(Node.createTree("script"), null);
    assertThat(rootScope).isNotNull();
  }

  @Test
  public void testFunctionDeclaration() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Node functionNode = Node.createTree(
        Token.FUNCTION,
        Node.createTree(Token.NAME, "myFunction"),
        Node.createTree(Token.LP, null),
        Node.createTree(Token.RP, null),
        Node.createTree(Token.LCURLY, null));

    Node rootNode = Node.createTree(Token.SCRIPT, null);

    Scope functionScope = scopeCreator.createScope(functionNode, scopeCreator.createScope(rootNode, null));

    Scope.Var functionVar = functionScope.getVar("myFunction");
    assertNull(functionVar);

    Scope parentScope = functionScope.getParent();
    Scope.Var functionDeclaredInParent = parentScope.getVar("myFunction");
    assertThat(functionDeclaredInParent).isNotNull();
    assertThat(functionDeclaredInParent.getDeclaringNode()).isEqualTo(functionNode);
  }

  @Test
  public void testVariableDeclaration() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Node variableNode = Node.createTree(Token.VAR, null);
    Node nameNode = Node.createTree(Token.NAME, "myVariable");

    Node rootNode = Node.createTree(Token.SCRIPT, null);
    Scope rootScope = scopeCreator.createScope(rootNode, null);

    Node parentNode = Node.createTree(Token.NAME, "someFunction");
    Node parentScopeNode = Node.createTree(Token.FUNCTION, parentNode, Node.createTree(Token.LP, null), Node.createTree(Token.RP, null), Node.createTree(Token.LCURLY, null));
    Scope parentScope = scopeCreator.createScope(parentScopeNode, rootScope);

    Node functionNode = Node.createTree(Token.FUNCTION, null);
    Scope functionScope = scopeCreator.createScope(functionNode, parentScope);

    Node variableFunctionScope = scopeCreator.createScope(variableNode, functionScope);

    Scope.Var variable = variableFunctionScope.getVar("myVariable");
    assertNull(variable);

    Scope parentVariableScope = variableFunctionScope.getParent();
    Scope.Var variableDeclaredInParent = parentVariableScope.getVar("myVariable");
    assertThat(variableDeclaredInParent).isNull();

    Scope grandparentVariableScope = parentVariableScope.getParent();
    Scope.Var variableDeclaredInGrandparent = grandparentVariableScope.getVar("myVariable");
    assertThat(variableDeclaredInGrandparent).isNull();

    Scope.Var variableDeclaredInParentFunction = parentScope.getVar("myVariable");
    assertThat(variableDeclaredInParentFunction).isNull();

    Scope.Var variableDeclaredInRoot = rootScope.getVar("myVariable");
    assertThat(variableDeclaredInRoot).isNull();
  }

  @Test
  public void testVariableDeclarationWithType() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Node variableNode = Node.createTree(
        Token.VAR,
        Node.createTree(Token.NAME, "myVariable"),
        Node.createTree(Token.ASSIGN, null, Node.createTree(Token.NUMBER, 42), null));

    Node rootNode = Node.createTree(Token.SCRIPT, null);

    Scope rootScope = scopeCreator.createScope(rootNode, null);

    Node parentNode = Node.createTree(Token.NAME, "someFunction");
    Node parentScopeNode = Node.createTree(Token.FUNCTION, parentNode, Node.createTree(Token.LP, null), Node.createTree(Token.RP, null), Node.createTree(Token.LCURLY, null));
    Scope parentScope = scopeCreator.createScope(parentScopeNode, rootScope);

    Node functionNode = Node.createTree(Token.FUNCTION, null);
    Scope functionScope = scopeCreator.createScope(functionNode, parentScope);

    Node variableFunctionScope = scopeCreator.createScope(variableNode, functionScope);

    Scope.Var variable = variableFunctionScope.getVar("myVariable");
    assertThat(variable).isNotNull();
    JSType expectedType = JSType.NUMBER;
    JSType actualType = variable.getType();
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testVariableDeclarationWithTypeAnnotation() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Node variableNode = Node.createTree(
        Token.VAR,
        Node.createTree(Token.NAME, "myVariable"),
        Node.createTree(Token.ASSIGN, null, Node.createTree(Token.NUMBER, 42), null),
        Node.createTree(Token.AS, null, Node.createTree(Token.NAME, "Number"), null));

    Node rootNode = Node.createTree(Token.SCRIPT, null);

    Scope rootScope = scopeCreator.createScope(rootNode, null);

    Node parentNode = Node.createTree(Token.NAME, "someFunction");
    Node parentScopeNode = Node.createTree(Token.FUNCTION, parentNode, Node.createTree(Token.LP, null), Node.createTree(Token.RP, null), Node.createTree(Token.LCURLY, null));
    Scope parentScope = scopeCreator.createScope(parentScopeNode, rootScope);

    Node functionNode = Node.createTree(Token.FUNCTION, null);
    Scope functionScope = scopeCreator.createScope(functionNode, parentScope);

    Node variableFunctionScope = scopeCreator.createScope(variableNode, functionScope);

    Scope.Var variable = variableFunctionScope.getVar("myVariable");
    assertThat(variable).isNotNull();
    JSType expectedType = JSType.NUMBER;
    JSType actualType = variable.getType();
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testVariableDeclarationWithJSDoc() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Node variableNode = Node.createTree(
        Token.VAR,
        Node.createTree(Token.NAME, "myVariable"),
        Node.createTree(Token.ASSIGN, null, Node.createTree(Token.NUMBER, 42), null),
        Node.createTree(Token.AS, null, Node.createTree(Token.NAME, "Number"), null),
        Node.createTree(Token.NAME, "myVariable"),
        Node.createTree(Token.EQUAL, null, Node.createTree(Token.NAME, "myVariable"), null));

    Node jsdocInfoNode = Node.createTree(
      Token.COMMENT,
      "/**\n" +
      " * @type {Number}\n" +
      " */",
      Node.createTree(Token.LP, null),
      Node.createTree(Token.RP, null),
      Node.createTree(Token.RP, null));

    Node rootNode = Node.createTree(Token.SCRIPT, null);

    Scope rootScope = scopeCreator.createScope(rootNode, null);

    Node parentNode = Node.createTree(Token.NAME, "someFunction");
    Node parentScopeNode = Node.createTree(Token.FUNCTION, parentNode, Node.createTree(Token.LP, null), Node.createTree(Token.RP, null), Node.createTree(Token.LCURLY, null));
    Scope parentScope = scopeCreator.createScope(parentScopeNode, rootScope);

    Node functionNode = Node.createTree(Token.FUNCTION, null);
    Scope functionScope = scopeCreator.createScope(functionNode, parentScope);

    Node variableFunctionScope = scopeCreator.createScope(variableNode, functionScope);

    Scope.Var variable = variableFunctionScope.getVar("myVariable");
    assertThat(variable).isNotNull();
    JSType expectedType = JSType.NUMBER;
    JSType actualType = variable.getType();
    assertThat(actualType).isEqualTo(expectedType);

    JSDocInfo actualJSDocInfo = variable.getJSDocInfo();
    JSDocInfo expectedJSDocInfo = new JSDocInfoImpl();
    expectedJSDocInfo.setType("Number");
    assertThat(actualJSDocInfo).isEqualTo(expectedJSDocInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullParentScope() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Node rootNode = Node.createTree(Token.SCRIPT, null);
    scopeCreator.createScope(Node.createTree(Token.LP, null), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullChildNode() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);

    Node parentNode = Node.createTree(Token.FUNCTION, null);
    scopeCreator.createScope(null, parentNode);
  }
}