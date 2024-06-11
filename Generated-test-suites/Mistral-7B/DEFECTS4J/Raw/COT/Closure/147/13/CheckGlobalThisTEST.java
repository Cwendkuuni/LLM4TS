import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testShouldTraverseFunctionsWithNoJsDocInfo() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    when(node.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    assertTrue(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithJsDocConstructor() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    when(node.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    assertFalse(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithJsDocThis() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    when(jsDoc.hasThisType()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    when(node.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    assertFalse(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithJsDocInterface() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    when(jsDoc.isInterface()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    when(node.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    assertFalse(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithoutParentOrGrandparent() {
    Node node = Mockito.mock(Node.class);

    when(node.getType()).thenReturn(Token.FUNCTION);

    assertFalse(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), node, null));
  }

  @Test
  public void testShouldTraverseAssignments() {
    Node assignNode = Mockito.mock(Node.class);
    Node assignLeftHandSide = Mockito.mock(Node.class);
    Node assignRightHandSide = Mockito.mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignLeftHandSide.getType()).thenReturn(Token.NAME);
    when(assignRightHandSide.getType()).thenReturn(Token.THIS);

    Node parent = Mockito.mock(Node.class);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    Node grandParent = Mockito.mock(Node.class);
    when(grandParent.getType()).thenReturn(Token.SCRIPT);
    when(assignNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(grandParent);

    assertTrue(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), assignRightHandSide, assignNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentsToPrototypeProperties() {
    Node assignNode = Mockito.mock(Node.class);
    Node assignLeftHandSide = Mockito.mock(Node.class);
    Node assignRightHandSide = Mockito.mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignLeftHandSide.getType()).thenReturn(Token.GETPROP);
    when(assignLeftHandSide.getLastChild().getString()).thenReturn("prototype");

    Node parent = Mockito.mock(Node.class);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    Node grandParent = Mockito.mock(Node.class);
    when(grandParent.getType()).thenReturn(Token.SCRIPT);
    when(assignNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(grandParent);

    assertFalse(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), assignRightHandSide, assignNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentsToPrototypeSubProperties() {
    Node assignNode = Mockito.mock(Node.class);
    Node assignLeftHandSide = Mockito.mock(Node.class);
    Node assignRightHandSide = Mockito.mock(Node.class);
    Node prototypeNode = Mockito.mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignLeftHandSide.getType()).thenReturn(Token.GETPROP);
    when(assignLeftHandSide.getFirstChild()).thenReturn(prototypeNode);
    when(prototypeNode.getLastChild().getString()).thenReturn("prototype");

    Node parent = Mockito.mock(Node.class);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    Node grandParent = Mockito.mock(Node.class);
    when(grandParent.getType()).thenReturn(Token.SCRIPT);
    when(assignNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(grandParent);

    assertFalse(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), assignRightHandSide, assignNode));
  }

  @Test
  public void testShouldTraverseLeftHandSideOfAssignment() {
    Node assignNode = Mockito.mock(Node.class);
    Node assignLeftHandSide = Mockito.mock(Node.class);
    Node assignRightHandSide = Mockito.mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignLeftHandSide.getType()).thenReturn(Token.NAME);

    Node parent = Mockito.mock(Node.class);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    Node grandParent = Mockito.mock(Node.class);
    when(grandParent.getType()).thenReturn(Token.SCRIPT);
    when(assignNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(grandParent);

    Node lhs = Mockito.mock(Node.class);
    when(assignLeftHandSide.getFirstChild()).thenReturn(lhs);

    Node rhs = Mockito.mock(Node.class);
    when(assignRightHandSide.getType()).thenReturn(Token.THIS);

    when(lhs.getType()).thenReturn(Token.NAME);

    assertTrue(checker.shouldTraverse(Mockito.mock(NodeTraversal.class), rhs, assignNode));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    when(node.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    when(shouldReportThis(node, parent)).thenReturn(true);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    checker.visit(traversal, node, parent);

    verify(compiler, times(1)).report(eq(traversal.makeError(node, WARNING, GLOBAL_THIS)));
  }

  @Test
  public void testShouldNotReportGlobalThisOnLeftHandSideOfAssignment() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    when(node.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    when(shouldReportThis(node, parent)).thenReturn(false);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    checker.visit(traversal, node, parent);

    verify(compiler, never()).report(any());
  }

  @Test
  public void testGetFunctionJsDocInfoReturnsJsDocInfo() {
    Node node = Mockito.mock(Node.class);

    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(false);
    when(jsDoc.isInterface()).thenReturn(false);
    when(jsDoc.hasThisType()).thenReturn(false);
    when(jsDoc.isOverride()).thenReturn(false);

    when(node.getJSDocInfo()).thenReturn(jsDoc);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    JSDocInfo result = checker.getFunctionJsDocInfo(node);

    assertEquals(jsDoc, result);
  }
}