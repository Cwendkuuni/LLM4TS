import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.FUNCTION);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.STRING);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.SCRIPT));
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.SCRIPT));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
}