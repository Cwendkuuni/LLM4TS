
import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    Node node2 = new Node(Token.FOR);
    node2.addChildToFront(new Node(Token.IF));
    node2.addChildToFront(new Node(Token.EXPANDO));
    node2.addChildToFront(new Node(Token.BLOCK));
    Assert.assertTrue(NodeUtil.isForIn(node1));
    Assert.assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.IF);
    Node node2 = new Node(Token.EXPR_RESULT);
    Node node3 = new Node(Token.SCRIPT);
    Node node4 = new Node(Token.BLOCK);
    Node node5 = new Node(Token.LABEL);
    Assert.assertFalse(NodeUtil.isStatement(node1));
    Assert.assertTrue(NodeUtil.isStatement(node2));
    Assert.assertTrue(NodeUtil.isStatement(node3));
    Assert.assertTrue(NodeUtil.isStatement(node4));
    Assert.assertTrue(NodeUtil.isStatement(node5));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.SCRIPT);
    node2.addChildToFront(new Node(Token.BLOCK));
    node2.addChildToFront(node1);
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(node1));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToFront(new Node(Token.BLOCK));
    parent1.addChildToFront(node1);
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToFront(new Node(Token.BLOCK));
    parent2.addChildToFront(node2);
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent1));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent2));
  }
}
