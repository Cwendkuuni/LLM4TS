package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SymbolTable;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {
  private static final String TEST_CODE =
      "function evil1() { this.x = 1; } "
      + "function evil2() { this.x = 1; } "
      + "function ok1() { var self = this; self.x = 1; } "
      + "function ok2() { this.constructor.prototype.x = 1; } "
      + "function ok3() { new (function() { this.x = 1; })(); } "
      + "function ok4() { var x = function() { this.x = 1; }; } ";

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private NodeFactory nodeFactory;
  @Mock
  private SymbolTable symbolTable;
  @Mock
  private NodeTraversal traversal;

  private Node rootNode;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    rootNode = parseCode(TEST_CODE, nodeFactory, symbolTable);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testVisit_evilFunction1() {
    Node evilNode = findFunctionNodeByName("evil1", rootNode);
    checker.visit(traversal, evilNode, null);
    Node errorNode = traversal.getCurrentError();
    assertThat(errorNode).isNotNull();
    DiagnosticPosition position = errorNode.getPosition();
    assertThat(position.getLineNumber()).isEqualTo(1);
    assertThat(position.getCharacterOffset()).isWithin(0, 3);
    assertThat(errorNode.getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testVisit_evilFunction2() {
    Node evilNode = findFunctionNodeByName("evil2", rootNode);
    checker.visit(traversal, evilNode, null);
    Node errorNode = traversal.getCurrentError();
    assertThat(errorNode).isNotNull();
    DiagnosticPosition position = errorNode.getPosition();
    assertThat(position.getLineNumber()).isEqualTo(2);
    assertThat(position.getCharacterOffset()).isWithin(0, 3);
    assertThat(errorNode.getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testVisit_okFunction1() {
    Node okNode = findFunctionNodeByName("ok1", rootNode);
    checker.visit(traversal, okNode, null);
    Node errorNode = traversal.getCurrentError();
    assertNull(errorNode);
  }

  @Test
  public void testVisit_okFunction2() {
    Node okNode = findFunctionNodeByName("ok2", rootNode);
    checker.visit(traversal, okNode, null);
    Node errorNode = traversal.getCurrentError();
    assertNull(errorNode);
  }

  @Test
  public void testVisit_okFunction3() {
    Node okNode = findFunctionNodeByName("ok3", rootNode);
    checker.visit(traversal, okNode, null);
    Node errorNode = traversal.getCurrentError();
    assertNull(errorNode);
  }

  @Test
  public void testVisit_okFunction4() {
    Node okNode = findFunctionNodeByName("ok4", rootNode);
    checker.visit(traversal, okNode, null);
    Node errorNode = traversal.getCurrentError();
    assertNull(errorNode);
  }

  @Test
  public void testShouldTraverse_functionAnnotatedWithConstructor() {
    Node constructorNode = findFunctionNodeByName("evil1", rootNode);
    JSDocInfo jsDoc = getFunctionJsDocInfo(constructorNode);
    when(constructorNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);

    Node traversedNode = new Node(Token.ASSIGN, null, null);
    Node parentNode = new Node(Token.ASSIGN, null, null);
    parentNode.setFirstChild(constructorNode);
    parentNode.setNext(traversedNode);

    checker.shouldTraverse(traversal, traversedNode, parentNode);
    assertFalse(traversal.getCurrentError() == null);
  }

  @Test
  public void testShouldTraverse_functionAnnotatedWithThis() {
    Node thisAnnotatedNode = new Node(Token.FUNCTION, null, null);
    JSDocInfo jsDoc = new JSDocInfo(new ImmutableList.Builder<JSDocMember>().add(new JSDocMember(JSDocMember.Tag.this_, ""))
        .build());
    when(thisAnnotatedNode.getJSDocInfo()).thenReturn(jsDoc);

    Node traversedNode = new Node(Token.ASSIGN, null, null);
    Node parentNode = new Node(Token.ASSIGN, null, null);
    parentNode.setFirstChild(thisAnnotatedNode);
    parentNode.setNext(traversedNode);

    checker.shouldTraverse(traversal, traversedNode, parentNode);
    assertNull(traversal.getCurrentError());
  }

  @Test
  public void testShouldTraverse_functionInAssignedValue() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node leftHandSideNode = new Node(Token.GETPROP, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null);

    leftHandSideNode.setFirstChild(new Node(Token.NAME, null, null, "a"));
    assignNode.setFirstChild(leftHandSideNode);
    assignNode.setNext(functionNode);

    Node parentNode = new Node(Token.ASSIGN, null, null);
    parentNode.setFirstChild(assignNode);

    checker.shouldTraverse(traversal, functionNode, parentNode);
    assertNull(traversal.getCurrentError());
  }

  @Test
  public void testShouldTraverse_functionWithPrototypePropertyAccess() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node leftHandSideNode = new Node(Token.GETPROP, null, null);
    Node prototypeNode = new Node(Token.GETPROP, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null);

    leftHandSideNode.setFirstChild(new Node(Token.NAME, null, null, "a"));
    prototypeNode.setFirstChild(new Node(Token.NAME, null, null, "prototype"));
    leftHandSideNode.setLastChild(prototypeNode);
    assignNode.setFirstChild(leftHandSideNode);
    assignNode.setNext(functionNode);

    Node parentNode = new Node(Token.ASSIGN, null, null);
    parentNode.setFirstChild(assignNode);

    checker.shouldTraverse(traversal, functionNode, parentNode);
    assertFalse(traversal.getCurrentError() == null);
  }

  @Test
  public void testShouldTraverse_functionWithPrototypeSubPropertyAccess() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node leftHandSideNode = new Node(Token.GETPROP, null, null);
    Node prototypeNode = new Node(Token.GETPROP, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null);

    leftHandSideNode.setFirstChild(new Node(Token.NAME, null, null, "a"));
    prototypeNode.setFirstChild(new Node(Token.NAME, null, null, "prototype"));
    prototypeNode.setLastChild(new Node(Token.NAME, null, null, "x"));
    leftHandSideNode.setLastChild(prototypeNode);
    assignNode.setFirstChild(leftHandSideNode);
    assignNode.setNext(functionNode);

    Node parentNode = new Node(Token.ASSIGN, null, null);
    parentNode.setFirstChild(assignNode);

    checker.shouldTraverse(traversal, functionNode, parentNode);
    assertFalse(traversal.getCurrentError() == null);
  }

  private Node findFunctionNodeByName(String name, Node root) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION && child.getString().equals(name)) {
        return child;
      }
      if (child.getType() == Token.BLOCK) {
        Node functionNode = findFunctionNodeByName(name, child);
        if (functionNode != null) {
          return functionNode;
        }
      }
    }
    return null;
  }

  private JSDocInfo getFunctionJsDocInfo(Node node) {
    return node.getJSDocInfo();
  }

  private Node parseCode(String code, NodeFactory nodeFactory, SymbolTable symbolTable) {
    RhinoJsCompiler compiler = new RhinoJsCompiler(code, nodeFactory, symbolTable);
    return compiler.parseTree();
  }
}