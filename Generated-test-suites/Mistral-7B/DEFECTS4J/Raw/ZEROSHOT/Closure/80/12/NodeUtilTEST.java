import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[1]);
    Node nodeExpression = new Node(Token.PLUS, new int[]{}, new int[2]);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    nodeFunction.setProperty("id", "myFunction");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    Node parent = new Node(Token.ASSIGN, new int[]{"id"}, new int[1]);
    parent.setChild(0, nodeFunctionExpression);
    nodeFunctionExpression.setProperty("id", "myFunction");

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    nodeFunction.setProperty("id", "myFunction");
    nodeScript.setChild(0, nodeFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    nodeFunctionInFunction.setProperty("id", "myFunction");
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    nodeFunctionInScript.setChild(0, nodeFunctionInFunction);
    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    parentFunction.setChild(0, nodeFunctionInScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}