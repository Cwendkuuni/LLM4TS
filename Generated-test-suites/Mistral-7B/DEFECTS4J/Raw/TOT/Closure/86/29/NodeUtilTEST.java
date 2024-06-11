import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newForInStatement(0, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(node));

    Node incorrectNode = Node.newExpressionStatement(Token.ASSIGN, null, null);
    assertFalse(NodeUtil.isForIn(incorrectNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Token.ASSIGN, null, null);
    Node expressionNode = Node.newExpression(Token.ADD, null, Node.newNumberLiteral(1), Node.newNumberLiteral(2));

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunctionDeclaration("testFunction", null, Node.newList(), Node.newBlock(null));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newExpressionStatement(Token.ASSIGN, null, Node.newFunctionExpression(null, null, Node.newList(), Node.newBlock(null)));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newFunctionDeclaration("testFunction", null, Node.newList(), Node.newBlock(null));
    Node scriptNode = Node.newScript(null, Node.newList(), null);
    Node functionNode = Node.newExpressionStatement(Token.ASSIGN, scriptNode, Node.newExpressionStatement(Token.ASSIGN, null, hoistedFunctionDeclarationNode));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode.getFirstChild()));

    Node incorrectHoistedFunctionDeclarationNode = Node.newFunctionDeclaration("testFunction", null, Node.newList(), Node.newBlock(null));
    Node incorrectParentNode = Node.newLabelStatement(null, Node.newIdentifier("testLabel"));
    Node incorrectFunctionNode = Node.newExpressionStatement(Token.ASSIGN, incorrectParentNode, incorrectHoistedFunctionDeclarationNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(incorrectHoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(incorrectFunctionNode));
  }

  @Test
  public void testNullInput() {
    assertNull(NodeUtil.isForIn(null));
    assertNull(NodeUtil.isStatement(null));
    assertNull(NodeUtil.isFunctionDeclaration(null));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }
}