import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.BLOCK;
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
    nodeForIn.setChild(0, new Node("for"));
    nodeForIn.setChild(1, new Node("in"));
    nodeForIn.setChild(2, new Node("identifier", "x"));

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(FOR);
    nodeNotForIn.setChild(0, new Node("for"));
    nodeNotForIn.setChild(1, new Node("identifier", "x"));
    nodeNotForIn.setChild(2, new Node("identifier", "y"));

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT);
    Node nodeStatement = new Node(LABEL);
    nodeStatement.setChild(0, new Node("label", "myLabel"));

    Node nodeExpression = new Node(IDENTIFIER, "myExpression");

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(FUNCTION);
    nodeFunction.setChild(0, new Node("function", "myFunction"));
    nodeFunction.setChild(1, new Node(BLOCK));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(FUNCTION);
    nodeFunctionExpression.setChild(0, new Node("function"));
    nodeFunctionExpression.setChild(1, new Node("identifier", "myFunction"));
    nodeFunctionExpression.setChild(2, new Node(BLOCK));
    nodeFunctionExpression.setParent(new Node(ASSIGNMENT));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunction = new Node(FUNCTION);
    nodeHoistedFunction.setChild(0, new Node("function", "myFunction"));
    nodeHoistedFunction.setChild(1, new Node(IDENTIFIER, "myFunction"));
    nodeHoistedFunction.setChild(2, new Node(BLOCK));

    Node nodeParentScript = new Node(SCRIPT);
    nodeParentScript.setChild(0, nodeHoistedFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunction));

    Node nodeParentFunction = new Node(FUNCTION);
    nodeParentFunction.setChild(0, new Node("function", "myFunction"));
    nodeParentFunction.setChild(1, nodeHoistedFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunction));
  }

  @Test(expected = RuntimeException.class)
  public void testParentNull() {
    Node node = new Node(SCRIPT);
    NodeUtil.isStatement(node);
  }
}