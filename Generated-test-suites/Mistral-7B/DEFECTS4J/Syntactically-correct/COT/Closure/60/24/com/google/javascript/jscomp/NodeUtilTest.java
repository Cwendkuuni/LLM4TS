import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(node));

    node.setChildCount(2);
    assertFalse(NodeUtil.isForIn(node));

    node.setChildCount(3);
    Node child1 = createNode(Token.FOR, null, null, null, null, null, null, null);
    Node child2 = createNode(Token.IDENT, "name", null, null, null, null, null, null);
    Node child3 = createNode(Token.SEMICOLON, null, null, null, null, null, null, null);
    node.setChild(0, child1);
    node.setChild(1, child2);
    node.setChild(2, child3);

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createNode(Token.IF, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(node));

    node = createNode(Token.IDENT, "name", null, null, null, null, null, null);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, "name", null, null, null, null, null, null);
    node.setChildCount(3);
    Node child1 = createNode(Token.IDENT, "name", null, null, null, null, null, null);
    Node child2 = createNode(Token.OPEN_PAREN, null, null, null, null, null, null, null);
    Node child3 = createNode(Token.CLOSE_PAREN, null, null, null, null, null, null, null);
    node.setChild(1, child1);
    node.setChild(2, child2);

    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node.setParent(createNode(Token.SCRIPT, null, null, null, null, null, null, null));
    Preconditions.checkState(node.getParent() != null);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withFunctionInFunction() {
    Node topLevelFunction = createNode(Token.FUNCTION, "topLevelFunction", null, null, null, null, null, null);
    topLevelFunction.setChildCount(3);
    Node child1 = createNode(Token.IDENT, "name", null, null, null, null, null, null);
    Node child2 = createNode(Token.OPEN_PAREN, null, null, null, null, null, null, null);
    Node child3 = createNode(Token.CLOSE_PAREN, null, null, null, null, null, null, null);
    Node innerFunction = createNode(Token.FUNCTION, "innerFunction", null, null, null, null, null, null);
    innerFunction.setChildCount(3);
    Node innerChild1 = createNode(Token.IDENT, "name2", null, null, null, null, null, null);
    Node innerChild2 = createNode(Token.OPEN_PAREN, null, null, null, null, null, null, null);
    Node innerChild3 = createNode(Token.CLOSE_PAREN, null, null, null, null, null, null, null);
    innerFunction.setChild(1, innerChild1);
    innerFunction.setChild(2, innerChild2);

    topLevelFunction.setChild(2, innerFunction);

    topLevelFunction.setParent(createNode(Token.SCRIPT, null, null, null, null, null, null, null));
    Preconditions.checkState(topLevelFunction.getParent() != null);

    assertTrue(NodeUtil.isFunctionDeclaration(innerFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(innerFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(topLevelFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckState() {
    Node node = createNode(Token.SCRIPT, null, null, null, null, null, null, null);
    NodeUtil.isStatement(node);

    node.setParent(null);
    NodeUtil.isStatement(node);
  }
}