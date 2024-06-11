import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createFunctionNode;
import static com.google.javascript.rhino.NodeFactory.createScriptNode;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = NodeUtil.createNode(createToken(Token.FOR, "for"), null);
    Node listNode = NodeUtil.createNode(createToken(Token.LIST, "["), null);
    Node inNode = NodeUtil.createNode(createToken(Token.IN, "in"), null);
    Node semicolonNode = NodeUtil.createNode(createToken(Token.SEMICOLON, ";"), null);

    Node forNodeWithThreeChildren = NodeUtil.createNode(createToken(Token.FOR, "for"), listNode, inNode, semicolonNode);
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));

    Node invalidForNode = NodeUtil.createNode(createToken(Token.FOR, "for"), null);
    assertFalse(NodeUtil.isForIn(invalidForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = NodeUtil.createNode(createToken(Token.IF, "if"), null);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = NodeUtil.createNode(createFunctionNode(null, null, null), null);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = NodeUtil.createNode(createFunctionNode("func", null, null), null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = NodeUtil.createNode(createFunctionNode(null, null, null), null);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = NodeUtil.createNode(createScriptNode(null), null);
    Node funcNode = NodeUtil.createNode(createFunctionNode("func", null, null), scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(funcNode));

    Node functionInFunctionNode = NodeUtil.createNode(createFunctionNode("func", null, null), NodeUtil.createNode(createFunctionNode(null, null, null), null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInFunctionNode));
  }

}