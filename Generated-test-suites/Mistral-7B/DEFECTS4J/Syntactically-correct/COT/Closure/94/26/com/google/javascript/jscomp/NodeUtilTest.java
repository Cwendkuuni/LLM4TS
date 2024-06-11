import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    nodeForIn.setChildCount(3);

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    nodeStatement.setParent(parentScript);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.ADD, null);
    Node parentExpression = new Node(Token.MUL, null);
    nodeNotStatement.setParent(parentExpression);

    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "myFunction");
    nodeFunctionDeclaration.setParent(new Node(Token.SCRIPT, null));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    nodeFunctionExpression.setParent(new Node(Token.ASSIGN, null));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, "myFunction");
    nodeHoistedFunctionDeclaration.setParent(new Node(Token.SCRIPT, null));
    Node parentScript = nodeHoistedFunctionDeclaration.getParent();

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, "myFunction");
    nodeFunctionDeclarationInFunction.setParent(new Node(Token.FUNCTION, null));
    Node parentFunction = nodeFunctionDeclarationInFunction.getParent();

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, "myFunction");
    nodeFunctionDeclarationInBlock.setParent(new Node(Token.BLOCK, null));
    Node parentBlock = nodeFunctionDeclarationInBlock.getParent();

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullParent() {
    Node nodeStatement = new Node(Token.PRINT, null);

    NodeUtil.isStatement(nodeStatement);
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);

    NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration);
  }
}