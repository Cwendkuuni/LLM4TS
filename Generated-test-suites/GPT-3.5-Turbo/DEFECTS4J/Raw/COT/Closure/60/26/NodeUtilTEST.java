import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.VAR));
    node1.addChildToBack(new Node(Token.STRING));
    node1.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node1));
    
    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.VAR));
    node2.addChildToBack(new Node(Token.STRING));
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node child1 = new Node(Token.STRING);
    node1.addChildToBack(child1);
    assertTrue(NodeUtil.isStatement(child1));
    
    Node node2 = new Node(Token.IF);
    node2.addChildToBack(new Node(Token.STRING));
    assertFalse(NodeUtil.isStatement(node2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    assertFalse(NodeUtil.isFunctionDeclaration(node1));
    
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.BLOCK);
    parent2.addChildToBack(node2);
    assertTrue(NodeUtil.isFunctionDeclaration(node2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
    
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(node2);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
  }
  
}