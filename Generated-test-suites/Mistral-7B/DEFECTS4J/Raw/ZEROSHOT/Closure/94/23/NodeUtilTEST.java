import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

/**
 * Test class for NodeUtil.
 */
public class NodeUtilTest {

  /**
   * Test method for NodeUtil.isForIn(Node).
   */
  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, null, null, null, null, null, 0);
    Node nodeForWithThreeChildren = new Node(Token.FOR, null, new Node[] { new Node(Token.IDENT, "i"), new Node(Token.ASSIGN, new Node(Token.IDENT, "i"), new Node(Token.NUMBER, 0)), new Node(Token.SEMICOLON, null, null, null, null, null, 0) }, null, null, null, 0);
    Node nodeNotFor = new Node(Token.IF, null, null, null, null, null, 0);

    assertTrue(NodeUtil.isForIn(nodeForWithThreeChildren));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeNotFor));
  }

  /**
   * Test method for NodeUtil.isStatement(Node).
   */
  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null, null, null, null, null, 0);
    Node nodeExpression = new Node(Token.IDENT, "i", null, null, null, null, 0);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  /**
   * Test method for NodeUtil.isFunctionDeclaration(Node).
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, new Node[] { new Node(Token.IDENT, "myFunction") }, null, null, null, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, new Node[] { new Node(Token.IDENT, "myFunction"), new Node(Token.ASSIGN, null, new Node[] { nodeFunctionExpression, new Node(Token.NUMBER, 0) }) }, null, null, null, 0);
    Node nodeIfStatement = new Node(Token.IF, null, null, null, null, null, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIfStatement));
  }

  /**
   * Test method for NodeUtil.isHoistedFunctionDeclaration(Node).
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null, null, null, 0);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, new Node[] { new Node(Token.IDENT, "myFunction") }, null, null, null, 0);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, null, null, null, null, null, 0, new Node[] { nodeFunctionDeclaration }, null, null, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScript));
  }
}