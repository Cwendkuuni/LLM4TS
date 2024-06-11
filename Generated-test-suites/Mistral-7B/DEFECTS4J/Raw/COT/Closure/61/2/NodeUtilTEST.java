import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0}, new int[]{0, 0, 0, 0});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1}, new int[]{0, 0, 0, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(
        Token.FUNCTION,
        new int[]{0},
        new int[]{0, 0, 0, 0}
    );
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node functionNode = new Node(
        Token.FUNCTION,
        new int[]{0},
        new int[]{0, 0, 0, 0},
        scriptNode
    );

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.IF, new int[]{}, new int[]{0, 0, 0})));
  }

  @Test(expected = NullPointerException.class)
  public void testPreconditionsCheckStateWithNullParent() {
    Node node = new Node(Token.IF, null, new int[]{0, 0, 0});
    NodeUtil.isStatement(node);
  }
}