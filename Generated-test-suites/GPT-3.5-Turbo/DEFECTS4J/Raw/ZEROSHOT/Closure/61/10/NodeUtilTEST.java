import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);

    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    boolean result = NodeUtil.isForIn(node);
    assertTrue(result);
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.WHILE);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);

    node.addChildToBack(child1);
    node.addChildToBack(child2);

    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isStatement(node);
    assertTrue(result);
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isStatement(node);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(parent1);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }
}