import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  /**
   * Test for isForIn method.
   */
  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR, null, createNode(Token.IDENT, "for"), createNode(Token.KEYWORD, "in"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null));
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.IF, null, createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  /**
   * Test for isStatement method.
   */
  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.IDENT, "statement");
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode(Token.ADD, null, createNode(Token.NUM, 1), createNode(Token.NUM, 2));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  /**
   * Test for isFunctionDeclaration method with valid nodes.
   */
  @Test
  public void testIsFunctionDeclarationWithValidNodes() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "functionName"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null), null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  /**
   * Test for isFunctionDeclaration method with an invalid node.
   */
  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithInvalidNode() {
    Node nodeInvalid = createNode(Token.IDENT, "invalidNode");
    NodeUtil.isFunctionDeclaration(nodeInvalid);
  }

  /**
   * Test for isHoistedFunctionDeclaration method with valid nodes.
   */
  @Test
  public void testIsHoistedFunctionDeclarationWithValidNodes() {
    Node nodeHoistedFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "hoistedFunction"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null), null);
    Node nodeScript = createNode(Token.SCRIPT, null, null);
    Node nodeFunction = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "function"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null), nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction.getChildren()[0]));
  }

  /**
   * Test for isHoistedFunctionDeclaration method with invalid nodes.
   */
  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithInvalidNodes() {
    Node nodeInvalid = createNode(Token.IDENT, "invalidNode");
    NodeUtil.isHoistedFunctionDeclaration(nodeInvalid);
  }
}