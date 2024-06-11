import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newName("x");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newName("foo"), new int[0], Node.newBlock(new Node[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunction(Node.newName("foo"), new int[0], Node.newBlock(new Node[0])));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newName("foo"), new int[0], Node.newBlock(new Node[0]));
    Node parentScript = Node.newScript();
    parentScript.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        Node.newName("foo"), new int[0], Node.newBlock(new Node[0]));
    Node parentFunction = Node.newFunction(Node.newName("bar"), new int[0], Node.newBlock(new Node[0]));
    parentFunction.addChild(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFail() {
    Node node = Node.newExpressionStatement(Node.newName("x"));
    NodeUtil.isStatement(null);
  }
}