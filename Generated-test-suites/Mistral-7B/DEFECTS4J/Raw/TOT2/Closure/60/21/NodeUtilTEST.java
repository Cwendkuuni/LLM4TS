import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR, new int[] {0, 0}, new int[] {1, 1, 1});
    Node notForInNode = new Node(Token.IF, new int[] {0, 0}, new int[] {1, 1});
    assertTrue(NodeUtil.isForIn(forInNode));
    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[] {0, 0}, new int[] {1, 1});
    Node expressionNode = new Node(Token.FUNCTION, new int[] {0, 0}, new int[] {1, 1, 1});
    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[] {0, 0}, new int[] {1, 1, 1});
    Node functionExpressionNode = new Node(Token.FUNCTION, new int[] {0, 0}, new int[] {1, 1, 1});
    functionExpressionNode.setProperty("name", "foo");
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {0, 0}, new int[] {1, 1, 1}, "foo");
    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {0, 0}, new int[] {1, 1, 1}, "foo");
    Node parentScriptNode = new Node(Token.SCRIPT, new int[] {0, 0}, new int[] {1, 1});
    Node parentFunctionNode = new Node(Token.FUNCTION, new int[] {0, 0}, new int[] {1, 1});
    parentScriptNode.addChild(hoistedFunctionDeclarationNode);
    parentFunctionNode.addChild(notHoistedFunctionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }

  // Add test cases for edge cases and exceptions here

  @Test
  public void testEmptyNode() {
    Node emptyNode = new Node(Token.EMPTY, new int[] {0, 0}, new int[] {0, 0});
    assertFalse(NodeUtil.isForIn(emptyNode));
    assertFalse(NodeUtil.isStatement(emptyNode));
    assertFalse(NodeUtil.isFunctionDeclaration(emptyNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(emptyNode));
  }

  @Test
  public void testMalformedNode() {
    Node malformedNode = new Node(-1, new int[] {0, 0}, new int[] {-1, -1, -1});
    assertFalse(NodeUtil.isForIn(malformedNode));
    assertFalse(NodeUtil.isStatement(malformedNode));
    assertFalse(NodeUtil.isFunctionDeclaration(malformedNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(malformedNode));
  }

  @Test
  public void testNodeWithInvalidChildCount() {
    Node nodeWithInvalidChildCount = new Node(Token.IF, new int[] {0, 0}, new int[] {1, 1});
    nodeWithInvalidChildCount.setChildCount(3);
    assertFalse(NodeUtil.isForIn(nodeWithInvalidChildCount));
    assertFalse(NodeUtil.isStatement(nodeWithInvalidChildCount));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeWithInvalidChildCount));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeWithInvalidChildCount));
  }
}