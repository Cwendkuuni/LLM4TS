import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.object();
    node.setType(FOR);
    node.setChildCount(3);

    assertTrue(NodeUtil.isForIn(node));

    node.setType(IF);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.object();
    Node parent = Node.object();

    assertFalse(NodeUtil.isStatement(node));

    parent.setType(SCRIPT);
    node.setParent(parent);

    assertTrue(NodeUtil.isStatement(node));

    parent.setType(EXPR_STATEMENT);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = Node.object();

    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.object();
    functionNode.setType(FUNCTION);
    Node statementNode = Node.object();
    statementNode.setType(SCRIPT);
    functionNode.setParent(statementNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = Node.object();
    functionExpressionNode.setType(FUNCTION);
    Node expressionNode = Node.object();
    expressionNode.setType(EXPR_STATEMENT);
    functionExpressionNode.setParent(expressionNode);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = Node.object();
    hoistedFunctionNode.setType(FUNCTION);
    Node scriptNode = Node.object();
    scriptNode.setType(SCRIPT);
    hoistedFunctionNode.setParent(scriptNode);

    Node functionNode = Node.object();
    functionNode.setType(FUNCTION);
    Node functionStatementNode = Node.object();
    functionStatementNode.setType(STATEMENT);
    functionNode.setParent(functionStatementNode);
    Node scriptFunctionNode = Node.object();
    scriptFunctionNode.setType(FUNCTION);
    scriptFunctionNode.setParent(scriptNode);
    functionStatementNode.setParent(scriptFunctionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}