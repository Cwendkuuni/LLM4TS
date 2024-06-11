import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.KEYWORD, "for"));
    forNode.setChild(1, new Node(Token.LPAREN));
    forNode.setChild(2, new Node(Token.IDENT, "i"));
    forNode.setChild(3, new Node(Token.RPAREN));
    forNode.setChild(4, new Node(Token.SEMICOLON));

    Node notForInNode = new Node(Token.IF);
    Node ifStatementNode = new Node(Token.FUNCTION, null);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(ifStatementNode));
  }

  @Test
  public void testIsStatement() {
    Node ifStatementNode = new Node(Token.IF);
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL, "label");
    Node functionNode = new Node(Token.FUNCTION, null);
    Node statementNode = new Node(Token.ASSIGN, null);

    assertFalse(NodeUtil.isStatement(ifStatementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, null);
    functionDeclarationNode.setChild(0, new Node(Token.KEYWORD, "function"));
    functionDeclarationNode.setChild(1, new Node(Token.IDENT, "functionName"));
    functionDeclarationNode.setChild(2, new Node(Token.LPAREN));
    functionDeclarationNode.setChild(3, new Node(Token.RPAREN));
    functionDeclarationNode.setChild(4, new Node(Token.LBRACE));
    functionDeclarationNode.setChild(5, new Node(Token.RBRACE));

    Node functionExpressionNode = new Node(Token.FUNCTION, null);
    functionExpressionNode.setChild(0, new Node(Token.IDENT, "functionName"));
    functionExpressionNode.setChild(1, new Node(Token.LPAREN));
    functionExpressionNode.setChild(2, new Node(Token.RPAREN));
    functionExpressionNode.setChild(3, new Node(Token.LBRACE));
    functionExpressionNode.setChild(4, new Node(Token.RBRACE));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null);
    hoistedFunctionDeclarationNode.setChild(0, new Node(Token.KEYWORD, "function"));
    hoistedFunctionDeclarationNode.setChild(1, new Node(Token.IDENT, "functionName"));
    hoistedFunctionDeclarationNode.setChild(2, new Node(Token.LPAREN));
    hoistedFunctionDeclarationNode.setChild(3, new Node(Token.RPAREN));
    hoistedFunctionDeclarationNode.setChild(4, new Node(Token.LBRACE));
    hoistedFunctionDeclarationNode.setChild(5, new Node(Token.RBRACE));

    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null);
    notHoistedFunctionDeclarationNode.setChild(0, new Node(Token.KEYWORD, "function"));
    notHoistedFunctionDeclarationNode.setChild(1, new Node(Token.IDENT, "functionName"));
    notHoistedFunctionDeclarationNode.setChild(2, new Node(Token.LPAREN));
    notHoistedFunctionDeclarationNode.setChild(3, new Node(Token.RPAREN));
    notHoistedFunctionDeclarationNode.setChild(4, new Node(Token.LBRACE));
    notHoistedFunctionDeclarationNode.setParent(new Node(Token.BLOCK));

    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testNullArguments() {
    assertNull(NodeUtil.isForIn(null));
    assertNull(NodeUtil.isStatement(null));
    assertNull(NodeUtil.isFunctionDeclaration(null));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  @Test(expected = NullPointerException.class)
  public void testNullNodeChildren() {
    Node node = new Node(Token.IDENT, "nodeName");
    node.setChild(0, null);

    NodeUtil.isForIn(node);
    NodeUtil.isStatement(node);
    NodeUtil.isFunctionDeclaration(node);
    NodeUtil.isHoistedFunctionDeclaration(node);
  }

  @Test
  public void testNegativeChildIndex() {
    Node node = new Node(Token.IDENT, "nodeName");
    node.setChild(0, new Node(Token.KEYWORD, " keyword"));

    assertNull(NodeUtil.isForIn(node));
    assertNull(NodeUtil.isStatement(node));
    assertNull(NodeUtil.isFunctionDeclaration(node));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(node));

    try {
      NodeUtil.isForIn(node).getChild(-1);
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    try {
      NodeUtil.isStatement(node).getChild(-1);
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    try {
      NodeUtil.isFunctionDeclaration(node).getChild(-1);
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    try {
      NodeUtil.isHoistedFunctionDeclaration(node).getChild(-1);
    } catch (IndexOutOfBoundsException e) {
      // expected
    }
  }
}