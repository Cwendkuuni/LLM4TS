import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test case 1
    Node node1 = new Node(Token.FOR);
    node1.addChildToFront(new Node(Token.NAME));
    node1.addChildToFront(new Node(Token.NAME));
    node1.addChildToFront(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node1));
    
    // Test case 2
    Node node2 = new Node(Token.NAME);
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    // Test case 1
    Node node1 = new Node(Token.SCRIPT);
    Node node1Child = new Node(Token.NAME);
    node1.addChildToFront(node1Child);
    assertTrue(NodeUtil.isStatement(node1Child));
    
    // Test case 2
    Node node2 = new Node(Token.EXPR_RESULT);
    Node node2Child1 = new Node(Token.NAME);
    node2.addChildToFront(node2Child1);
    assertTrue(NodeUtil.isStatement(node2Child1));
    
    // Test case 3
    Node node3 = new Node(Token.ADD);
    Node node3Child1 = new Node(Token.NAME);
    node3.addChildToFront(node3Child1);
    assertFalse(NodeUtil.isStatement(node3Child1));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test case 1
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    
    // Test case 2
    Node node2 = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test case 1
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));
    Node node1Parent = new Node(Token.SCRIPT);
    node1Parent.addChildToFront(node1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    
    // Test case 2
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.NAME));
    Node node2Parent = new Node(Token.BLOCK);
    node2Parent.addChildToFront(node2);
    Node node2Grandparent = new Node(Token.FUNCTION);
    node2Grandparent.addChildToFront(node2Parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
    
    // Test case 3
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToFront(new Node(Token.NAME));
    Node node3Parent = new Node(Token.NAME);
    node3Parent.addChildToFront(node3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}