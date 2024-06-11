import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

@RunWith(JUnit4.class)
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {0, 1, 2});
    Node parentStatement = Node.newNode(Token.SCRIPT, new int[0]);
    parentStatement.addChild(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3, 4});
    Node nameNode = Node.newNode(Token.IDENT, new int[] {0, "name".length()});
    nodeFunctionDeclaration.setChild(0, nameNode);
    Node bodyNode = Node.newNode(Token.BLOCK, new int[0]);
    nodeFunctionDeclaration.setChild(1, bodyNode);

    Node parentScript = Node.newNode(Token.SCRIPT, new int[0]);
    parentScript.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3, 4, 5});
    Node nameNodeExpression = Node.newNode(Token.IDENT, new int[] {0, "name".length()});
    nodeFunctionExpression.setChild(0, nameNodeExpression);
    Node expressionBody = Node.newNode(Token.ASSIGN, new int[] {0, 1, 2});
    nodeFunctionExpression.setChild(1, expressionBody);

    Node parentExpression = Node.newNode(Token.ASSIGN, new int[] {0, 2, 3});
    parentExpression.setChild(1, nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3, 4});
    Node nameNode = Node.newNode(Token.IDENT, new int[] {0, "name".length()});
    nodeHoistedFunctionDeclaration.setChild(0, nameNode);
    Node bodyNode = Node.newNode(Token.BLOCK, new int[0]);
    nodeHoistedFunctionDeclaration.setChild(1, bodyNode);

    Node parentScript = Node.newNode(Token.SCRIPT, new int[0]);
    parentScript.addChild(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3, 4});
    Node nameNodeInFunction = Node.newNode(Token.IDENT, new int[] {0, "name".length()});
    nodeFunctionDeclarationInFunction.setChild(0, nameNodeInFunction);
    Node bodyNodeInFunction = Node.newNode(Token.BLOCK, new int[0]);
    nodeFunctionDeclarationInFunction.setChild(1, bodyNodeInFunction);

    Node parentFunction = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2});
    parentFunction.setChild(0, nameNodeInFunction);
    parentFunction.setChild(1, nodeFunctionDeclarationInFunction);

    Node parentScriptInFunction = Node.newNode(Token.SCRIPT, new int[0]);
    parentScriptInFunction.addChild(parentFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionFailureIsFunctionDeclaration() {
    Node nodeInvalid = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2});
    Preconditions.checkState(NodeUtil.isFunctionDeclaration(nodeInvalid));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionFailureIsHoistedFunctionDeclaration() {
    Node nodeInvalid = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2});
    Preconditions.checkState(NodeUtil.isHoistedFunctionDeclaration(nodeInvalid));
  }

  @Test
  public void testPreconditionFailureIsStatementParentIsNull() {
    Node nodeInvalid = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2});
    Preconditions.checkState(NodeUtil.isStatement(nodeInvalid), "node has null parent");
  }
}