import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, null);
    Node nodeFor = new Node(Token.FOR, null, null, null, null);
    Node nodeIf = new Node(Token.IF, null, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, null);
    Node nodeBlock = new Node(Token.BLOCK, null, null);
    Node nodeFunction = new Node(Token.FUNCTION, null, null, null, null);
    Node nodeExpression = new Node(Token.ASSIGN, null, nodeFunction);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateWithNullParent() {
    Node node = new Node(Token.SCRIPT, null, null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, null, null, null);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null, null);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, null, null, null);
    Node nodeFunctionDeclarationAtRoot = new Node(Token.FUNCTION, null, null, null, null);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, null, null, null, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationAtRoot));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}