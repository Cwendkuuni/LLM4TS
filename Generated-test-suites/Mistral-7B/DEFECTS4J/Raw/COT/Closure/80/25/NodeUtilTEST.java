import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    parentScript.addChildToTree(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    Node parentExpression = new Node(Token.ADD, new int[]{}, new int[]{0, 1, 2});
    parentExpression.addChildToTree(nodeNotStatement);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"a"}, new int[]{0, 1});
    nodeFunctionDeclaration.setProperty("id", "testFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1});
    nodeFunctionExpression.setProperty("id", "testFunction");
    Node parentExpression = new Node(Token.ADD, new int[]{}, new int[]{0, 1, 2});
    parentExpression.addChildToTree(nodeFunctionExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"a"}, new int[]{0, 1});
    nodeHoistedFunctionDeclaration.setProperty("id", "testFunction");
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    parentScript.addChildToTree(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"a"}, new int[]{0, 1});
    nodeFunctionDeclarationInFunction.setProperty("id", "testFunction");
    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1});
    parentFunction.addChildToTree(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    NodeUtil.isStatement(node);
    Node parentNode = null;
    NodeUtil.isStatement(parentNode);
  }
}