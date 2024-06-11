import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{3}, new Node[]{});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{2}, new Node[]{});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{1}, new Node[]{});
    Node nodeExpression = Node.newNode(Token.PLUS, new int[]{}, new int[]{2, 2}, new Node[]{Node.newInt(1), Node.newInt(1)});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullParentNode() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{1}, new Node[]{});
    Node nullParentNode = null;

    Preconditions.checkState(nodeStatement.getParent() != null);
    NodeUtil.isStatement(nodeStatement);

    // This test case should throw an AssertionError due to the checkState call
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{1}, new Node[]{Node.newIdentifier("foo")});
    Node nodeNotFunctionDeclaration = Node.newNode(Token.IF, new int[]{}, new int[]{1}, new Node[]{});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{1}, new Node[]{Node.newIdentifier("foo")});
    Node nodeFunctionInFunction = Node.newNode(
        Token.FUNCTION, new int[]{1}, new int[]{1, 1},
        new Node[]{Node.newIdentifier("bar"), Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{1}, new Node[]{Node.newIdentifier("foo")})});
    Node nodeNotFunctionDeclaration = Node.newNode(Token.IF, new int[]{}, new int[]{1}, new Node[]{});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotFunctionDeclaration));
  }
}