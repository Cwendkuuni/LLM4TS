import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, 1, 3, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(1, 1, new NodeOperatorNew(1, CONSTRUCTOR, "Array"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(1, 1, new NodeIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpressionStatement(1, 1, new NodeNumber(1));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, 1, "foo", new NodeParameterList(), null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(1, 1, Node.newFunction(1, 1, null, new NodeParameterList(), null, null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newFunction(1, 1, "foo", new NodeParameterList(), null, null, null);
    Node scriptNode = Node.newScript(1, 1, null, null, null, null, nodeFunctionDeclarationInScript, null);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(1, 1, "foo", new NodeParameterList(), null, null, null);
    Node functionNode = Node.newFunction(1, 1, "bar", null, null, nodeFunctionDeclarationInFunction, null, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}