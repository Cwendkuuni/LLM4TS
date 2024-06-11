import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_true() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_true() {
    Node node = new Node(Token.SCRIPT);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.NAME);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(node);
    node.setParent(parent);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandparent = new Node(Token.SCRIPT);
    grandparent.addChildToBack(parent);
    parent.addChildToBack(node);
    node.setParent(parent);
    parent.setParent(grandparent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}