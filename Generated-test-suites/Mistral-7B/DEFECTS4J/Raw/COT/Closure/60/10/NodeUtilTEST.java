import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.ASSIGN, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.IF, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, "funcName", Node.newList(), Node.newList(), null, null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, Node.newList(), null, null, null, null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, "funcName", Node.newList(), Node.newList(), null, null, null, null, Node.newScript(null));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, "funcName", Node.newList(), Node.newList(), null, null, null, null, Node.newFunction(null, null, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testNullParentInIsStatement() {
    Node nodeStatement = Node.newNode(Token.ASSIGN, null, null, null, null, null, null, null, null);
    Node nullParent = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null);
    Node nodeWithNullParent = Node.util.setParent(nodeStatement, nullParent);
    NodeUtil.isStatement(nodeWithNullParent);
  }

  @Test(expected = AssertionError.class)
  public void testNullNodeInIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "funcName", Node.newList(), Node.newList(), null, null, null, null, null);
    Node nullNode = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null);
    Node nodeWithNullNode = Node.util.setParent(nullNode, nodeFunctionDeclaration);
    NodeUtil.isFunctionDeclaration(nodeWithNullNode);
  }

  @Test(expected = AssertionError.class)
  public void testNullNodeInIsFunctionExpression() {
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, Node.newList(), null, null, null, null, null, null);
    Node nullNode = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null);
    Node nodeWithNullNode = Node.util.setParent(nullNode, nodeFunctionExpression);
    NodeUtil.isFunctionExpression(nodeWithNullNode);
  }

  @Test
  public void testIsFunctionExpression() {
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, Node.newList(), null, null, null, null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }
}