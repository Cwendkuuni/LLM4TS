import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, new int[]{}, new int[]{}, new int[0]);
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{}, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);

    Node nodeExpression = new Node(Token.IDENT, new int[]{}, new int[]{}, new int[0]);
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{}, new int[0]);
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{}, new int[0]);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node nodeFunctionStatement = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[1]);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionStatement));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node nodeScriptFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0], new Node(Node.SCRIPT, new int[]{}, new int[]{}, new int[0]));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScriptFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }
}