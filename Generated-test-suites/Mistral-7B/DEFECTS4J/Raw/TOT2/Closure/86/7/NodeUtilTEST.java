import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.LEFT_PAREN, new Node[]{new Node(Token.IDENTIFIER, "array")}, null)}, null);

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "foo")}, null)));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.ASSIGN, new Node[]{new Node(Token.IDENTIFIER, "x"), new Node(Token.NUMBER, 5)}, null);
    Node expressionNode = new Node(Token.PLUS, new Node[]{new Node(Token.NUMBER, 3), new Node(Token.NUMBER, 4)}, null);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "foo")}, null);
    Node functionExpression = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "bar")}, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "foo")}, null);
    Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "bar")}, null);

    Node scriptNode = new Node(Token.SCRIPT, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, new Node[]{scriptNode});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }

  @Test(expected = NullPointerException.class)
  public void testIsForInWithNullNode() {
    Node nullNode = null;
    assertFalse(NodeUtil.isForIn(nullNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInWithInvalidNodeType() {
    Node invalidNode = new Node(Token.NUMBER, new Node[]{}, null);
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInWithInvalidNumberOfChildren() {
    Node invalidNode = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.LEFT_PAREN, null, null)}, null);
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  // Add more test cases for other edge cases and exceptions here
}