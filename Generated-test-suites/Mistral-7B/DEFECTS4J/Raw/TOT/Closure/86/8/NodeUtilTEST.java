import static org.junit.Assert.assertEquals;
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
    Node forNode = Node.newForStatement();
    assertTrue(NodeUtil.isForIn(forNode));

    Node invalidNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newExpression(Node.newNumberLiteral(1));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock());
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock()));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(Node.newList(), Node.newBlock());
    Node functionDeclarationNode = Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock());
    Node hoistedFunctionDeclarationNode = Node.newExpressionStatement(functionDeclarationNode);
    scriptNode.setFirstChild(hoistedFunctionDeclarationNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node invalidNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
  }

  @Test
  public void testIsFunctionExpression() {
    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock()));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));

    Node functionDeclarationNode = Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock());
    Node hoistedFunctionDeclarationNode = Node.newExpressionStatement(functionDeclarationNode);
    Node scriptNode = Node.newScript(Node.newList(), Node.newBlock());
    scriptNode.setFirstChild(hoistedFunctionDeclarationNode);

    Node functionExpressionInScriptNode = Node.newExpressionStatement(functionExpressionNode);
    scriptNode.addChild(functionExpressionInScriptNode);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionInScriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionInScriptNode));

    Node functionDeclarationInFunctionNode = Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock());
    Node hoistedFunctionDeclarationInFunctionNode = Node.newExpressionStatement(functionDeclarationInFunctionNode);
    Node functionNode = Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock());
    functionNode.setBody(Node.newBlock());
    functionNode.getBody().addChild(hoistedFunctionDeclarationInFunctionNode);

    Node functionExpressionInFunctionNode = Node.newExpressionStatement(functionExpressionNode);
    functionNode.getBody().addChild(functionExpressionInFunctionNode);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionInFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionInFunctionNode));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationInFunctionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunctionNode));
  }

  @Test
  public void testNullNode() {
    assertNull(NodeUtil.isForIn(null));
    assertNull(NodeUtil.isStatement(null));
    assertNull(NodeUtil.isFunctionDeclaration(null));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  @Test
  public void testEmptyNode() {
    Node emptyNode = Node.newEmptyStatement();
    assertFalse(NodeUtil.isForIn(emptyNode));
    assertTrue(NodeUtil.isStatement(emptyNode));
    assertFalse(NodeUtil.isFunctionDeclaration(emptyNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(emptyNode));
  }
}