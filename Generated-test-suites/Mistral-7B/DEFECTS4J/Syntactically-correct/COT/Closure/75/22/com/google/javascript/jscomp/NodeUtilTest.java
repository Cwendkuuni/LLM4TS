import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[0]);
    Node nodeNotForIn = new Node(Token.IF, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[0]);
    Node nodeExpression = new Node(Token.ADD, new int[0]);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{-1, 1});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{-1, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[0]);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{-1, 1});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{-1, 1});
    Node nodeFunctionDeclarationInBlock = new Node(Token.BLOCK, new int[0]);
    Node nodeFunctionDeclarationInBlockWithFunction = new Node(Token.FUNCTION, new int[]{-1, 1});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlockWithFunction));

    Node nodeFunctionDeclarationInScript = new Node(Token.SCRIPT, new int[0]);
    Node nodeFunctionDeclarationInScriptWithFunction = new Node(Token.FUNCTION, new int[]{-1, 1});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScriptWithFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}