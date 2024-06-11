import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(FOR, new int[]{}, new int[]{}, new Object[]{}, null);
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode = Node.newNode(IF, new int[]{}, new int[]{}, new Object[]{}, null);
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(PRINT, new int[]{}, new int[]{}, new Object[]{"x"}, null);
    Node expressionNode = Node.newNode(ADD, new int[]{}, new int[]{}, new Object[]{Node.newNode(NUM, 1), Node.newNode(NUM, 2)}, null);

    Node parentScriptNode = Node.newNode(SCRIPT, new int[]{}, new int[]{}, new Object[]{}, null);
    Node parentBlockNode = Node.newNode(BLOCK, new int[]{}, new int[]{}, new Object[]{}, null);
    Node labelNode = Node.newNode(IDENT, new int[]{}, new int[]{}, new Object[]{"label"}, null);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(parentScriptNode));
    assertTrue(NodeUtil.isStatement(parentBlockNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(
        FUNCTION, new int[]{}, new int[]{}, new Object[]{"funcName"}, new Object[]{}, null);
    Node functionExpressionNode = Node.newNode(
        FUNCTION, new int[]{}, new int[]{}, new Object[]{Node.newNode(IDENT, new int[]{}, new int[]{}, new Object[]{"funcName"}, null), Node.newNode(BLOCK, new int[]{}, new int[]{}, new Object[]{}, null)}, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(
        FUNCTION, new int[]{}, new int[]{}, new Object[]{"funcName"}, new Object[]{}, Node.newNode(SCRIPT, new int[]{}, new int[]{}, new Object[]{}, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node nonHoistedFunctionDeclarationNode = Node.newNode(
        FUNCTION, new int[]{}, new int[]{}, new Object[]{"funcName"}, new Object[]{}, Node.newNode(BLOCK, new int[]{}, new int[]{}, new Object[]{}, Node.newNode(FUNCTION, new int[]{}, new int[]{}, new Object[]{Node.newNode(IDENT, new int[]{}, new int[]{}, new Object[]{"funcName"}, null), Node.newNode(BLOCK, new int[]{}, new int[]{}, new Object[]{}, null)}, null)));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParentNode() {
    Node statementNode = Node.newNode(PRINT, new int[]{}, new int[]{}, new Object[]{"x"}, null);
    NodeUtil.isStatement(null);
    NodeUtil.isStatement(statementNode);
    NodeUtil.isStatement(statementNode.getParent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }

}