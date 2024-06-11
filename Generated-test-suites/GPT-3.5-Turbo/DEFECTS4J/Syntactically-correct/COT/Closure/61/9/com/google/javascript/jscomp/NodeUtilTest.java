
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    Assert.assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.EMPTY);

    Assert.assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    script.addChildToBack(node);

    Assert.assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.EMPTY);

    Assert.assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(function);

    Assert.assertTrue(NodeUtil.isFunctionDeclaration(function));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);

    Assert.assertFalse(NodeUtil.isFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(function);

    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);

    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
  }

}
