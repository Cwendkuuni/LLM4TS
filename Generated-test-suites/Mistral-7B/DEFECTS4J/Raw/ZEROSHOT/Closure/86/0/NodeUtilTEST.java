import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for NodeUtil.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newNode(Token.ADD, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, "myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, "myFunction");
    nodeFunctionDeclaration.setParent(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node nodeFunction = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, "myFunction");
    nodeFunctionDeclarationInFunction.setParent(nodeFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}