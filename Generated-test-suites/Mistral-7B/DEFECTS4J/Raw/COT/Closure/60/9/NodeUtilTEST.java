import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 1}, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 1, 2}, null);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1}, null);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2}, null);
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1}, null);
    nodeFunctionExpression.setProperty("id", "myFunction");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0}, null);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2}, null);
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    nodeFunctionDeclaration.setParent(nodeScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2}, null);
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2}, null);
    nodeFunction.setProperty("id", "outerFunction");
    nodeFunctionDeclaration.setParent(nodeFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateIsStatementFailure() {
    Node node = new Node(Token.SCRIPT, new int[]{}, new int[]{0}, null);
    NodeUtil.isStatement(node);
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateIsFunctionExpressionFailure() {
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1}, null);
    NodeUtil.isFunctionDeclaration(nodeFunctionExpression);
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateNullParent() {
    Node node = new Node(Token.SCRIPT, new int[]{}, new int[]{0}, null);
    NodeUtil.isStatement(node);

    Node nodeParentNull = new Node(Token.SCRIPT, new int[]{}, new int[]{0}, null);
    node.setParent(nodeParentNull);
  }
}