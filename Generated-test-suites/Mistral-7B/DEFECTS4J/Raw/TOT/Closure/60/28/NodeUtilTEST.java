import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement("for (var i in array) {}", Token.newNode("array"));
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newAssignment("x", Node.newNumberLiteral(1)));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newAssignment("x", Node.newNumberLiteral(1)));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newExpression("1 + 1");
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        "myFunction",
        Node.newList(),
        Node.newBlock("return 1;")
    );
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(
        Node.newFunction("myFunction", Node.newList(), Node.newBlock("return 1;"))
    );
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        "myFunction",
        Node.newList(),
        Node.newBlock("return 1;"),
        Node.newScript()
    );
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(
        "myFunction",
        Node.newList(),
        Node.newBlock("return 1;"),
        Node.newBlock(Node.newExpressionStatement(Node.newAssignment("x", Node.newNumberLiteral(1))))
    );
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFails() {
    Node node = Node.newExpressionStatement(Node.newAssignment("x", Node.newNumberLiteral(1)));
    Node parentNull = null;
    Preconditions.checkState(parentNull != null);
  }

  @org.junit.Test
  public void testIsForInWithParentNull() {
    Node nodeForIn = Node.newForInStatement("for (var i in array) {}", Token.newNode("array"));
    Preconditions.checkState(nodeForIn.getParent() != null);
    Node nodeNullParent = Node.newScript();
    assertFalse(isForIn(nodeForIn.cloneTree())); // cloneTree to not affect the original Node
    nodeForIn.setParent(nodeNullParent);
    assertFalse(isForIn(nodeForIn));
  }
}