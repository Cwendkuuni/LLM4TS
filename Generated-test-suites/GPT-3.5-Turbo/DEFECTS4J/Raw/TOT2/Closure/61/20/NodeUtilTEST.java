import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(node));

    Node node2 = new Node(Token.IF, new Node(), new Node());
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.BLOCK);
    Node node = new Node(Token.SCRIPT, parent);
    assertTrue(NodeUtil.isStatement(node));

    Node parent2 = new Node(Token.FUNCTION, new Node(), new Node());
    Node node2 = new Node(Token.SCRIPT, parent2);
    assertFalse(NodeUtil.isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    Node node2 = new Node(Token.IF);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION, parent);
    Node node = new Node(Token.FUNCTION, grandParent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    Node node2 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }

}