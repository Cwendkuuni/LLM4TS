import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node("for", new int[]{}, new int[]{Token.KEYWORD, Token.LEFT_PAREN, Token.IDENT, Token.SEMICOLON, Token.EXPRESSION, Token.SEMICOLON, Token.RIGHT_PAREN});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn1 = new Node("if", new int[]{}, new int[]{Token.KEYWORD, Token.LEFT_PAREN, Token.EXPRESSION, Token.RIGHT_PAREN, Token.LEFT_BRACE, Token.RIGHT_BRACE});
    assertFalse(NodeUtil.isForIn(nodeNotForIn1));

    Node nodeNotForIn2 = new Node("function", new int[]{}, new int[]{Token.KEYWORD, Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE, Token.RIGHT_BRACE});
    assertFalse(NodeUtil.isForIn(nodeNotForIn2));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node("if", new int[]{}, new int[]{Token.KEYWORD, Token.LEFT_PAREN, Token.EXPRESSION, Token.RIGHT_PAREN, Token.LEFT_BRACE, Token.RIGHT_BRACE});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node("+", new int[]{}, new int[]{Token.PLUS, Token.NUMBER, Token.NUMBER});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node("function", new int[]{}, new int[]{Token.KEYWORD, Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE, Token.RIGHT_BRACE});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node("function", new int[]{}, new int[]{Token.KEYWORD, Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node("function", new int[]{}, new int[]{Token.KEYWORD, Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE, Token.RIGHT_BRACE});
    Node parentScript = new Node("script", new int[]{}, new int[]{});
    parentScript.appendChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = new Node("function", new int[]{}, new int[]{Token.KEYWORD, Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE, Token.RIGHT_BRACE});
    Node parentFunction = new Node("function", new int[]{}, new int[]{Token.KEYWORD, Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE, Token.RIGHT_BRACE});
    parentFunction.appendChild(nodeNotHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}