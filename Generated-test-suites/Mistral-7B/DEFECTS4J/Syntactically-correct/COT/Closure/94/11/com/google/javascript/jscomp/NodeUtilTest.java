import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.BLOCK;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.FOR;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.SCRIPT;

import com.google.common.base.Preconditions;

import org.junit.Test;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  /**
   * Test method for {@link NodeUtil#isForIn(Node)}.
   */
  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForStatement(new Node(Token.VAR, "i"), new Node(Token.IDENT, "arr"),
        new Node(Token.ASSIGN, new Node(Token.IDENT, "arr"), new Node(Token.INDEX, new Node(Token.IDENT, "arr"),
            new Node(Token.INTEGER, 0))), new Node(Token.BLOCK, null), null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(new Node(Token.IDENT, "x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  /**
   * Test method for {@link NodeUtil#isStatement(Node)}.
   */
  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newAssign(Node.newVariable("x"),
        Node.newNumberLiteral(5)));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newExpression(Node.newAdd(Node.newNumberLiteral(2),
        Node.newNumberLiteral(3)));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  /**
   * Test method for {@link NodeUtil#isFunctionDeclaration(Node)}.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null,
        new Node(Token.VAR, "x"), new Node(Token.BLOCK, null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = Node.newExpressionStatement(
        Node.newAssign(Node.newVariable("x"), Node.newNumberLiteral(5)));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  /**
   * Test method for {@link NodeUtil#isHoistedFunctionDeclaration(Node)}.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(null, null,
        new Node(Token.VAR, "x"), new Node(Token.BLOCK, null), null);
    Node scriptNode = Node.newScript(null);
    scriptNode.setFirstChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(null, null,
        new Node(Token.VAR, "x"), new Node(Token.BLOCK, null), Node.newExpressionStatement(
            Node.newAssign(Node.newVariable("y"), Node.newNumberLiteral(5))));
    Node functionNode = Node.newFunction(null, null, null, null);
    functionNode.setFirstChild(nodeNotHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  /**
   * Test edge case for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * A function expression can't be a function declaration as it doesn't have a name.
   */
  @Test
  public void testIsFunctionDeclarationWithFunctionExpression() {
    Node nodeFunctionExpression = Node.newExpressionFunction(Node.newVariable("x"), null,
        Node.newBlock(null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  /**
   * Test edge case for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * A function declaration with empty parameter list can exist.
   */
  @Test
  public void testIsFunctionDeclarationWithEmptyParameterList() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, null, new Node(Token.BLOCK, null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  /**
   * Test edge case for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * A function declaration with a single statement can exist.
   */
  @Test
  public void testIsFunctionDeclarationWithSingleStatement() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, null, Node.newReturnStatement(
        Node.newNumberLiteral(5)));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  /**
   * Test edge case for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * A function declaration with multiple statements can exist.
   */
  @Test
  public void testIsFunctionDeclarationWithMultipleStatements() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, null, Node.newBlock(
        Node.newReturnStatement(Node.newNumberLiteral(5)),
        Node.newExpressionStatement(Node.newAssign(Node.newVariable("x"), Node.newNumberLiteral(4)))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  /**
   * Test edge case for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * A function declaration with a labeled statement can exist.
   */
  @Test
  public void testIsFunctionDeclarationWithLabeledStatement() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, null, Node.newLabeledStatement(
        new Node(Token.IDENT, "label"), Node.newReturnStatement(Node.newNumberLiteral(5))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  /**
   * Test edge case for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * A function declaration with a block statement containing a for statement can exist.
   */
  @Test
  public void testIsFunctionDeclarationWithForStatement() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, null, Node.newBlock(
        Node.newForStatement(new Node(Token.VAR, "i"), Node.newIdent("arr"), Node.newAssign(
            Node.newIdent("arr"), Node.newIndex(Node.newIdent("arr"), Node.newIntegerLiteral(0))),
            Node.newBlock(Node.newReturnStatement(Node.newNumberLiteral(5)),
                Node.newExpressionStatement(Node.newAssign(Node.newVariable("x"), Node.newNumberLiteral(4)))))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  /**
   * Test exception handling for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * A null node should throw a Preconditions check state exception.
   */
  @Test(expected = NullPointerException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  /**
   * Test exception handling for {@link NodeUtil#isFunctionDeclaration(Node)}.
   * An invalid node type should throw a Preconditions check state exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithInvalidNodeType() {
    Node nodeInvalidType = Node.newExpressionStatement(Node.newAssign(Node.newVariable("x"),
        Node.newNumberLiteral(5)));
    nodeInvalidType.setType(Token.IF);
    NodeUtil.isFunctionDeclaration(nodeInvalidType);
  }
}