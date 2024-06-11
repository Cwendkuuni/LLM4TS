import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    Node notForNode = Node.newNode(Token.IF, new int[] {1, 2});

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] {1, 2});
    Node notStatementNode = Node.newNode(Token.AND, new int[] {1, 2, 3});

    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(
        Token.FUNCTION,
        new int[] {1, 2},
        "myFunction",
        new Node[0]);
    Node functionExpressionNode = Node.newNode(
        Token.FUNCTION,
        new int[] {1, 2},
        null,
        new Node[0]);

    assertTrue(isFunctionDeclaration(functionDeclarationNode));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {1});
    Node hoistedFunctionDeclarationNode = Node.newNode(
        Token.FUNCTION,
        new int[] {1, 2},
        "myFunction",
        new Node[0]);
    Node functionDeclarationNode = Node.newNode(
        Token.FUNCTION,
        new int[] {1, 2},
        "myFunction",
        new Node[0]);
    Node notHoistedFunctionDeclarationNode = Node.newNode(
        Token.FUNCTION,
        new int[] {1, 2},
        "myFunction",
        new Node[0]);

    Node functionInsideFunctionNode = Node.newNode(
        Token.FUNCTION,
        new int[] {1, 2},
        null,
        new Node[] {hoistedFunctionDeclarationNode});

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionInsideFunctionNode));
  }

  @Test
  public void testPreconditionsThrownWhenParentIsNull() {
    Node node = Node.newNode(Token.IF, new int[] {1, 2});
    Preconditions.checkState(node.getParent() != null);
  }
}