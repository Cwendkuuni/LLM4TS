import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {1, 2, 3});
    Node parentOfStatement = Node.newNode(Token.SCRIPT, new int[] {});
    parentOfStatement.addChildToEnd(nodeStatement);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertFalse(isStatement(nodeStatement));
    Preconditions.checkState(nodeStatement.getParent() != null);
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, Node.newName("funcName"));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2});
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, Node.newName("funcName"));
    Node parentOfFunctionDeclaration = Node.newNode(Token.SCRIPT, new int[] {});
    parentOfFunctionDeclaration.addChildToEnd(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, Node.newName("funcName"));
    Node parentOfFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, new int[] {});
    parentOfFunctionDeclarationInFunction.addChildToEnd(nodeFunctionDeclarationInFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @org.junit.Test
  public void testIsForInWithOneChild() {
    Node nodeForInWithOneChild = Node.newNode(Token.FOR, new int[] {1});
    assertFalse(isForIn(nodeForInWithOneChild));
  }

  @org.junit.Test
  public void testIsForInWithFourChildren() {
    Node nodeForInWithFourChildren = Node.newNode(Token.FOR, new int[] {1, 2, 3, 4});
    assertFalse(isForIn(nodeForInWithFourChildren));
  }

  @org.junit.Test
  public void testIsStatementWithTwoChildren() {
    Node nodeStatementWithTwoChildren = Node.newNode(Token.IF, new int[] {1, 2});
    Node parentOfStatementWithTwoChildren = Node.newNode(Token.SCRIPT, new int[] {});
    parentOfStatementWithTwoChildren.addChildToEnd(nodeStatementWithTwoChildren);
    assertFalse(isStatement(nodeStatementWithTwoChildren));
  }

  @org.junit.Test
  public void testIsStatementWithZeroChildren() {
    Node nodeStatementWithZeroChildren = Node.newNode(Token.IF, new int[] {});
    Node parentOfStatementWithZeroChildren = Node.newNode(Token.SCRIPT, new int[] {});
    parentOfStatementWithZeroChildren.addChildToEnd(nodeStatementWithZeroChildren);
    assertFalse(isStatement(nodeStatementWithZeroChildren));
  }
}