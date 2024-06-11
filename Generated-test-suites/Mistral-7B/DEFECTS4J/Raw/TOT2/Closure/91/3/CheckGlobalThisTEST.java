package com.example;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal nodeTraversal;
  private Node functionNode;
  private Node assignNode;
  private Node prototypeNode;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    nodeTraversal = mock(NodeTraversal.class);
    functionNode = new Node();
    assignNode = new Node();
    prototypeNode = new Node();
  }

  @Test
  public void testCheckGlobalThisConstructor() {
    assertNotNull(checkGlobalThis);
  }

  @Test
  public void testShouldTraverse_FunctionNode() {
    when(nodeTraversal.getCurrentNode()).thenReturn(functionNode);
    when(nodeTraversal.getParentNode()).thenReturn(null);

    checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

    verify(nodeTraversal).traverse(functionNode);
  }

  @Test
  public void testShouldTraverse_PrototypeMethod() {
    when(nodeTraversal.getCurrentNode()).thenReturn(functionNode);
    when(nodeTraversal.getParentNode()).thenReturn(prototypeNode);

    checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, prototypeNode);

    verify(nodeTraversal, never()).traverse(functionNode);
  }

  @Test
  public void testShouldTraverse_FunctionAnnotatedWithConstructor() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

    verify(nodeTraversal, never()).traverse(functionNode);
  }

  @Test
  public void testShouldTraverse_FunctionAnnotatedWithThis() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setHasThisType(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

    verify(nodeTraversal, never()).traverse(functionNode);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceToPutAThisAnnotation() {
    when(functionNode.getParentNode()).thenReturn(new Node());

    checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

    verify(nodeTraversal).traverse(functionNode);
  }

  @Test
  public void testShouldTraverse_FunctionGettingLentToPrototype() {
    when(nodeTraversal.getCurrentNode()).thenReturn(functionNode);
    when(nodeTraversal.getParentNode()).thenReturn(prototypeNode);

    checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, prototypeNode);

    verify(nodeTraversal, never()).traverse(functionNode);
  }

  @Test
  public void testShouldTraverse_AssignmentNodeLeftSide() {
    when(nodeTraversal.getCurrentNode()).thenReturn(assignNode);
    when(nodeTraversal.getParentNode()).thenReturn(null);

    Node lhs = new Node();
    Node rhs = new Node();
    Node assignParent = new Node();

    assignNode.setFirstChild(lhs);
    assignNode.setNext(rhs);
    assignParent.setFirstChild(assignNode);

    checkGlobalThis.assignLhsChild = lhs;

    checkGlobalThis.shouldTraverse(nodeTraversal, lhs, assignParent);

    verify(nodeTraversal).traverse(lhs);
  }

  @Test
  public void testShouldTraverse_AssignmentNodeRightSide() {
    when(nodeTraversal.getCurrentNode()).thenReturn(assignNode);
    when(nodeTraversal.getParentNode()).thenReturn(null);

    Node lhs = new Node();
    Node rhs = new Node();
    Node assignParent = new Node();

    assignNode.setFirstChild(lhs);
    assignNode.setNext(rhs);
    assignParent.setFirstChild(assignNode);

    checkGlobalThis.shouldTraverse(nodeTraversal, rhs, assignParent);

    verify(nodeTraversal, never()).traverse(rhs);
  }

  @Test
  public void testVisit_ThisKeywordOnLeftSideOfAssignment() {
    when(nodeTraversal.getCurrentNode()).thenReturn(new Node(Token.THIS));
    when(nodeTraversal.getParentNode()).thenReturn(assignNode);

    checkGlobalThis.visit(nodeTraversal, new Node(Token.THIS), assignNode);

    verify(compiler).report(argThat(diagnostic ->
        diagnostic.getMessage().contains("dangerous use of the global 'this' object")),
        eq(CheckLevel.WARNING), eq(GLOBAL_THIS));
  }

  @Test
  public void testVisit_ThisKeywordOnRightSideOfAssignment() {
    when(nodeTraversal.getCurrentNode()).thenReturn(new Node(Token.THIS));
    when(nodeTraversal.getParentNode()).thenReturn(assignNode);

    checkGlobalThis.visit(nodeTraversal, assignNode, null);

    verify(compiler, never()).report(any(), eq(CheckLevel.WARNING), eq(GLOBAL_THIS));
  }

  @Test
  public void testVisit_ThisKeywordOnPropertyAccess() {
    when(nodeTraversal.getCurrentNode()).thenReturn(new Node(Token.THIS));
    when(nodeTraversal.getParentNode()).thenReturn(new Node(Token.GETPROP, new Node(Token.STRING, "prototype")));

    checkGlobalThis.visit(nodeTraversal, new Node(Token.THIS), null);

    verify(compiler).report(argThat(diagnostic ->
        diagnostic.getMessage().contains("dangerous use of the global 'this' object")),
        eq(CheckLevel.WARNING), eq(GLOBAL_THIS));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    JSDocInfo jsDoc = new JSDocInfo();

    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    CheckGlobalThis.JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_NoJSDocInfo() {
    when(functionNode.getJSDocInfo()).thenReturn(null);

    CheckGlobalThis.JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    assertNull(result);
  }
}
