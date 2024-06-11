import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node();
    Node child2 = new Node();
    Node child3 = new Node();
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    
    assertEquals(true, NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node();
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    
    assertEquals(true, NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    
    assertEquals(true, NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    node.setParent(parent);
    parent.setParent(grandparent);
    
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
}