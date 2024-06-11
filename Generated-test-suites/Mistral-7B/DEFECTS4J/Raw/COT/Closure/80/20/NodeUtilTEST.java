import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(FOR, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(IF, new int[] {0, 1});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(PRINT, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = createNode(IDENT, new int[] {0, 1});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(FUNCTION, new int[] {0, 1, 2}, "test");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(FUNCTION, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createNode(
        FUNCTION, new int[] {0, 1, 2}, "test", null, createNode(SCRIPT, new int[] {}, null));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = createNode(
        FUNCTION, new int[] {0, 1, 2}, "test", null, createNode(BLOCK, new int[] {}, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentIsNull() {
    Node node = createNode(IDENT, new int[] {0, 1});
    Preconditions.checkState(node.getParent() != null);
  }
}