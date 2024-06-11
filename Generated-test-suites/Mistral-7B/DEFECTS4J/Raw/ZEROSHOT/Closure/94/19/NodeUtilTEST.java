import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0});
    Node nodeExpression = new Node(Token.ADD, new int[]{}, new int[]{0, 0});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 0});
    nodeFunctionDeclaration.setProperty("name", "testFunction");

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0});
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 0});
    nodeFunctionDeclaration.setProperty("name", "testFunction");
    nodeFunctionDeclaration.setParent(nodeScript);

    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0});
    nodeFunction.setParent(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}