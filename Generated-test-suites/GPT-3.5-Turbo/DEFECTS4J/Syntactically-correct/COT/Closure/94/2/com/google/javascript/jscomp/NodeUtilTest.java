import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.BLOCK);
    parent.addChildToFront(grandParent);
    grandParent.addChildToBack(n);
    n.setParent(grandParent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}