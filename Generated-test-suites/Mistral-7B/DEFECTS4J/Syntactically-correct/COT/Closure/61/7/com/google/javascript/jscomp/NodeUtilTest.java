import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    nodeForIn.setChildCount(3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    nodeStatement.setParent(parentScript);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, null);
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, null);
    Node parentBlock = new Node(Token.BLOCK, null);
    nodeFunction.setParent(parentBlock);
    assertTrue(isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    Node parentExpression = new Node(Token.BINARY_PLUS, null);
    nodeFunctionExpression.setParent(parentExpression);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    nodeFunction.setParent(parentScript);
    assertTrue(isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = new Node(Token.FUNCTION, null);
    Node parentFunction = new Node(Token.FUNCTION, null);
    nodeFunction.setParent(parentFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullParent() {
    Node nodeStatement = new Node(Token.IF, null);
    assertFalse(isStatement(nodeStatement));
    nodeStatement.setParent(null); // Should fail here
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node nodeFunction = new Node(Token.FUNCTION, null);
    assertFalse(isFunctionDeclaration(nodeFunction));
    nodeFunction.setParent(null); // Should fail here
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNonStatementParent() {
    Node nodeFunction = new Node(Token.FUNCTION, null);
    Node parentExpression = new Node(Token.BINARY_PLUS, null);
    nodeFunction.setParent(parentExpression);
    assertFalse(isFunctionDeclaration(nodeFunction));
  }
}