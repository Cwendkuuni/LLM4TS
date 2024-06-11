import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IDENT, "foo", null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null, null, null);
    nodeFunctionDeclaration.setChild(1, Node.newNode(Token.BLOCK, null, null, null, null, null, null, null, null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null);
    nodeFunctionExpression.setChild(0, Node.newNode(Token.IDENT, "myFunction", null, null, null, null, null, null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null, null, null);
    nodeFunctionDeclaration.setChild(1, Node.newNode(Token.BLOCK, null, null, null, null, null, null, null, null));
    nodeScript.setChild(0, nodeFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionInsideFunction = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null);
    Node nodeFunctionDeclarationInsideFunction = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null, null, null);
    nodeFunctionDeclarationInsideFunction.setChild(1, Node.newNode(Token.BLOCK, null, null, null, null, null, null, null, null));
    nodeFunctionInsideFunction.setChild(0, nodeFunctionDeclarationInsideFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInsideFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInWithNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}