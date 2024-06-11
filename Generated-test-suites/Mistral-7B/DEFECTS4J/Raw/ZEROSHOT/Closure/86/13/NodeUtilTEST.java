import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, null, 1, 2, 3);
    Node nodeNotForIn = new Node(Token.IF, null, null, 1, 2, 3);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null, null, 1, 2, 3);
    Node nodeExpression = new Node(Token.FUNCTION, null, null, 1, 2, 3);
    Node nodeScript = new Node(Token.SCRIPT, null, null, 1, 2, 3);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
    assertTrue(isStatement(nodeScript));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node(Token.IF, null, null, 1, 2, 3);
    Preconditions.checkState(node.getParent() != null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "myFunction", null, 1, 2, 3);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, 1, 2, 3);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, "myFunction", null, 1, 2, 3);
    Node nodeFunctionDeclarationInFunction = new Node(
        Token.FUNCTION, null, new Node(Token.FUNCTION, null, null, 1, 2, 3), 1, 2, 3);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}