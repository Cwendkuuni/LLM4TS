import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    Assert.assertTrue(NodeUtil.isForIn(node1));
    
    // Edge case - not a FOR loop
    Node node2 = new Node(Token.IF);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    Assert.assertFalse(NodeUtil.isForIn(node2));
    
    // Error scenario - missing child nodes
    Node node3 = new Node(Token.FOR);
    node3.addChildToBack(new Node(Token.EMPTY));
    Assert.assertFalse(NodeUtil.isForIn(node3));
  }
  
  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.SCRIPT);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    Assert.assertTrue(NodeUtil.isStatement(node1));
    
    // Edge case - parent is not SCRIPT, BLOCK, or LABEL
    Node node2 = new Node(Token.IF);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    Assert.assertFalse(NodeUtil.isStatement(node2));
    
    // Error scenario - null parent
    Node node3 = new Node(Token.SCRIPT);
    node3.addChildToBack(new Node(Token.EMPTY));
    NodeUtil.isStatement(node3);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(node1));
    
    // Edge case - not a statement
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    Node parent2 = new Node(Token.ADD);
    parent2.addChildToBack(node2);
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node2));
    
    // Error scenario - null parent
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    NodeUtil.isFunctionDeclaration(node3);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    
    // Edge case - parent is not SCRIPT or FUNCTION
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    Node parent2 = new Node(Token.ADD);
    parent2.addChildToBack(node2);
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    
    // Error scenario - null parent
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    NodeUtil.isHoistedFunctionDeclaration(node3);
  }
}