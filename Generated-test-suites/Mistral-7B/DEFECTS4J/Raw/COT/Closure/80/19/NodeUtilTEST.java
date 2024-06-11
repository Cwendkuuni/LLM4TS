import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.IF, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.IF, new int[] {0, 1, 2});
    Node parentStatement = createNode(Token.BLOCK, new int[] {0, 1});
    parentStatement.addChild(nodeStatement);

    Preconditions.checkState(nodeStatement.getParent() == parentStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = createNode(Token.VAR, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, new int[] {0, 1, 2}, "testFunction");
    Node parentBlock = createNode(Token.BLOCK, new int[] {0, 1});
    parentBlock.addChild(nodeFunctionDeclaration);

    Preconditions.checkState(nodeFunctionDeclaration.getParent() == parentBlock);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(Token.FUNCTION, new int[] {0, 1, 2});
    ParentNode parentExpression = createNode(Token.ASSIGN, new int[] {0, 1, 2});
    parentExpression.setLeftChild(nodeFunctionExpression);

    Preconditions.checkState(nodeFunctionExpression.getParent() == parentExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createNode(Token.FUNCTION, new int[] {0, 1, 2}, "testFunction");
    Node parentScript = createNode(Token.SCRIPT, new int[] {0, 1});
    parentScript.addChild(nodeHoistedFunctionDeclaration);

    Preconditions.checkState(nodeHoistedFunctionDeclaration.getParent() == parentScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = createNode(Token.FUNCTION, new int[] {0, 1, 2}, "testFunction");
    Node parentFunction = createNode(Token.FUNCTION, new int[] {0, 1, 2}, "testFunction");
    parentFunction.addChild(nodeFunctionDeclarationInFunction);

    Preconditions.checkState(nodeFunctionDeclarationInFunction.getParent() == parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testParentIsNull() {
    Node node = createNode(Token.FUNCTION, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isStatement(node));
    Preconditions.checkState(node.getParent() != null);
  }
}