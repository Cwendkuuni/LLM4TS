import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForInTypical() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.FOR_NAME));
    node.addChildToFront(new Node(Token.IN));
    node.addChildToFront(new Node(Token.INIT));
    // Test that a node with type FOR and 3 children is recognized as a FOR-IN loop
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInBoundary() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.FOR_NAME));
    node.addChildToFront(new Node(Token.IN));
    // Test that a node with type FOR and 2 children is not recognized as a FOR-IN loop
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatementTypical() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.PARAM_LIST));
    node.addChildToFront(new Node(Token.BLOCK));
    // Test that a node with type FUNCTION and parent with type SCRIPT, BLOCK, or LABEL is recognized as a statement
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatementSpecial() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.PARAM_LIST));
    node.addChildToFront(new Node(Token.BLOCK));
    Node parent = new Node(Token.IF);
    parent.addChildToBack(node);
    // Test that a node with type FUNCTION and parent with type IF is not recognized as a statement
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclarationTypical() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.PARAM_LIST));
    node.addChildToFront(new Node(Token.BLOCK));
    // Test that a node with type FUNCTION and parent recognized as a statement is recognized as a function declaration
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationSpecial() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.PARAM_LIST));
    node.addChildToFront(new Node(Token.BLOCK));
    Node parent = new Node(Token.IF);
    parent.addChildToBack(node);
    // Test that a node with type FUNCTION and parent not recognized as a statement is not recognized as a function declaration
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationTypical() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.PARAM_LIST));
    node.addChildToFront(new Node(Token.BLOCK));
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node);
    // Test that a node with type FUNCTION and parent as SCRIPT is recognized as a hoisted function declaration
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(new Node(Token.BLOCK));
    parent2.addChildToBack(node);
    // Test that a node with type FUNCTION and grandparent as FUNCTION is recognized as a hoisted function declaration
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationSpecial() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.PARAM_LIST));
    node.addChildToFront(new Node(Token.BLOCK));
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.IF);
    grandparent.addChildToBack(node);
    parent.addChildToBack(grandparent);
    // Test that a node with type FUNCTION and parent as SCRIPT and grandparent as IF is not recognized as a hoisted function declaration
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}