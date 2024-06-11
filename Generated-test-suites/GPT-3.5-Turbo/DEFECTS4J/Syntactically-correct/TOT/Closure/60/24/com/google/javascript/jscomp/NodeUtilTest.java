import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    Node child3 = new Node(Token.NAME);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_edgeCase() {
    Node node = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_error() {
    Node node = null;
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_typical() {
    Node node = new Node(Token.SCRIPT);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_edgeCase() {
    Node node = new Node(Token.LABEL);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_error() {
    Node node = null;
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_edgeCase() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_error() {
    Node node = null;
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    node.setParent(parent1);
    parent1.setParent(parent2);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_edgeCase() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_error() {
    Node node = null;
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}