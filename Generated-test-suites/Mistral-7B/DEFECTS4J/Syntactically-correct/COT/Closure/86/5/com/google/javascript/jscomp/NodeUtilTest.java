import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[0]);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[1]);
    Node nodeNotStatement = new Node(Token.IDENT, new int[]{1}, new int[0]);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(Token.IF, new int[]{}, new int[0]);
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1}, new int[1]);
    Node nodeNotFunctionDeclaration = new Node(Token.IF, new int[]{}, new int[0]);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1}, new int[1]);
    Node nodeFunctionDeclarationInFunction =
        new Node(
            Token.FUNCTION,
            new int[]{1, 1},
            new int[]{1, 1, 1},
            null,
            nodeFunctionDeclaration);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}