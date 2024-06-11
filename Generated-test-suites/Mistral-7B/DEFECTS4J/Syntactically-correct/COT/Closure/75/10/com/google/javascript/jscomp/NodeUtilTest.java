import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{3});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[]{}, new int[]{});
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{});
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node(Token.PRINT, new int[]{}, new int[]{});
    Preconditions.checkState(node.getParent() != null);
    Preconditions.checkStateFailure();
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"arg1"}, new int[]{1, 2, 3});
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(Token.IF, new int[]{}, new int[]{});
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationOutsideFunction = new Node(Token.FUNCTION, new int[]{"arg1"}, new int[]{1, 2, 3});
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationOutsideFunction));

    Node nodeFunctionDeclarationInsideFunction = new Node(
        Token.FUNCTION, new int[]{"arg1"}, new int[]{1, 2, 3});
    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{});
    parentFunction.addChild(nodeFunctionDeclarationInsideFunction);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInsideFunction));
  }
}