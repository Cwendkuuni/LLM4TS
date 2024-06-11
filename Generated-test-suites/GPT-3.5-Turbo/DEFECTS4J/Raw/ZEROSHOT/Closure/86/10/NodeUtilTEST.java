import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    Node child3 = new Node(Token.NAME);
    node.addChildToFront(child1);
    node.addChildAfter(child2, child1);
    node.addChildAfter(child3, child2);
    
    boolean result = NodeUtil.isForIn(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_notForIn() {
    Node node = new Node(Token.DEFAULT_VALUE);
    Node child = new Node(Token.NAME);
    node.addChildToFront(child);
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_script() {
    Node node = new Node(Token.SCRIPT);
    Node child = new Node(Token.NAME);
    node.addChildToFront(child);
    child.addChildToBack(new Node(Token.STATEMENT));
    
    boolean result = NodeUtil.isStatement(child);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_block() {
    Node node = new Node(Token.BLOCK);
    Node child = new Node(Token.NAME);
    node.addChildToFront(child);
    child.addChildToBack(new Node(Token.STATEMENT));
    
    boolean result = NodeUtil.isStatement(child);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_label() {
    Node node = new Node(Token.LABEL);
    Node child = new Node(Token.NAME);
    node.addChildToFront(child);
    child.addChildToBack(new Node(Token.STATEMENT));
    
    boolean result = NodeUtil.isStatement(child);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_other() {
    Node parent = new Node(Token.IF);
    Node child = new Node(Token.NAME);
    parent.addChildToFront(child);
    
    boolean result = NodeUtil.isStatement(child);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_functionStatement() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_functionExpression() {
    Node node = new Node(Token.FUNCTION_NAME);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_rootScope() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notRootScope() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.SCRIPT);
    parent2.addChildToFront(parent1);
    parent1.addChildToFront(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoisted() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.IF);
    Node parent2 = new Node(Token.SCRIPT);
    parent2.addChildToFront(parent1);
    parent1.addChildToFront(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(result);
  }
}