import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeOperations;

public class NodeUtilTest {

  private NodeOperations nodeOps = mock(NodeOperations.class);

  @Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, 0, null, null, null);
    assertEquals(true, NodeUtil.isForIn(node));

    Node invalidNode = createNode(Token.IF, 0, null, null, null);
    assertEquals(false, NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.IF, 0, null, null, null);
    Node expressionNode = createNode(Token.PLUS, 0, statementNode, null, null);

    assertEquals(true, NodeUtil.isStatement(statementNode));
    assertEquals(false, NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, 0, "funcName", null, null);
    assertEquals(true, NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode(Token.FUNCTION, 0, null, null, null);
    assertEquals(false, NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = createNode(Token.FUNCTION, 0, "funcName", null, null);
    Node scriptNode = createNode(Token.SCRIPT, 0, null, null, hoistedFunctionNode);
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

    Node functionNodeInFunction = createNode(Token.FUNCTION, 0, "funcName", null, null);
    Node functionNodeParent = createNode(Token.FUNCTION, 0, null, null, functionNodeInFunction);
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInWithNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testPreconditionsCheckStateWithNullParentNode() {
    NodeUtil.isStatement(createNode(Token.SCRIPT, 0, null, null, null));
  }

  private Node createNode(int type, int lineNumber, String id, Node[] children, Node parent) {
    Node node = new Node();
    node.setType(type);
    node.setLineNumber(lineNumber);
    node.setChildren(children);
    node.setParent(parent);
    if (id != null) {
      node.setId(id);
    }
    return node;
  }
}