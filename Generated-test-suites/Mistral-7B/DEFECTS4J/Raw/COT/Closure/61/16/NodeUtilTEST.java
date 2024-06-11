import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(IF, new int[] {1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(PRINT, new int[] {1, 2});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(ADD, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(NONE, new int[0]);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, new int[] {1, 2, 3, 4});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(FUNCTION, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(
        FUNCTION, new int[] {1, 2, 3, 4}, new Node(NONE, new int[0]));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(
        FUNCTION, new int[] {1, 2, 3, 4}, new Node(NONE, new int[0])).setParent(
        new Node(FUNCTION, new int[] {1, 2}));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}