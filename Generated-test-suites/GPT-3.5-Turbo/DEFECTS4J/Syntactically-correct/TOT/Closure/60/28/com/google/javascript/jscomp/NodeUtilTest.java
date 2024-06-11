import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_typicalCase() {
    // Typical case: FOR node with three children
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.LEFT));
    node.addChildToBack(new Node(Token.RIGHT));
    node.addChildToBack(new Node(Token.STATEMENT));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_incorrectNodeType() {
    // Incorrect node type: WHILE instead of FOR
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node(Token.LEFT));
    node.addChildToBack(new Node(Token.RIGHT));
    node.addChildToBack(new Node(Token.STATEMENT));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_incorrectChildrenCount() {
    // Incorrect children count: FOR node with two children instead of three
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.LEFT));
    node.addChildToBack(new Node(Token.RIGHT));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_parentIsScript() {
    // Parent node type is SCRIPT
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.STATEMENT);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_parentIsBlock() {
    // Parent node type is BLOCK
    Node parent = new Node(Token.BLOCK);
    Node node = new Node(Token.STATEMENT);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_parentIsLabel() {
    // Parent node type is LABEL
    Node parent = new Node(Token.LABEL);
    Node node = new Node(Token.STATEMENT);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_parentIsWhile() {
    // Parent node type is WHILE
    Node parent = new Node(Token.WHILE);
    Node node = new Node(Token.STATEMENT);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    // Typical case: FUNCTION node that is a statement
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.SCRIPT);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_notStatement() {
    // FUNCTION node that is not a statement
    Node parent = new Node(Token.EXPRESSION_STATEMENT);
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.EXPRESSION_STATEMENT);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    // Typical case: Function declaration in the root scope
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.SCRIPT);
    grandparent.setParent(parent);
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.SCRIPT);
    node.setParent(grandparent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    // FUNCTION node that is not a function declaration
    Node parent = new Node(Token.FOR);
    Node grandparent = new Node(Token.SCRIPT);
    grandparent.setParent(parent);
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.FOR);
    node.setParent(grandparent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notInRootScope() {
    // Function declaration in a non-root scope
    Node parent = new Node(Token.FUNCTION);
    Node grandparent = new Node(Token.FUNCTION);
    grandparent.setParent(parent);
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.FUNCTION);
    node.setParent(grandparent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}