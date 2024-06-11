
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, null);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
    
    Node node2 = new Node(Token.FOR, null);
    node2.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT, null);
    Node child = new Node(Token.EMPTY);
    child.setParent(node);
    assertTrue(NodeUtil.isStatement(child));
    
    Node node2 = new Node(Token.EMPTY, null);
    assertFalse(NodeUtil.isStatement(node2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, null);
    Node parent = new Node(Token.SCRIPT, null);
    Node grandparent = new Node(Token.SCRIPT, null);
    node.setParent(parent);
    parent.setParent(grandparent);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FUNCTION, null);
    Node parent2 = new Node(Token.EMPTY, null);
    node2.setParent(parent2);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, null);
    Node parent = new Node(Token.SCRIPT, null);
    Node grandparent = new Node(Token.SCRIPT, null);
    node.setParent(parent);
    parent.setParent(grandparent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FUNCTION, null);
    Node parent2 = new Node(Token.EMPTY, null);
    node2.setParent(parent2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    
    Node node3 = new Node(Token.SCRIPT, null);
    Node parent3 = new Node(Token.SCRIPT, null);
    Node grandparent3 = new Node(Token.SCRIPT, null);
    node3.setParent(parent3);
    parent3.setParent(grandparent3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
  
}
