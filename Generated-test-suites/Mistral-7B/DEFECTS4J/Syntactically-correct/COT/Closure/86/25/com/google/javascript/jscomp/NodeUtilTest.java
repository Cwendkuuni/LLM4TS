import org.junit.Test;
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

  @Test(expected = AssertionError.class)
  public void testIsStatementNullParent() {
    Node node = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 0}, new int[]{0, 0, 0, 0});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{0, 1}, new int[]{0, 0, 0, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{0, 0}, new int[]{0, 0, 0, 0});
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 0}, new int[]{0, 0, 0, 0});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{0, 0}, new int[]{0, 0, 0, 0});
    nodeFunctionDeclarationInFunction.addChildToChildren(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}