import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    Node child3 = new Node(Token.NAME);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    boolean result = NodeUtil.isForIn(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);

    boolean result = NodeUtil.isStatement(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, Token.NAME);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    Assert.assertTrue(result);
  }
}