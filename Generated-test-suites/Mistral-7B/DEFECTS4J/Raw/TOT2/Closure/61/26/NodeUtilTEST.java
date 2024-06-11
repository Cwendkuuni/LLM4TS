import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createFunctionDeclaration;
import static com.google.javascript.rhino.NodeFactory.createLabel;
import static com.google.javascript.rhino.NodeFactory.createForStatement;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createStatement;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createForStatement();
    assertTrue(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsForIn_False() {
    Node notForNode = createScript("1 + 1;");
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createScript("function foo() {}");
    Node statementNode = scriptNode.getChild(0);
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsStatement_False() {
    Node expressionNode = createScript("1 + 1;");
    Node notStatementNode = expressionNode.getChild(0);
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createFunctionDeclaration("foo", new Node[0]);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node expressionNode = createScript("1 + 1;");
    Node notFunctionDeclarationNode = expressionNode.getChild(0);
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
  }

  @Test
  public void testIsFunctionDeclarationPartOfExpression() {
    Node expressionNode = createScript("function foo() {} + 1;");
    Node functionDeclarationNode = expressionNode.getChild(0);
    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createScript("function foo() {}");
    Node functionDeclarationNode = scriptNode.getChild(0);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node blockNode = createBlock();
    Node functionDeclarationNode = createFunctionDeclaration("foo", new Node[0]);
    blockNode.addChild(functionDeclarationNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInsideFunction() {
    Node functionNode = createFunction("foo", new Node[0], new Node[0]);
    Node functionDeclarationNode = createFunctionDeclaration("bar", new Node[0]);
    functionNode.addChild(functionDeclarationNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInsideLabel() {
    Node labelNode = createLabel("foo");
    Node functionDeclarationNode = createFunctionDeclaration("bar", new Node[0]);
    labelNode.addChild(functionDeclarationNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
  }
}