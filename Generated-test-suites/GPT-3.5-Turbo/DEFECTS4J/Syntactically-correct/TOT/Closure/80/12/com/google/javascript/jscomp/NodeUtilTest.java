import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    // Typical use case - node is a FOR loop with 3 children
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_border() {
    // Border case - node is a FOR loop with 2 children
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_edge() {
    // Edge case - node is not a FOR loop
    Node node = new Node(Token.WHILE);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_typical() {
    // Typical use case - node has a parent of type SCRIPT
    Node node = new Node();
    node.setParent(new Node(Token.SCRIPT));
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_border() {
    // Border case - node has a parent of type BLOCK
    Node node = new Node();
    node.setParent(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_edge() {
    // Edge case - node has a parent of type LABEL
    Node node = new Node();
    node.setParent(new Node(Token.LABEL));
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_false() {
    // Node has a parent that is not of type SCRIPT, BLOCK, or LABEL
    Node node = new Node();
    node.setParent(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_typical() {
    // Typical use case - node is of type FUNCTION and is a statement
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_false1() {
    // Node is not of type FUNCTION
    Node node = new Node();
    node.setParent(new Node(Token.SCRIPT));
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_false2() {
    // Node is not a statement
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.BLOCK));
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    // Typical use case - node is a function declaration in the root scope
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false1() {
    // Node is not a function declaration
    Node node = new Node();
    node.setParent(new Node(Token.SCRIPT));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false2() {
    // Node is a function declaration but not in the root scope
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(new Node(Token.SCRIPT));
    
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}