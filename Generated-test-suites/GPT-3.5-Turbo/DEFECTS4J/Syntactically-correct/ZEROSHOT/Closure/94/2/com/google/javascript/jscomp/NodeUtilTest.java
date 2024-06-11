import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));

    Node node2 = new Node(Token.EMPTY);

    Assert.assertTrue(NodeUtil.isForIn(node1));
    Assert.assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
    Node otherNode = new Node(Token.EMPTY);

    Assert.assertTrue(NodeUtil.isStatement(scriptNode));
    Assert.assertTrue(NodeUtil.isStatement(blockNode));
    Assert.assertTrue(NodeUtil.isStatement(labelNode));
    Assert.assertFalse(NodeUtil.isStatement(otherNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node statementNode = new Node(Token.EMPTY);

    functionNode.setParent(new Node(Token.SCRIPT));
    statementNode.setParent(new Node(Token.SCRIPT));

    Assert.assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(statementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode1 = new Node(Token.FUNCTION);
    Node functionNode2 = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    Node scriptParentNode = new Node(Token.SCRIPT);
    Node otherNode = new Node(Token.EMPTY);

    functionNode1.setParent(scriptNode);
    functionNode2.setParent(scriptParentNode);
    scriptParentNode.setParent(scriptNode);
    otherNode.setParent(scriptNode);

    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(otherNode));
  }
}