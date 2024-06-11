import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{0, 1, 2});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{0, 1, 2});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{0, 1, 2});
    Node parentOfStatement = Node.newNode(Token.BLOCK, new int[]{0, 1, 2});
    parentOfStatement.addChild(nodeStatement);

    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IF, new int[]{0, 1, 2});
    assertFalse(isStatement(nodeNotStatement));

    Node parentOfNotStatement = Node.newNode(Token.ASSIGN, new int[]{0, 1, 2});
    parentOfNotStatement.addChild(nodeNotStatement);
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new String[]{"a"}, new int[]{1, 0, 1, 1}, new int[]{0, 1, 2, 3});
    Node parentOfFunction = Node.newNode(Token.BLOCK, new int[]{0, 1, 2});
    parentOfFunction.addChild(nodeFunction);

    assertTrue(isFunctionDeclaration(nodeFunction));

    Node nodeNotFunction = Node.newNode(Token.FUNCTION, new int[]{0, 1, 2}, new int[]{0, 1, 2, 3}, new int[]{0, 1, 2, 3});
    assertFalse(isFunctionDeclaration(nodeNotFunction));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new String[]{}, new int[]{1, 0, 1, 1}, new int[]{0, 1, 2, 3});
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new String[]{"a"}, new int[]{1, 0, 1, 1}, new int[]{0, 1, 2, 3});
    Node parentOfFunction = Node.newNode(Token.SCRIPT, new int[]{0, 1, 2});
    parentOfFunction.addChild(nodeFunction);

    assertTrue(isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInsideFunction = Node.newNode(Token.FUNCTION, new String[]{"a"}, new int[]{1, 0, 1, 1}, new int[]{0, 1, 2, 3});
    Node parentOfFunctionInsideFunction = Node.newNode(Token.FUNCTION, new String[]{}, new int[]{1, 0, 1, 1}, new int[]{0, 1, 2, 3});
    parentOfFunctionInsideFunction.addChild(nodeFunction);
    Node parentOfFunctionInsideFunctionParent = Node.newNode(Token.SCRIPT, new int[]{0, 1, 2});
    parentOfFunctionInsideFunctionParent.addChild(parentOfFunctionInsideFunction);

    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
  }
}