import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isStatement;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isHoistedFunctionDeclaration;
import com.google.javascript.jscomp.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {0}, new int[] {0}, 0, 0);
    Node nodeNotForIn = new Node(Token.IF, new int[] {0}, new int[] {0}, 0, 0);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[] {0}, new int[] {0}, 0, 0);
    Node nodeExpression = new Node(Token.FUNCTION, new int[] {0}, new int[] {0}, 0, 0);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, new int[] {0}, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1}, new int[] {0}, 0, 0);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[] {1, 2}, new int[] {0}, 0, 0, null, null, null, null);
    Node nodeFunctionDeclarationInFunction = new Node(
        Token.FUNCTION, new int[] {1, 2}, new int[] {0}, 0, 0, null, null, null, null);
    Node nodeFunctionDeclarationInScript = new Node(
        Token.FUNCTION, new int[] {1, 2}, new int[] {0}, 0, 0, null, null, null, null);

    nodeFunctionDeclarationInFunction.setParent(new Node(Token.FUNCTION, new int[] {0}, new int[] {0}, 0, 0, null, null, null, null));
    nodeFunctionDeclarationInScript.setParent(new Node(Token.SCRIPT, new int[] {0}, new int[] {0}, 0, 0, null, null, null, null));

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
  }

  @Test
  public void testIsForInWithEmptyChildren() {
    Node nodeForInWithEmptyChildren = new Node(Token.FOR, new int[] {0}, new int[] {}, 0, 0);

    assertFalse(isForIn(nodeForInWithEmptyChildren));
  }

  @Test
  public void testIsStatementWithEmptyChildren() {
    Node nodeStatementWithEmptyChildren = new Node(Token.IF, new int[] {}, new int[] {}, 0, 0);

    assertTrue(isStatement(nodeStatementWithEmptyChildren));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNegativeChildCount() {
    Node nodeFunctionDeclarationWithNegativeChildCount = new Node(
        Token.FUNCTION, new int[] {-1}, new int[] {0}, 0, 0, null, null, null, null);

    isFunctionDeclaration(nodeFunctionDeclarationWithNegativeChildCount);
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNonFunctionNodeType() {
    Node nodeNotFunctionNode = new Node(Token.IF, new int[] {0}, new int[] {0}, 0, 0);

    isFunctionDeclaration(nodeNotFunctionNode);
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNonFunctionNodeType() {
    Node nodeNotFunctionNode = new Node(Token.IF, new int[] {0}, new int[] {0}, 0, 0);

    isHoistedFunctionDeclaration(nodeNotFunctionNode);
  }
}