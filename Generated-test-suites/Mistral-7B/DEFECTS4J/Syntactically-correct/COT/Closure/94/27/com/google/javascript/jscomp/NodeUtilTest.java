import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  // Test for isForIn method
  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, 1, new int[]{}, new int[]{1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, 1, new int[]{}, new int[]{1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  // Test for isStatement method
  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, 1, new int[]{}, new int[]{1, 2, 3});
    Node parentStatement = new Node(Token.BLOCK, 1, new int[]{}, new int[]{1, 2, 3, 4, 5});
    parentStatement.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  // Test for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{1, 2, 3});
    nodeFunctionDeclaration.setChild(0, new Node("name", 1, new int[]{}, new int[]{1}));
    Node parentStatement = new Node(Token.BLOCK, 1, new int[]{}, new int[]{1, 2, 3, 4, 5});
    parentStatement.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{1, 2, 3});
    Node parentExpression = new Node(Token.ASSIGN, 1, new int[]{}, new int[]{1, 2, 3});
    parentExpression.setChild(0, new Node("identifier", 1, new int[]{}, new int[]{1}));
    parentExpression.setChild(1, nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  // Test for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{1, 2, 3});
    nodeHoistedFunctionDeclaration.setChild(0, new Node("name", 1, new int[]{}, new int[]{1}));
    Node parentScript = new Node(Token.SCRIPT, 1, new int[]{}, new int[]{1, 2, 3});
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{1, 2, 3});
    nodeFunctionDeclarationInFunction.setChild(0, new Node("name", 1, new int[]{}, new int[]{1}));
    Node parentFunction = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{1, 2, 3});
    parentFunction.setChild(0, nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  // Edge cases and exception handling tests
  @Test(expected = IllegalArgumentException.class)
  public void testIsForInWithNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}