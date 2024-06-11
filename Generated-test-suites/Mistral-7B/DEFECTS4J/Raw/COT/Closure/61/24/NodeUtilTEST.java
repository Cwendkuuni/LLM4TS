import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{}, new int[0]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new Node[]{}, new int[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.PRINT, new int[]{}, new Node[]{}, new int[0]);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IDENT, new int[]{}, new Node[]{}, new int[0]);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new int[0]);
    nodeFunctionDeclaration.setProperty("id", Node.newIdentifier("testFunction"));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new int[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new int[0]);
    nodeHoistedFunctionDeclaration.setParent(Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, new int[0]));
    nodeHoistedFunctionDeclaration.setProperty("id", Node.newIdentifier("testFunction"));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new int[0]);
    nodeNotHoistedFunctionDeclaration.setParent(Node.newNode(Node.newNode(Token.IF, new int[]{}, new Node[]{}, new int[0]), new int[]{}, new Node[]{}, new int[0]));
    nodeNotHoistedFunctionDeclaration.setProperty("id", Node.newIdentifier("testFunction"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  // Edge cases and exception handling scenarios

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

  @Test
  public void testIsForInWithEmptyChildArray() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[0], new int[0]);
    assertFalse(NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsForInWithTwoChildren() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[] { Node.newNode(Token.IDENT, new int[]{}, new Node[]{}, new int[0]), Node.newNode(Token.ASSIGN, new int[]{}, new Node[] { Node.newNode(Token.IDENT, new int[]{}, new Node[]{}, new int[0]), Node.newNode(Token.NUMBER, new int[]{}, new Node[]{}, new int[0]) }, new int[0]) }, new int[0]);
    assertFalse(NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsStatementWithNoParent() {
    Node nodeStatement = Node.newNode(Token.PRINT, new int[]{}, new Node[]{}, new int[0]);
    nodeStatement.setParent(null);
    assertFalse(NodeUtil.isStatement(nodeStatement));
  }

  @Test
  public void testIsStatementWithThreeChildren() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new Node[] { Node.newNode(Token.PRINT, new int[]{}, new Node[]{}, new int[0]), Node.newNode(Token.PRINT, new int[]{}, new Node[]{}, new int[0]), Node.newNode(Token.PRINT, new int[]{}, new Node[]{}, new int[0]) }, new int[0]);
    assertFalse(NodeUtil.isStatement(nodeStatement));
  }

  @Test
  public void testIsFunctionDeclarationWithNoType() {
    Node nodeFunctionDeclaration = Node.newNode(0, new int[]{}, new Node[]{}, new int[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationWithNoStatementParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new int[0]);
    nodeFunctionDeclaration.setParent(Node.newNode(Token.EXPRESSION_STATEMENT, new int[]{}, new Node[] { nodeFunctionDeclaration }, new int[0]));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }
}