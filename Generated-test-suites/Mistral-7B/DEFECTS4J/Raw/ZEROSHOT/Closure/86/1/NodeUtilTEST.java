import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 1});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 1});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 1});
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.PLUS, new int[]{}, new int[]{0, 0, 1, 2});
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"myFunction"}, new int[]{0, 0, 1});
    Node nodeFunctionInExpression = new Node(
        Token.FUNCTION, new int[]{}, new int[]{0, 0, 1, 1});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 1});

    nodeFunction.setParent(nodeScript);
    assertTrue(isFunctionDeclaration(nodeFunction));
    assertFalse(isFunctionDeclaration(nodeFunctionInExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"myFunction"}, new int[]{0, 0, 1});
    Node nodeFunctionInScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 1});
    Node nodeFunctionInFunction = new Node(
        Token.FUNCTION, new int[]{"myFunctionInFunction"}, new int[]{0, 0, 1});

    nodeFunction.setParent(nodeFunctionInScript);
    assertTrue(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionIsForIn() {
    Node nodeNull = null;
    isForIn(nodeNull);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionIsStatement() {
    Node nodeNull = null;
    isStatement(nodeNull);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionIsFunctionDeclaration() {
    Node nodeNull = null;
    isFunctionDeclaration(nodeNull);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionIsHoistedFunctionDeclaration() {
    Node nodeNull = null;
    isHoistedFunctionDeclaration(nodeNull);
  }

  @Test(expected = AssertionError.class)
  public void testAssertionErrorParentIsNull() {
    Node node = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 1});
    Preconditions.checkState(node.getParent() != null);
    node.setParent(null);
  }
}