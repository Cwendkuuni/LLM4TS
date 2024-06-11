import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, null, createNode(IDENT, "i"), null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createNode(IF, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(PRINT, null, createNode(STRING_LITERAL, "hello"), null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode(ADD, null, createNode(INT_LITERAL, 1), createNode(INT_LITERAL, 2), null, null, null, null, null);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(FUNCTION, null, createNode(IDENT, "myFunction"), null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode(FUNCTION, null, null, null, null, null, null, null, null, null, createNode(IDENT, "myFunction"));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(SCRIPT, null, null, null, null, null, null, null, null);
    Node functionDeclarationNode = createNode(FUNCTION, null, createNode(IDENT, "myFunction"), null, null, null, null, null, null, null, null);
    Node functionCallNode = createNode(CALL, null, functionDeclarationNode, null, null, null, null, null, null);

    scriptNode.addChild(functionDeclarationNode);
    scriptNode.addChild(functionCallNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    Node functionDeclarationOutsideScriptNode = createNode(FUNCTION, null, createNode(IDENT, "myFunction"), null, null, null, null, null, null, null, null);
    Node scriptNodeWithFunctionDeclarationOutside = createNode(SCRIPT, null, null, null, null, null, null, functionDeclarationOutsideScriptNode, null);
    Node functionCallNodeOutside = createNode(CALL, null, functionDeclarationOutsideScriptNode, null, null, null, null, null, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationOutsideScriptNode));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node parentlessNode = createNode(IDENT, "node");
    NodeUtil.isStatement(parentlessNode);
  }
}