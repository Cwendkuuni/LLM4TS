import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(1, Node.newIdentifier("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(1, Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        1, Node.newIdentifier("myFunction"), null, null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        1, Node.newFunction(1, Node.newIdentifier("myFunction"), null, null, null, null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression.getFirstChild()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        1, Node.newIdentifier("myFunction"), null, null, null, null, null);
    Node parentScript = Node.newScript(1, null);
    Node parentFunction = Node.newFunction(1, Node.newIdentifier("outerFunction"), null, null, null, null, null);
    parentScript.addChildToChildren(parentFunction);
    parentFunction.addChildToChildren(nodeFunctionDeclaration);

    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);

    NodeUtil.isHoistedFunctionDeclaration(Node.newFunction(
        1, Node.newIdentifier("myFunction2"), null, null, null, null, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(Node.newFunctionExpression(
        1, Node.newIdentifier("myFunction3"), null, null, null, null, null)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}