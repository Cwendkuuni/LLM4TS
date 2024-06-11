import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    Node node2 = new Node(Token.FOR);
    node2.addChildToFront(new Node(Token.NAME));
    node2.addChildToFront(new Node(Token.NAME));
    node2.addChildToFront(new Node(Token.NAME));

    boolean result1 = NodeUtil.isForIn(node1);
    boolean result2 = NodeUtil.isForIn(node2);

    Assert.assertFalse(result1);
    Assert.assertTrue(result2);
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.LABEL);
    Node node4 = new Node(Token.NAME);

    boolean result1 = NodeUtil.isStatement(node1);
    boolean result2 = NodeUtil.isStatement(node2);
    boolean result3 = NodeUtil.isStatement(node3);
    boolean result4 = NodeUtil.isStatement(node4);

    Assert.assertTrue(result1);
    Assert.assertTrue(result2);
    Assert.assertTrue(result3);
    Assert.assertFalse(result4);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));

    boolean result1 = NodeUtil.isFunctionDeclaration(node1);

    Assert.assertTrue(result1);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(node1);

    boolean result1 = NodeUtil.isHoistedFunctionDeclaration(node1);
    boolean result2 = NodeUtil.isHoistedFunctionDeclaration(scriptNode);

    Assert.assertFalse(result1);
    Assert.assertTrue(result2);
  }
}