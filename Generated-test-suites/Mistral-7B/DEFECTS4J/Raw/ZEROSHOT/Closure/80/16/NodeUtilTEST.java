import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction("myFunction", new String[]{"x"}, null, Node.newBlock(null));
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = Node.newIfStatement(Node.newExpression(Node.newName("x")), Node.newBlock(null), Node.newBlock(null));
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeLabel = Node.newLabel("myLabel");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeVariable = Node.newVariable("x", null, null);
    assertFalse(NodeUtil.isStatement(nodeVariable));

    Node nodeAssignment = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    assertFalse(NodeUtil.isStatement(nodeAssignment));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("myFunction", new String[]{"x"}, null, Node.newBlock(null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("myFunction", new String[]{"x"}, null, Node.newBlock(null)));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeFunctionDeclaration = Node.newFunction("myFunction", new String[]{"x"}, null, Node.newBlock(null));
    Node childFunctionDeclaration = Node.newFunction("myChildFunction", new String[]{"y"}, null, Node.newBlock(null));
    nodeFunctionDeclaration.addChildToEnd(childFunctionDeclaration);
    nodeFunctionDeclaration.addChildToEnd(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunction = Node.newFunction("myFunction", new String[]{"x"}, null, Node.newBlock(null));
    Node parentBlock = Node.newBlock(null);
    parentBlock.addChildToEnd(nodeFunction);
    nodeFunction.addChildToEnd(nodeScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}