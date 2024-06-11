import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3}); // FOR node with 3 children
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2}); // IF node with 2 children
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[] {1, 2}); // PRINT node as a statement
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}); // FUNCTION node as an expression
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(Token.SCRIPT, new int[] {1});
    Preconditions.checkState(node.getParent() != null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3, 4, 5}); // Function node with children
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}); // Function node without children
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    Node nodeIfStatement = new Node(Token.IF, new int[] {1, 2, 3}); // IF node as a statement
    assertFalse(isFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[] {1, 2, 3, 4, 5, 6}, new Node(Token.SCRIPT, new int[0]));
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[] {1, 2, 3, 4, 5, 6}, new Node(Token.BLOCK, new int[0]));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(
        Token.FUNCTION, new int[] {1, 2, 3, 4, 5, 6}, new Node(
            Token.FUNCTION, new int[] {1, 2, 3}, new Node[0]));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}