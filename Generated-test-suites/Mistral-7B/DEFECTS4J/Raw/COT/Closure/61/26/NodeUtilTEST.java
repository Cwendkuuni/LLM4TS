import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[1], new Object[1]);
    Node nodeNotForIn = new Node(Token.IF, new int[1], new Object[1]);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[1], new Object[1]);
    Node nodeExpression = new Node(Token.ADD, new int[2], new Object[2]);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateFailedParentIsNull() {
    Node node = new Node(Token.IDENT, new int[1], new Object[1]);
    Node parentNull = null;

    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[2], new Object[2]);
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[1], new Object[1]);
    nodeFunctionExpression.setProperty("id", "testFunction");
    Node parent = new Node(Token.SCRIPT, new int[1], new Object[1]);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    nodeFunctionExpression.setParent(parent);
    assertTrue(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[2], new Object[2]);
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node nodeFunctionDeclarationInScript = new Node(Token.SCRIPT, new int[1], new Object[1]);
    nodeFunctionDeclarationInScript.setChild(0, nodeFunctionDeclaration);

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[1], new Object[1]);
    nodeFunctionDeclarationInFunction.setChild(0, nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}