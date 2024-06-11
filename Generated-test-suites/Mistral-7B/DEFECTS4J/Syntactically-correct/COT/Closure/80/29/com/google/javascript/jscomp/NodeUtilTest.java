import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{}, new Object[]{}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{}, new Object[]{}, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new Object[]{}, null);
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{}, new Object[]{}, null);
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{}, new Object[]{}, null);
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{}, new Object[]{}, null);
    Node nodeExpression = new Node(Token.ADD, new int[]{}, new int[]{}, new Object[]{}, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE},
        new int[]{Token.IDENT, Token.LEFT_BRACE},
        new Object[]{null, null},
        null
    );
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(
        Token.FUNCTION,
        new int[]{Token.LEFT_PAREN},
        new int[]{Token.RIGHT_PAREN, Token.LEFT_BRACE},
        new Object[]{null, null},
        null
    );
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE},
        new int[]{Token.SCRIPT},
        new Object[]{null, null},
        null
    );
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

    Node nodeFunctionInFunction = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE},
        new int[]{Token.FUNCTION},
        new Object[]{null, null},
        null
    );
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @org.junit.Test
  public void testParentOfStatementNodeIsNotNull() {
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{}, new Object[]{}, null);
    Node parent = nodeStatement.getParent();
    assertNotNull(parent);
  }

  @org.junit.Test
  public void testParentOfExpressionNodeIsNotNull() {
    Node nodeExpression = new Node(Token.ADD, new int[]{}, new int[]{}, new Object[]{}, null);
    Node parent = nodeExpression.getParent();
    assertNotNull(parent);
  }

  @org.junit.Test
  public void testParentOfNodeWithNoParentIsNull() {
    Node node = new Node(Token.IDENT, new int[]{}, new int[]{}, new Object[]{}, null);
    Node parent = node.getParent();
    assertNull(parent);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testGetParentOfNullNodeThrowsIllegalStateException() {
    Node node = null;
    node.getParent();
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNodeThrowsIllegalArgumentException() {
    Node node = null;
    NodeUtil.isStatement(node);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNodeThrowsIllegalArgumentException() {
    Node node = null;
    NodeUtil.isFunctionDeclaration(node);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNodeThrowsIllegalArgumentException() {
    Node node = null;
    NodeUtil.isHoistedFunctionDeclaration(node);
  }
}