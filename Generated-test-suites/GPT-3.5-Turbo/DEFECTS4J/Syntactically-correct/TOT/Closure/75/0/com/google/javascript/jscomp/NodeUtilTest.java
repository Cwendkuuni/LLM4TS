import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalUseCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_edgeCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement_typicalUseCase() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isStatement(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_edgeCase() {
    Node node = new Node(Token.NAME);

    boolean result = NodeUtil.isStatement(node);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_typicalUseCase() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));

    boolean result = NodeUtil.isFunctionDeclaration(node);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_edgeCase() {
    Node node = new Node(Token.ADD);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalUseCase() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));

    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToBack(functionNode);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_edgeCase() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));

    Node parentNode = new Node(Token.FUNCTION);
    parentNode.addChildToBack(functionNode);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

    Assert.assertFalse(result);
  }
}