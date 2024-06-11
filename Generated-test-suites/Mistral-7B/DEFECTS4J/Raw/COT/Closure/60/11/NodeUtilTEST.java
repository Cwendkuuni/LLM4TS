import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{1, 2, 3});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{1, 2, 3});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{1, 2, 3});
    Node nodeNotStatement = Node.newNode(Token.IDENT, new int[]{}, new int[]{1, 2, 3});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4, 5});
    Node nodeNotFunction = Node.newNode(Token.IDENT, new int[]{}, new int[]{1, 2, 3});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{1, 2, 3, 4, 5, 6, 7});
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    Node nodeFunctionDeclarationInBlock = Node.newNode(Token.BLOCK, new int[]{}, new int[]{1, 2, 3, 4, 5});
    Node nodeFunctionDeclarationInBlockWithFunction = Node.newNode(Token.BLOCK, new int[]{},
        new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});

    Node functionDeclarationInScript = NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
    Node functionDeclarationInFunction = NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction);
    Node functionDeclarationInBlock = NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock);
    Node functionDeclarationInBlockWithFunction = NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlockWithFunction);

    assertEquals(nodeFunctionDeclaration, functionDeclarationInScript);
    assertNull(functionDeclarationInFunction);
    assertNull(functionDeclarationInBlock);
    assertTrue(functionDeclarationInBlockWithFunction instanceof Node);
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }

  @Test
  public void testIsForInWithNullNode() {
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsStatementWithNullNode() {
    Node parent = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isStatement(null));

    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{1, 2, 3});
    Node nodeStatementWithParent = Node.newNode(Token.IF, new int[]{}, new int[]{1, 2, 3});
    nodeStatementWithParent.setParent(parent);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeStatementWithParent));
  }
}