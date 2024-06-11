import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_InvalidNodeType() {
    Node node = new Node(Token.SCRIPT);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_InvalidChildCount() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_NullParent() {
    Node node = new Node(Token.EXPR_RESULT);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);

    Node grandparent = new Node(Token.SCRIPT);
    grandparent.addChildToFront(parent);
    parent.setParent(grandparent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NullParent() {
    Node node = new Node(Token.FUNCTION);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NullGrandparent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}