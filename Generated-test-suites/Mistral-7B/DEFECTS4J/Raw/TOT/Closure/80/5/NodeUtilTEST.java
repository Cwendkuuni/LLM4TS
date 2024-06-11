import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForInStatement(1, null, null, null, null);
    assertTrue(isForIn(forNode));

    Node notForInNode = Node.newExpressionStatement(Node.newExpression(1));
    assertFalse(isForIn(notForInNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newExpression(1));
    assertTrue(isStatement(statementNode));

    Node expressionNode = Node.newExpression(1);
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction(1, "testFunction", new String[]{}, Node.SCOPE_GLOBAL, null, null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(1))));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newExpression(Node.newFunction(1, "testFunction", new String[]{}, Node.SCOPE_GLOBAL, null, null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(1)))));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newFunction(1, "testFunction", new String[]{}, Node.SCOPE_GLOBAL, null, null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(1))));
    Node scriptNode = Node.newExpression(Node.newName("testFunction", null));
    Node functionNode = Node.newExpression(Node.newName("testFunction", null), null, null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(1))));
    Node blockNode = Node.newBlock(Node.newExpressionStatement(Node.newExpression(1)));
    Node scriptWithFunctionNode = Node.newBlock(scriptNode, Node.newStatementList(functionNode, blockNode));

    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertTrue(isHoistedFunctionDeclaration(scriptNode));
    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }
}