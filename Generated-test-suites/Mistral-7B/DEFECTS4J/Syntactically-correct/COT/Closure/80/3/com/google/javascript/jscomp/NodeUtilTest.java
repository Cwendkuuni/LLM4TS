import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement("for (var i = 0; i < 10; i++) { }");
    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = Node.newExpressionStatement(
        Node.newIfStatement(Node.newBooleanLiteral(true), Node.newStatementList(), Node.newStatementList()));
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newExpression("1 + 2");
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newStatementList(), Node.newStatementList(), Node.newFunctionStatement());
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunctionExpression(Node.newIdentifier("myFunction"), Node.newStatementList()));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = Node.newStatementList().add(
        Node.newVariableStatement(Node.newVariable("myFunction"), Node.newFunctionStatement()))
        .add(Node.newStatementList().add(Node.newExpressionStatement(Node.newIdentifier("myFunction"))))
        .add(Node.newStatementList().add(Node.newReturnStatement(Node.newNumberLiteral(42))));

    Node nonHoistedFunctionNode = Node.newStatementList().add(
        Node.newVariableStatement(Node.newVariable("myFunction"), Node.newFunctionStatement()))
        .add(Node.newStatementList().add(Node.newExpressionStatement(Node.newIdentifier("x"))));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode.get(0)));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode.get(0)));
  }

  @Test(expected = NullPointerException.class)
  public void testCheckStateWithNullParent() {
    Node n = Node.newExpression(Token.NULL);
    NodeUtil.isStatement(n);
  }
}