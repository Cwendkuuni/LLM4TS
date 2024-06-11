import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{1, 2, 3});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{1, 2, 3});
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{1, 2, 3});
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{1, 2, 3});
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{1, 2, 3});
    Node nodeExpression = new Node(Token.ADD, new int[]{}, new int[]{1, 2, 3});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3});

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeBlock));
    assertTrue(isStatement(nodeLabel));
    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
    assertFalse(isStatement(nodeFunction));
    assertFalse(isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"identifier1"}, new int[]{1, 2, 3});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{1, 2, 3});
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{1, 2, 3});
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{1, 2, 3});
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3});

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeScript));
    assertFalse(isFunctionDeclaration(nodeBlock));
    assertFalse(isFunctionDeclaration(nodeLabel));
    assertFalse(isFunctionDeclaration(nodeIf));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{"identifier1"}, new int[]{1, 2, 3});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{1, 2, 3});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPreconditionsCheckStateThrowsException() {
    Node node = new Node(Token.SCRIPT, new int[]{}, new int[]{1, 2, 3});
    Preconditions.checkState(node.getParent() == null);
  }
}