import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_Typical() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    node.addChildToBack(new Node(Token.STRING));
    node.addChildToBack(new Node(Token.BLOCK));

    boolean result = NodeUtil.isForIn(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    node.addChildToBack(new Node(Token.STRING));

    boolean result = NodeUtil.isForIn(node);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement_Statement() {
    Node node = new Node(Token.BLOCK);

    boolean result = NodeUtil.isStatement(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node node = new Node(Token.EXPR_RESULT);

    boolean result = NodeUtil.isStatement(node);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_IsDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.BLOCK));

    boolean result = NodeUtil.isFunctionDeclaration(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_NotDeclaration() {
    Node node = new Node(Token.VAR);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_HoistedDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.BLOCK));
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.BLOCK));
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(node);
    node.setParent(parent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    Assert.assertFalse(result);
  }
}