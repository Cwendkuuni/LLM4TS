import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

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
    Node nodeFor = Node.newForStatement();
    assertTrue(NodeUtil.isStatement(nodeFor));

    Node nodeIf = Node.newIfStatement();
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeFunction = Node.newFunctionExpression(null, null, null);
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunctionDeclaration(null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newFunctionExpression(null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeFunction = Node.newFunctionDeclaration(null, null, null);
    nodeScript.addChild(nodeFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInsideFunction = Node.newFunctionDeclaration(null, null, null);
    Node nodeFunctionParent = Node.newFunctionExpression(null, null, null);
    nodeFunctionParent.addChild(nodeFunctionInsideFunction);
    nodeScript.addChild(nodeFunctionParent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }

  @Test(expected = AssertionError.class)
  public void testCheckState() {
    Node node = Node.newScript();
    Preconditions.checkState(node.getType() == Token.SCRIPT);
  }
}