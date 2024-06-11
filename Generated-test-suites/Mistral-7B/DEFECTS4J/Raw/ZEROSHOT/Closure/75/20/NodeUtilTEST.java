import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[] {1});
    Node nodeBlock = new Node(Token.BLOCK, new int[] {1});
    Node nodeLabel = new Node(Token.LABEL, new int[] {1, 2});
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2});
    Node nodeStatement = new Node(Token.ASSIGN, new int[] {1, 2});
    Node nodeExpression = new Node(Token.ADD, new int[] {1, 2});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2});
    Node nodeFunctionStatement = new Node(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2}, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionStatement));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);

    Node functionNodeInScript = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    Node functionNodeInFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
  }
}