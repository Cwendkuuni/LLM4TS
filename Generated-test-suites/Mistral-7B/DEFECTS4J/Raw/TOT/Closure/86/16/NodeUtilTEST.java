import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(Token.newForKeyword(), null, null, null, null, null);
    assertTrue(isForIn(forNode));

    Node invalidNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(statementNode));

    Node notStatementNode = Node.newExpression(Node.newIdentifier("x"));
    assertFalse(isStatement(notStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunctionDeclaration("myFunction", null, null, null);
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunctionExpression(null, null, null));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newFunctionDeclaration("myFunction", null, null, null);
    Node scriptNode = Node.newScript();
    scriptNode.addChild(hoistedFunctionDeclarationNode);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionDeclarationNode = Node.newFunctionDeclaration("myFunction", null, null, null);
    Node functionNode = Node.newFunctionStatement(Node.newIdentifier("myFunction"), null, null);
    Node blockNode = Node.newBlockStatement(null);
    blockNode.addChild(functionNode);
    assertTrue(isFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));
  }

  @org.junit.Test(expected = java.lang.AssertionError.class)
  public void testUnexpectedParentTypeForFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunctionDeclaration("myFunction", null, null, null);
    Node arrayNode = Node.newArrayLiteralExpression(null);
    arrayNode.addChild(functionDeclarationNode);
    isFunctionDeclaration(functionDeclarationNode);
  }
}