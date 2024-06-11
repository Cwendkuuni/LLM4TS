import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
    
    Node node2 = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT, new Node(Token.FUNCTION), new Node(Token.VAR));
    Node node = new Node(Token.EMPTY, parent);
    assertTrue(NodeUtil.isStatement(node));
    
    Node node2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isStatement(node2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT, new Node(Token.FUNCTION));
    Node node = new Node(Token.FUNCTION, parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }
}