import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0});
    Node nodeExpression = new Node(Token.ID, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1});

    // Function declaration
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    // Function expression
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2, 3});

    // Function declaration in script
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    // Function declaration inside a function
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node nodeNullParent = new Node(Token.FUNCTION, new int[]{}, null);
    NodeUtil.isStatement(nodeNullParent);
  }

  @Test
  public void testIsForInWithEmptyChildren() {
    Node nodeForInWithEmptyChildren = new Node(Token.FOR, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isForIn(nodeForInWithEmptyChildren));
  }

  @Test
  public void testIsForInWithLessThanThreeChildren() {
    Node nodeForInWithLessThanThreeChildren = new Node(Token.FOR, new int[]{}, new int[]{0, 1});
    assertFalse(NodeUtil.isForIn(nodeForInWithLessThanThreeChildren));
  }

  @Test
  public void testIsStatementWithNullParent() {
    Node nodeWithNullParent = new Node(Token.IF, null, new int[]{0});
    assertFalse(NodeUtil.isStatement(nodeWithNullParent));
  }

  @Test
  public void testIsStatementWithNonStatementParent() {
    Node nodeWithNonStatementParent = new Node(Token.ID, new Node(Token.IF, new int[]{}, new int[]{0}), new int[]{0});
    assertFalse(NodeUtil.isStatement(nodeWithNonStatementParent));
  }
}