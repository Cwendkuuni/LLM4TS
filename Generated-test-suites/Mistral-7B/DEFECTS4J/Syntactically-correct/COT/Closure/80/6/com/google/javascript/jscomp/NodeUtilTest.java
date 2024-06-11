import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeConstants;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement();
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = Node.newIfStatement();
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIntLiteral(1));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeFunction = Node.newFunction(null, null);
    assertFalse(NodeUtil.isStatement(nodeFunction));

    Node nodeScript = Node.newScript(NodeConstants.EMPTY_NODE_ARRAY);
    assertTrue(NodeUtil.isStatement(nodeScript));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeExpression = Node.newExpressionStatement(nodeFunction);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(null, null);
    Node script = Node.newScript(Node.newArray(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInsideFunction = Node.newFunction(null, null);
    Node functionNode = Node.newFunction(null, null);
    Node nodeFunctionInsideFunctionInsideFunction = Node.newFunction(null, null);
    Node function2Node = Node.newFunction(null, null);
    Node node = Node.newScript(
        Node.newArray(
            functionNode,
            Node.newArray(
                nodeFunctionInsideFunction,
                Node.newArray(nodeFunctionInsideFunctionInsideFunction)
            )
        ));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunctionInsideFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = null;
    NodeUtil.isStatement(node);
  }
}