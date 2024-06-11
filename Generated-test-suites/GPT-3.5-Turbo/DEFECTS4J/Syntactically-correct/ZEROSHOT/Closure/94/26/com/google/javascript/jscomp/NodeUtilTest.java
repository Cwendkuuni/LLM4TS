import org.junit.Test;
import static org.junit.Assert.*;

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
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isStatement(node);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node node = new Node(Token.ROOT);

    boolean result = NodeUtil.isStatement(node);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_NotDeclaration() {
    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToBack(new Node(Token.FUNCTION));

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);

    parent2.addChildToBack(node);
    parent1.addChildToBack(parent2);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.BLOCK);
    Node node = new Node(Token.FUNCTION);

    parent2.addChildToBack(node);
    parent1.addChildToBack(parent2);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertFalse(result);
  }
  
}