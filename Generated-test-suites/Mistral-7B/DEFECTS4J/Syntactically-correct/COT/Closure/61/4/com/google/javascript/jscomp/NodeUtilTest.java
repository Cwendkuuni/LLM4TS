import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 0, 0}, "functionName");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 0, 0}, "functionName")
        .setParent(new Node(Token.SCRIPT, new int[]{0, 0, 0}));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNonHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 0, 0}, "functionName")
        .setParent(new Node(Token.BLOCK, new int[]{0, 0, 0}));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateFailure() {
    NodeUtil.isStatement(null);
  }
}