import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForStatement(1, "label1", Node.newExpressionStatement(Token.newIdentifier("i")), Node.newExpressionStatement(Token.newIdentifier("i")), Node.newExpressionStatement(Token.newExpression("++i")), Node.newExpressionStatement(Token.newIdentifier("i")), Node.newExpressionStatement(Token.newExpression("i < 10")), Node.newExpressionStatement(Token.newIdentifier("label1")), Node.newBlock(null, Node.newStatementList(), null));
    Node nodeNotForIn = Node.newExpressionStatement(Token.newIdentifier("i"));

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("i"));
    Node nodeNotStatement = Node.newExpression(Token.NEW);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, "func", Node.newStatementList(), Node.newBlock(null, Node.newStatementList(), Node.newExpressionStatement(Token.newExpression("return 1"))), null);
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(1, null, Node.newStatementList(), Node.newBlock(null, Node.newStatementList(), Node.newExpressionStatement(Token.newExpression("return 1"))), null));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(1, "func", Node.newStatementList(), Node.newBlock(null, Node.newStatementList(), Node.newExpressionStatement(Token.newExpression("return 1"))), Node.newScript(Node.newStatementList(), null));
    Node nodeFunctionDeclarationNotHoisted = Node.newFunction(1, "func", Node.newStatementList(), Node.newBlock(null, Node.newStatementList(), Node.newExpressionStatement(Token.newExpression("return 1"))), Node.newBlock(Node.newLabel("label1"), Node.newStatementList(), null));

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationNotHoisted));
  }
}