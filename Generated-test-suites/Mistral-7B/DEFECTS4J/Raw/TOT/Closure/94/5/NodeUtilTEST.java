import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node createForInNode() {
    Node script = new Node(Token.SCRIPT);
    Node forNode = new Node(Token.FOR);
    forNode.setProperty("init", new Node(Token.IDENT, "i"));
    forNode.setProperty("condition", new Node(Token.EQUALS, new Node(Token.IDENT, "i"), new Node(Token.INT_LITERAL, 5)));
    forNode.setProperty("next", new Node(Token.IDENT, "nodeList"));
    forNode.setProperty("statement", new Node(Token.ASSIGN, new Node(Token.IDENT, "i"), new Node(Token.INT_LITERAL, 1)));
    script.addChild(forNode);
    return forNode;
  }

  Node createStatementNode() {
    Node script = new Node(Token.SCRIPT);
    Node statement = new Node(Token.PRINT, new Node(Token.STRING_LITERAL, "Hello, World!"));
    script.addChild(statement);
    return statement;
  }

  Node createFunctionDeclarationNode() {
    Node script = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"));
    functionNode.setProperty("body", new Node(Token.RETURN, new Node(Token.INT_LITERAL, 42)));
    script.addChild(functionNode);
    return functionNode;
  }

  Node createHoistedFunctionDeclarationNode() {
    Node script = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"));
    functionNode.setProperty("body", new Node(Token.RETURN, new Node(Token.INT_LITERAL, 42)));
    script.addChild(functionNode);
    Node functionCallNode = new Node(Token.CALL, functionNode, new Node(Token.NULL));
    script.addChild(functionCallNode);
    return functionNode;
  }

  @Test
  public void testIsForIn() {
    Node forInNode = createForInNode();
    assertTrue(isForIn(forInNode));
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node statementNode = createStatementNode();
    assertFalse(isForIn(statementNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createStatementNode();
    assertTrue(isStatement(statementNode));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node expressionNode = new Node(Token.ADD, new Node(Token.INT_LITERAL, 1), new Node(Token.INT_LITERAL, 2));
    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createFunctionDeclarationNode();
    assertTrue(isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node statementNode = createStatementNode();
    assertFalse(isFunctionDeclaration(statementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createHoistedFunctionDeclarationNode();
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node functionDeclarationNode = createFunctionDeclarationNode();
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditions_ParentIsNull() {
    Node node = new Node(Token.IDENT, "node");
    assertFalse(isStatement(node));
    Preconditions.checkState(node.getParent() != null);
  }
}