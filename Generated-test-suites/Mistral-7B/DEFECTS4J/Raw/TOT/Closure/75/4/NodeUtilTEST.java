import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.SEMICOLON));
    forNode.setChild(1, new Node(Token.IDENTIFIER, "var"));
    forNode.setChild(2, new Node(Token.IDENTIFIER, "i"));
    forNode.setChild(3, new Node(Token.ASSIGN, null, new Node(Token.LITERAL, 0),
        new Node(Token.IDENTIFIER, "a")));
    Node listNode = new Node(Token.LIST);
    Node ifNode = new Node(Token.IF);
    Node ifStatement = new Node(Token.BLOCK, null);

    // FOR-IN loop
    Node forInNode = new Node(Token.FOR, null, forNode, new Node(Token.IDENTIFIER, "a"), listNode);
    assertTrue(NodeUtil.isForIn(forInNode));

    // NOT a FOR-IN loop
    assertFalse(NodeUtil.isForIn(ifNode));
    assertFalse(NodeUtil.isForIn(ifStatement));
    assertFalse(NodeUtil.isForIn(listNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK, null);
    Node labelNode = new Node(Token.LABEL, "label");
    Node functionNode = new Node(Token.FUNCTION, null, new Node(Token.IDENTIFIER, "myFunction"), null, null);
    Node functionExpressionNode = new Node(Token.FUNCTION, null, null, null, null);
    Node expressionStatementNode = new Node(Token.EXPRESSION_STATEMENT, null, functionExpressionNode);

    // Statement nodes
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(functionNode));

    // NOT statement nodes
    assertFalse(NodeUtil.isStatement(functionExpressionNode));
    assertFalse(NodeUtil.isStatement(expressionStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode1 = new Node(Token.FUNCTION, null, new Node(Token.IDENTIFIER, "myFunction"), null, null);
    Node functionNode2 = new Node(Token.FUNCTION, null, null, null, null);

    // Function declaration
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode1));

    // NOT function declaration
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionDeclarationNode = new Node(Token.FUNCTION, null, new Node(Token.IDENTIFIER, "myFunction"), null, null);

    // Hoisted function declaration
    scriptNode.setFirstChild(functionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    // NOT hoisted function declaration
    Node functionExpressionNode = new Node(Token.FUNCTION, null, null, null, null);
    scriptNode.setFirstChild(functionExpressionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
  }

  @Test(expected = AssertionError.class)
  public void testInvalidParentNodeType() {
    Node node = new Node(Token.IF);
    Node parentNode = new Node(Token.NUMBER, 0);
    NodeUtil.isStatement(node);
    node.setParent(parentNode);
  }
}