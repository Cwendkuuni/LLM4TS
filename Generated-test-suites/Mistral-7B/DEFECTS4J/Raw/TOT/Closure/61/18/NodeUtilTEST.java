import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(nodeNotForIn));

    Node nodeFor = Node.newToken(Token.FOR, "for");
    Node nodeInit = Node.newExpressionStatement(Node.newVariable("i"), Node.newNumber(0));
    Node nodeCond = Node.newExpressionStatement(Node.newLiteral(true));
    Node nodeInc = Node.newExpressionStatement(Node.newVariable("i"), Node.newAdd(Node.newVariable("i"), Node.newNumber(1)));
    Node nodeBody = Node.newExpressionStatement(Node.newPrint("i"));
    nodeFor.setChildren(Node.newSequence(nodeInit, nodeCond, nodeInc, nodeBody));
    assertFalse(isForIn(nodeFor));

    Node nodeInvalidForIn = Node.newToken(Token.FOR, "invalidForIn");
    assertFalse(isForIn(nodeInvalidForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newPrint("Hello World!"));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newMemberExpression(Node.newIdentifier("x"), Node.newIdentifier("toString"), false, null);
    assertFalse(isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(Node.newIdentifier("add"), Node.newArray(Node.newParameter("a", Node.INT_TYPE), Node.newParameter("b", Node.INT_TYPE)), Node.newBlock(Node.newReturnStatement(Node.newBinaryOperator(Node.NEW_ARRAY(Node.INT_TYPE, 1), Node.NEW_INT(0), Node.NEW_BinaryOperator(Node.NEW_INT(0), Node.PLUS, Node.NEW_INT(1), Node.NEW_INT(a.getValue()), Node.NEW_INT(b.getValue()))))));
    assertTrue(isStatement(nodeFunction));

    Node nodeNull = Node.NULL;
    assertFalse(isStatement(nodeNull));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newIdentifier("add"), Node.newArray(Node.newParameter("a", Node.INT_TYPE), Node.newParameter("b", Node.INT_TYPE)), Node.newBlock(Node.newReturnStatement(Node.NEW_BinaryOperator(Node.NEW_ARRAY(Node.INT_TYPE, 1), Node.NEW_INT(0), Node.NEW_BinaryOperator(Node.NEW_INT(0), Node.PLUS, Node.NEW_INT(1), Node.NEW_Variable("a"), Node.NEW_Variable("b"))))));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newIdentifier("add"), Node.newArray(Node.newParameter("a", Node.INT_TYPE), Node.newParameter("b", Node.INT_TYPE)), Node.newBlock(Node.newReturnStatement(Node.NEW_BinaryOperator(Node.NEW_ARRAY(Node.INT_TYPE, 1), Node.NEW_INT(0), Node.NEW_BinaryOperator(Node.NEW_INT(0), Node.PLUS, Node.NEW_INT(1), Node.NEW_Variable("a"), Node.NEW_Variable("b")))))));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariable = Node.newVariable("add");
    assertFalse(isFunctionDeclaration(nodeVariable));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = Node.newFunction(Node.newIdentifier("add"), Node.newArray(Node.newParameter("a", Node.INT_TYPE), Node.newParameter("b", Node.INT_TYPE)), Node.newBlock(Node.newReturnStatement(Node.NEW_BinaryOperator(Node.NEW_ARRAY(Node.INT_TYPE, 1), Node.NEW_INT(0), Node.NEW_BinaryOperator(Node.NEW_INT(0), Node.PLUS, Node.NEW_INT(1), Node.NEW_Variable("a"), Node.NEW_Variable("b"))))));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(Node.newIdentifier("f"), Node.newArray(), Node.newBlock(Node.newExpressionStatement(nodeFunctionDeclarationInRoot), Node.newReturnStatement(Node.NEW_NULL())));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}