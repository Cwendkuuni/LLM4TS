import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createLabel;

import org.junit.Test;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createToken(Token.FOR);
    Node child1 = createToken(Token.IDENTIFIER, "a");
    Node child2 = createToken(Token.KEYWORD, "in");
    Node child3 = createToken(Token.LEFT_PAREN);
    Node child4 = createToken(Token.IDENTIFIER, "array");
    Node child5 = createToken(Token.RIGHT_PAREN);

    Preconditions.checkState(node.getChildCount() == 3);
    node.setChildren(child1, child2, child3, child4, child5);

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_False() {
    Node node = createToken(Token.FOR);
    Node child1 = createToken(Token.KEYWORD, "let");
    Node child2 = createToken(Token.IDENTIFIER, "a");
    Node child3 = createToken(Token.EQUALS);
    Node child4 = createToken(Token.NUMBER, 10);
    Node child5 = createToken(Token.SEMICOLON);
    Node child6 = createToken(Token.KEYWORD, "if");
    Node child7 = createToken(Token.LEFT_PAREN);
    Node child8 = createToken(Token.BOOLEAN, true);
    Node child9 = createToken(Token.RIGHT_PAREN);
    Node child10 = createToken(Token.LEFT_BRACE);
    Node child11 = createToken(Token.RETURN);
    Node child12 = createToken(Token.NUMBER, 20);
    Node child13 = createToken(Token.SEMICOLON);
    Node child14 = createToken(Token.RIGHT_BRACE);

    Preconditions.checkState(node.getChildCount() > 3);
    node.setChildren(child1, child2, child3, child4, child5, child6, child7, child8, child9, child10, child11, child12, child13, child14);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createToken(Token.KEYWORD, "function");
    Node child1 = createToken(Token.IDENTIFIER, "name");
    Node child2 = createToken(Token.LEFT_PAREN);
    Node child3 = createToken(Token.RIGHT_PAREN);
    Node child4 = createToken(Token.LEFT_BRACE);
    Node child5 = createToken(Token.RETURN);
    Node child6 = createToken(Token.NUMBER, 42);
    Node child7 = createToken(Token.SEMICOLON);
    Node child8 = createToken(Token.RIGHT_BRACE);

    Preconditions.checkState(node.getParent() != null);
    node.setParent(createScript());

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_False() {
    Node node = createToken(Token.KEYWORD, "function");
    Node child1 = createToken(Token.IDENTIFIER, "name");
    Node child2 = createToken(Token.LEFT_PAREN);
    Node child3 = createToken(Token.RIGHT_PAREN);
    Node child4 = createToken(Token.IDENTIFIER, "x");
    Node child5 = createToken(Token.EQUALS);
    Node child6 = createToken(Token.NUMBER, 5);

    Preconditions.checkState(node.getParent() != null);
    node.setParent(createExpressionStatement(createAssignment(node, child4, child5, child6)));

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createFunction(createIdentifier("name"), createArray(), createBlock());

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = createFunctionExpression(createIdentifier("name"), createArray(), createBlock());

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createFunctionDeclaration(createIdentifier("name"), createArray());

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = createHoistedFunctionExpression(createIdentifier("name"), createArray());

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  private Node createFunctionDeclaration(Node name, Node[] args) {
    Node node = createFunction(name, args, createBlock());

    node.setParent(createScript());

    return node;
  }

  private Node createFunctionExpression(Node name, Node[] args, Node body) {
    Node node = createFunction(name, args, body);

    node.setExpression(true);

    return node;
  }

  private Node createHoistedFunctionExpression(Node name, Node[] args) {
    Node functionNode = createFunctionExpression(name, args, createBlock());

    Node parent = createLabel("label");
    Node scriptNode = createScript();
    Node blockNode = createBlock();
    Node functionDeclarationNode = createFunctionDeclaration(name, args);

    functionDeclarationNode.setParent(scriptNode);
    parent.setParent(scriptNode);
    scriptNode.setChildren(parent, functionDeclarationNode);
    functionNode.setParent(blockNode);
    blockNode.setChildren(functionNode);
    parent.setChildren(functionNode);
    scriptNode.setChildren(parent, functionDeclarationNode);

    return functionDeclarationNode;
  }

  private Node createAssignment(Node node, Node target, Node operator, Node value) {
    Node expr1 = createBinaryOperatorExpression(node, operator, target, value);

    Node parent = node.getParent();
    Node statement = createExpressionStatement(expr1);

    parent.setChildren(statement);

    return statement;
  }

  private Node createExpressionStatement(Node expression) {
    Node node = createToken(Token.EXPRESSION_STATEMENT);
    node.setChildren(expression);

    return node;
  }

  private Node createBinaryOperatorExpression(Node node, Node operator, Node left, Node right) {
    Node expr1 = createBinaryExpression(node, left, operator, right);

    Node parent = node.getParent();
    Node expr2 = createToken(Token.EXPRESSION);
    Node expression = createBinaryExpression(expr2, expr1, null, null);

    parent.setChildren(expression);

    return expression;
  }

  private Node createBinaryExpression(Node node, Node left, Node operator, Node right) {
    Node expr1 = createToken(Token.EXPRESSION);
    Node expr2 = createToken(Token.EXPRESSION);

    expr1.setChildren(left);
    expr2.setChildren(right);

    node.setChildren(operator, expr1, expr2);

    return node;
  }

  private Node createIdentifier(String name) {
    return createToken(Token.IDENTIFIER, name);
  }

  private Node createArray() {
    return createToken(Token.ARRAY);
  }

  private Node[] createArray(Node... elements) {
    Node node = createToken(Token.EXPRESSION_STATEMENT);
    node.setChildren(createToken(Token.ARRAY_CREATION, null, null), createToken(Token.LEFT_BRACE));

    for (Node element : elements) {
      node.getLastChild().setChildren(element);
    }

    node.getLastChild().setChildren(createToken(Token.RIGHT_BRACE));

    return new Node[] { node };
  }

  private Node createScript() {
    return createToken(Token.SCRIPT);
  }

  private Node createBlock() {
    return createToken(Token.BLOCK);
  }

  private Node createLabel(String name) {
    return createToken(Token.LABEL, name);
  }
}