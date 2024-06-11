import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    Node node2 = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    Node node2 = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isStatement(node1));
    assertFalse(NodeUtil.isStatement(node2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node node2 = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node node2 = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }
}