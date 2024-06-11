import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Node parentOfStatement = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    parentOfStatement.addChildToEnd(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 0, 0});
    nodeFunctionDeclaration.addChildToEnd(new Node(Token.IDENT, new int[]{"name"}, new int[]{0, 0, 0}));

    Node parentOfFunctionDeclaration = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    parentOfFunctionDeclaration.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 0, 0});
    nodeFunctionDeclaration.addChildToEnd(new Node(Token.IDENT, new int[]{"name"}, new int[]{0, 0, 0}));

    Node parentOfFunctionDeclaration = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    parentOfFunctionDeclaration.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 0, 0});
    nodeFunctionDeclarationInFunction.addChildToEnd(new Node(Token.IDENT, new int[]{"name"}, new int[]{0, 0, 0}));

    Node parentOfFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    parentOfFunctionDeclarationInFunction.addChildToEnd(nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}