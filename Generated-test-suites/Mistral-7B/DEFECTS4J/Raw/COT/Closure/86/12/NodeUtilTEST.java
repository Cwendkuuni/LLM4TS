import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;
import org.junit.Test;
import org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{-1, -1, -1}, new int[]{3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{-1, -1}, new int[]{1, 1, 1});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{-1, -1}, new int[]{1, 1, 1});
    Node parentStatement = new Node(Token.BLOCK, new int[]{-1, -1}, new int[]{1, 1});
    parentStatement.appendChild(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, new int[]{-1, -1}, new int[]{1, 1, 1});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentNull() {
    Node node = new Node(Token.IF, new int[]{-1, -1}, new int[]{1, 1, 1});
    assertFalse(NodeUtil.isStatement(node));
    Node parentNull = null;
    assertNull(parentNull);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{-1, -1}, new int[]{1, 1, 1});
    Node parentStatement = new Node(Token.BLOCK, new int[]{-1, -1}, new int[]{1, 1});
    parentStatement.appendChild(nodeFunctionDeclaration);
    parentStatement.setParent(new Node(Token.SCRIPT, new int[]{-1, -1}, new int[]{1, 1}));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{-1, -1}, new int[]{1, 1, 1});
    Node parentExpression = new Node(Token.ASSIGN, new int[]{-1, -1}, new int[]{1, 1, 1});
    parentExpression.appendChild(nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{-1, -1}, new int[]{1, 1, 1});
    Node parentScript = new Node(Token.SCRIPT, new int[]{-1, -1}, new int[]{1, 1});
    parentScript.appendChild(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{-1, -1}, new int[]{1, 1, 1});
    Node parentFunction = new Node(Token.FUNCTION, new int[]{-1, -1}, new int[]{1, 1, 1});
    parentFunction.setName("functionName");
    parentFunction.appendChild(nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}