import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, null, createNode(IDENTIFIER, "i"), createNode(INTEGER_LITERAL, 0), createNode(SEMICOLON, null), null, createNode(BLOCK, null, createNode(STATEMENT, null), createNode(STATEMENT, null), createNode(STATEMENT, null), null), createNode(SEMICOLON, null), createNode(RBRACE, null));
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createNode(IF, null, createNode(IDENTIFIER, "condition"), createNode(LBRACE, null), createNode(STATEMENT, null), createNode(RBRACE, null), null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(IDENTIFIER, "statement");
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode(ADD, null, createNode(IDENTIFIER, "x"), createNode(INTEGER_LITERAL, 5));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(FUNCTION, null, createNode(IDENTIFIER, "myFunction"), createNode(BLOCK, null, createNode(RETURN_STATEMENT, null, createNode(IDENTIFIER, "result")), null), null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode(IDENTIFIER, "myFunction");
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(SCRIPT, null, null, null);
    Node functionDeclarationNode = createNode(FUNCTION, null, createNode(IDENTIFIER, "myFunction"), createNode(BLOCK, null, createNode(RETURN_STATEMENT, null, createNode(IDENTIFIER, "result")), null), null);
    Node functionCallNode = createNode(IDENTIFIER, "myFunction");
    Node functionExpressionNode = createNode(IDENTIFIER, "myFunction");

    scriptNode = addChild(scriptNode, functionDeclarationNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isStatement(functionDeclarationNode));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionCallNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionCallNode));
    assertFalse(NodeUtil.isStatement(functionCallNode));

    scriptNode = addChild(scriptNode, functionExpressionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isStatement(functionExpressionNode));
  }

  private Node addChild(Node parent, Node child) {
    Preconditions.checkState(parent != null);
    Preconditions.checkState(child != null);

    parent.addChild(child);
    return parent;
  }
}