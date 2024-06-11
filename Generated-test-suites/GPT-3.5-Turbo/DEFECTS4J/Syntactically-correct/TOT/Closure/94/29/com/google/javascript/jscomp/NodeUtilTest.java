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

    // Edge case: empty for-in loop
    Node node2 = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(node2));

    // Edge case: invalid for-in loop
    Node node3 = new Node(Token.FOR);
    node3.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node3));

    // Error scenario: null node
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.SCRIPT);
    Node child1 = new Node(Token.EMPTY);
    node1.addChildToBack(child1);
    assertTrue(NodeUtil.isStatement(child1));

    // Edge case: empty statement
    Node node2 = new Node(Token.SCRIPT);
    Node child2 = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(child2));

    // Error scenario: null node
    assertFalse(NodeUtil.isStatement(null));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    Node child1 = new Node(Token.EMPTY);
    node1.addChildToBack(child1);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Edge case: empty function declaration
    Node node2 = new Node(Token.FUNCTION);
    Node child2 = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(child2));

    // Error scenario: null node
    assertFalse(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    // Edge case: function declaration in function body
    Node functionNode2 = new Node(Token.FUNCTION);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    Node grandparentNode = new Node(Token.EMPTY);
    functionNode2.addChildToBack(parentFunctionNode);
    parentFunctionNode.addChildToBack(grandparentNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));

    // Error scenario: null node
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
  }
}