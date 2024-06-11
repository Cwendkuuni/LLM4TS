import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    Node forNodeWithThreeChildren = Node.newNode(Token.FOR,
        Node.newNode(Token.IDENT, "i"),
        Node.newNode(Token.KEYWORD, "in"),
        Node.newNode(Token.IDENT, "arr"));

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT);
    Node statementNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.INT_LITERAL, 42));
    Node expressionNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.IDENT, "y"));

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(scriptNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "foo"), Node.newNode(Token.BLOCK, null));
    Node functionExpressionNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENT, "foo"), Node.newNode(Token.FUNCTION, null, Node.newNode(Token.BLOCK, null)));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT);
    Node globalFunctionDeclarationNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "foo"), Node.newNode(Token.BLOCK, null));
    Node functionDeclarationInFunctionNode = Node.newNode(
        Token.FUNCTION, Node.newNode(Token.IDENT, "bar"), Node.newNode(
            Node.newNode(Token.IDENT, "foo"), null
        ));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(globalFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }

  @Test(expected = AssertionError.class)
  public void testParentIsNull() {
    Node node = Node.newNode(Token.IDENT, "x");
    assertFalse(NodeUtil.isStatement(node));
    Preconditions.checkState(node.getParent() != null);
  }
}