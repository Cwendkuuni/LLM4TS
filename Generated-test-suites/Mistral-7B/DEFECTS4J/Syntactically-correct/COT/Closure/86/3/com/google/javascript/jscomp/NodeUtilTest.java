import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  private Node createNode(int type, Node parent) {
    Node n = new Node();
    n.setType(type);
    n.setParent(parent);
    return n;
  }

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, null);
    Node listNode = createNode(Token.LIST, forNode);
    Node inNode = createNode(Token.FOR_IN, listNode);
    Node bodyNode = createNode(Token.BLOCK, inNode);

    assertTrue(isForIn(inNode));

    Node invalidForNode = createNode(Token.IF, null);
    assertFalse(isForIn(invalidForNode));

    Node invalidForInNode = createNode(Token.IDENT, null);
    assertFalse(isForIn(invalidForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.ASSIGN, null);

    assertTrue(isStatement(statementNode));

    Node expressionNode = createNode(Token.ADD, null);

    assertFalse(isStatement(expressionNode));

    Node functionNode = createNode(Token.FUNCTION, null);

    // It is not possible to determine if a node is a statement or not
    // without its context in the AST. A function node can be either
    // part of an expression or a statement.
    Node scriptNode = createNode(Token.SCRIPT, null);
    scriptNode.addChild(functionNode);

    assertTrue(isStatement(functionNode));

    Node blockNode = createNode(Token.BLOCK, null);
    blockNode.addChild(functionNode);

    assertTrue(isStatement(functionNode));

    Node labelNode = createNode(Token.IDENT, null);
    labelNode.setName("label");

    assertTrue(isStatement(labelNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, null);
    Node nameNode = createNode(Token.IDENT, functionNode);
    nameNode.setName("funcName");
    functionNode.addChild(nameNode);
    functionNode.addChild(createNode(Token.BLOCK, functionNode));

    Node statementNode = createNode(Token.ASSIGN, null);

    Node functionExpressionNode = createNode(Token.FUNCTION, statementNode);
    Node nameNodeExp = createNode(Token.IDENT, functionExpressionNode);
    nameNodeExp.setName("funcName");
    functionExpressionNode.addChild(nameNodeExp);
    functionExpressionNode.addChild(createNode(Token.BLOCK, functionExpressionNode));

    assertTrue(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationNode = createNode(Token.FUNCTION, null);
    Node nameNode = createNode(Token.IDENT, functionDeclarationNode);
    nameNode.setName("funcName");
    functionDeclarationNode.addChild(nameNode);
    functionDeclarationNode.addChild(createNode(Token.BLOCK, functionDeclarationNode));

    Node scriptNode = createNode(Token.SCRIPT, null);
    scriptNode.addChild(functionDeclarationNode);

    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));

    Node functionNode = createNode(Token.FUNCTION, null);
    Node nameNode2 = createNode(Token.IDENT, functionNode);
    nameNode2.setName("funcName");
    functionNode.addChild(nameNode2);
    functionNode.addChild(createNode(Token.BLOCK, functionNode));

    Node functionExpressionNode = createNode(Token.FUNCTION, null);
    Node nameNode3 = createNode(Token.IDENT, functionExpressionNode);
    nameNode3.setName("funcName");
    functionExpressionNode.addChild(nameNode3);
    functionExpressionNode.addChild(createNode(Token.BLOCK, functionExpressionNode));

    Node statementNode = createNode(Token.ASSIGN, null);
    functionExpressionNode.addChild(statementNode);

    Node functionDeclarationInFunctionNode = createNode(Token.FUNCTION, functionNode);
    Node nameNode4 = createNode(Token.IDENT, functionDeclarationInFunctionNode);
    nameNode4.setName("funcName");
    functionDeclarationInFunctionNode.addChild(nameNode4);
    functionDeclarationInFunctionNode.addChild(createNode(Token.BLOCK, functionDeclarationInFunctionNode));

    Node functionInFunctionNode = createNode(Token.FUNCTION, functionDeclarationInFunctionNode);
    functionInFunctionNode.addChild(functionDeclarationInFunctionNode);

    scriptNode.addChild(functionDeclarationNode);
    scriptNode.addChild(functionInFunctionNode);

    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
    assertFalse(isHoistedFunctionDeclaration(functionInFunctionNode));
  }
}