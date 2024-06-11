import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    Node nodeNotForIn = new Node(Token.IF);

    assertEquals(true, NodeUtil.isForIn(nodeForIn));
    assertEquals(false, NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.ID, "stmt");
    Node nodeExpression = new Node(Token.ID, "expr");
    Node nodeFunction = new Node(Token.FUNCTION, null);
    Node nodeToken = new Node(Token.INVALID_TOKEN);
    Node nodeScript = new Node(Token.SCRIPT, null);

    assertEquals(true, NodeUtil.isStatement(nodeStatement));
    assertEquals(false, NodeUtil.isStatement(nodeExpression));
    assertEquals(true, NodeUtil.isStatement(nodeFunction));
    assertEquals(true, NodeUtil.isStatement(nodeScript));
    assertEquals(false, NodeUtil.isStatement(nodeToken));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    Node nodeFunctionDeclarationWithParent = new Node(Token.FUNCTION, null);
    Node nodeFunctionExpressionWithParent = new Node(Token.FUNCTION, null);
    Node nodeFunctionDeclarationNullParent = new Node(Token.FUNCTION, null);
    Node nodeFunctionExpressionNullParent = new Node(Token.FUNCTION, null);

    assertEquals(true, NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertEquals(false, NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertEquals(true, NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithParent));
    assertEquals(true, NodeUtil.isFunctionDeclaration(nodeFunctionExpressionWithParent));
    assertEquals(false, NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationNullParent));
    assertEquals(false, NodeUtil.isFunctionDeclaration(nodeFunctionExpressionNullParent));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, null);
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, null);

    nodeFunctionDeclaration.addChild(nodeScript);
    nodeFunctionDeclarationInFunction.addChild(nodeScript);
    nodeFunctionDeclarationInFunction.addChild(nodeFunctionDeclaration);
    nodeFunctionDeclarationInScript.addChild(nodeFunctionDeclaration);

    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
  }

  @Test
  public void testIsForInWithNullNode() {
    Node nodeNull = null;

    assertFalse(NodeUtil.isForIn(nodeNull));
  }

  @Test
  public void testIsStatementWithNullNode() {
    Node nodeNull = null;

    assertFalse(NodeUtil.isStatement(nodeNull));
  }

  @Test
  public void testIsFunctionDeclarationWithNullNode() {
    Node nodeFunctionDeclarationNull = null;

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationNull));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node nodeFunctionDeclarationNull = null;

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNull));
  }

  @Test(expected = NullPointerException.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    Node nodeNullParent = null;

    NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration);
    NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration); // Second call is to check for exceptions
    nodeFunctionDeclaration.setParent(nodeNullParent);
    NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration); // Third call is to check for exceptions
  }
}