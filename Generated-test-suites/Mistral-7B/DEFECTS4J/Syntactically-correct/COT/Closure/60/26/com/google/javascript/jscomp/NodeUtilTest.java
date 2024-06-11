import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isStatement;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isHoistedFunctionDeclaration;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{0, 1, 2}, new Node[3]);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{0, 1}, new Node[2]);
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{0, 1}, new Node[2]);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[]{0}, new Node[0]);
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new Node[2]);
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node nodeNotFunctionDeclaration = Node.newNode(Token.IF, new int[]{0, 1}, new Node[2]);
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new Node[2]);
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{0}, new Node[0]);
    nodeScript.addChild(nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionInsideFunction = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new Node[2]);
    nodeFunctionInsideFunction.setProperty("id", "testFunction");

    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{0}, new Node[0]);
    nodeFunction.addChild(nodeFunctionInsideFunction);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testPreconditionsCheckStateFailed() {
    Node node = Node.newNode(Token.SCRIPT, new int[]{0}, new Node[0]);
    Preconditions.checkState(node.getParent() == null);
  }
}