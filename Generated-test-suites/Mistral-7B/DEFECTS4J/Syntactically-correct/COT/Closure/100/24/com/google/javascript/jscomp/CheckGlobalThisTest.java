package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.WarningLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Test the CheckGlobalThis class.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private NodeTraversal nodeTraversal;
  @Mock
  private Node node;
  @Mock
  private Node parentNode;
  @Mock
  private Node assignParentNode;
  @Mock
  private Node assignChildNode;
  @Mock
  private Node getPropNode;

  private Callback checkGlobalThis;

  @Before
  public void setup() {
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.isConstructor()).thenReturn(false);
    Mockito.when(parentNode.hasThisType()).thenReturn(false);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionConstructor() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.isConstructor()).thenReturn(true);
    Mockito.when(parentNode.hasThisType()).thenReturn(false);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionThisAnnotation() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.isConstructor()).thenReturn(false);
    Mockito.when(parentNode.hasThisType()).thenReturn(true);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoThisAnnotation() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parentNode);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);

    Node left = Mockito.mock(Node.class);
    Mockito.when(parentNode.getFirstChild()).thenReturn(left);
    Mockito.when(left.getType()).thenReturn(Token.THIS);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Mockito.when(node.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);

    Node left = Mockito.mock(Node.class);
    Node getProp = Mockito.mock(Node.class);
    Node prototype = Mockito.mock(Node.class);

    Mockito.when(parentNode.getFirstChild()).thenReturn(left);
    Mockito.when(left.getType()).thenReturn(Token.GETPROP);
    Mockito.when(left.getLastChild()).thenReturn(getProp);
    Mockito.when(getProp.getString()).thenReturn("prototype");
    Mockito.when(getProp.getFirstChild()).thenReturn(prototype);
    Mockito.when(prototype.getType()).thenReturn(Token.NAME);
    Mockito.when(prototype.getString()).thenReturn("prototype");

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    Mockito.when(node.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);

    Node left = Mockito.mock(Node.class);
    Node getProp = Mockito.mock(Node.class);
    Node prototype = Mockito.mock(Node.class);
    Node subProperty = Mockito.mock(Node.class);

    Mockito.when(parentNode.getFirstChild()).thenReturn(left);
    Mockito.when(left.getType()).thenReturn(Token.GETPROP);
    Mockito.when(left.getLastChild()).thenReturn(getProp);
    Mockito.when(getProp.getString()).thenReturn("prototype");
    Mockito.when(getProp.getFirstChild()).thenReturn(prototype);
    Mockito.when(prototype.getType()).thenReturn(Token.GETPROP);
    Mockito.when(prototype.getLastChild()).thenReturn(subProperty);
    Mockito.when(subProperty.getType()).thenReturn(Token.NAME);
    Mockito.when(subProperty.getString()).thenReturn("subProperty");

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);

    Node left = Mockito.mock(Node.class);
    Node thisNode = Mockito.mock(Node.class);

    Mockito.when(parentNode.getFirstChild()).thenReturn(left);
    Mockito.when(left.getType()).thenReturn(Token.THIS);
    Mockito.when(left.getNext()).thenReturn(thisNode);

    assertFalse(checkGlobalThis.shouldReportThis(node, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);

    Node left = Mockito.mock(Node.class);
    Node thisNode = Mockito.mock(Node.class);

    Mockito.when(parentNode.getFirstChild()).thenReturn(left);
    Mockito.when(left.getType()).thenReturn(Token.NAME);
    Mockito.when(left.getString()).thenReturn("assignLhsChild");
    Mockito.when(left.getNext()).thenReturn(thisNode);
    Mockito.when(thisNode.getType()).thenReturn(Token.THIS);

    assertTrue(checkGlobalThis.shouldReportThis(node, parentNode));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);

    Node left = Mockito.mock(Node.class);
    Node thisNode = Mockito.mock(Node.class);
    Node rhs = Mockito.mock(Node.class);

    Mockito.when(parentNode.getFirstChild()).thenReturn(left);
    Mockito.when(left.getType()).thenReturn(Token.NAME);
    Mockito.when(left.getString()).thenReturn("assignLhsChild");
    Mockito.when(left.getNext()).thenReturn(thisNode);
    Mockito.when(thisNode.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getLastChild()).thenReturn(rhs);

    assertFalse(checkGlobalThis.shouldReportThis(node, parentNode));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.VAR);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    assertTrue(jsDocInfo != null);
  }
}