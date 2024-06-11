
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    Node node2 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isForIn(node1));
    assertTrue(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK, new Node(Token.EMPTY));
    Node node3 = new Node(Token.LABEL, new Node(Token.EMPTY), new Node(Token.EMPTY));
    Node node4 = new Node(Token.EMPTY);
    
    assertTrue(NodeUtil.isStatement(node1));
    assertTrue(NodeUtil.isStatement(node2));
    assertTrue(NodeUtil.isStatement(node3));
    assertFalse(NodeUtil.isStatement(node4));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node node3 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
    Node node4 = new Node(Token.EMPTY);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node1));
    assertTrue(NodeUtil.isFunctionDeclaration(node2));
    assertTrue(NodeUtil.isFunctionDeclaration(node3));
    assertFalse(NodeUtil.isFunctionDeclaration(node4));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node node3 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
    Node node4 = new Node(Token.EMPTY);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }
  
}
