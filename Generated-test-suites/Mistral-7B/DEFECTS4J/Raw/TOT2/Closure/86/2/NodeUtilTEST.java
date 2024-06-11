import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(1, 1, Node.newIdentifier("i"), Node.newExpressionStatement(Node.newExpression(1)));
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newExpressionStatement(Node.newExpression(1));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newExpression(1));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node notStatementNode = Node.newIdentifier("x");
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunction(
        1,
        Node.newIdentifier("myFunction"),
        Node.newBlock(Node.NEW, Node.newStatement(Node.newExpressionStatement(Node.newExpression(1)))));
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node notFunctionNode = Node.newExpression(1);
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = Node.newFunction(
        1,
        Node.newIdentifier("myFunction"),
        Node.newBlock(Node.NEW, Node.newStatement(Node.newExpressionStatement(Node.newExpression(1)))));
    Node functionParent = Node.newBlock(Node.NEW, Node.newStatement(Node.newExpressionStatement(hoistedFunctionNode)));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

    Node notHoistedFunctionNode = Node.newFunction(
        1,
        Node.newIdentifier("myFunction"),
        Node.newBlock(Node.NEW, Node.newStatement(Node.newExpressionStatement(Node.newExpression(1)))));
    Node notFunctionParent = Node.newExpressionStatement(Node.newExpression(1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
  }
}