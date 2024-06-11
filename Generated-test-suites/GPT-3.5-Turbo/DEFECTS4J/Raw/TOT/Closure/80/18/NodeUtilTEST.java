import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    Node node2 = new Node(Token.FOR);
    Node node3 = new Node(Token.WHILE);

    node1.addChildToFront(new Node(Token.EXPR_RESULT));
    node1.addChildToFront(new Node(Token.EMPTY));
    node1.addChildToFront(new Node(Token.EMPTY));

    node2.addChildToFront(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.LABEL);
    Node node4 = new Node(Token.EXPR_RESULT);

    assertTrue(NodeUtil.isStatement(node1));
    assertTrue(NodeUtil.isStatement(node2));
    assertTrue(NodeUtil.isStatement(node3));
    assertFalse(NodeUtil.isStatement(node4));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.FUNCTION);
    Node node3 = new Node(Token.VAR);
    Node node4 = new Node(Token.EXPR_RESULT);

    node1.addChildToFront(new Node(Token.NAME));
    node2.addChildToFront(new Node(Token.NAME));

    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
    assertFalse(NodeUtil.isFunctionDeclaration(node4));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.FUNCTION);
    Node node3 = new Node(Token.VAR);
    Node node4 = new Node(Token.SCRIPT);

    node1.addChildToFront(new Node(Token.NAME));
    node2.addChildToFront(new Node(Token.NAME));

    node4.addChildToFront(new Node(Token.FUNCTION));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }
}