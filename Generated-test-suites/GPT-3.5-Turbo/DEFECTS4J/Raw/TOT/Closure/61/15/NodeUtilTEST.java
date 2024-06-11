import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.LP);
    Node child3 = new Node(Token.LPAREN);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    boolean result = NodeUtil.isForIn(node);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_invalidType() {
    Node node = new Node(Token.IF);

    boolean result = NodeUtil.isForIn(node);

    assertFalse(result);
  }

  @Test
  public void testIsForIn_invalidChildCount() {
    Node node = new Node(Token.FOR);
    Node child = new Node(Token.NAME);
    node.addChildToBack(child);

    boolean result = NodeUtil.isForIn(node);

    assertFalse(result);
  }

  @Test
  public void testIsStatement_typical() {
    Node node = new Node(Token.SCRIPT);
    Node child = new Node(Token.NAME);
    node.addChildToBack(child);

    boolean result = NodeUtil.isStatement(child);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.IF);
    Node child = new Node(Token.NAME);
    node.addChildToBack(child);

    boolean result = NodeUtil.isStatement(child);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToBack(node);
    parent2.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoisted() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertFalse(result);
  }
}