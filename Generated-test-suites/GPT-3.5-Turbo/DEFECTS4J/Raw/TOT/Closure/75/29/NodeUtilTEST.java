import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_notForLoop() {
    Node node = new Node(Token.WHILE);
    node.addChildToFront(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsForIn_incompleteForLoop() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_statementNode() {
    Node node = new Node(Token.SCRIPT);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_nonStatementNode() {
    Node node = new Node(Token.ADD);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    node.setStaticSourceFile(new StaticSourceFile("test"));
    node.getParent().setType(Token.BLOCK);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    node.setStaticSourceFile(new StaticSourceFile("test"));
    node.getParent().setType(Token.SCRIPT);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(result);
  }
}