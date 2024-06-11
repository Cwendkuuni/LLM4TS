import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // typical use case
    Node node1 = new Node(Token.FOR);
    Node node2 = new Node(Token.NUMBER);
    Node node3 = new Node(Token.NAME);
    node1.addChildToBack(node2);
    node1.addChildToBack(node3);
    
    boolean result1 = NodeUtil.isForIn(node1);
    Assert.assertTrue(result1);
    
    // edge case: node type is not FOR
    Node node4 = new Node(Token.SWITCH);
    Node node5 = new Node(Token.NUMBER);
    Node node6 = new Node(Token.NAME);
    node4.addChildToBack(node5);
    node4.addChildToBack(node6);
    
    boolean result2 = NodeUtil.isForIn(node4);
    Assert.assertFalse(result2);
    
    // edge case: not enough child nodes
    Node node7 = new Node(Token.FOR);
    Node node8 = new Node(Token.NUMBER);
    node7.addChildToBack(node8);
    
    boolean result3 = NodeUtil.isForIn(node7);
    Assert.assertFalse(result3);
  }

  @Test
  public void testIsStatement() {
    // typical use case: parent node is a SCRIPT
    Node parent1 = new Node(Token.SCRIPT);
    Node node1 = new Node(Token.NAME);
    parent1.addChildToBack(node1);
    
    boolean result1 = NodeUtil.isStatement(node1);
    Assert.assertTrue(result1);
    
    // typical use case: parent node is a BLOCK
    Node parent2 = new Node(Token.BLOCK);
    Node node2 = new Node(Token.NAME);
    parent2.addChildToBack(node2);
    
    boolean result2 = NodeUtil.isStatement(node2);
    Assert.assertTrue(result2);
    
    // typical use case: parent node is a LABEL
    Node parent3 = new Node(Token.LABEL);
    Node node3 = new Node(Token.NAME);
    parent3.addChildToBack(node3);
    
    boolean result3 = NodeUtil.isStatement(node3);
    Assert.assertTrue(result3);
    
    // edge case: parent node is not a SCRIPT, BLOCK, or LABEL
    Node parent4 = new Node(Token.NAME);
    Node node4 = new Node(Token.NAME);
    parent4.addChildToBack(node4);
    
    boolean result4 = NodeUtil.isStatement(node4);
    Assert.assertFalse(result4);
  }

  @Test
  public void testIsFunctionDeclaration() {
    // typical use case: node type is FUNCTION and is a statement
    Node parent1 = new Node(Token.SCRIPT);
    Node node1 = new Node(Token.FUNCTION);
    parent1.addChildToBack(node1);
    
    boolean result1 = NodeUtil.isFunctionDeclaration(node1);
    Assert.assertTrue(result1);
    
    // edge case: node type is FUNCTION but not a statement
    Node parent2 = new Node(Token.NAME);
    Node node2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(node2);
    
    boolean result2 = NodeUtil.isFunctionDeclaration(node2);
    Assert.assertFalse(result2);
    
    // edge case: node type is not FUNCTION but is a statement
    Node parent3 = new Node(Token.SCRIPT);
    Node node3 = new Node(Token.NAME);
    parent3.addChildToBack(node3);
    
    boolean result3 = NodeUtil.isFunctionDeclaration(node3);
    Assert.assertFalse(result3);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // typical use case: node type is FUNCTION, is a statement, parent type is SCRIPT
    Node parent1 = new Node(Token.SCRIPT);
    Node node1 = new Node(Token.FUNCTION);
    parent1.addChildToBack(node1);
    
    boolean result1 = NodeUtil.isHoistedFunctionDeclaration(node1);
    Assert.assertTrue(result1);
    
    // typical use case: node type is FUNCTION, is a statement, grandparent type is FUNCTION
    Node grandparent2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.NAME);
    grandparent2.addChildToBack(parent2);
    Node node2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(node2);
    
    boolean result2 = NodeUtil.isHoistedFunctionDeclaration(node2);
    Assert.assertTrue(result2);
    
    // edge case: node type is FUNCTION but not a statement
    Node parent3 = new Node(Token.NAME);
    Node node3 = new Node(Token.FUNCTION);
    parent3.addChildToBack(node3);
    
    boolean result3 = NodeUtil.isHoistedFunctionDeclaration(node3);
    Assert.assertFalse(result3);
    
    // edge case: node type is FUNCTION, is a statement, parent type is not SCRIPT or FUNCTION
    Node parent4 = new Node(Token.NAME);
    Node node4 = new Node(Token.FUNCTION);
    parent4.addChildToBack(node4);
    
    boolean result4 = NodeUtil.isHoistedFunctionDeclaration(node4);
    Assert.assertFalse(result4);
  }
}