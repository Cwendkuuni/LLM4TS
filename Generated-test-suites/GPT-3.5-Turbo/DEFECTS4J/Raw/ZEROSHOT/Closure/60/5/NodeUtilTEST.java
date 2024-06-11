import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(n);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.addChildToBack(parent);
    
    boolean result = NodeUtil.isStatement(n);
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    n.addChildToBack(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToBack(parent2);
    parent2.addChildToBack(n);
    n.addChildToBack(parent2);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertTrue(result);
  }

}