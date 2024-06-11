import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  // Test case for method: isForIn(Node n)
  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node("init"));
    forNode.setChild(1, new Node(Token.KEYWORD, "in"));
    forNode.setChild(2, new Node("next"));
    assertTrue(isForIn(forNode));

    Node notForNode = new Node(Token.IF);
    assertFalse(isForIn(notForNode));

    Node invalidForNode = new Node(Token.FOR, 2, new Node[0]);
    assertFalse(isForIn(invalidForNode));
  }

  // Test case for method: isStatement(Node n)
  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF);
    statementNode.setParent(new Node(Token.BLOCK));
    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.IF);
    Node parent = new Node(Token.ASSIGN);
    parent.setLeft(new Node("x"));
    parent.setRight(expressionNode);
    assertFalse(isStatement(expressionNode));
  }

  // Test case for method: isFunctionDeclaration(Node n)
  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "myFunction");
    functionDeclarationNode.setBody(new Node(Token.BLOCK));
    functionDeclarationNode.setParent(new Node(Token.SCRIPT));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION);
    functionExpressionNode.setBody(new Node(Token.BLOCK));
    functionExpressionNode.setParent(new Node(Token.ASSIGN));
    assertFalse(isFunctionDeclaration(functionExpressionNode));

    Node wrongTypeNode = new Node(Token.VAR);
    assertFalse(isFunctionDeclaration(wrongTypeNode));
  }

  // Test case for method: isHoistedFunctionDeclaration(Node n)
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, "myFunction");
    hoistedFunctionDeclarationNode.setBody(new Node(Token.BLOCK));
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, "myFunction");
    nonHoistedFunctionDeclarationNode.setBody(new Node(Token.BLOCK));
    Node parent = new Node(Token.VAR, "x");
    parent.setInit(nonHoistedFunctionDeclarationNode);
    nonHoistedFunctionDeclarationNode.setParent(parent);
    assertFalse(isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
}