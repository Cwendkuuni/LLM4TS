import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"foo"}, new int[]{0, 0, 0});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"foo"}, new int[]{0, 0, 0});
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{"bar"}, new int[]{0, 0, 0}, Node.newNode(nodeFunctionDeclaration, new int[]{}, new int[]{0, 0, 0}));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"foo"}, new int[]{0, 0, 0});
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
    Preconditions.checkState(nodeFunctionDeclaration.getParent() != null);
    nodeFunctionDeclaration.setParent(null);
    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }
}