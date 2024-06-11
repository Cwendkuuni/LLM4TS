import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(n);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
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
    n.setParent(parent2);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertTrue(result);
  }
}