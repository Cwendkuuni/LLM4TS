import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, 1, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration(
        1, "myFunction", Node.newNodeList(), Node.newBlock(null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunctionExpression(null, Node.newIdentifier("myFunction"),
            Node.newNodeList(), Node.newBlock(null)));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunctionDeclaration(
        1, "myFunction", Node.newNodeList(), Node.newBlock(null));
    Node parentScript = Node.newScript(Node.newNodeList(), null);
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunctionDeclaration(
        1, "myFunction", Node.newNodeList(), Node.newBlock(null));
    Node parentFunction = Node.newFunctionStatement(
        1, null, Node.newNodeList(), Node.newBlock(null), null);
    parentFunction.addChildToEnd(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsForInWithNullNode() {
    assertNull(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsStatementWithNullNode() {
    assertNull(NodeUtil.isStatement(null));
  }

  @Test
  public void testIsFunctionDeclarationWithNullNode() {
    assertNull(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }
}