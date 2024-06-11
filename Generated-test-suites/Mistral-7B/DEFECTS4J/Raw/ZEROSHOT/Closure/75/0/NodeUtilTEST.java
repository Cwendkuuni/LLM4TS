import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, null, null, null, 0, 0, 0, 0, 0, 3);
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = new Node(Token.IF, null, null, null, 0, 0, 0, 0, 0, 1);
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null, 0, 0, 0, 0, 0, 0);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = new Node(Token.FUNCTION, null, null, null, 0, 0, 0, 0, 0, 1);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeVariable = new Node(Token.VAR, null, null, null, 0, 0, 0, 0, 0, 1);
    assertFalse(NodeUtil.isStatement(nodeVariable));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "funcName", null, null, 0, 0, 0, 0, 0, 1);
    nodeFunctionDeclaration.setParent(new Node(Token.SCRIPT, null, null, null, 0, 0, 0, 0, 0, 0));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, null, 0, 0, 0, 0, 0, 1);
    nodeFunctionExpression.setParent(new Node(Token.IDENTIFIER, "funcName", null, null, 0, 0, 0, 0, 0, 0));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null, 0, 0, 0, 0, 0, 0);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "funcName", null, null, 0, 0, 0, 0, 0, 1);
    nodeFunctionDeclaration.setParent(nodeScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclaration2 = new Node(Token.FUNCTION, "funcName", null, null, 0, 0, 0, 0, 0, 1);
    Node nodeFunction = new Node(Token.FUNCTION, null, null, null, 0, 0, 0, 0, 0, 1);
    nodeFunctionDeclaration2.setParent(nodeFunction);
    nodeFunction.setParent(new Node(Token.SCRIPT, null, null, null, 0, 0, 0, 0, 0, 0));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));
  }
}