import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test when node is a FOR-IN loop
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.NAME));
    node1.addChildToBack(new Node(Token.NAME));
    node1.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node1));

    // Test when node is not a FOR-IN loop
    Node node2 = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    // Test when node is used as a statement
    Node node1 = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node1));

    // Test when node is not used as a statement
    Node node2 = new Node(Token.NAME);
    node2.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test when node is a function declaration
    Node node1 = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Test when node is not a function declaration
    Node node2 = new Node(Token.NAME);
    node2.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test when node is a hoisted function declaration
    Node node1 = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(node1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Test when node is not a hoisted function declaration
    Node node2 = new Node(Token.FUNCTION);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.addChildToFront(node2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }
}