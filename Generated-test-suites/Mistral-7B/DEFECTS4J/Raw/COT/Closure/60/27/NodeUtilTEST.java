import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null);
    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeStatement = Node.newNode(Token.IF, null, null, null, null);
    Node nodeExpression = Node.newNode(Token.FUNCTION, null, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunction", null, null, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration_FunctionInScript() {
    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, "myFunction", null, null, null);
    Node scriptNode = Node.newNode(Token.SCRIPT, null, null, null, nodeFunctionInScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration_FunctionInFunction() {
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, "myFunction", null, null, null);
    Node functionNode = Node.newNode(Token.FUNCTION, null, null, null, null);
    functionNode.setFirstChild(nodeFunctionInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclaration_NullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunction", null, null, null);

    Preconditions.checkState(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration)); // Throws if not a function declaration
    assertNull(nodeFunctionDeclaration.getParent());
    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration_NotFunctionDeclaration() {
    Node nodeNotFunctionDeclaration = Node.newNode(Token.VAR, null, null, null, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotFunctionDeclaration));
  }
}