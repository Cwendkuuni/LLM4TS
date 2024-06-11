import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createLabel;
import static com.google.javascript.rhino.NodeFactory.createFunction;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR, null, createToken(Token.FOR, null), createToken(Token.IN, null), null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.IF, null, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.IF, null, null, null, null, null, null, null, null, null);
    Node parentScript = createScript(null);
    parentScript.addChild(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode(Token.IF, null, null, null, null, null, null, null, createToken(Token.NUMBER, null, 1), null);
    Node parentFunction = createFunction("functionName", null, null, null, null, null, null, null, null, null);
    parentFunction.addChild(nodeExpression);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createFunction("functionName", null, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null);
    Node parentScript = createScript(null);
    parentScript.addChild(nodeFunctionExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = createFunction("functionName", null, null, null, null, null, null, null, null, null);
    Node parentScript = createScript(null);
    parentScript.addChild(nodeFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = createFunction("functionName", null, null, null, null, null, null, null, null, null);
    Node parentFunction = createFunction("parentFunctionName", null, null, null, null, null, null, null, null, null);
    parentFunction.addChild(nodeFunctionDeclarationInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateThrowsException() {
    Node node = createNode(Token.IF, null, null, null, null, null, null, null, null, null);
    Node parentNull = null;
    NodeUtil.isStatement(node);
    NodeUtil.checkState(parentNull != null, "parent is null");
  }
}