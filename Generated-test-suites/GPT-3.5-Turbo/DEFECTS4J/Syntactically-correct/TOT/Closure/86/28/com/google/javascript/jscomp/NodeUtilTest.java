import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_withForInLoop_shouldReturnTrue() {
    Node node = createForInLoop();
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_withNonForInLoop_shouldReturnFalse() {
    Node node = createNonForInLoop();
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_withStatementNode_shouldReturnTrue() {
    Node node = createStatementNode();
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_withNonStatementNode_shouldReturnFalse() {
    Node node = createNonStatementNode();
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_withFunctionDeclaration_shouldReturnTrue() {
    Node node = createFunctionDeclaration();
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_withNonFunctionDeclaration_shouldReturnFalse() {
    Node node = createNonFunctionDeclaration();
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withHoistedFunctionDeclaration_shouldReturnTrue() {
    Node node = createHoistedFunctionDeclaration();
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withNonHoistedFunctionDeclaration_shouldReturnFalse() {
    Node node = createNonHoistedFunctionDeclaration();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  // Helper methods for creating test nodes.

  private Node createForInLoop() {
    Node forNode = new Node(Token.FOR);
    forNode.addChildToFront(new Node(Token.EMPTY));
    forNode.addChildToFront(new Node(Token.EMPTY));
    forNode.addChildToFront(new Node(Token.EMPTY));
    return forNode;
  }

  private Node createNonForInLoop() {
    return new Node(Token.EMPTY);
  }

  private Node createStatementNode() {
    Node parent = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.EMPTY);
    parent.addChildToBack(statementNode);
    return statementNode;
  }

  private Node createNonStatementNode() {
    return new Node(Token.EMPTY);
  }

  private Node createFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.EMPTY));
    return functionNode;
  }

  private Node createNonFunctionDeclaration() {
    return new Node(Token.EMPTY);
  }

  private Node createHoistedFunctionDeclaration() {
    Node functionNode = createFunctionDeclaration();
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionNode);
    return functionNode;
  }

  private Node createNonHoistedFunctionDeclaration() {
    Node functionNode = createFunctionDeclaration();
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(functionNode);
    return functionNode;
  }
}