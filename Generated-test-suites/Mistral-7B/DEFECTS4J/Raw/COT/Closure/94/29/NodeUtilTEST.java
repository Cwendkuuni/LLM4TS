import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, null, null, null, null);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createNode(Token.IF, null, null, null, null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.IF, null, null, null, null);
    Node notStatementNode = createNode(Token.IDENT, "func", null, null, null);

    Node parentScript = createNode(Token.SCRIPT, null, null, null, null);
    Node parentBlock = createNode(Token.BLOCK, null, null, null, null);
    Node parentLabel = createNode(Token.LABEL, "label", null, null, null);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(parentScript));
    assertTrue(NodeUtil.isStatement(parentBlock));
    assertFalse(NodeUtil.isStatement(parentLabel));
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, "func", null, null, null);
    Node notFunctionNode = createNode(Token.IF, null, null, null, null);

    Node statementParent = createNode(Token.BLOCK, null, null, null, null);
    Node expressionParent = createNode(Token.IDENT, "expression", null, null, null);

    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(expressionParent));

    Node functionDeclaration = createNode(Token.FUNCTION, "func", null, null, null);
    Node functionStatement = createNode(Token.FUNCTION, "func", null, statementParent, null);

    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isFunctionDeclaration(functionStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = createNode(Token.FUNCTION, "func", null, createNode(Token.SCRIPT, null, null, null, null), null);
    Node notHoistedFunctionDeclaration = createNode(Token.FUNCTION, "func", null, createNode(Token.BLOCK, null, null, null, null), null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateThrowsException() {
    Node n = createNode(Token.IDENT, "n", null, null, null);
    Preconditions.checkState(n.getParent() == null);
  }
}