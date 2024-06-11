import static org.junit.Assert.assertFalse;
import static org.junit.Assert.asserttrue;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(FOR, 0, new Object[] {createNode(IDENT, "i"), createNode(LEFT_PAREN, null), createNode(IDENT, "array"), createNode(RIGHT_PAREN, null), createNode(BLOCK, null, new Object[] {})});
    asserttrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(IF, 0, new Object[] {createNode(IDENT, "condition"), createNode(BLOCK, null, new Object[] {})});
    assertfalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(IF, 0, new Object[] {createNode(IDENT, "condition"), createNode(BLOCK, null, new Object[] {})});
    asserttrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = createNode(IDENT, "identifier");
    assertfalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(FUNCTION, 0, new Object[] {"name", createNode(BLOCK, null, new Object[] {})});
    nodeFunctionDeclaration.setProperty("name", createNode(IDENT, "name"));
    asserttrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = createNode(IDENT, "identifier");
    assertfalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = createNode(SCRIPT, 0, new Object[] {});
    Node nodeFunctionDeclaration = createNode(FUNCTION, 0, new Object[] {"name", createNode(BLOCK, null, new Object[] {})});
    nodeFunctionDeclaration.setProperty("name", createNode(IDENT, "name"));
    nodeScript.setChild(0, nodeFunctionDeclaration);
    asserttrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionInExpression = createNode(FUNCTION, 0, new Object[] {"name", createNode(BLOCK, null, new Object[] {})});
    nodeFunctionInExpression.setProperty("name", createNode(IDENT, "name"));
    Node nodeExpression = createNode(EXPR_STATEMENT, 0, new Object[] {nodeFunctionInExpression});
    Node nodeStatement = createNode(BLOCK, 0, new Object[] {nodeExpression});
    assertfalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInExpression));
  }

  @Test(expected = IllegalStateException.class)
  public void testPreconditionsCheckStateFailure() {
    Node nodeNullParent = createNode(BLOCK, null, new Object[] {});
    Node node = createNode(IDENT, "identifier");
    NodeUtil.isStatement(node);
    node.setParent(nodeNullParent);
  }
}