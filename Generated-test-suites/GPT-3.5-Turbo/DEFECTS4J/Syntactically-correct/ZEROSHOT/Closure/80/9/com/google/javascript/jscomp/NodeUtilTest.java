import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test when node represents a FOR-IN loop
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));

    // Test when node does not represent a FOR-IN loop
    node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    // Test when node is used as a statement
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isStatement(node));

    // Test when node is not used as a statement
    node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.EXPR_RESULT));
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test when node is a function declaration
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    // Test when node is not a function declaration
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.EXPR_RESULT));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test when node is a hoisted function declaration
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    // Test when node is not a hoisted function declaration
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}