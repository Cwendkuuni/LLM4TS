import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.FOR;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.SCRIPT;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR);
    Node child1 = new Node(1);
    Node child2 = new Node(1);
    Node child3 = new Node(1);
    nodeForIn.setChild(0, child1);
    nodeForIn.setChild(1, child2);
    nodeForIn.setChild(2, child3);

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(FUNCTION);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT);
    Node nodeStatement = new Node(1);
    Node nodeFunction = new Node(FUNCTION);

    nodeScript.setChild(0, nodeStatement);
    nodeScript.setChild(1, nodeFunction);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeFunction));

    Node nodeBlock = new Node(BLOCK);
    nodeBlock.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = new Node(LABEL);
    nodeLabel.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION);
    Node nodeStatement = new Node(1);
    Node nodeScript = new Node(SCRIPT);
    Node nodeFunctionInExpression = new Node(FUNCTION);

    nodeFunctionDeclaration.setChild(0, nodeStatement);
    nodeFunctionDeclaration.setName("functionName");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeStatement));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeScript));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionInExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION);
    Node nodeStatement = new Node(1);
    Node nodeScript = new Node(SCRIPT);
    Node nodeFunction = new Node(FUNCTION);

    nodeFunctionDeclaration.setChild(0, nodeStatement);
    nodeFunctionDeclaration.setName("functionName");

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    nodeScript.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    nodeFunction.setChild(0, nodeFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFail() {
    Node node = new Node(1);
    Preconditions.checkState(node.getParent() == null);
  }
}