import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, null, createNode(IDENT, "i"), null, null, null, null, null);
    Node notForNode = createNode(IF, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createNode(SCRIPT, null, null, null, null, null, null, null);
    Node statementNode = createNode(ASSIGN, null, createNode(IDENT, "x"), null, createNode(NUM, 5), null, null, null);
    Node expressionNode = createNode(ADD, null, createNode(NUM, 1), createNode(NUM, 2), null, null, null, null);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(FUNCTION, null, createNode(IDENT, "foo"), null, null, null, null, null);
    Node functionExpression = createNode(ASSIGN, null, createNode(IDENT, "bar"), createNode(FUNCTION, null, null, null, null, null, null, null), null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = createNode(FUNCTION, null, createNode(IDENT, "foo"), null, null, null, null, null);
    Node functionDeclarationInFunction = createNode(FUNCTION, null, null, null, null, null, null, null, createNode(FUNCTION, null, null, null, null, null, null, null), null, null, null);
    Node functionDeclarationInScript = createNode(FUNCTION, null, null, null, null, null, null, null, null, null, createNode(IDENT, "foo"), null, null, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScript));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}