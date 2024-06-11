import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    node.setType(Token.FUNCTION);
    Node child = new Node(Token.BLOCK);
    node.addChildToBack(child);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.EMPTY));
    node.setType(Token.FUNCTION);
    Node child = new Node(Token.BLOCK);
    node.addChildToBack(child);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    node.setType(Token.FUNCTION);
    Node child = new Node(Token.BLOCK);
    node.addChildToBack(child);

    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    node.setType(Token.FUNCTION);
    Node child = new Node(Token.BLOCK);
    node.addChildToBack(child);

    Node parent = new Node(Token.WHILE);
    parent.addChildToBack(node);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}