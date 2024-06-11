import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, null, createNode(Identifier, "i"), null, null, createNode(Identifier, "arr"), null, createNode(Block, null, null, null), null);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createNode(IF, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Identifier, "x");
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode(BinaryOp, OPERATOR_PLUS, createNode(NumberLiteral, 1), createNode(NumberLiteral, 2));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Function, null, createNode(Identifier, "func"), null, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode(Function, null, null, null, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = createNode(Function, null, createNode(Identifier, "func"), null, null, null, null, null, null, null, null, null, createNode(Block, null, null, null));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

    Node notHoistedFunctionNode = createNode(Function, null, createNode(Identifier, "func"), null, null, null, null, null, null, null, null, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateFailure() {
    Node n = createNode(Identifier, "x");
    Node parent = createNode(Identifier, "y");
    Node parentOfParent = createNode(Identifier, "z");
    n.setParent(parent);
    parent.setParent(parentOfParent);

    NodeUtil.isStatement(n);
  }
}