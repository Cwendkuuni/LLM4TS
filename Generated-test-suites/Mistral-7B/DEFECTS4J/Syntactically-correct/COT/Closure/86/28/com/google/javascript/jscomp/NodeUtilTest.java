import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new int[0]);
    Node threeChildrenNode = new Node(Token.FOR, new int[] {Token.IDENT, Token.SEMICOLON, Token.SEMICOLON});

    assertTrue(isForIn(threeChildrenNode));
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.FOR, new int[0]);
    Node scriptNode = new Node(Token.SCRIPT, new int[0]);
    Node statementNode = new Node(Token.ASSIGN, new int[] {Token.IDENT, Token.NUMBER});
    Node blockNode = new Node(Token.BLOCK, new int[0]);

    Node parentOfStatement = new Node(Token.BLOCK, new int[0]);
    parentOfStatement.addChild(statementNode);

    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(node));
    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(blockNode));
    assertFalse(isStatement(parentOfStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[] {Token.IDENT, Token.SEMICOLON});
    Node functionExpressionNode = new Node(Token.FUNCTION, new int[] {Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.SEMICOLON});

    assertFalse(isFunctionDeclaration(functionExpressionNode));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nullNode = null;
    isFunctionDeclaration(nullNode);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {Token.IDENT, Token.SEMICOLON});
    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {Token.IDENT, Token.SEMICOLON});
    Node notFunctionNode = new Node(Token.IF, new int[0]);

    Node scriptParentNode = new Node(Token.SCRIPT, new int[0]);
    Node functionParentNode = new Node(Token.FUNCTION, new int[0]);
    functionParentNode.addChild(hoistedFunctionDeclarationNode);

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(notFunctionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node nullNode = null;
    isHoistedFunctionDeclaration(nullNode);
  }
}