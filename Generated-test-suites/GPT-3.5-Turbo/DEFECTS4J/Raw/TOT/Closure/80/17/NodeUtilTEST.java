import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_true() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    node.addChildToBack(new Node(Token.NUMBER));
    node.addChildToBack(new Node(Token.TRUE));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.NUMBER));
    node.addChildToBack(new Node(Token.TRUE));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_true() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.IF);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.BLOCK));
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(new Node(Token.IF));
    parent1.addChildToBack(parent2);
    node.setParent(parent1);
    parent2.setParent(parent1);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.BLOCK));
    Node parent = new Node(Token.WHILE);
    parent.addChildToBack(new Node(Token.IF));
    node.setParent(parent);
    parent.setParent(new Node(Token.SCRIPT));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}