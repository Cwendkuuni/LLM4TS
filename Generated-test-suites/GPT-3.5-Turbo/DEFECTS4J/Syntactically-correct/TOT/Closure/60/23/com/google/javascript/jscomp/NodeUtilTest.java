import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.VAR));
    node.addChildToBack(new Node(Token.TRUE));
    node.addChildToBack(new Node(Token.NULL));

    Assert.assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_notForInLoop() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.VAR));
    node.addChildToBack(new Node(Token.TRUE));

    Assert.assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
    Node otherNode = new Node(Token.VAR);

    Assert.assertTrue(NodeUtil.isStatement(scriptNode));
    Assert.assertTrue(NodeUtil.isStatement(blockNode));
    Assert.assertTrue(NodeUtil.isStatement(labelNode));
    Assert.assertFalse(NodeUtil.isStatement(otherNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setIsFunction(true);
    functionNode.setIsStatement(true);

    Assert.assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setIsFunction(true);
    functionNode.setIsStatement(false);

    Assert.assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setIsFunction(true);
    functionNode.setIsStatement(true);
    scriptNode.addChildToBack(functionNode);

    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setIsFunction(true);
    functionNode.setIsStatement(true);
    Node otherNode = new Node(Token.VAR);
    scriptNode.addChildToBack(otherNode);
    scriptNode.addChildToBack(functionNode);

    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}