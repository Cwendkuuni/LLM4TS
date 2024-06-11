import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.SCRIPT);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getString()).thenReturn("constructor");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getString()).thenReturn("this");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getString()).thenReturn("override");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionNotAllowedTypeParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.NUMBER);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignNode = mock(Node.class);
    Node thisNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(thisNode);
    when(thisNode.getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(traversal, null, assignNode);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getNode = mock(Node.class);
    Node thisNode = mock(Node.class);

    when(getNode.getType()).thenReturn(Token.GETPROP);
    when(getNode.getLastChild()).thenReturn(thisNode);
    when(thisNode.getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(traversal, null, getNode);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignNode = mock(Node.class);
    Node thisNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getLastChild()).thenReturn(thisNode);
    when(thisNode.getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(traversal, null, assignNode);
    assertFalse(checkGlobalThis.shouldReportThis(thisNode, assignNode));
  }

  @Test
  public void testShouldNotReportThisOnGetProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    Node thisNode = mock(Node.class);

    when(getNode.getType()).thenReturn(Token.GETPROP);
    when(getNode.getLastChild()).thenReturn(thisNode);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getString()).thenReturn("prototype");

    checkGlobalThis.visit(traversal, null, getNode);
    assertFalse(checkGlobalThis.shouldReportThis(thisNode, getNode));
  }

  @Test
  public void testShouldNotReportThisOnGetPropertyWithPrototype() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    Node thisNode = mock(Node.class);

    when(getNode.getType()).thenReturn(Token.GETPROP);
    when(getNode.getLastChild()).thenReturn(thisNode);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getString()).thenReturn("prototype");
    when(thisNode.getType()).thenReturn(Token.GETPROP);
    when(thisNode.getLastChild().getString()).thenReturn("prototype");

    checkGlobalThis.visit(traversal, null, getNode);
    assertFalse(checkGlobalThis.shouldReportThis(thisNode, getNode));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    Node functionNode = mock(Node.class);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    assertFalse(jsDocInfo == null);
  }
}