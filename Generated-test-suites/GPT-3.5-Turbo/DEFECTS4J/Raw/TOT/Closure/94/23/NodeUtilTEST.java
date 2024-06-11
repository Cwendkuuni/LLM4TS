import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_WithValidForIn_ReturnsTrue() {
    Node forInNode = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.IN);
    Node child3 = new Node(Token.STRING);
    forInNode.addChildToBack(child1);
    forInNode.addChildToBack(child2);
    forInNode.addChildToBack(child3);

    boolean result = NodeUtil.isForIn(forInNode);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_WithInvalidFor_ReturnsFalse() {
    Node invalidForNode = new Node(Token.IF);

    boolean result = NodeUtil.isForIn(invalidForNode);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsForIn_WithInvalidChildCount_ReturnsFalse() {
    Node forInNode = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    forInNode.addChildToBack(child1);

    boolean result = NodeUtil.isForIn(forInNode);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement_WithValidStatement_ReturnsTrue() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node statementNode = new Node(Token.VAR);
    scriptNode.addChildToBack(blockNode);
    blockNode.addChildToBack(statementNode);

    boolean result = NodeUtil.isStatement(statementNode);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_WithInvalidStatement_ReturnsFalse() {
    Node invalidStatementNode = new Node(Token.FUNCTION);

    boolean result = NodeUtil.isStatement(invalidStatementNode);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_WithValidFunctionDeclaration_ReturnsTrue() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(blockNode);
    blockNode.addChildToBack(functionNode);

    boolean result = NodeUtil.isFunctionDeclaration(functionNode);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_WithInvalidFunction_ReturnsFalse() {
    Node invalidFunctionNode = new Node(Token.VAR);

    boolean result = NodeUtil.isFunctionDeclaration(invalidFunctionNode);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithValidHoistedFunctionDeclaration_ReturnsTrue() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    functionNode.addChildToBack(parentFunctionNode);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithInvalidHoistedFunction_ReturnsFalse() {
    Node invalidHoistedFunctionNode = new Node(Token.BLOCK);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(invalidHoistedFunctionNode);

    Assert.assertFalse(result);
  }
}