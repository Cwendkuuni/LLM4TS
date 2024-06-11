import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0, 1, 2}, null);
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {0, 1}, null);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.ASSIGN, new int[] {0, 1}, null);
    Node nodeExpression = Node.newNode(Token.IF, new int[] {0, 1}, null);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {0, 1}, new int[] {1, 2}, null);
    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, new int[] {0, 1, 1}, null, null);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[0], null);
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {0, 1}, new int[] {1, 2}, null);
    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, new int[] {0, 1}, new int[] {1, 2}, null);
    Node nodeFunctionDeclarationInBlock = Node.newNode(
        Token.BLOCK, new int[0], null);
    Node nodeFunctionDeclarationInBlockWithFunction = Node.newNode(
        Token.FUNCTION, new int[] {0, 1}, new int[] {1, 2}, null);
    Node nodeFunctionInBlock = Node.newNode(
        Token.FUNCTION, new int[] {0, 1}, null, null);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpression));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlockWithFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInBlock));
  }
}