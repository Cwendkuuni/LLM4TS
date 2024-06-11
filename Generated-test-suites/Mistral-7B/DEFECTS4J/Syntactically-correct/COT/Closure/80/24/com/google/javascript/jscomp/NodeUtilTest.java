import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;
import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, new int[] { 0, 1, 2 });
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createNode(Token.IF, new int[] { 0 });
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.PRINT, new int[] { 0 });
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode(Token.FUNCTION, new int[] { 0, 1, 2 });
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParentNode() {
    Node parentLessNode = createNode(Token.PRINT, new int[] { 0 });
    NodeUtil.isStatement(parentLessNode);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, new int[] { 0, 1, 2 });
    functionNode.setProperty("id", "myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node notFunctionNode = createNode(Token.IF, new int[] { 0 });
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = createNode(Token.FUNCTION, new int[] { 0, 1, 2, 3 });
    Node functionScopeNode = createNode(Token.SCRIPT, new int[] { 0 });
    Node functionNode = createNode(Token.FUNCTION, new int[] { 0, 1, 2, 3 });
    functionNode.setParent(functionScopeNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    functionScopeNode.setProperty("parent", createNode(Token.FUNCTION, new int[] { 0, 1, 2, 3 }));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}