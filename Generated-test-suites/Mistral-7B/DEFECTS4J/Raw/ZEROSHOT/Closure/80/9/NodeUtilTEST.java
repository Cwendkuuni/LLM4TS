import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createLabel;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createToken(Token.FOR);
    Node arrayNode = createToken(Token.IDENTIFIER, "array");
    Node inNode = createToken(Token.KEYWORD, "in");
    Node bodyNode = createToken(Token.IDENTIFIER, "body");

    Node forNodeWithThreeChildren = createNode(forNode, arrayNode, inNode, bodyNode);
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));

    Node forNodeWithTwoChildren = createNode(forNode, arrayNode, bodyNode);
    assertFalse(NodeUtil.isForIn(forNodeWithTwoChildren));

    Node forNodeWithFourChildren = createNode(forNode, arrayNode, inNode, bodyNode, createToken(Token.SEMICOLON));
    assertFalse(NodeUtil.isForIn(forNodeWithFourChildren));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createToken(Token.IDENTIFIER, "statement");
    Node expressionNode = createToken(Token.IDENTIFIER, "expression");

    Node statementParentScript = createScript();
    statementParentScript.addChild(statementNode);

    Node statementParentBlock = createBlock();
    statementParentBlock.addChild(statementNode);

    Node statementParentLabel = createLabel();
    statementParentLabel.addChild(statementNode);

    Node expressionParentScript = createScript();
    expressionParentScript.addChild(expressionNode);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(statementNode.getParent()));

    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(expressionNode.getParent()));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createFunction(createToken(Token.IDENTIFIER, "functionName"), createBlock());
    Node functionExpression = createExpressionStatement(createFunction(createToken(Token.IDENTIFIER, "functionName"), createBlock()));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = createScript();

    Node functionDeclaration = createFunction(createToken(Token.IDENTIFIER, "functionName"), createBlock());
    script.addChild(functionDeclaration);

    Node functionDeclarationInFunction = createNode(
        createFunction(createToken(Token.IDENTIFIER, "outerFunctionName"), createBlock()),
        functionDeclaration);
    script.addChild(functionDeclarationInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  private Node createNode(Node... children) {
    Node node = createToken(Token.IDENTIFIER, "node");
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private Node createNode(Node parent, Node... children) {
    Node node = createNode(children);
    parent.addChild(node);
    return node;
  }

  private Node createExpressionStatement(Node expression) {
    return createBlock(expression);
  }
}