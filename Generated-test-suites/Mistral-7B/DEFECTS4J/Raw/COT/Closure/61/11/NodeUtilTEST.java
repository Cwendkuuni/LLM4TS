import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node validForInNode = new Node(Token.FOR, null, 0, 0);
    validForInNode.setChild(0, new Node(Token.KEYWORD, "for", 0, 0));
    validForInNode.setChild(1, new Node(Token.IDENT, "var", 0, 0));
    validForInNode.setChild(2, new Node(Token.LPAREN, null, 0, 0));
    validForInNode.setChild(3, new Node(Token.IDENT, "i", 0, 0));
    validForInNode.setChild(4, new Node(Token.RPAREN, null, 0, 0));
    validForInNode.setChild(5, new Node(Token.LBRACE, null, 0, 0));
    validForInNode.setChild(6, new Node(Token.RBRACE, null, 0, 0));

    assertTrue(NodeUtil.isForIn(validForInNode));

    Node invalidForInNode = new Node(Token.FOR, null, 0, 0);
    assertFalse(NodeUtil.isForIn(invalidForInNode));

    Node invalidStatementNode = new Node(Token.IF, null, 0, 0);
    assertFalse(NodeUtil.isForIn(invalidStatementNode));
  }

  @Test
  public void testIsStatement() {
    Node validStatementNode = new Node(Token.ASSIGN, null, 0, 0);
    validStatementNode.setChild(0, new Node(Token.IDENT, "x", 0, 0));
    validStatementNode.setChild(1, new Node(Token.OP_ASSIGN, null, 0, 0));
    validStatementNode.setChild(2, new Node(Token.NUMBER, "5", 0, 0));

    Node scriptNode = new Node(Token.SCRIPT, null, 0, 0);
    Node blockNode = new Node(Token.BLOCK, null, 0, 0);
    Node labelNode = new Node(Token.LABEL, "label", 0, 0);

    Node validStatementNodes[] = new Node[] {validStatementNode, scriptNode, blockNode, labelNode};

    for (Node node : validStatementNodes) {
      assertTrue(NodeUtil.isStatement(node));
    }

    Node invalidStatementNode = new Node(Token.FUNCTION, null, 0, 0);
    assertFalse(NodeUtil.isStatement(invalidStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node validFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 0);
    validFunctionDeclarationNode.setChild(0, new Node(Token.KEYWORD, "function", 0, 0));
    validFunctionDeclarationNode.setChild(1, new Node(Token.IDENT, "foo", 0, 0));
    validFunctionDeclarationNode.setChild(2, new Node(Token.LPAREN, null, 0, 0));
    validFunctionDeclarationNode.setChild(3, new Node(Token.RPAREN, null, 0, 0));
    validFunctionDeclarationNode.setChild(4, new Node(Token.LBRACE, null, 0, 0));
    validFunctionDeclarationNode.setChild(5, new Node(Token.RBRACE, null, 0, 0));

    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 0);
    hoistedFunctionDeclarationNode.setChild(0, new Node(Token.KEYWORD, "function", 0, 0));
    hoistedFunctionDeclarationNode.setChild(1, new Node(Token.IDENT, "foo", 0, 0));
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, null, 0, 0));

    Node validFunctionExpressionNode = new Node(Token.FUNCTION, null, 0, 0);
    validFunctionExpressionNode.setChild(0, new Node(Token.IDENT, "foo", 0, 0));
    validFunctionExpressionNode.setChild(1, new Node(Token.LPAREN, null, 0, 0));
    validFunctionExpressionNode.setChild(2, new Node(Token.RPAREN, null, 0, 0));
    validFunctionExpressionNode.setChild(3, new Node(Token.LBRACE, null, 0, 0));
    validFunctionExpressionNode.setChild(4, new Node(Token.RBRACE, null, 0, 0));
    validFunctionExpressionNode.setParent(new Node(Token.ASSIGN, null, 0, 0));

    Node invalidFunctionDeclarationNode = new Node(Token.IDENT, "foo", 0, 0);
    assertFalse(NodeUtil.isFunctionDeclaration(invalidFunctionDeclarationNode));

    Node[] functionNodes = new Node[] {validFunctionDeclarationNode, hoistedFunctionDeclarationNode, validFunctionExpressionNode};

    for (Node node : functionNodes) {
      assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node validHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 0);
    validHoistedFunctionDeclarationNode.setChild(0, new Node(Token.KEYWORD, "function", 0, 0));
    validHoistedFunctionDeclarationNode.setChild(1, new Node(Token.IDENT, "foo", 0, 0));
    validHoistedFunctionDeclarationNode.setChild(2, new Node(Token.LPAREN, null, 0, 0));
    validHoistedFunctionDeclarationNode.setChild(3, new Node(Token.RPAREN, null, 0, 0));
    validHoistedFunctionDeclarationNode.setChild(4, new Node(Token.LBRACE, null, 0, 0));
    validHoistedFunctionDeclarationNode.setChild(5, new Node(Token.RBRACE, null, 0, 0));
    validHoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, null, 0, 0));

    Node invalidHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 0);
    invalidHoistedFunctionDeclarationNode.setChild(0, new Node(Token.KEYWORD, "function", 0, 0));
    invalidHoistedFunctionDeclarationNode.setChild(1, new Node(Token.IDENT, "foo", 0, 0));
    invalidHoistedFunctionDeclarationNode.setChild(2, new Node(Token.LPAREN, null, 0, 0));
    invalidHoistedFunctionDeclarationNode.setChild(3, new Node(Token.RPAREN, null, 0, 0));
    invalidHoistedFunctionDeclarationNode.setChild(4, new Node(Token.LBRACE, null, 0, 0));
    invalidHoistedFunctionDeclarationNode.setChild(5, new Node(Token.RBRACE, null, 0, 0));
    invalidHoistedFunctionDeclarationNode.setParent(new Node(Token.FUNCTION, null, 0, 0));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(validHoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidHoistedFunctionDeclarationNode));
  }
}