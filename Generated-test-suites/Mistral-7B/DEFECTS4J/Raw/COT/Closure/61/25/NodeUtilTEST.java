import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.makeNode;

import org.junit.Test;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = makeNode(createToken(Token.FOR, null));
    Node forNodeWithThreeChildren = makeNode(createToken(Token.FOR, null), createToken(Token.IDENT, ""), createToken(Token.SEMICOLON, ""));
    Node notForNode = makeNode(createToken(Token.IF, null));

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = makeNode(createToken(Token.SCRIPT, null));
    Node blockNode = makeNode(createToken(Token.BLOCK, null));
    Node labelNode = makeNode(createToken(Token.LABEL, ""), createToken(Token.IDENT, ""));
    Node ifNode = makeNode(createToken(Token.IF, null), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""));

    Node statementNode1 = makeNode(createToken(Token.IDENT, "x"), createToken(Token.ASSIGN, "="), createToken(Token.NUM, "5"));
    Node statementNode2 = makeNode(createToken(Token.IDENT, "x"), createToken(Token.ASSIGN, "="), makeNode(createToken(Token.IDENT, "y"), createToken(Token.PLUS, "+"), createToken(Token.NUM, "3")));
    Node expressionNode = makeNode(createToken(Token.IDENT, "x"), createToken(Token.PLUS, "+"), createToken(Token.NUM, "5"));

    assertTrue(NodeUtil.isStatement(statementNode1));
    assertTrue(NodeUtil.isStatement(statementNode2));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(ifNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = makeNode(createToken(Token.FUNCTION, null), createToken(Token.IDENT, "functionName"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""));
    Node functionExpressionNode = makeNode(createToken(Token.FUNCTION, null), createToken(Token.IDENT, "functionName"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RPAREN, ""));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node functionDeclarationNode = makeNode(createToken(Token.FUNCTION, null), createToken(Token.IDENT, "functionName"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""));
    Node nullParentNode = null;
    Preconditions.checkState(NodeUtil.isFunctionDeclaration(functionDeclarationNode), "function declaration should be a statement");
    Preconditions.checkState(functionDeclarationNode.getParent() != nullParentNode, "function declaration's parent should not be null");
    functionDeclarationNode.setParent(nullParentNode);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = makeNode(createToken(Token.SCRIPT, null), createToken(Token.FUNCTION, null), createToken(Token.IDENT, "functionName"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""));
    Node functionDeclarationInFunctionNode = makeNode(createToken(Token.FUNCTION, null), createToken(Token.IDENT, "functionName"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RPAREN, ""));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node scriptNode = makeNode(createToken(Token.SCRIPT, null), createToken(Token.FUNCTION, null), createToken(Token.IDENT, "functionName"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""));
    Node nullParentNode = null;
    Preconditions.checkState(NodeUtil.isHoistedFunctionDeclaration(scriptNode), "function declaration in script should be hoisted");
    scriptNode.setParent(nullParentNode);
  }
}