import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENT, "i"));
    forNode.setChild(1, new Node(Token.KEYWORD, "in"));
    forNode.setChild(2, new Node(Token.IDENT, "arr"));

    assertTrue(isForIn(forNode));

    Node invalidForNode = new Node(Token.FOR);
    invalidForNode.setChild(0, new Node(Token.IDENT, "i"));
    invalidForNode.setChild(1, new Node(Token.ASSIGN, new Node(Token.NUM, 5)));
    invalidForNode.setChild(2, new Node(Token.IDENT, "arr"));

    assertFalse(isForIn(invalidForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.NUM, 5));
    Node expressionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.IDENT, "y"));

    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL, "myLabel");

    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK));
    Node functionExpressionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK), new Node(Token.IDENT, "x"));

    Node ifStatementNode = new Node(Token.IF, null, new Node(Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.NUM, 5)));
    Node whileStatementNode = new Node(Token.WHILE, null, new Node(Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.NUM, 5)));

    assertTrue(isStatement(statementNode));
    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(blockNode));
    assertTrue(isStatement(labelNode));
    assertTrue(isStatement(functionNode));
    assertTrue(isStatement(ifStatementNode));
    assertTrue(isStatement(whileStatementNode));

    assertFalse(isStatement(expressionNode));
    assertFalse(isStatement(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode1 = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK));
    Node functionDeclarationNode2 = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK), new Node(Token.IDENT, "x"));

    Node functionExpressionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK), new Node(Token.IDENT, "x"));

    assertTrue(isFunctionDeclaration(functionDeclarationNode1));
    assertTrue(isFunctionDeclaration(functionDeclarationNode2));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationNode1 = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK));
    Node functionDeclarationNode2 = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK), new Node(Token.IDENT, "x"));

    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK), new Node(Token.IDENT, "x"));

    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode1));
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode2));
    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }
}