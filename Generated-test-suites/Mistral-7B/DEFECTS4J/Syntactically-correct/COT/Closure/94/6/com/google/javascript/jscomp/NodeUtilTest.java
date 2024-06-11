import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeToken.FOR;
import static com.google.javascript.rhino.NodeToken.FUNCTION;
import static com.google.javascript.rhino.NodeToken.LABEL;
import static com.google.javascript.rhino.NodeToken.SCRIPT;
import static com.google.javascript.rhino.NodeType.BLOCK;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR);
    nodeForIn.setChild(0, new Node("for"));
    nodeForIn.setChild(1, new Node("in"));
    nodeForIn.setChild(2, new Node("("));
    nodeForIn.setChild(3, new Node(1));
    nodeForIn.setChild(4, new Node(")"));
    nodeForIn.setChild(5, new Node("("));
    nodeForIn.setChild(6, new Node(2));
    nodeForIn.setChild(7, new Node(")"));

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(FOR);
    nodeNotForIn.setChild(0, new Node("for"));
    nodeNotForIn.setChild(1, new Node("for")); // extra 'for'
    nodeNotForIn.setChild(2, new Node("("));
    nodeNotForIn.setChild(3, new Node(1));
    nodeNotForIn.setChild(4, new Node(")"));
    nodeNotForIn.setChild(5, new Node("("));
    nodeNotForIn.setChild(6, new Node(2));
    nodeNotForIn.setChild(7, new Node(")"));

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(SCRIPT);
    nodeStatement.setChild(0, new Node(BLOCK));
    nodeStatement.setChild(1, new Node(1)); // statement node

    assertTrue(NodeUtil.isStatement(nodeStatement.getChild(1)));

    Node nodeFunction = new Node(FUNCTION);
    nodeFunction.setChild(0, new Node("myFunction"));
    nodeFunction.setChild(1, new Node(BLOCK));

    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeLabel = new Node(LABEL);
    nodeLabel.setChild(0, new Node("label1"));

    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeNotStatement = new Node(SCRIPT);
    nodeNotStatement.setChild(0, new Node(BLOCK));
    nodeNotStatement.setChild(1, new Node(1)); // expression node

    assertFalse(NodeUtil.isStatement(nodeNotStatement.getChild(1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node(SCRIPT);
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION);
    nodeFunctionDeclaration.setChild(0, new Node("myFunction"));
    nodeFunctionDeclaration.setChild(1, new Node(BLOCK));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(FUNCTION);
    nodeFunctionExpression.setChild(0, new Node("myFunction"));
    nodeFunctionExpression.setChild(1, new Node("("));
    nodeFunctionExpression.setChild(2, new Node(1));
    nodeFunctionExpression.setChild(3, new Node(")"));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION);
    nodeFunctionDeclaration.setChild(0, new Node("myFunction"));
    nodeFunctionDeclaration.setChild(1, new Node(BLOCK));

    Node parentScript = new Node(SCRIPT);
    parentScript.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION);
    nodeFunctionDeclarationInFunction.setChild(0, new Node("function1"));
    nodeFunctionDeclarationInFunction.setChild(1, new Node(BLOCK));

    Node parentFunction = new Node(FUNCTION);
    parentFunction.setChild(0, new Node("function0"));
    parentFunction.setChild(1, nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}