import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.common.base.Preconditions;

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
    Node nodeNotStatement = Node.newExpr(Token.IDENT, "a");

    Node parentStatement = Node.newBlock(new int[] {0, 0, 0});
    parentStatement.setLineNumber(1);
    parentStatement.setLineOffset(1);

    Node parentExpression = Node.newExpr(Token.IDENT, "a");
    parentExpression.setLineNumber(1);
    parentExpression.setLineOffset(2);

    nodeStatement.setParent(parentStatement);
    nodeNotStatement.setParent(parentExpression);

    Preconditions.checkState(parentStatement != null);
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("f"), new int[] {0, 1}, null, null, null);
    nodeFunctionDeclaration.setLineNumber(1);
    nodeFunctionDeclaration.setLineOffset(1);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newFunction(
        Node.newIdentifier("g"), new int[] {0, 1}, null, null, null);
    nodeFunctionExpression.setLineNumber(1);
    nodeFunctionExpression.setLineOffset(2);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("h"), new int[] {0, 1}, null, null, null);
    nodeHoistedFunctionDeclaration.setLineNumber(1);
    nodeHoistedFunctionDeclaration.setLineOffset(1);
    Node parentScript = Node.newBlock(new int[] {0, 0, 0});
    parentScript.setLineNumber(1);
    parentScript.setLineOffset(1);
    nodeHoistedFunctionDeclaration.setParent(parentScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        Node.newIdentifier("i"), new int[] {0, 1}, null, null, null);
    nodeFunctionDeclarationInFunction.setLineNumber(1);
    nodeFunctionDeclarationInFunction.setLineOffset(1);
    Node parentFunction = Node.newFunction(
        Node.newIdentifier("j"), new int[] {0, 1}, null, null, null);
    parentFunction.setLineNumber(1);
    parentFunction.setLineOffset(1);
    nodeFunctionDeclarationInFunction.setParent(parentFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}