import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newName("x");
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateThrowsException() {
    Node node = Node.newExpressionStatement(Node.newName("x"));
    Preconditions.checkState(node.getParent() == null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newName("myFunction"));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newName("myFunction")));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), null);
    Node parentScript = Node.newBlock(Node.newStatementList());
    parentScript.setParent(null);
    nodeHoistedFunctionDeclaration.setParent(parentScript);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), null);
    Node parentFunction = Node.newFunction(Node.newName("parentFunction"), null);
    nodeNotHoistedFunctionDeclaration.setParent(parentFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}