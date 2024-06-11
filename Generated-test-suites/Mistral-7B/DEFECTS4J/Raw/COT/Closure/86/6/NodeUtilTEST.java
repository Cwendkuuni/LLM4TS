import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.PRINT, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = createNode(Token.IDENT, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    Node nodeNull = null;
    NodeUtil.isStatement(nodeNull);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, new int[] {1, 2, 3, 4});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(Token.FUNCTION, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nodeNull = null;
    NodeUtil.isFunctionDeclaration(nodeNull);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationAtRoot = createNode(Token.SCRIPT, new int[] {1, 2});
    Node functionDeclarationNode = createNode(Token.FUNCTION, new int[] {1, 2, 3, 4});
    NodeUtil.appendChild(nodeFunctionDeclarationAtRoot, functionDeclarationNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    Node nodeFunctionDeclarationInsideFunction = createNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node functionDeclarationNodeInsideFunction = createNode(Token.FUNCTION, new int[] {1, 2, 3, 4});
    NodeUtil.appendChild(nodeFunctionDeclarationNode, functionDeclarationNodeInsideFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNodeInsideFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node nodeNull = null;
    NodeUtil.isHoistedFunctionDeclaration(nodeNull);
  }
}