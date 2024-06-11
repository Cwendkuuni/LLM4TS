import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{0, 1, 2});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{0, 1, 2, 3, 4});

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{0, 1, 2, 3, 4});
    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[]{0, 1, 2});
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
    Node nodeBlock = Node.newNode(Token.BLOCK, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
    Node nodeLabel = Node.newNode(Token.LABEL, new int[]{0, 1, 2, 3, 4});

    Node nodeWithNoParent = Node.newNode(Token.IF, new int[]{0});

    assertTrue(isStatement(nodeStatement));
    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeBlock));
    assertTrue(isStatement(nodeLabel));
    assertFalse(isStatement(nodeExpression));
    assertFalse(isStatement(nodeWithNoParent));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1, 2, 3}, "testFunction");
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{0, 1, 2}, null);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[]{0, 1, 2, 3}, "testFunction");
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{0, 1, 2}, "testFunction",
        Node.newNode(Token.FUNCTION, new int[]{0, 1, 2}, null));

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}