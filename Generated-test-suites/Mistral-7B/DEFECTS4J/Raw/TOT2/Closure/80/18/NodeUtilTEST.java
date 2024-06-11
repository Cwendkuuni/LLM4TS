import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.truth.Truth.assertThat;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR, "forIn");
    assertTrue(NodeUtil.isForIn(forInNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IDENT, "statement");
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "functionDeclaration");
    Node statementParentNode = new Node(Token.SCRIPT, "script");
    statementParentNode.addChildToStatementList(functionDeclarationNode);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, "hoistedFunctionDeclaration");
    Node scriptNode = new Node(Token.SCRIPT, "script");
    scriptNode.addChildToStatementList(hoistedFunctionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  // Edge cases and exception handling tests

  @org.junit.Test
  public void testIsForInInvalidType() {
    Node invalidNode = new Node(Token.IF, "ifStatement");
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsForInLessThanThreeChildren() {
    Node forInNodeWithTwoChildren = new Node(Token.FOR, "forIn");
    assertFalse(NodeUtil.isForIn(forInNodeWithTwoChildren));
  }

  @org.junit.Test
  public void testIsForInMoreThanThreeChildren() {
    Node forInNodeWithFourChildren = new Node(Token.FOR, "forIn");
    forInNodeWithFourChildren.addChildToStatementList(new Node(Token.IF, "ifStatement"));
    assertFalse(NodeUtil.isForIn(forInNodeWithFourChildren));
  }

  @org.junit.Test
  public void testIsStatementInvalidType() {
    Node invalidNode = new Node(Token.IF, "ifStatement");
    assertFalse(NodeUtil.isStatement(invalidNode));
  }

  @org.junit.Test
  public void testIsStatementNullParent() {
    Node statementNode = new Node(Token.IDENT, "statement");
    assertFalse(NodeUtil.isStatement(statementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationInvalidType() {
    Node invalidNode = new Node(Token.IF, "ifStatement");
    assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationNoParent() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "functionDeclaration");
    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationNonStatementParent() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "functionDeclaration");
    Node expressionParentNode = new Node(Token.EXPRESSION, "expression");
    expressionParentNode.addChildToStatementList(functionDeclarationNode);
    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationNoParent() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, "hoistedFunctionDeclaration");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationNonScriptParent() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, "hoistedFunctionDeclaration");
    Node blockParentNode = new Node(Token.BLOCK, "block");
    blockParentNode.addChildToStatementList(hoistedFunctionDeclarationNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationNonFunctionParent() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, "hoistedFunctionDeclaration");
    Node variableDeclarationParentNode = new Node(Token.VAR, "variableDeclaration");
    variableDeclarationParentNode.addChildToStatementList(hoistedFunctionDeclarationNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }
}