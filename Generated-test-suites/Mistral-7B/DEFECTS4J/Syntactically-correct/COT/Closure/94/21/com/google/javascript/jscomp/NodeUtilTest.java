import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp. Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue("isForIn should return true for FOR-IN loop", NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertFalse("isForIn should return false for non-FOR-IN node", NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {1, 2, 3});
    Node parent = Node.newNode(Token.BLOCK, new int[] {1});
    parent.addChildToEnd(nodeStatement);

    assertTrue("isStatement should return true for statement node", NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertFalse("isStatement should return false for non-statement node", NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, "myFunction");
    assertTrue("isFunctionDeclaration should return true for function declaration",
        NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2});
    assertFalse("isFunctionDeclaration should return false for non-function declaration",
        NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, "myFunction");
    Node parentScript = Node.newNode(Token.SCRIPT, new int[] {});
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);

    assertTrue("isHoistedFunctionDeclaration should return true for hoisted function declaration",
        NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, "myFunction");
    Node parentFunction = Node.newNode(
        Token.FUNCTION, new int[] {1}, "outerFunction");
    parentFunction.addChildToEnd(nodeNotHoistedFunctionDeclaration);

    assertFalse("isHoistedFunctionDeclaration should return false for non-hoisted function declaration",
        NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInWithNullNode() {
    assertThrows("isForIn should throw an IllegalArgumentException for null node",
        IllegalStateException.class, () -> NodeUtil.isForIn(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    assertThrows("isStatement should throw an IllegalArgumentException for null node",
        IllegalStateException.class, () -> NodeUtil.isStatement(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    assertThrows("isFunctionDeclaration should throw an IllegalArgumentException for null node",
        IllegalStateException.class, () -> NodeUtil.isFunctionDeclaration(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithoutName() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2});
    assertThrows("isFunctionDeclaration should throw an IllegalArgumentException for function declaration without name",
        IllegalArgumentException.class, () -> NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testPreconditionsCheckStateWithNullParent() {
    Node node = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertThrows("Preconditions.checkState should throw an IllegalStateException for null parent",
        IllegalStateException.class, () -> Preconditions.checkState(node.getParent() == null));
  }

}