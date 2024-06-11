import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsForIn_False() {
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);

    boolean result = NodeUtil.isStatement(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.IF);

    boolean result = NodeUtil.isStatement(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.VAR);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(new Node(Token.EMPTY));
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertEquals(false, result);
  }
}