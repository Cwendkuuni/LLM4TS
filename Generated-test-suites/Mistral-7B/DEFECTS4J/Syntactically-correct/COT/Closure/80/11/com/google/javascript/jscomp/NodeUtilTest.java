import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeStatementInsideScript = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});
    parentScript.addChildToEnd(nodeStatement);
    parentScript.addChildToEnd(nodeStatementInsideScript);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeStatementInsideScript));

    Node nodeNotStatement = Node.newNode(Token.NUMBER, new int[]{}, new int[]{0, 0, 0});
    Node parentBlock = Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    parentBlock.addChildToEnd(nodeNotStatement);

    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"a"}, new int[]{0, 0, 0});
    nodeFunctionDeclaration.addChildToEnd(Node.newNode(Token.IDENT, new int[]{"a"}, new int[]{0, 0, 0}));
    nodeFunctionDeclaration.addChildToEnd(Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0}));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    nodeFunctionExpression.addChildToEnd(Node.newNode(Token.IDENT, new int[]{"a"}, new int[]{0, 0, 0}));
    nodeFunctionExpression.addChildToEnd(Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0}));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"a"}, new int[]{0, 0, 0});
    nodeHoistedFunctionDeclaration.addChildToEnd(Node.newNode(Token.IDENT, new int[]{"a"}, new int[]{0, 0, 0}));
    nodeHoistedFunctionDeclaration.addChildToEnd(Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0}));
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInsideFunction = Node.newNode(Token.FUNCTION, new int[]{"a"}, new int[]{0, 0, 0});
    nodeFunctionDeclarationInsideFunction.addChildToEnd(Node.newNode(Token.IDENT, new int[]{"a"}, new int[]{0, 0, 0}));
    nodeFunctionDeclarationInsideFunction.addChildToEnd(Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0}));
    Node parentFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    parentFunction.addChildToEnd(nodeFunctionDeclarationInsideFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInsideFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    NodeUtil.isStatement(node);
    fail("Expected AssertionError due to Preconditions.checkState(parent != null) failure");
  }
}