import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.FOR));
    node1.addChildToBack(new Node(Token.FOR));
    assertTrue(NodeUtil.isForIn(node1));

    // Edge case
    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.FOR));
    node2.addChildToBack(new Node(Token.FOR));
    node2.addChildToBack(new Node(Token.FOR));
    assertFalse(NodeUtil.isForIn(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node1));

    // Edge case
    Node node2 = new Node(Token.LABEL);
    assertTrue(NodeUtil.isStatement(node2));

    // Error scenario
    Node node3 = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isStatement(node3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Edge case
    Node node2 = new Node(Token.VAR);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    Node script1 = new Node(Token.SCRIPT);
    script1.addChildToBack(node1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Edge case
    Node node2 = new Node(Token.FUNCTION);
    Node script2 = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    script2.addChildToBack(function);
    function.addChildToBack(node2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

    // Error scenario
    Node node3 = new Node(Token.FUNCTION);
    node3.setParent(null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}