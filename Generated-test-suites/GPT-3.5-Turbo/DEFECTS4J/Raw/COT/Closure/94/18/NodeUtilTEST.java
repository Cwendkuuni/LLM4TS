import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test when the node is a FOR-IN loop
    Node n1 = new Node(Token.FOR);
    n1.addChildToBack(new Node(Token.EMPTY));
    n1.addChildToBack(new Node(Token.EMPTY));
    n1.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n1));

    // Test when the node is not a FOR-IN loop
    Node n2 = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    // Test when the node is used as a statement
    Node n1 = new Node(Token.BLOCK);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(n1);
    assertTrue(NodeUtil.isStatement(n1));

    // Test when the node is not used as a statement
    Node n2 = new Node(Token.EMPTY);
    Node parent2 = new Node(Token.SCRIPT);
    parent2.addChildToBack(n2);
    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test when the node is a function declaration
    Node n1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(n1);
    assertTrue(NodeUtil.isFunctionDeclaration(n1));

    // Test when the node is not a function declaration
    Node n2 = new Node(Token.EMPTY);
    Node parent2 = new Node(Token.SCRIPT);
    parent2.addChildToBack(n2);
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test when the node is a hoisted function declaration
    Node n1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(n1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));

    // Test when the node is not a hoisted function declaration
    Node n2 = new Node(Token.EMPTY);
    Node parent2 = new Node(Token.SCRIPT);
    parent2.addChildToBack(n2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
  }
}