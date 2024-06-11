import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(
        Token.FUNCTION, new int[]{Token.IDENT, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        new int[]{0, 1, 1, 0, 0, 0, 0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
    Node parentScript = new Node(Token.SCRIPT, new int[]{0, 0, 0}, new int[]{0, 0, 0});
    Node functionNodeInsideFunction = new Node(
        Token.FUNCTION, new int[]{0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
    Node parentFunction = new Node(
        Token.FUNCTION, new int[]{Token.IDENT, Token.OPEN_PAREN, Token.CLOSE_PAREN},
        new int[]{0, 1, 1, 0, 0, 0, 0, 0, 0});
    Node functionNodeInsideScript = new Node(
        Token.FUNCTION, new int[]{0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});

    // Hoisted function declaration
    NodeUtil.setParent(nodeHoistedFunctionDeclaration, parentScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    // Function declaration inside a function
    NodeUtil.setParent(functionNodeInsideFunction, parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideFunction));

    // Function declaration inside a script
    NodeUtil.setParent(functionNodeInsideScript, parentScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideScript));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}