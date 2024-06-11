import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{1, 2, 3});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{1, 2, 3});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{1, 2, 3});
    Node parentOfStatement = new Node(Token.SCRIPT, new int[]{});
    parentOfStatement.addChild(nodeStatement);

    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.PLUS, new int[]{1, 2, 3});
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2, 3, 4, 5});
    Node parentOfFunctionDeclaration = new Node(Token.SCRIPT, new int[]{});
    parentOfFunctionDeclaration.addChild(nodeFunctionDeclaration);

    // Function declaration as a statement
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1, 2, 3});
    Node parentOfFunctionExpression = new Node(Token.EXPRESSION_STATEMENT, new int[]{1, 2});
    parentOfFunctionExpression.addChild(nodeFunctionExpression);

    // Function expression
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2, 3, 4, 5});
    Node scriptNode = new Node(Token.SCRIPT, new int[]{});
    Node functionNode = new Node(Token.FUNCTION, new int[]{});
    scriptNode.addChild(functionNode);
    functionNode.addChild(nodeHoistedFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNonHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2, 3, 4, 5});
    Node functionDeclarationParent = new Node(Token.BLOCK, new int[]{});
    scriptNode.addChild(functionDeclarationParent);
    functionDeclarationParent.addChild(nodeNonHoistedFunctionDeclaration);

    assertFalse(isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateParentIsNull() {
    Node node = new Node(Token.IF, new int[]{1, 2, 3});
    Preconditions.checkState(node.getParent() != null);
  }
}