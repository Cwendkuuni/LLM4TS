import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.ASSIGN), new Node(Token.TRUE), new Node(Token.NULL));
    assertTrue(NodeUtil.isForIn(node));
  
    Node node2 = new Node(Token.IF, new Node(Token.ASSIGN), new Node(Token.TRUE), new Node(Token.NULL));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.BLOCK, new Node(Token.TRUE), new Node(Token.FALSE));
    assertTrue(NodeUtil.isStatement(node));
  
    Node node2 = new Node(Token.IF, new Node(Token.ASSIGN), new Node(Token.TRUE), new Node(Token.NULL));
    assertFalse(NodeUtil.isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.ASSIGN), new Node(Token.TRUE), new Node(Token.FALSE));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  
    Node node2 = new Node(Token.IF, new Node(Token.ASSIGN), new Node(Token.TRUE), new Node(Token.NULL));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.ASSIGN), new Node(Token.TRUE), new Node(Token.FALSE));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  
    Node node2 = new Node(Token.IF, new Node(Token.ASSIGN), new Node(Token.TRUE), new Node(Token.NULL));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }

}