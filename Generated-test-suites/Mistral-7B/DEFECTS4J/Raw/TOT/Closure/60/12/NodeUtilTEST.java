import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, null, createNode(Identifier, "i"), null, null, createNode(Block, null, null, null));
    Node notForNode = createNode(IF, null, createNode(Identifier, "condition"), null, null, createNode(Block, null, null, null));

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(PrintStatement, null, createNode(StringLiteral, "Hello, World!"), null);
    Node notStatementNode = createNode(Identifier, "x");

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(notStatementNode));

    Node functionNode = createNode(FunctionDeclaration, null, createNode(Identifier, "functionName"), null, null, null);
    assertTrue(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(FunctionDeclaration, null, createNode(Identifier, "functionName"), null, null, null);
    Node functionExpressionNode = createNode(FunctionExpression, null, createNode(Identifier, "functionName"), null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createNode(FunctionDeclaration, createNode(LabeledStatement, null, createNode(Identifier, "label"), functionDeclarationNode), null, null, null, null);
    Node notHoistedFunctionDeclarationNode = createNode(FunctionDeclaration, null, createNode(Identifier, "functionName"), null, null, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = createNode(Identifier, "node");
    Node parent = createNode(IF, null, null, null, null, node);
    Preconditions.checkState(parent == null);
  }
}