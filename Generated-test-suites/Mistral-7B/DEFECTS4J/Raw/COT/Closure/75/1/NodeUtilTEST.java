import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChild(0, new Node(Token.IDENTIFIER, "forVar"));
    nodeForIn.setChild(1, new Node(Token.KEYWORD, "in"));
    nodeForIn.setChild(2, new Node(Token.IDENTIFIER, "collection"));

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF);
    assertTrue(NodeUtil.isForIn(nodeNotForIn) == false);
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.ASSIGN);
    Node left = new Node(Token.IDENTIFIER, "x");
    Node right = new Node(Token.NUMBER, 42);
    nodeStatement.setChild(0, left);
    nodeStatement.setChild(1, right);

    Node parentScript = new Node(Token.SCRIPT);
    parentScript.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IDENTIFIER, "x");

    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.BLOCK, 0, "{}"));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION);
    nodeFunctionExpression.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeFunctionExpression.setChild(1, new Node(Token.BLOCK, 0, "{}"));
    nodeFunctionExpression.setParent(new Node(Token.ASSIGN));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION);
    nodeHoistedFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeHoistedFunctionDeclaration.setChild(1, new Node(Token.BLOCK, 0, "{}"));
    Node parentScript = new Node(Token.SCRIPT);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION);
    nodeFunctionDeclarationInFunction.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeFunctionDeclarationInFunction.setChild(1, new Node(Token.BLOCK, 0, "{}"));
    Node parentFunction = new Node(Token.FUNCTION);
    parentFunction.setChild(0, new Node(Token.IDENTIFIER, "outerFunction"));
    parentFunction.setChild(1, new Node(Token.BLOCK, 0, "{}"));
    parentFunction.setChild(2, nodeFunctionDeclarationInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testNullParentNode() {
    Node node = new Node(Token.ASSIGN);
    assertFalse(NodeUtil.isStatement(node));
    node.setParent(null);
    NodeUtil.isStatement(node);
  }
}