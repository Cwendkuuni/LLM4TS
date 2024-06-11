package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = new Node(Token.FUNCTION);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("constructor");
    functionNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(true);
    functionNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotationOrPrototype() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = new Node(Token.FUNCTION);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypePropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeAccess = new Node(Token.GETPROP);
    prototypeAccess.setQualifiedName("prototype");
    functionNode.addChildToEnd(prototypeAccess);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignNode = new Node(Token.ASSIGN);
    Node thisNode = new Node(Token.THIS);
    assignNode.addChildToEnd(thisNode);
    Node lhs = assignNode.getFirstChild();

    assertTrue(checker.shouldTraverse(traversal, thisNode, lhs));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThisInParent() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node constructorNode = new Node(Token.FUNCTION, "constructor");
    constructorNode.setJSDocInfo(new JSDocInfo("constructor", true));
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.NAME, "Parent");
    parentNode.setJSDocInfo(new JSDocInfo("constructor", true));
    parentNode.addChildToEnd(constructorNode);
    parentNode.addChildToEnd(functionNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisInPrototype() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeNode = new Node(Token.GETPROP);
    prototypeNode.setQualifiedName("prototype");
    prototypeNode.addChildToEnd(new Node(Token.THIS));
    functionNode.addChildToEnd(prototypeNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testReportGlobalThisError() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node thisNode = new Node(Token.THIS);

    checker.visit(traversal, thisNode, null);

    Node errorNode = traversal.getCurrentError();
    assertEquals(DiagnosticType.WARNING, errorNode.getDiagnosticType());
    assertEquals("JSC_USED_GLOBAL_THIS", errorNode.getMessage());
  }
}