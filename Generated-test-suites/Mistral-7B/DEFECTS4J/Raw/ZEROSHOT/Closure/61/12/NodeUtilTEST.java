import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, new int[]{}, new int[]{}, new Node[3]);
    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = createNode(IF, new int[]{}, new int[]{}, createNode(IDENT, "if"));
    assertFalse(NodeUtil.isForIn(ifNode));

    Node functionNode = createNode(FUNCTION, new int[]{}, new int[]{}, new Node[0]);
    assertFalse(NodeUtil.isForIn(functionNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createNode(SCRIPT, new int[]{}, new int[]{}, new Node[0]);
    Node statementNode1 = createNode(IDENT, "statement1");
    Node statementNode2 = createNode(IF, new int[]{}, new int[]{}, new Node[0]);

    assertTrue(NodeUtil.isStatement(statementNode1));
    assertTrue(NodeUtil.isStatement(statementNode2));
    assertTrue(NodeUtil.isStatement(scriptNode));

    Node expressionNode = createNode(IDENT, "expression");
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(FUNCTION, new int[]{}, new int[]{}, new Node[1]);
    functionDeclarationNode.setChild(0, createNode(IDENT, "functionName"));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = createNode(FUNCTION, new int[]{}, new int[]{}, new Node[1]);
    Node functionExpressionParent = createNode(ASSIGN, new int[]{}, new int[]{}, functionDeclarationNode, createNode(IDENT, "variableName"), functionExpressionNode);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(SCRIPT, new int[]{}, new int[]{}, new Node[0]);
    Node functionDeclarationNode = createNode(FUNCTION, new int[]{}, new int[]{}, new Node[1]);
    functionDeclarationNode.setChild(0, createNode(IDENT, "functionName"));

    Node hoistedFunctionDeclarationNode = createNode(FUNCTION, new int[]{}, new int[]{}, new Node[1]);
    hoistedFunctionDeclarationNode.setChild(0, createNode(IDENT, "functionName"));
    scriptNode.setChild(0, hoistedFunctionDeclarationNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = createNode(FUNCTION, new int[]{}, new int[]{}, new Node[1]);
    notHoistedFunctionDeclarationNode.setChild(0, createNode(IDENT, "functionName"));
    scriptNode.setChild(1, notHoistedFunctionDeclarationNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}