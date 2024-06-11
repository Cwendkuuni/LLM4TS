import com.google.javascript.rhino.Node;
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
    Node child = new Node(Token.EMPTY);
    node.addChildToBack(child);
    
    boolean result = NodeUtil.isStatement(child);
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node);
    
    boolean result1 = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result1);
    
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(node);
    
    boolean result2 = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result2);
  }

  @Test
  public void testIsForIn_Invalid() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);
  }

  @Test
  public void testIsStatement_Invalid() {
    Node node = new Node(Token.ADD);

    boolean result = NodeUtil.isStatement(node);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_Invalid() {
    Node node = new Node(Token.EMPTY);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_Invalid() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }
}