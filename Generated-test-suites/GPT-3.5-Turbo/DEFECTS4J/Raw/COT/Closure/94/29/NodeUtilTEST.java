import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
    
    Node node2 = new Node(Token.FOR);
    node2.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node));
    
    Node node2 = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isStatement(node2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    Node node2 = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }
}