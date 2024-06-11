import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpressionStatement(Node.newBinaryOperator(
        Node.newName("x"), Token.DIVIDE, Node.newNumber(2)));
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration(
        "myFunction", Node.newList(), Node.newList(), Node.newBlock(Node.newList()));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunctionExpression(Node.newName("myFunction"), Node.newList(), Node.newList(), Node.newBlock(Node.newList())));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunctionDeclaration(
        "myFunction", Node.newList(), Node.newList(), Node.newBlock(Node.newList()));
    Node scriptNode = Node.newScript(Node.newList(nodeHoistedFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionInsideFunction = Node.newFunctionDeclaration(
        "myFunction", Node.newList(), Node.newList(), Node.newBlock(Node.newList(
            Node.newExpressionStatement(nodeHoistedFunctionDeclaration))));
    Node functionNode = Node.newFunctionDeclaration(
        "parentFunction", Node.newList(), Node.newList(), Node.newBlock(Node.newList(nodeFunctionInsideFunction)));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }
}