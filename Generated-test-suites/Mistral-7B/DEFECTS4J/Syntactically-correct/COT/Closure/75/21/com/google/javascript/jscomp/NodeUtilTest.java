import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{}, new int[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{}, new int[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{}, new int[1]);
    Node parent = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    parent.setChild(0, nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, new int[]{}, new int[]{}, new int[1]);
    Node parentNotScript = new Node(Token.BLOCK, new int[]{}, new int[]{}, new int[0]);
    parentNotScript.setChild(0, nodeNotStatement);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));

    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[1]);
    Node parentFunction = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    parentFunction.setChild(0, nodeFunction);
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[2]);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, new int[]{}, new int[1], "functionName".toCharArray()));
    Node parent = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    parent.setChild(0, nodeFunctionDeclaration);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[1]);
    Node parentFunctionExpression = new Node(Token.ASSIGNMENT, new int[]{}, new int[]{}, new int[2]);
    parentFunctionExpression.setChild(0, new Node(Token.IDENTIFIER, new int[]{}, new int[1], "x".toCharArray()));
    parentFunctionExpression.setChild(1, nodeFunctionExpression);
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    parentScript.setChild(0, parentFunctionExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariableDeclaration = new Node(Token.VAR, new int[]{}, new int[]{}, new int[2]);
    nodeVariableDeclaration.setChild(0, new Node(Token.IDENTIFIER, new int[]{}, new int[1], "functionName".toCharArray()));
    nodeVariableDeclaration.setChild(1, new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[1]));
    Node parentVariableDeclaration = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    parentVariableDeclaration.setChild(0, nodeVariableDeclaration);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[2]);
    nodeHoistedFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, new int[]{}, new int[1], "functionName".toCharArray()));
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[2]);
    nodeFunctionDeclarationInFunction.setChild(0, new Node(Token.IDENTIFIER, new int[]{}, new int[1], "functionName".toCharArray()));
    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[1]);
    parentFunction.setChild(0, nodeFunctionDeclarationInFunction);
    Node parentScriptFunction = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    parentScriptFunction.setChild(0, parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}