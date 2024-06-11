import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(Token.IF, new int[]{}, new int[]{0});
    NodeUtil.isStatement(node);
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2, 3});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2, 3});
    Node parentNonFunctionNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    parentNonFunctionNode.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNonHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2, 3});
    Node parentFunctionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    parentFunctionNode.addChild(nodeNonHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2, 3});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    Preconditions.checkState(nodeFunctionDeclaration.getParent() != null);
  }
}