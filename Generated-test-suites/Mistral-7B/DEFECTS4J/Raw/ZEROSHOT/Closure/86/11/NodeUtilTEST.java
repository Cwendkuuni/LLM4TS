import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeStatement = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, Node.newIdentifier("foo"), null, null, null, null, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null, null, null, Node.newLiteral(Token.NULL), null, null);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(
        Token.SCRIPT, null, null, null, null, null, null, null, null, null, null, null, Node.newNode(
            Token.FUNCTION, Node.newIdentifier("foo"), null, null, null, null, null, null, null, null, null, null, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));

    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, Node.newIdentifier("foo"), null, null, null, null, null, null, null, null, null, null, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateFail() {
    Node node = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null, null, null, null, null);
    NodeUtil.isStatement(node);
    Preconditions.checkState(false);
  }

}