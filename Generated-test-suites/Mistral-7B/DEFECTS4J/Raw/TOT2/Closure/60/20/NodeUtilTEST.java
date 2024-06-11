import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.IDENTIFIER), new Node(Token.SEMICOLON)});
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(new Node(Token.IDENTIFIER)));

    // Edge cases
    Node invalidForNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(invalidForNode));
    Node forNodeWithTwoChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.SEMICOLON)});
    assertFalse(NodeUtil.isForIn(forNodeWithTwoChildren));
    Node forNodeWithFourChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.IDENTIFIER), new Node(Token.SEMICOLON), new Node(Token.IDENTIFIER)});
    assertFalse(NodeUtil.isForIn(forNodeWithFourChildren));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node statementNode = new Node(Token.ASSIGNMENT);
    Node expressionNode = new Node(Token.ADD);
    Node functionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "funcName")}, new Node[]{new Node(Token.BLOCK)});

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));

    // Edge cases
    Node functionExpressionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "funcName")}, null);
    assertTrue(NodeUtil.isStatement(functionExpressionNode));
    Node invalidStatementNode = new Node(Token.IF);
    assertFalse(NodeUtil.isStatement(invalidStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNodeWithName = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "funcName")}, new Node[]{new Node(Token.BLOCK)});
    Node functionNodeWithoutName = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "funcName"), new Node(Token.ASSIGNMENT)}, new Node[]{new Node(Token.BLOCK)});

    assertTrue(NodeUtil.isFunctionDeclaration(functionNodeWithName));
    assertFalse(NodeUtil.isFunctionDeclaration(functionNodeWithoutName));

    // Edge cases
    Node invalidFunctionDeclarationNode = new Node(Token.FUNCTION, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(invalidFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "funcName")}, new Node[]{new Node(Token.BLOCK)});
    Node functionNodeInFunction = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "parentFuncName")}, new Node[]{functionNode, new Node(Token.BLOCK)});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));

    // Edge cases
    Node hoistedFunctionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "funcName")}, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    Node functionNodeInVariableDeclaration = new Node(Token.VAR, new Node[]{new Node(Token.IDENTIFIER, "funcName")}, new Node[]{functionNode, new Node(Token.SEMICOLON)});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInVariableDeclaration));
  }
}