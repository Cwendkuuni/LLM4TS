import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.TokenType.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(FOR, null, createNode(IDENT, "for"), createNode(ASSIGN, null, createNode(IDENT, "i"), createNode(LITERAL, 0)), createNode(SEMICOLON, null), null, createNode(PARENTHESIS_OPEN, null), null, createNode(PARENTHESIS_CLOSE, null), null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(IF, null, null, createNode(IDENT, "condition"), null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(PRINT, null, createNode(STRING_LITERAL, "Hello World"), null);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode(ADD, null, createNode(IDENT, "a"), createNode(LITERAL, 5));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(FUNCTION, null, createNode(IDENT, "functionName"), null, createNode(BLOCK, null), null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(IDENT, "functionName");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createNode(FUNCTION, null, createNode(IDENT, "functionName"), null, createNode(BLOCK, null), null);
    Node parentScript = createNode(SCRIPT, null, null);
    parentScript.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeHoistedFunctionDeclarationInFunction = createNode(FUNCTION, null, null, null, createNode(BLOCK, null), null);
    Node parentFunction = createNode(FUNCTION, null, createNode(IDENT, "parentFunctionName"), null, createNode(BLOCK, null), null);
    parentFunction.addChild(nodeHoistedFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node nodeParentNull = createNode(SCRIPT, null, null);
    Node nodeChild = createNode(IDENT, "child");
    nodeParentNull.addChild(nodeChild);
    NodeUtil.isStatement(nodeParentNull);
  }

  @Test
  public void testIsFunctionExpression() {
    // This method is not in the given NodeUtil class, but it is related.
    // Testing it here for completeness.

    Node nodeFunctionExpression = createNode(IDENT, "functionName");
    nodeFunctionExpression.setProperty("type", Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpression));
  }
}