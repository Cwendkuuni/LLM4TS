import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node1));
    
    // Edge case: empty node
    Node node2 = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(node2));
    
    // Error scenario: incorrect node type
    Node node3 = new Node(Token.WHILE);
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node1));
    
    // Edge case: node with parent of type Label
    Node node2 = new Node(Token.EMPTY);
    node2.setParent(new Node(Token.LABEL));
    assertTrue(NodeUtil.isStatement(node2));
    
    // Error scenario: node without parent
    Node node3 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isStatement(node3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    
    // Edge case: function node with a parent of type SCRIPT
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isFunctionDeclaration(node2));
    
    // Error scenario: node without a parent
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: function node with a parent of type SCRIPT
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    
    // Edge case: function node with a parent of type FUNCTION
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.getParent().setType(Token.FUNCTION);
    node2.getParent().getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
    
    // Error scenario: node without a parent
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}