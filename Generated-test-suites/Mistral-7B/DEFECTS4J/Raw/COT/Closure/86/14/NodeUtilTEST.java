import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;

public class NodeUtilTest {

  // Test for isForIn method
  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChildCount(3);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  // Test for isStatement method
  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.ASSIGN);
    Node parentScriptNode = new Node(Token.SCRIPT);
    parentScriptNode.setChild(statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));

    Node notStatementNode = new Node(Token.IF);
    Node parentBlockNode = new Node(Token.BLOCK);
    parentBlockNode.setChild(notStatementNode);

    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  // Test for isFunctionDeclaration method
  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parentBlockNode = new Node(Token.BLOCK);
    parentBlockNode.setChild(functionDeclarationNode);
    Node functionNameNode = new Node(Token.IDENTIFIER, "myFunction");
    functionDeclarationNode.setChild(functionNameNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION);
    Node parentExpressionNode = new Node(Token.ASSIGN);
    parentExpressionNode.setChild(functionExpressionNode);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  // Test for isHoistedFunctionDeclaration method
  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.IDENTIFIER, "myFunction");
    hoistedFunctionDeclarationNode.setChild(functionNameNode);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.setChild(hoistedFunctionDeclarationNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node functionNameNode2 = new Node(Token.IDENTIFIER, "myFunction");
    Node blockNode = new Node(Token.BLOCK);
    blockNode.setChild(notHoistedFunctionDeclarationNode);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.setChild(blockNode);
    Node scriptNode2 = new Node(Token.SCRIPT);
    scriptNode2.setChild(parentFunctionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}