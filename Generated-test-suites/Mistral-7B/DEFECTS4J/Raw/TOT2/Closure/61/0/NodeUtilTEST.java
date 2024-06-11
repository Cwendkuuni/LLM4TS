import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn_Normal() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.FOR);
    when(node.getChildCount()).thenReturn(3);
    assertEquals(true, NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsForIn_False() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.IF);
    when(node.getChildCount()).thenReturn(2);
    assertEquals(false, NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement_Normal() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.IF);
    when(node.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    assertEquals(true, NodeUtil.isStatement(node));
  }

  @org.junit.Test
  public void testIsStatement_False() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.IF);
    when(node.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.EXPRESSION);
    assertEquals(false, NodeUtil.isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration_Normal() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    assertEquals(true, NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration_False() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.VAR);
    when(node.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    assertEquals(false, NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration_Normal() {
    Node node = mock(Node.class);
    Node functionNode = mock(Node.class);
    Node parentScript = mock(Node.class);
    Node parentFunction = mock(Node.class);

    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getParent()).thenReturn(parentFunction);
    when(parentFunction.getType()).thenReturn(Token.FUNCTION);
    when(parentFunction.getParent()).thenReturn(parentScript);
    when(parentScript.getType()).thenReturn(Token.SCRIPT);

    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = mock(Node.class);
    Node functionNode = mock(Node.class);
    Node parentScript = mock(Node.class);
    Node parentFunction = mock(Node.class);

    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getParent()).thenReturn(parentFunction);
    when(parentFunction.getType()).thenReturn(Token.BLOCK);
    when(parentFunction.getParent()).thenReturn(parentScript);
    when(parentScript.getType()).thenReturn(Token.SCRIPT);

    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsForIn_NullNode() {
    NodeUtil.isForIn(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsForIn_InvalidNodeType() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.VAR);
    NodeUtil.isForIn(node);
  }

  // Add similar test cases for other methods

  @org.junit.Test
  public void testIsStatement_NullNode() {
    NodeUtil.isStatement(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsStatement_InvalidNodeType() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.VAR);
    NodeUtil.isStatement(node);
  }

  @org.junit.Test
  public void testIsFunctionDeclaration_NullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclaration_InvalidNodeType() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.VAR);
    NodeUtil.isFunctionDeclaration(node);
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration_NullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclaration_InvalidNodeType() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.VAR);
    NodeUtil.isHoistedFunctionDeclaration(node);
  }
}