import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0, 1}, null);
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {0, 1}, null);

    // Test case 1: Check if a FOR-IN loop is identified correctly
    boolean resultForIn = NodeUtil.isForIn(nodeForIn);
    assertEquals(true, resultForIn);

    // Test case 2: Check if a node that is not a FOR-IN loop is identified correctly
    boolean resultNotForIn = NodeUtil.isForIn(nodeNotForIn);
    assertEquals(false, resultNotForIn);
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statement = Node.newNode(Token.IF, new int[] {0, 1}, null);

    // Test case 1: Check if a statement node is identified correctly
    assertTrue(NodeUtil.isStatement(statement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 1}, new int[] {0, 1});
    functionDeclaration.setProperty("id", "testFunction");
    Node expressionFunction = Node.newNode(
        Token.FUNCTION, new int[] {0, 1}, new int[] {0, 1});

    // Test case 1: Check if a function declaration is identified correctly
    boolean isFunctionDeclaration = NodeUtil.isFunctionDeclaration(functionDeclaration);
    assertEquals(true, isFunctionDeclaration);

    // Test case 2: Check if a function expression is not identified as a function declaration
    boolean isExpressionFunction = NodeUtil.isFunctionDeclaration(expressionFunction);
    assertEquals(false, isExpressionFunction);

    // Test case 3: Check if a null node is handled correctly
    boolean isNullNode = NodeUtil.isFunctionDeclaration(null);
    assertNull(isNullNode);
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {0, 1}, null);
    Node functionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 1}, new int[] {0, 1});
    functionDeclaration.setProperty("id", "testFunction");

    // Test case 1: Check if a hoisted function declaration is identified correctly
    boolean isHoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);
    assertTrue(isHoistedFunctionDeclaration);

    // Test case 2: Check if a non-hoisted function declaration is identified correctly
    Node nonHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 1}, new int[] {0, 1});
    nonHoistedFunctionDeclaration.setProperty("id", "testFunction");
    Node statementNode = Node.newNode(Token.PRINT, new int[] {0, 1}, null);
    scriptNode.setChild(statementNode);
    scriptNode.setChild(nonHoistedFunctionDeclaration);

    boolean isNonHoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration);
    assertFalse(isNonHoistedFunctionDeclaration);

    // Test case 3: Check if a null node is handled correctly
    boolean isNullNode = NodeUtil.isHoistedFunctionDeclaration(null);
    assertNull(isNullNode);
  }
}