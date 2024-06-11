import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_true() {
    // Typical use case
    Node node = createForInNode();
    boolean result = NodeUtil.isForIn(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_false() {
    // Edge case: node type is not FOR
    Node node = createForStatementNode();
    boolean result = NodeUtil.isForIn(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsForIn_false_noChild() {
    // Edge case: node does not have 3 children
    Node node = createForInNode();
    node.removeChild(node.getFirstChild());
    boolean result = NodeUtil.isForIn(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement_true() {
    // Typical use case
    Node node = createStatementNode();
    boolean result = NodeUtil.isStatement(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_false() {
    // Edge case: parent type is not SCRIPT, BLOCK, or LABEL
    Node node = createFunctionDeclarationNode();
    boolean result = NodeUtil.isStatement(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_true() {
    // Typical use case
    Node node = createFunctionDeclarationNode();
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_false_parentNotStatement() {
    // Edge case: parent type is not a statement
    Node node = createFunctionDeclarationNode();
    Node parent = createScriptNode();
    parent.addChildToFront(node);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_false_nodeNotFunction() {
    // Edge case: node type is not FUNCTION
    Node node = createStatementNode();
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    // Typical use case
    Node node = createHoistedFunctionDeclarationNode();
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false_notInScopeRoot() {
    // Edge case: node parent is not SCRIPT or FUNCTION
    Node node = createHoistedFunctionDeclarationNode();
    Node parent = createBlockNode();
    parent.addChildToFront(node);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false_notFunctionDeclaration() {
    // Edge case: node is not a function declaration
    Node node = createStatementNode();
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  private Node createForInNode() {
    Node forNode = new Node(Token.FOR);
    Node firstChild = new Node(Token.NAME);
    Node secondChild = new Node(Token.EMPTY);
    Node thirdChild = new Node(Token.EMPTY);
    forNode.addChildToBack(firstChild);
    forNode.addChildToBack(secondChild);
    forNode.addChildToBack(thirdChild);
    return forNode;
  }

  private Node createForStatementNode() {
    Node forNode = new Node(Token.FOR_STMT);
    return forNode;
  }

  private Node createStatementNode() {
    Node statementNode = new Node(Token.IF);
    Node parent = new Node(Token.SCRIPT);
    statementNode.setParent(parent);
    return statementNode;
  }

  private Node createFunctionDeclarationNode() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    functionNode.setParent(parent);
    return functionNode;
  }

  private Node createScriptNode() {
    return new Node(Token.SCRIPT);
  }

  private Node createHoistedFunctionDeclarationNode() {
    Node functionNode = createFunctionDeclarationNode();
    Node scriptNode = createScriptNode();
    scriptNode.addChildToFront(functionNode);
    return functionNode;
  }

  private Node createBlockNode() {
    return new Node(Token.BLOCK);
  }
}