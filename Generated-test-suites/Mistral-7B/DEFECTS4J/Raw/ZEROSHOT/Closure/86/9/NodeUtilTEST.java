import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    nodeForIn.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    nodeStatement.setParent(parentScript);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.ADD, null);
    parentScript.setFirstChild(nodeExpression);
    nodeExpression.setFirstChild(nodeStatement);

    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "foo");
    nodeFunctionDeclaration.setFirstChild(new Node(Token.NAME, "foo"));
    Node parentBlock = new Node(Token.BLOCK, null);
    nodeFunctionDeclaration.setParent(parentBlock);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    nodeFunctionExpression.setFirstChild(new Node(Token.NAME, "foo"));
    Node parentExpression = new Node(Token.ASSIGN, null);
    parentExpression.setFirstChild(nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, "foo");
    nodeHoistedFunctionDeclaration.setFirstChild(new Node(Token.NAME, "foo"));
    Node parentScript = new Node(Token.SCRIPT, null);
    nodeHoistedFunctionDeclaration.setParent(parentScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNonHoistedFunctionDeclaration = new Node(Token.FUNCTION, "foo");
    nodeNonHoistedFunctionDeclaration.setFirstChild(new Node(Token.NAME, "foo"));
    Node parentBlock = new Node(Token.BLOCK, null);
    nodeNonHoistedFunctionDeclaration.setParent(parentBlock);
    Node parentScript2 = new Node(Token.SCRIPT, null);
    parentBlock.setParent(parentScript2);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }
}