import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR);
    forNode.setChild(0, createNode(Token.KEYWORD, "for"));
    forNode.setChild(1, createNode(Token.LPAREN, "("));
    forNode.setChild(2, createNode(Token.IDENT, "i"));
    forNode.setChild(3, createNode(Token.RPAREN, ")"));
    forNode.setChild(4, createNode(Token.LBRACE, "{"));
    forNode.setChild(5, createNode(Token.STATEMENT, null, "// statement 1"));
    forNode.setChild(6, createNode(Token.RPAREN, ")"));

    assertTrue("Incorrectly identified FOR-IN loop", NodeUtil.isForIn(forNode));

    Node ifNode = createNode(Token.IF);
    assertFalse("Identified FOR-IN loop when it's not", NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createNode(Token.SCRIPT);
    Node statementNode = createNode(Token.IDENT, "myVariable");

    scriptNode.setChild(0, statementNode);

    assertTrue("Incorrectly identified statement", NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode(Token.IDENT, "myVariable");
    Node assignmentNode = createNode(Token.ASSIGN, null, null, expressionNode);

    scriptNode.setChild(0, assignmentNode);

    assertFalse("Identified statement when it's not", NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, "myFunction", null, null, null, null);
    Node statementNode = createNode(Token.STATEMENT, null, "// statement 1");

    functionNode.setChild(0, functionNode.getFunctionNameNode());
    functionNode.setChild(1, NodeUtil.createBlockNode(statementNode));

    assertTrue("Incorrectly identified function declaration", NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode(Token.FUNCTION, null, null, null, null, null);
    functionExpressionNode.setChild(0, NodeUtil.createBlockNode(statementNode));

    assertFalse("Identified function declaration when it's not", NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(Token.SCRIPT);
    Node functionDeclarationNode = createNode(Token.FUNCTION, "myFunction", null, null, null, null);
    Node statementNode = createNode(Token.STATEMENT, null, "// statement 1");

    functionDeclarationNode.setChild(0, functionDeclarationNode.getFunctionNameNode());
    functionDeclarationNode.setChild(1, NodeUtil.createBlockNode(statementNode));

    scriptNode.setChild(0, functionDeclarationNode);

    assertTrue("Incorrectly identified hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    Node functionNode = createNode(Token.FUNCTION, "myFunction", null, null, null, null);
    statementNode = createNode(Token.STATEMENT, null, "// statement 1");

    functionNode.setChild(0, functionNode.getFunctionNameNode());
    functionNode.setChild(1, NodeUtil.createBlockNode(statementNode));

    scriptNode.setChild(0, createNode(Token.STATEMENT, null, functionNode));

    assertFalse("Identified hoisted function declaration when it's not", NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  private static Node createBlockNode(Node node) {
    Node blockNode = createNode(Token.BLOCK);
    blockNode.setChild(0, node);
    return blockNode;
  }
}