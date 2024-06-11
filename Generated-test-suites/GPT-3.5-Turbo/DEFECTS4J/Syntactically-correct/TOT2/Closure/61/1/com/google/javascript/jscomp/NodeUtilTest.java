import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  
    Node node2 = new Node(Token.IF, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION, parent);
    assertTrue(NodeUtil.isStatement(node));
  
    Node parent2 = new Node(Token.EXPR_RESULT);
    Node node2 = new Node(Token.FUNCTION, parent2);
    assertFalse(NodeUtil.isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  
    Node node2 = new Node(Token.VAR, new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION, parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  
    Node parent2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node node2 = new Node(Token.FUNCTION, parent2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }
}