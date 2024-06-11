import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());
    
    assertTrue(NodeUtil.isForIn(n));
    
    n.removeChild(2);
    
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node parentNode = new Node(Token.SCRIPT);
    Node n = new Node();
    parentNode.addChildToBack(n);
    
    assertTrue(NodeUtil.isStatement(n));
    
    parentNode = new Node(Token.FUNCTION);
    n = new Node();
    parentNode.addChildToBack(n);
    
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    n = new Node(Token.VAR);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToBack(n);
    grandParent.addChildToBack(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    grandParent.removeChild(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}