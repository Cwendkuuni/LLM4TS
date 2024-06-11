import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, new Node[]{}, new Node[]{Node.newIdentifier("i"), Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("i"), Node.newNumberLiteral(0)))}, Node.newBlock(new Node[]{Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("arr"), Node.newArrayLiteral(new Node[]{Node.newNumberLiteral(1), Node.newNumberLiteral(2)})))}, Node.newStatementList()));
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newNumberLiteral(10)));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newNumberLiteral(10)));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNumberLiteral(10);
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("myFunction", new Node[]{Node.newParameter("arg", Node.TYPE_NONE)}, Node.newBlock(Node.newStatementList(), Node.newReturnStatement(Node.newNumberLiteral(10))));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newFunctionExpression(new Node[]{Node.newIdentifier("arg")}, Node.newBlock(Node.newStatementList(), Node.newReturnStatement(Node.newNumberLiteral(10))))));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunctionDeclaration("myFunction", new Node[]{Node.newParameter("arg", Node.TYPE_NONE)}, Node.newBlock(Node.newStatementList(), Node.newReturnStatement(Node.newNumberLiteral(10))), Node.newScript(Node.newStatementList()));
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunctionDeclaration("myFunction", new Node[]{Node.newParameter("arg", Node.TYPE_NONE)}, Node.newBlock(Node.newStatementList(), Node.newReturnStatement(Node.newNumberLiteral(10))), Node.newBlock(Node.newStatementList(), Node.newStatementList()));
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}