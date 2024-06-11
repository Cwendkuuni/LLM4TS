import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);
    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParentNode() {
    Node node = new Node(Token.IF, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);
    Preconditions.checkState(node.getParent() != null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);
    Node nodeFunctionDeclarationInFunction = new Node(
        Token.FUNCTION, new int[]{}, new int[]{}, new int[]{}, new int[]{}, new Node(Token.SCRIPT, new int[]{}, null, null, null, null));

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullParentNode() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{}, new int[]{}, null);
    isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }
}