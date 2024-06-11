import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createNode;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, null, createNode(Token.IDENT, "for"), null, createNode(Token.ASSIGN, null, createNode(Token.IDENT, "i"), null, createNode(Token.KEYWORD, "in"), null, createNode(Token.IDENT, "array"), null));
    assertTrue(NodeUtil.isForIn(node));

    Node nodeFalse = createNode(Token.FOR, null, createNode(Token.IDENT, "for"), null, createNode(Token.ASSIGN, null, createNode(Token.IDENT, "i"), null, createNode(Token.KEYWORD, "as"), null, createNode(Token.IDENT, "type"), null));
    assertFalse(NodeUtil.isForIn(nodeFalse));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.ASSIGN, null, createNode(Token.IDENT, "x"), null, createNode(Token.IDENT, "10"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode(Token.ADD, null, createNode(Token.IDENT, "x"), null, createNode(Token.INT_LITERAL, 5));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "add"), null, createNodeList(
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "x")),
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "y"))), null, null, createNodeList(
        createNode(Token.BLOCK, null, createNode(Token.RETURN, null, createNode(Token.ADD, null, createNode(Token.IDENT, "x"), createNode(Token.INT_LITERAL, 5)))),
        null), null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "add"), null, createNodeList(
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "x")),
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "y"))), null, null, createNodeList(
        createNode(Token.BLOCK, null, createNode(Token.RETURN, null, createNode(Token.ADD, null, createNode(Token.IDENT, "x"), createNode(Token.INT_LITERAL, 5)))),
        null), null, createNode(Token.IDENT, "x"));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "add"), null, createNodeList(
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "x")),
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "y"))), null, null, createNodeList(
        createNode(Token.BLOCK, null, createNode(Token.RETURN, null, createNode(Token.ADD, null, createNode(Token.IDENT, "x"), createNode(Token.INT_LITERAL, 5)))),
        null), null);
    Node parent = createNode(Token.SCRIPT, null, null, null, createNodeList(nodeHoistedFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "add"), null, createNodeList(
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "x")),
        createNode(Token.FORMAL_PARAM, null, null, createNode(Token.IDENT, "y"))), null, null, createNodeList(
        createNode(Token.BLOCK, null, createNode(Token.RETURN, null, createNode(Token.ADD, null, createNode(Token.IDENT, "x"), createNode(Token.INT_LITERAL, 5)))),
        null), null);
    Node parentNotHoisted = createNode(Token.BLOCK, null, null, null, createNodeList(nodeNotHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  private NodeList createNodeList(Node... nodes) {
    NodeList list = NodeFactory.createNodeList();
    for (Node node : nodes) {
      list.addLast(node);
    }
    return list;
  }
}