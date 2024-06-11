import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 1});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 1});

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 1});
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 1});
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{0, 0, 1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 1});
    Node nodeExpression = new Node(Token.ADD, new int[]{}, new int[]{0, 0, 1});

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeBlock));
    assertTrue(isStatement(nodeLabel));
    assertFalse(isStatement(nodeFunction));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 1});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 1});

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 1});
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 1});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 1});

    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 1});
    parentFunction.setChild(0, nodeFunctionDeclarationInFunction);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript.getParent()));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction.getParent()));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 1});
    Node parent = null;

    Preconditions.checkState(parent != null);
  }
}