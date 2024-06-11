import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

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
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.IDENT, new int[]{}, new Node[]{}, "foo");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, "foo");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(
        Token.SCRIPT, new int[]{}, new Node[]{}, null);
    Node functionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, "foo");
    Node hoistedFunctionDeclaration = Node.addChild(scriptNode, functionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node functionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node hoistedFunctionDeclarationInFunction = Node.addChild(
        Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, "bar"),
        functionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = null;
    NodeUtil.isStatement(node);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node node = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    NodeUtil.isStatement(node);
  }
}