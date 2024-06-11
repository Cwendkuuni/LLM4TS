import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.common.base.Preconditions.checkState;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] { 0, 1, 2 });
    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = Node.newNode(Token.IF, new int[] { 0, 1 });
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.PRINT, new int[] { 0, 1 });
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newNode(Token.ADD, new int[] { 0, 1, 2 });
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2 }, "myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2 }, null);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] { 0 });
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2 }, "myFunction");
    Node functionDeclarationInScriptNode = Node.newNode(Token.ADD, new int[] { 0, 1 }, functionDeclarationNode);
    Node hoistedFunctionDeclarationNode = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2 }, "myFunction");
    Node hoistedFunctionDeclarationInScriptNode = Node.newNode(Token.ADD, new int[] { 0, 1 }, hoistedFunctionDeclarationNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionNode = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2 }, "myFunction");
    Node functionInFunctionNode = Node.newNode(Token.ADD, new int[] { 0, 1 }, functionNode);
    Node hoistedFunctionInFunctionNode = Node.newNode(Token.ADD, new int[] { 0, 1 }, hoistedFunctionDeclarationNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInFunctionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionInFunctionNode));
  }

  @Test
  public void testInvalidParentNodeType() {
    Node node = Node.newNode(Token.ADD, new int[] { 0, 1, 2 });
    Node parentNode = Node.newNode(Token.IDENT, new int[] { 0 });
    checkState(node.getParent() != null, "Node should have a parent");
    NodeUtil parentCheck = () -> NodeUtil.isStatement(parentNode);
    try {
      parentCheck.call();
      fail("Expected Preconditions check to fail");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}