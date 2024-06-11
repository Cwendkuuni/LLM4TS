import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    
    boolean result = NodeUtil.isStatement(node);
    
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.BLOCK));
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));
    functionNode.addChildToBack(new Node(Token.BLOCK));
    scriptNode.addChildToBack(functionNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
    
    assertTrue(result);
  }
  
  // Test edge cases and exceptions
  
  @Test
  public void testIsForIn_NullNode() {
    boolean result = NodeUtil.isForIn(null);
    
    assertFalse(result);
  }

  @Test
  public void testIsStatement_NullNode() {
    boolean result = NodeUtil.isStatement(null);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_NullNode() {
    boolean result = NodeUtil.isFunctionDeclaration(null);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NullNode() {
    boolean result = NodeUtil.isHoistedFunctionDeclaration(null);
    
    assertFalse(result);
  }

}