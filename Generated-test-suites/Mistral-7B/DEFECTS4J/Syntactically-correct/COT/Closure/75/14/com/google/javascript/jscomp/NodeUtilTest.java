import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.BLOCK;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.SCRIPT;
import static com.google.javascript.rhino.TokenType.FOR;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(FOR);
    Node child1 = Node.newNode(Token.IDENTIFIER, "for");
    Node child2 = Node.newNode(Token.IDENTIFIER, "in");
    Node child3 = Node.newNode(Token.IDENTIFIER, "array");

    Preconditions.checkArgument(NodeUtil.isForIn(Node.newNode(FOR, child1, child2, child3)));
    Preconditions.checkArgument(!NodeUtil.isForIn(Node.newNode(FOR, child1, child2)));
    Preconditions.checkArgument(!NodeUtil.isForIn(Node.newNode(FOR, child1)));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.newNode(Token.IDENTIFIER, "statement");

    Node parentScript = Node.newNode(SCRIPT);
    Node parentBlock = Node.newNode(BLOCK);
    Node parentLabel = Node.newNode(LABEL, "label");

    Preconditions.checkArgument(NodeUtil.isStatement(node.setParent(parentScript)));
    Preconditions.checkArgument(NodeUtil.isStatement(node.setParent(parentBlock)));
    Preconditions.checkArgument(NodeUtil.isStatement(node.setParent(parentLabel)));

    Node parentExpression = Node.newNode(Token.IDENTIFIER, "expression");
    Preconditions.checkArgument(!NodeUtil.isStatement(node.setParent(parentExpression)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(FUNCTION, Node.newNode(Token.IDENTIFIER, "functionName"), null, Node.newNodeList());

    Node statementFunctionNode = Node.newNode(FUNCTION, Node.newNode(Token.IDENTIFIER, "functionName"), null, Node.newNodeList());
    statementFunctionNode.setParent(Node.newNode(BLOCK));

    Node hoistedFunctionDeclaration = Node.newNode(FUNCTION, Node.newNode(Token.IDENTIFIER, "functionName"), null, Node.newNodeList());
    hoistedFunctionDeclaration.setParent(Node.newNode(SCRIPT));

    Node functionExpression = Node.newNode(FUNCTION, null, null, Node.newNodeList());
    functionExpression.setParent(Node.newNode(BLOCK));

    Preconditions.checkArgument(NodeUtil.isFunctionDeclaration(functionNode));
    Preconditions.checkArgument(!NodeUtil.isFunctionDeclaration(statementFunctionNode));
    Preconditions.checkArgument(NodeUtil.isFunctionDeclaration(hoistedFunctionDeclaration));
    Preconditions.checkArgument(!NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(Node.newNode(FUNCTION, Node.newNode(Token.IDENTIFIER, "functionName"), null, Node.newNodeList()));

    Node parentScript = Node.newNode(SCRIPT);
    Node functionDeclaration = Node.newNode(FUNCTION, Node.newNode(Token.IDENTIFIER, "functionName"), null, Node.newNodeList());
    functionDeclaration.setParent(parentScript);

    Preconditions.checkArgument(hoistedFunctionDeclaration);
    Preconditions.checkArgument(!hoistedFunctionDeclaration(functionDeclaration.getParent()));
  }
}