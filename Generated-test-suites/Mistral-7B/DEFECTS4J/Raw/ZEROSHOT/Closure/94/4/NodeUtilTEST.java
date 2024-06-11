import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[]{}, new Node[3]);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    Node notStatementNode = Node.newNode(Token.ADD, new int[]{}, new Node[] { Node.newNumber(1), Node.newNumber(2) });

    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);
    Node parentBlock = Node.newNode(Token.BLOCK, new int[]{}, new Node[0]);
    Node parentLabel = Node.newNode(Token.LABEL, new int[]{}, new Node[0]);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(parentScript.getFirstChild()));
    assertTrue(NodeUtil.isStatement(parentBlock.getFirstChild()));
    assertFalse(NodeUtil.isStatement(notStatementNode));
    assertFalse(NodeUtil.isStatement(parentLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {Token.ID, Token.RPAREN, Token.LPAREN, Token.RPAREN, Token.LCURL, Token.RCURL}, new Node[0]);
    functionDeclaration.getFirstChild().setString("testFunction");

    Node functionExpression = Node.newNode(Token.FUNCTION, new int[] {Token.LPAREN, Token.RPAREN, Token.LCURL, Token.RCURL}, new Node[0]);
    functionExpression.getFirstChild().setToken(Token.ID);
    functionExpression.getFirstChild().setString("testFunction");

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {Token.ID, Token.RPAREN, Token.LPAREN, Token.RPAREN, Token.LCURL, Token.RCURL}, new Node[0]);
    hoistedFunctionDeclaration.getFirstChild().setString("testFunction");

    Node functionDeclarationInBlock = Node.newNode(Token.FUNCTION, new int[] {Token.ID, Token.RPAREN, Token.LPAREN, Token.RPAREN, Token.LCURL, Token.RCURL}, new Node[0]);
    functionDeclarationInBlock.getFirstChild().setString("testFunction");

    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);
    Node functionNodeInScript = Node.newNode(Token.FUNCTION, new int[] {Token.ID, Token.RPAREN, Token.LPAREN, Token.RPAREN, Token.LCURL, Token.RCURL}, new Node[0]);
    functionNodeInScript.getFirstChild().setString("testFunction");

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlock));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNodeInScript));
  }
}