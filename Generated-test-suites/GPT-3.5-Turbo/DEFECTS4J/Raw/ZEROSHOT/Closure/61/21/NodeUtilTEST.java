import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node child = new Node(Token.EMPTY);
    node.addChildToBack(child);

    boolean result = NodeUtil.isStatement(child);
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }
}