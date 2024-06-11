import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode(createToken(Token.FOR, 0, 0), null);
    assertTrue(NodeUtil.isForIn(node));

    node = createNode(createToken(Token.IF, 0, 0), null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createNode(createToken(Token.SCRIPT, 0, 0), null);
    assertTrue(NodeUtil.isStatement(node));

    node = createNode(createToken(Token.IF, 0, 0), node);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(createToken(Token.FUNCTION, 0, 0), null);
    functionNode.setProperty("id", createNode(createToken(Token.IDENTIFIER, 0, "foo"), null));
    Node statementNode = createNode(createToken(Token.IF, 0, 0), null);
    Node parentScriptNode = createNode(createToken(Token.SCRIPT, 0, 0), null);
    parentScriptNode.appendChild(functionNode);
    parentScriptNode.appendChild(statementNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(statementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = createNode(createToken(Token.FUNCTION, 0, 0), null);
    functionNode.setProperty("id", createNode(createToken(Token.IDENTIFIER, 0, "foo"), null));
    Node scriptNode = createNode(createToken(Token.SCRIPT, 0, 0), null);
    scriptNode.appendChild(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode(createToken(Token.FUNCTION, 0, 0), null);
    functionExpressionNode.setProperty("id", createNode(createToken(Token.IDENTIFIER, 0, "foo"), null));
    Node statementNode = createNode(createToken(Token.IF, 0, 0), null);
    scriptNode.appendChild(functionExpressionNode);
    scriptNode.appendChild(statementNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
  }
}