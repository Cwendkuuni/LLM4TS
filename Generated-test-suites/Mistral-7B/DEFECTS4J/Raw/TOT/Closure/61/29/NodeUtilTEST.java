import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForStatement(1, "label", Node.newExpressionStatement(Node.newIdentifier("i"), Node.newNumber(1)), Node.newExpressionStatement(Node.newIdentifier("i"), Node.newLessThanOrEqual(Node.newIdentifier("i"), Node.newNumber(5))), Node.newBlock(Node.newExpressionStatement(Node.newPrintStatement(Node.newIdentifier("i"))), Node.newExpressionStatement(Node.newAssign(Node.newIdentifier("i"), Node.newAdd(Node.newIdentifier("i"), Node.newNumber(1))))));
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newFunction(1, null, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newPrintStatement(Node.newIdentifier("x")));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, "myFunction", null, Node.newBlock(Node.newReturnStatement(Node.newNumber(42))));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("myFunction"), Node.newFunction(1, null, null, Node.newBlock(Node.newReturnStatement(Node.newNumber(42))))));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression.getChild(0)));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(1, "myFunction", null, Node.newBlock(Node.newReturnStatement(Node.newNumber(42))));
    Node scriptNode = Node.newBlock(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(1, "myFunction", null, Node.newBlock(Node.newReturnStatement(Node.newNumber(42))));
    Node functionNode = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), nodeNotHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}