import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(node);
    assertEquals(false, result);
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node child = new Node(Token.EMPTY);
    node.addChildToBack(child);
    
    boolean result = NodeUtil.isStatement(child);
    assertEquals(true, result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandParent = new Node(Token.SCRIPT);
    grandParent.addChildToBack(parent);
    parent.addChildToBack(node);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertEquals(true, result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.SCRIPT);
    grandParent.addChildToBack(parent);
    parent.addChildToBack(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertEquals(true, result);
  }
}