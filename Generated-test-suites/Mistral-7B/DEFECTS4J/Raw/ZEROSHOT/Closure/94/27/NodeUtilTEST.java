import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(ForStatement.class, new int[] {Token.FOR, 0, 0, 1});
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newNode(IfStatement.class, new int[] {If.IF, 0, 0, 0});
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Script.class, new int[] {Script.SCRIPT, 0, 0, 0});
    Node blockNode = Node.newNode(Block.class, new int[] {Block.BLOCK, 0, 0, 0});
    Node functionNode = Node.newNode(FunctionDeclaration.class, new int[] {Function.FUNCTION, 0, 0, 0});
    Node labelNode = Node.newNode(LabeledStatement.class, new int[] {LabeledStatement.LABEL, 0, 0, 0});
    Node expressionNode = Node.newNode(ExpressionStatement.class, new int[] {ExpressionStatement.EXPRESSION, 0, 0, 0});

    Node parentScript = Node.newNode(Script.class, new int[] {Script.SCRIPT, 0, 0, 0});
    Node parentBlock = Node.newNode(Block.class, new int[] {Block.BLOCK, 0, 0, 0});
    Node parentFunction = Node.newNode(FunctionDeclaration.class, new int[] {Function.FUNCTION, 0, 0, 0});
    Node parentLabel = Node.newNode(LabeledStatement.class, new int[] {LabeledStatement.LABEL, 0, 0, 0});

    Node statementNodes[] = {scriptNode, blockNode, functionNode, labelNode, expressionNode};
    Node nonStatementNodes[] = {parentScript, parentBlock, parentFunction, parentLabel, expressionNode};

    for (Node node : statementNodes) {
      Node parent = Node.newNode(Script.class, new int[] {Script.SCRIPT, 0, 0, 0});
      node.setParent(parent);
      assertTrue(NodeUtil.isStatement(node));
    }

    for (Node node : nonStatementNodes) {
      assertFalse(NodeUtil.isStatement(node));
    }
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(FunctionDeclaration.class, new int[] {Function.FUNCTION, 0, 0, 0});
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newNode(FunctionExpression.class, new int[] {Function.FUNCTION, 0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(FunctionDeclaration.class, new int[] {Function.FUNCTION, 0, 0, 0});
    Node parentScriptNode = Node.newNode(Script.class, new int[] {Script.SCRIPT, 0, 0, 0});
    Node scriptParentNode = Node.newNode(Script.class, new int[] {Script.SCRIPT, 0, 0, 0});

    hoistedFunctionDeclarationNode.setParent(parentScriptNode);
    parentScriptNode.setParent(scriptParentNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = Node.newNode(FunctionDeclaration.class, new int[] {Function.FUNCTION, 0, 0, 0});
    Node parentBlockNode = Node.newNode(Block.class, new int[] {Block.BLOCK, 0, 0, 0});

    notHoistedFunctionDeclarationNode.setParent(parentBlockNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}