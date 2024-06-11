import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  // Test for isForIn method
  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  // Test for isStatement method
  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  // Test for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  // Test for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node parentOfHoistedFunctionDeclaration = new Node(Token.SCRIPT, new int[]{0}, new int[]{});
    parentOfHoistedFunctionDeclaration.addChildToEnd(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node parentOfNotHoistedFunctionDeclaration = new Node(Token.BLOCK, new int[]{0}, new int[]{});
    parentOfNotHoistedFunctionDeclaration.addChildToEnd(nodeNotHoistedFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}