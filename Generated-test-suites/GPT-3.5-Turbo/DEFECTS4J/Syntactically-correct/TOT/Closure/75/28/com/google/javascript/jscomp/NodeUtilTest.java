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

    // Edge case: Less than 3 child nodes
    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node2));

    // Error scenario: Node is not a FOR loop
    Node node3 = new Node(Token.IF);
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    // Typical use case: Node is a statement
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    node1.setParent(parent1);
    assertTrue(NodeUtil.isStatement(node1));

    // Typical use case: Node is not a statement
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.EXPR_RESULT);
    parent2.addChildToBack(node2);
    node2.setParent(parent2);
    assertFalse(NodeUtil.isStatement(node2));

    // Edge case: Node is part of a LABEL
    Node node3 = new Node(Token.VAR);
    Node parent3 = new Node(Token.LABEL);
    parent3.addChildToBack(node3);
    node3.setParent(parent3);
    assertTrue(NodeUtil.isStatement(node3));

    // Error scenario: No parent node
    Node node4 = new Node(Token.RETURN);
    assertFalse(NodeUtil.isStatement(node4));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case: Node is a function declaration
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    node1.setParent(parent1);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Typical use case: Node is a function expression
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.EXPR_RESULT);
    parent2.addChildToBack(node2);
    node2.setParent(parent2);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    // Error scenario: Node is not a function
    Node node3 = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: Function declaration in script scope
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    node1.setParent(parent1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Typical use case: Function declaration in function scope
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(node2);
    Node grandparent2 = new Node(Token.SCRIPT);
    grandparent2.addChildToBack(parent2);
    node2.setParent(parent2);
    parent2.setParent(grandparent2);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

    // Edge case: Function expression in script scope
    Node node3 = new Node(Token.FUNCTION);
    Node parent3 = new Node(Token.SCRIPT);
    parent3.addChildToBack(node3);
    node3.setParent(parent3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));

    // Error scenario: Node is not a function declaration
    Node node4 = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));

  }
}