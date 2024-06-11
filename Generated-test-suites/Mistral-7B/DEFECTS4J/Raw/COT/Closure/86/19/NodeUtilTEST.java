import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newExpression("1 + 2");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDecl = Node.newFunction(
        Node.newIdentifier("foo"), null, Node.newBlock(Node.newReturnStatement(Node.newNull())));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDecl));

    Node nodeFunctionExp = Node.newExpressionFunction(
        Node.newIdentifier("bar"), new Node[]{}, Node.newBlock(Node.newReturnStatement(Node.newNull())));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExp));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDecl = Node.newFunction(
        Node.newIdentifier("foo"), null, Node.newBlock(Node.newReturnStatement(Node.newNull())));
    Node parentScript = Node.newScript(new Node[] {nodeHoistedFunctionDecl});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDecl));

    Node nodeFunctionDeclInFunction = Node.newFunction(
        Node.newIdentifier("foo"), null, Node.newBlock(Node.newReturnStatement(Node.newNull())));
    Node parentFunction = Node.newFunction(
        Node.newIdentifier("bar"), null, Node.newBlock(new Node[] {nodeFunctionDeclInFunction}));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = Node.newExpressionStatement(Node.newIdentifier("x"));
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsForInWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatementWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclarationWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}