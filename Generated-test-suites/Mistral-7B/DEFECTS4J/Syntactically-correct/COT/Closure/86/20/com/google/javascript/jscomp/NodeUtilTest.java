import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createNode;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, null, null, null, null);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));

    node.setChildCount(2);
    assertFalse(NodeUtil.isForIn(node));

    node.setType(Token.IF);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createNode(Token.IF, null, null, null, null);
    Node parent = createNode(Token.SCRIPT, null, node, null, null);
    node.setParent(parent);
    assertTrue(NodeUtil.isStatement(node));

    parent.setType(Token.BLOCK);
    assertTrue(NodeUtil.isStatement(node));

    parent.setType(Token.LABEL);
    assertTrue(NodeUtil.isStatement(node));

    parent.setType(Token.FUNCTION);
    assertFalse(NodeUtil.isStatement(node));

    parent.setType(Token.ASSIGN);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, "foo", null, null, null);
    node.setParent(createNode(Token.SCRIPT, null, node, null, null));
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node.setParent(createNode(Token.BLOCK, null, node, null, null));
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node.setParent(createNode(Token.LABEL, "foo", null, node, null));
    assertFalse(NodeUtil.isFunctionDeclaration(node));

    node.setType(Token.FUNCTION_EXPRESSION);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, "foo", null, null, null);
    Node parent = createNode(Token.SCRIPT, null, node, null, null);
    node.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    parent = createNode(Token.FUNCTION, null, null, null, node);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckState() {
    Node node = createNode(Token.SCRIPT, null, null, null, null);
    NodeUtil.isStatement(node);
  }
}