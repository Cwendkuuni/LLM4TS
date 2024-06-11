import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0});
    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        new int[]{0, 1, 2}
    );
    Node nodeFunctionExpression = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.OPEN_PAREN, Token.OPEN_BRACE},
        new int[]{0, 1, 2}
    );

    Node nodeNotFunction = new Node(Token.IDENT, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        new int[]{0, 1, 2}
    );
    Node nodeFunctionInFunction = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        new int[]{0, 1, 2}
    );
    Node nodeFunctionInBlock = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        new int[]{0, 1, 2}
    );

    Node nodeFunctionInScriptParent = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunctionInFunctionParent = new Node(
        Token.FUNCTION,
        new int[]{Token.IDENT, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        new int[]{0, 1, 2}
    );
    Node nodeFunctionInBlockParent = new Node(Token.BLOCK, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));

    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript);
    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScriptParent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }

  @Test(expected = AssertionError.class)
  public void testIsForInNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}