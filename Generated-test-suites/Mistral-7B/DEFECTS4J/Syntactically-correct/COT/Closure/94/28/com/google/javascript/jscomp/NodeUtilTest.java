import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[] {1, 2, 3});
    Node parent = new Node(Token.SCRIPT, new int[] {});
    parent.addChildToEnd(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    nodeFunction.setProperty("id", "testFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    nodeFunction.setProperty("id", "testFunction");
    Node scriptNode = new Node(Token.SCRIPT, new int[] {});
    scriptNode.addChildToEnd(nodeFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node functionNodeInsideFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    functionNodeInsideFunction.setProperty("id", "testFunction");
    Node parentFunctionNode = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    parentFunctionNode.setProperty("id", "parentFunction");
    parentFunctionNode.addChildToEnd(functionNodeInsideFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}