import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(node));
    
    Node node2 = new Node(Token.FOR);
    node2.addChildToFront(new Node(Token.NAME));
    node2.addChildToFront(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node childNode = new Node(Token.NAME);
    node.addChildToFront(childNode);
    
    assertTrue(NodeUtil.isStatement(childNode));
    
    Node node2 = new Node(Token.IF);
    node2.addChildToFront(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isStatement(node2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.NAME));
    Node parent2 = new Node(Token.BLOCK);
    parent2.addChildToFront(node2);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.NAME));
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToFront(node2);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }

}