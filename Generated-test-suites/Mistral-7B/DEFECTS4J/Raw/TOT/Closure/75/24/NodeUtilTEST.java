import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class NodeUtilTest {

  private NodeFactory nodeFactory = NodeFactory.getJavascriptContext();

  @ org.junit.Test
  public void testIsForIn() {
    Node node = nodeFactory.createTokenNode(Token.FOR, null);
    Node arrayNode = nodeFactory.createExpressionStatement(nodeFactory.createNameNode("forInArray"));
    node.setParent(arrayNode);
    node.setChild(0, nodeFactory.createTokenNode(Token.KEYWORD, "for"));
    node.setChild(1, nodeFactory.createNameNode("i"));
    node.setChild(2, nodeFactory.createTokenNode(Token.KEYWORD, "in"));
    Node iterNode = nodeFactory.createTokenNode(Token.FOR, null);
    iterNode.setParent(node);
    iterNode.setChild(0, nodeFactory.createTokenNode(Token.KEYWORD, "for"));
    iterNode.setChild(1, nodeFactory.createNameNode("iter"));
    iterNode.setChild(2, nodeFactory.createExpressionStatement(nodeFactory.createNewArrayExpression(null, 5)));

    Node incorrectNode = nodeFactory.createTokenNode(Token.IF, null);
    Node incorrectParent = nodeFactory.createTokenNode(Token.SCRIPT, null);
    incorrectNode.setParent(incorrectParent);

    List<Node> forInNodes = Lists.newArrayList(node, iterNode);
    List<Node> incorrectNodes = Lists.newArrayList(incorrectNode);

    for (Node n : forInNodes) {
      assertTrue(isForIn(n));
    }

    for (Node n : incorrectNodes) {
      assertFalse(isForIn(n));
    }
  }

  @ org.junit.Test
  public void testIsStatement() {
    Node statementNode = nodeFactory.createExpressionStatement(nodeFactory.createNameNode("statement"));
    Node expressionNode = nodeFactory.createExpressionStatement(nodeFactory.createBinaryOperator(
        nodeFactory.createNameNode("x"),
        Token.OP_PLUS,
        nodeFactory.createNumberLiteral(1)
    ));

    Node parentNode1 = nodeFactory.createBlockStatement(Lists.newArrayList(statementNode));
    Node parentNode2 = nodeFactory.createExpressionStatement(expressionNode);

    assertTrue(isStatement(statementNode));
    assertTrue(isStatement(parentNode1));
    assertFalse(isStatement(expressionNode));
    assertFalse(isStatement(parentNode2));
  }

  @ org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = nodeFactory.createFunctionDeclaration("functionName", null, null, Lists.newArrayList(
        nodeFactory.createParameter(nodeFactory.createNameNode("paramName"), null, null)
    ));

    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = nodeFactory.createExpressionStatement(nodeFactory.createFunctionExpression(null, null, null, null));

    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @ org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = nodeFactory.createScriptStatementList(Lists.newArrayList(
        nodeFactory.createFunctionDeclaration("hoistedFunction", null, null, Lists.newArrayList(
            nodeFactory.createParameter(nodeFactory.createNameNode("param"), null, null)
        )),
        nodeFactory.createExpressionStatement(nodeFactory.createNameNode("callHoistedFunction"))
    ));

    Node functionNode = scriptNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(functionNode));

    Node childFunctionNode = nodeFactory.createExpressionStatement(nodeFactory.createFunctionExpression(null, null, null, null));
    Node functionParentNode = nodeFactory.createBlockStatement(Lists.newArrayList(childFunctionNode));

    Node hoistedFunctionParentNode = nodeFactory.createScriptStatementList(Lists.newArrayList(
        nodeFactory.createExpressionStatement(functionParentNode)
    ));

    Node childFunction = childFunctionNode.getChild(0);
    assertFalse(isHoistedFunctionDeclaration(childFunction));

    Node hoistedFunction = functionParentNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunction));

    Node hoistedFunctionInScript = hoistedFunctionParentNode.getChild(0).getChild(0);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionInScript));
  }
}