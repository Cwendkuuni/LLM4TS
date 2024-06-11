import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for NodeUtil methods.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = new Node(Token.IF, new int[]{}, new int[1]);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.ASSIGN, new int[]{}, new int[2]);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node notStatementNode = new Node(Token.IF, new int[]{}, new int[1]);
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{"funcName"}, new int[1]);
    functionDeclarationNode.setProperty("funcName", new NodeText("funcName"));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{"funcName"}, new int[1]);
    hoistedFunctionDeclarationNode.setProperty("funcName", new NodeText("funcName"));
    Node parentScriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    hoistedFunctionDeclarationNode.setParent(parentScriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionDeclarationNodeInFunction = new Node(Token.FUNCTION, new int[]{"funcName"}, new int[1]);
    functionDeclarationNodeInFunction.setProperty("funcName", new NodeText("funcName"));
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    functionDeclarationNodeInFunction.setParent(functionNode);
    Node parentScriptNodeInFunction = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    functionNode.setParent(parentScriptNodeInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNodeInFunction));
  }
}