import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node nodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENT, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.EXPR_LIST)});

    assertTrue(isForIn(nodeWithThreeChildren));
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.ASSIGN, new Node[]{new Node(Token.IDENT, "x"), new Node(Token.INT_LITERAL, 5)});
    Node expressionNode = new Node(Token.PLUS, new Node[]{new Node(Token.IDENT, "x"), new Node(Token.INT_LITERAL, 5)});

    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(expressionNode));
    assertTrue(isStatement(node));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{});
    Node functionExpressionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{new Node(Token.EXPR_LIST)});

    assertTrue(isFunctionDeclaration(functionDeclarationNode));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{});
    Node functionExpressionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{new Node(Token.EXPR_LIST)});
    Node functionDeclarationInFunctionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "outerFunction")},
        new Node[]{functionDeclarationNode});

    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}