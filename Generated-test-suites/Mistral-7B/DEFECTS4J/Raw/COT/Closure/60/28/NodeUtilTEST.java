import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, null, null, null);
    Node threeChildrenForNode = createNode(Token.FOR, null, createNode(Token.IDENT, "x"), createNode(Token.SEMICOLON, null, null, null), createNode(Token.SEMICOLON, null, null, null), forNode);

    assertTrue(NodeUtil.isForIn(forNode));
    assertTrue(NodeUtil.isForIn(threeChildrenForNode));

    Node notForNode = createNode(Token.IF, null, null, null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.IDENT, "x");
    Node expressionNode = createNode(Token.PLUS, null, createNode(Token.NUM, 1), statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = createNode(Token.FUNCTION, null, null, null, null, createNode(Token.IDENT, "myFunction"));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), null, null, null);
    Node scriptNode = createNode(Token.SCRIPT, null, null, null);
    Node functionNode = createNode(Token.FUNCTION, null, null, null, null, null);
    scriptNode.setFirstChild(hoistedFunctionDeclaration);
    scriptNode.setLastChild(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateFailure() {
    Node node = createNode(Token.IDENT, "x");
    Node parent = createNode(Token.SCRIPT, null, null, null);
    parent.setFirstChild(node);
    NodeUtil.isStatement(parent);
  }
}