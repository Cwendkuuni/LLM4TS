import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[5]);
    Node nodeNotForIn = new Node(Token.IF, new int[5]);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[5]);
    Node nodeExpression = new Node(Token.ADD, new int[5]);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(Token.PRINT, new int[5]);
    Preconditions.checkState(node.getParent() != null);
    node.getParent() = null;
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[5]);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[5]);
    Node nodeStatement = new Node(Token.PRINT, new int[5]);

    nodeFunctionDeclaration.setParent(new Node(Token.SCRIPT, new int[5]));
    nodeFunctionExpression.setParent(new Node(Token.ASSIGN, new int[5]));
    nodeStatement.setParent(new Node(Token.BLOCK, new int[5]));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isFunctionDeclaration(nodeStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullParent() {
    Node node = new Node(Token.FUNCTION, new int[5]);
    node.setParent(null);
    isFunctionDeclaration(node);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[5]);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[5]);

    Node scriptNode = new Node(Token.SCRIPT, new int[5]);
    Node functionNode = new Node(Token.FUNCTION, new int[5]);

    nodeHoistedFunctionDeclaration.setParent(scriptNode);
    nodeFunctionDeclaration.setParent(functionNode);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node node = new Node(Token.FUNCTION, new int[5]);
    node.setParent(null);
    isHoistedFunctionDeclaration(node);
  }
}