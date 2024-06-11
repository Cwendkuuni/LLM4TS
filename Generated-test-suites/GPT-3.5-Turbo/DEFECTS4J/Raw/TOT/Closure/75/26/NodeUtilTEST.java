import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node forInNode = createForInNode();
    boolean result = NodeUtil.isForIn(forInNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsForIn_emptyForLoop() {
    Node forLoopNode = createForLoopNode();
    boolean result = NodeUtil.isForIn(forLoopNode);
    assertEquals(false, result);
  }

  @Test
  public void testIsForIn_missingCondition() {
    Node forInNode = createForInNode();
    forInNode.removeChild(forInNode.getLastChild());
    boolean result = NodeUtil.isForIn(forInNode);
    assertEquals(false, result);
  }

  @Test
  public void testIsStatement_inScript() {
    Node statementNode = createStatementNode();
    boolean result = NodeUtil.isStatement(statementNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsStatement_inBlock() {
    Node statementNode = createStatementNode();
    Node blockNode = new Node(Token.BLOCK, statementNode);
    boolean result = NodeUtil.isStatement(blockNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsStatement_inLabel() {
    Node statementNode = createStatementNode();
    Node labelNode = new Node(Token.LABEL, statementNode);
    boolean result = NodeUtil.isStatement(labelNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsStatement_asExpression() {
    Node expressionNode = createExpressionNode();
    boolean result = NodeUtil.isStatement(expressionNode);
    assertEquals(false, result);
  }

  @Test
  public void testIsFunctionDeclaration_inScript() {
    Node functionDeclarationNode = createFunctionDeclarationNode();
    boolean result = NodeUtil.isFunctionDeclaration(functionDeclarationNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsFunctionDeclaration_inBlock() {
    Node functionDeclarationNode = createFunctionDeclarationNode();
    Node blockNode = new Node(Token.BLOCK, functionDeclarationNode);
    boolean result = NodeUtil.isFunctionDeclaration(blockNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsFunctionDeclaration_asExpression() {
    Node functionExpressionNode = createFunctionExpressionNode();
    boolean result = NodeUtil.isFunctionDeclaration(functionExpressionNode);
    assertEquals(false, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_inScript() {
    Node hoistedFunctionDeclarationNode = createHoistedFunctionDeclarationNode();
    boolean result = NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_inFunction() {
    Node hoistedFunctionDeclarationNode = createHoistedFunctionDeclarationNode();
    Node functionNode = new Node(Token.FUNCTION, hoistedFunctionDeclarationNode);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_inBlock() {
    Node hoistedFunctionDeclarationNode = createHoistedFunctionDeclarationNode();
    Node blockNode = new Node(Token.BLOCK, hoistedFunctionDeclarationNode);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(blockNode);
    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_asExpression() {
    Node functionExpressionNode = createFunctionExpressionNode();
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode);
    assertEquals(false, result);
  }

  private Node createForInNode() {
    Node forNode = new Node(Token.FOR);
    Node conditionNode = new Node(Token.EMPTY);
    forNode.addChildToFront(conditionNode);
    forNode.addChildToFront(new Node(Token.EMPTY));
    forNode.addChildToFront(new Node(Token.EMPTY));
    return forNode;
  }

  private Node createForLoopNode() {
    return new Node(Token.FOR);
  }

  private Node createStatementNode() {
    return new Node(Token.SCRIPT);
  }

  private Node createExpressionNode() {
    return new Node(Token.EXPR_RESULT);
  }

  private Node createFunctionDeclarationNode() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(functionNode);
    return scriptNode;
  }

  private Node createFunctionExpressionNode() {
    return new Node(Token.FUNCTION);
  }

  private Node createHoistedFunctionDeclarationNode() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToFront(parentFunctionNode);
    parentFunctionNode.addChildToFront(functionNode);
    return scriptNode;
  }
}