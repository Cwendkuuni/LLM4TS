import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createToken(Token.FOR, "for", null);
    Node nodeWithThreeChildren = createToken(Token.FOR, "for", new Object[]{"init", "condition", "next"});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertTrue(NodeUtil.isForIn(nodeWithThreeChildren));

    Node nodeNotForIn = createToken(Token.IF, "if", null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createToken(Token.IF, "if", null);
    Node nodeNotStatement = createToken(Token.IDENT, "identifier", null);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalStateException.class)
  public void testParentIsNull() {
    Node node = createToken(Token.IF, "if", null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createFunctionDeclaration();
    Node nodeFunctionExpression = createFunctionExpression();

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createHoistedFunctionDeclaration();
    Node nodeNonHoistedFunctionDeclaration = createNonHoistedFunctionDeclaration();

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }

  private Node createFunctionDeclaration() {
    Node functionDeclaration = createToken(Token.FUNCTION, "functionName", null);
    Node script = createToken(Token.SCRIPT, null, null);
    Node block = createBlock();
    script.setChild(0, functionDeclaration);
    script.setChild(1, block);
    return script;
  }

  private Node createFunctionExpression() {
    Node functionExpression = createToken(Token.FUNCTION, "functionName", null);
    Node expression = createToken(Token.ASSIGN, null, new Object[]{createIdentifier(), functionExpression});
    Node script = createToken(Token.SCRIPT, null, null);
    script.setChild(0, expression);
    return script;
  }

  private Node createIdentifier() {
    return createToken(Token.IDENT, "identifier", null);
  }

  private Node createHoistedFunctionDeclaration() {
    Node functionDeclaration = createFunctionDeclaration();
    return functionDeclaration;
  }

  private Node createNonHoistedFunctionDeclaration() {
    Node functionDeclaration = createFunctionDeclaration();
    Node function = createToken(Token.FUNCTION, "functionName", null);
    Node script = createToken(Token.SCRIPT, null, null);
    script.setChild(0, function);
    script.setChild(1, functionDeclaration);
    return script;
  }

  private Node createBlock() {
    return createToken(Token.BLOCK, null, null);
  }
}