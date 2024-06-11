import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;

import org.junit.Test;

/**
 * Test class for NodeUtil class.
 */
public class NodeUtilTest {

  /**
   * Test for isForIn method.
   */
  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {0});

    assertTrue("isForIn should return true for valid FOR-IN statement",
        NodeUtil.isForIn(nodeForIn));
    assertFalse("isForIn should return false for invalid node", NodeUtil.isForIn(nodeNotForIn));
  }

  /**
   * Test for isStatement method.
   */
  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {0, 1, 2});
    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2});

    assertTrue("isStatement should return true for valid statement", NodeUtil.isStatement(nodeStatement));
    assertFalse("isStatement should return false for invalid expression", NodeUtil.isStatement(nodeExpression));
  }

  /**
   * Test for isFunctionDeclaration method.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {0, 1, 2}, "myFunction");
    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, new int[] {0, 1, 2}, null);

    assertTrue("isFunctionDeclaration should return true for valid function declaration",
        NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse("isFunctionDeclaration should return false for invalid function expression",
        NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  /**
   * Test for isHoistedFunctionDeclaration method.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {0, 1, 2}, "myFunction", Node.newNode(Token.SCRIPT, new int[0]));
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {0, 1, 2}, "myFunction", Node.newNode(Token.BLOCK, new int[0]));

    assertTrue("isHoistedFunctionDeclaration should return true for hoisted function declaration",
        NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse("isHoistedFunctionDeclaration should return false for non-hoisted function declaration",
        NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  /**
   * Test for isFunctionDeclaration with null parent node.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {0, 1, 2}, "myFunction");
    NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration);
  }

  /**
   * Test for isStatement with null parent node.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {0, 1, 2});
    NodeUtil.isStatement(nodeStatement);
  }
}