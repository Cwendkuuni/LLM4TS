import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeForStatement = Node.newNode(Token.FOR, null, null, null, null, null, null);
    nodeForStatement.setType(Token.STATEMENT);
    assertFalse(NodeUtil.isForIn(nodeForStatement));

    Node nodeIfStatement = Node.newNode(Token.IF, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeIfStatement));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeForIn));

    Node nodeIf = Node.newNode(Token.IF, null, null, null, null, null, null);
    assertFalse(NodeUtil.isStatement(nodeIf));

    Node nodeExpression = Node.newNode(Token.ADD, null, null, null, null, null, null);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null);
    nodeFunction.setType(Token.STATEMENT);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null);
    nodeFunctionExpression.setType(Token.EXPRESSION);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariable = Node.newNode(Token.VAR, "myFunction", null, null, null, null, null);
    nodeVariable.setType(Token.STATEMENT);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null);
    nodeFunction.setType(Token.STATEMENT);
    nodeFunction.setParent(nodeScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, null, null, null, null, null, null);
    nodeFunctionInFunction.setType(Token.STATEMENT);
    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, null, null, null, null, null, null);
    nodeFunctionInScript.setType(Token.STATEMENT);
    nodeFunctionInScript.setParent(nodeScript);
    nodeFunction.setParent(nodeFunctionInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}