import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    Node forNodeWithThreeChildren = Node.newNode(Token.FOR, new Node[]{Node.newNode(Token.IDENT, "forVar"), Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENT, "array"), Node.newNode(Token.CALL, Node.newNode(Token.IDENT, "Object"), Node.keys(Node.newNode(Token.IDENT, "arr")))), Node.newNode(Token.IDENT, "condition")});
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.NUMBER, 5));
    Node expressionNode = Node.newNode(Token.ADD, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.NUMBER, 3));
    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new Node[]{Node.newNode(Token.IDENT, "myFunction")}, Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = Node.newNode(Token.FUNCTION, new Node[]{Node.newNode(Token.IDENT, "myFunction")}, Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    Node scriptNode = Node.newNode(Token.SCRIPT, null, Node.newNode(hoistedFunctionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
  }

  // Add tests for edge cases and exceptions here
}