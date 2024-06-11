import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newInstance(TOKEN, FOR, new int[] { 1, 2, 3 });
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode = Node.newInstance(TOKEN, IF, new int[] { 1 });
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newInstance(TOKEN, IDENT, "statement");
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newInstance(TOKEN, ADD, new int[] { 1, 2 });
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullParentNode() {
    Node node = Node.newInstance(TOKEN, IDENT, "statement");
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newInstance(TOKEN, FUNCTION, new int[] { 1, 2 });
    functionDeclarationNode.setProperty("id", Node.newProperty(IDENT, "myFunction"));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newInstance(TOKEN, FUNCTION, new int[] { 1, 2 });
    functionExpressionNode.setProperty("id", Node.newProperty(IDENT, "myFunction"));
    functionExpressionNode.setProperty("expression", Node.newInstance(TOKEN, IDENT, "myFunction"));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Preconditions.checkArgument(NodeUtil.isFunctionDeclaration(null) == false);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationNode = Node.newInstance(TOKEN, FUNCTION, new int[] { 1, 2 });
    functionDeclarationNode.setProperty("id", Node.newProperty(IDENT, "myFunction"));
    Node scriptNode = Node.newInstance(TOKEN, SCRIPT, new int[] { 1 });
    scriptNode.setProperty("body", functionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    Node functionNodeInFunctionNode = Node.newInstance(TOKEN, FUNCTION, new int[] { 1, 2 });
    functionNodeInFunctionNode.setProperty("id", Node.newProperty(IDENT, "myFunction"));
    Node functionNode = Node.newInstance(TOKEN, FUNCTION, new int[] { 1, 2 });
    functionNode.setProperty("body", functionNodeInFunctionNode);
    Node parentFunctionNode = Node.newInstance(TOKEN, FUNCTION, new int[] { 1, 2 });
    parentFunctionNode.setProperty("body", functionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunctionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Preconditions.checkArgument(NodeUtil.isHoistedFunctionDeclaration(null) == false);
  }
}