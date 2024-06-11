import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[]{}, new int[1]);
    Node parentScript = new Node(Token.SCRIPT, new int[0]);
    parentScript.addChildToEnd(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IDENT, new int[]{1}, new int[0]);
    Node parentExpression = new Node(Token.ADD, new int[]{1, 1}, new int[2]);
    parentExpression.addChildToEnd(nodeNotStatement);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1}, new int[1]);
    nodeFunctionDeclaration.setProperty("id", "testFunction");
    Node parentBlock = new Node(Token.BLOCK, new int[0]);
    parentBlock.addChildToEnd(nodeFunctionDeclaration);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1}, new int[1]);
    Node parentExpression = new Node(Token.ADD, new int[]{1, 1}, new int[2]);
    parentExpression.addChildToEnd(nodeFunctionExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1}, new int[1]);
    nodeHoistedFunctionDeclaration.setProperty("id", "testFunction");
    Node parentScript = new Node(Token.SCRIPT, new int[0]);
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{1}, new int[1]);
    nodeFunctionDeclarationInFunction.setProperty("id", "testFunction");
    Node parentFunction = new Node(Token.FUNCTION, new int[]{1}, new int[1]);
    parentFunction.addChildToEnd(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testIsForInWithNullNode() {
    Node node = null;
    NodeUtil.isForIn(node);
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullNode() {
    Node node = null;
    Node parent = new Node(Token.SCRIPT, new int[0]);
    parent.addChildToEnd(node);
    NodeUtil.isStatement(node);
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node node = null;
    Node parentBlock = new Node(Token.BLOCK, new int[0]);
    parentBlock.addChildToEnd(node);
    NodeUtil.isFunctionDeclaration(node);
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node node = null;
    Node parentScript = new Node(Token.SCRIPT, new int[0]);
    parentScript.addChildToEnd(node);
    NodeUtil.isHoistedFunctionDeclaration(node);
  }
}