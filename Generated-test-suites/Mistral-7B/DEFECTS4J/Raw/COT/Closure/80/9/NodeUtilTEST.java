import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeNotForIn = new Node(Token.VAR, new int[]{}, new int[]{0});

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
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0});
    Node nodeFunctionDeclarationInBlock = new Node(Token.BLOCK, new int[]{}, new int[]{0});
    Node nodeFunctionDeclarationInBlockInFunction = new Node(Token.BLOCK, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlockInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScript));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node nodeNullParent = new Node(Token.SCRIPT, new int[]{}, null);
    NodeUtil.isStatement(nodeNullParent);
  }

  @Test
  public void testIsFunctionDeclarationWithEmptyNameArray() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }
}