import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new int[]{}, new int[0]);
    assertTrue(isForIn(node));

    Node invalidNode1 = new Node(Token.IF, new int[]{}, new int[0]);
    assertFalse(isForIn(invalidNode1));

    Node invalidNode2 = new Node(Token.ASSIGN, new int[]{}, new int[0]);
    assertFalse(isForIn(invalidNode2));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{}, new int[0]);
    assertTrue(isStatement(statementNode));

    Node notStatementNode = new Node(Token.ASSIGN, new int[]{}, new int[0]);
    assertFalse(isStatement(notStatementNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    Node node = null;
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    functionDeclarationNode.setName("testFunction");
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    functionExpressionNode.setName("testFunction");
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    hoistedFunctionDeclarationNode.setName("testFunction");
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    scriptNode.addChild(hoistedFunctionDeclarationNode);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionNodeInsideFunctionNode = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    functionNodeInsideFunctionNode.setName("testFunction");
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    functionNode.addChild(functionNodeInsideFunctionNode);
    assertFalse(isHoistedFunctionDeclaration(functionNodeInsideFunctionNode));
  }
}