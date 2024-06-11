package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private ScopeCreator creator;
  private Scope rootScope;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    creator = new SyntacticScopeCreator(compiler);
    rootScope = creator.createScope(null, null);
  }

  @Test
  public void testCreateRootScope() {
    Scope scope = creator.createScope(null, null);
    assertThat(scope).isInstanceOf(Scope.class);
    Preconditions.checkState(scope.getParent() == null);
  }

  @Test
  public void testCreateFunctionScope() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Scope scope = creator.createScope(functionNode, null);
    assertThat(scope).isInstanceOf(Scope.class);
    assertThat(scope.getParent()).isNotNull();
    assertThat(scope.getName()).isEqualTo("test");
  }

  @Test
  public void testCreateBlockScope() {
    Node blockNode = Node.newBlockNode();
    Scope scope = creator.createScope(blockNode, null);
    assertThat(scope).isInstanceOf(Scope.class);
    assertThat(scope.getParent()).isNotNull();
  }

  @Test
  public void testCreateGlobalScope() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node blockNode = Node.newBlockNode();
    Scope scope = creator.createScope(blockNode, functionNode);
    assertThat(scope).isInstanceOf(Scope.class);
    Preconditions.checkState(scope.getParent() == null);
    assertThat(scope.getName()).isNull();
  }

  @Test
  public void testCreateFunctionVariable() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node varNode = Node.newNameNode("x");
    creator.declareVar("x", varNode, functionNode, null, null, functionNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("x");
    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == varNode);
  }

  @Test
  public void testCreateGlobalVariable() {
    Node blockNode = Node.newBlockNode();
    Node varNode = Node.newNameNode("x");
    creator.declareVar("x", varNode, null, null, null, blockNode);

    Scope scope = creator.createScope(blockNode, null);
    Scope.Var var = scope.getVar("x");
    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == varNode);
  }

  @Test
  public void testCreateVariableWithType() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node varNode = Node.newNameNode("x");
    JSType type = JSType.NUMBER;
    creator.declareVar("x", varNode, functionNode, null, type, functionNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("x");
    Preconditions.checkState(var != null);
    Preconditions.checkState(var.type == type);
  }

  @Test
  public void testCreateVariableWithoutType() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node varNode = Node.newNameNode("x");
    creator.declareVar("x", varNode, functionNode, null, null, functionNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("x");
    Preconditions.checkState(var != null);
    Preconditions.checkState(var.type == null);
  }

  @Test
  public void testCreateVariableWithDuplicateName() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node varNode1 = Node.newNameNode("x");
    Node varNode2 = Node.newNameNode("x");

    creator.declareVar("x", varNode1, functionNode, null, null, functionNode);
    creator.declareVar("x", varNode2, functionNode, null, null, functionNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var1 = scope.getVar("x");
    Scope.Var var2 = scope.getVar("x");

    RedeclarationHandler handler = creator.redeclarationHandler;
    Preconditions.checkState(handler instanceof DefaultRedeclarationHandler);
    DefaultRedeclarationHandler defaultHandler = (DefaultRedeclarationHandler) handler;
    Preconditions.checkState(defaultHandler.onCalled);
    Preconditions.checkState(defaultHandler.onCalledCount == 1);

    Preconditions.checkState(var1 != null);
    Preconditions.checkState(var2 != null);
    Preconditions.checkState(var1.node == varNode1);
    Preconditions.checkState(var2.node == varNode2);
  }

  @Test
  public void testCreateVariableWithDuplicateNameInGlobalScope() {
    Node blockNode1 = Node.newBlockNode();
    Node blockNode2 = Node.newBlockNode();
    Node varNode = Node.newNameNode("x");

    creator.declareVar("x", varNode, null, null, null, blockNode1);
    creator.declareVar("x", varNode, null, null, null, blockNode2);

    Scope scope = creator.createScope(blockNode2, null);
    Scope.Var var = scope.getVar("x");

    RedeclarationHandler handler = creator.redeclarationHandler;
    Preconditions.checkState(handler instanceof DefaultRedeclarationHandler);
    DefaultRedeclarationHandler defaultHandler = (DefaultRedeclarationHandler) handler;
    Preconditions.checkState(defaultHandler.onCalled);
    Preconditions.checkState(defaultHandler.onCalledCount == 1);

    Preconditions.checkState(var == null);

    compiler.reportCollector.clear();
    creator.createScope(blockNode1, rootScope);
    compiler.reportCollector.getErrors().forEach(e -> System.out.println(e.getMessage()));

    Preconditions.checkState(compiler.reportCollector.getErrors().size() > 0);
  }

  @Test
  public void testCreateVariableWithDuplicateNameInFunctionScope() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node varNode = Node.newNameNode("x");

    creator.declareVar("x", varNode, functionNode, null, null, functionNode);
    creator.declareVar("x", varNode, functionNode, null, null, functionNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("x");

    RedeclarationHandler handler = creator.redeclarationHandler;
    Preconditions.checkState(handler instanceof DefaultRedeclarationHandler);
    DefaultRedeclarationHandler defaultHandler = (DefaultRedeclarationHandler) handler;
    Preconditions.checkState(defaultHandler.onCalled);
    Preconditions.checkState(defaultHandler.onCalledCount == 1);

    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == varNode);
  }

  @Test
  public void testCreateCatchVariable() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node catchNode = Node.newNameNode("e");
    Node blockNode = Node.newBlockNode();

    creator.declareVar("e", catchNode, catchNode, functionNode, null, blockNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("e");

    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == catchNode);
  }

  @Test
  public void testCreateArrayDeclaration() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node arrayVarNode = Node.newNameNode("arr");
    Node arrayTypeNode = Node.newArrayTypeNode(JSType.NUMBER);
    Node arrayDeclarationNode = Node.newVarNode(arrayVarNode, null, null, null, null, arrayTypeNode, null, null);

    creator.declareVar("arr", arrayVarNode, functionNode, null, null, arrayDeclarationNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("arr");

    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == arrayVarNode);
    Preconditions.checkState(var.type.isArray());
  }

  @Test
  public void testCreateFunctionDeclaration() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node functionVarNode = Node.newNameNode("test");
    Node functionTypeNode = Node.newObjectTypeNode(Node.makeQualifiedName("Function"));
    Node functionDeclarationNode = Node.newVarNode(functionVarNode, null, null, null, null, functionTypeNode, null, null);

    creator.declareVar("test", functionVarNode, functionNode, null, null, functionDeclarationNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("test");

    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == functionVarNode);
    Preconditions.checkState(var.type.isFunction());
  }

  @Test
  public void testCreateParameterDeclaration() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node parameterNode = Node.newNameNode("p");

    creator.declareVar("p", parameterNode, functionNode, null, null, functionNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("p");

    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == parameterNode);
    Preconditions.checkState(var.isParameter());
  }

  @Test
  public void testCreateParameterDeclarationWithType() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node parameterNode = Node.newNameNode("p");
    JSType type = JSType.NUMBER;

    creator.declareVar("p", parameterNode, functionNode, null, type, functionNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("p");

    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == parameterNode);
    Preconditions.checkState(var.type == type);
  }

  @Test
  public void testCreateParameterDeclarationWithJSDoc() {
    Node functionNode = Node.newFunctionNode("test", null, null, null);
    Node parameterNode = Node.newNameNode("p");
    JSType type = JSType.NUMBER;

    JSDocInfo docInfo = new JSDocInfo(true, true, null, null, "Description");
    Node jsDocNode = Node.newJsDocNode(null, null, docInfo);

    creator.declareVar("p", parameterNode, functionNode, null, type, jsDocNode);

    Scope scope = creator.createScope(functionNode, null);
    Scope.Var var = scope.getVar("p");

    Preconditions.checkState(var != null);
    Preconditions.checkState(var.node == parameterNode);
    Preconditions.checkState(var.type == type);
    Preconditions.checkState(var.jsDocInfo != null);
  }
}

class DefaultRedeclarationHandler extends SyntacticScopeCreator.RedeclarationHandler {
  boolean onCalled = false;
  int onCalledCount = 0;

  @Override
  public void onRedeclaration(
      Scope s, String name,
      Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
    onCalled = true;
    onCalledCount++;
  }
}