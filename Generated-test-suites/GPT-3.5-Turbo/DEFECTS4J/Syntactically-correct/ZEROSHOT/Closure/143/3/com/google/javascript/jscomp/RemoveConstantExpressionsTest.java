import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RemoveConstantExpressionsTest {

  private RemoveConstantExpressions removeConstantExpressions;
  private Node externs;
  private Node root;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
    externs = new Node(Token.EXTERN);
    root = new Node(Token.ROOT);
  }

  @Test
  public void testProcess_NoExpressionStatements_NodesRemainUnchanged() {
    // Arrange

    // Act
    removeConstantExpressions.process(externs, root);

    // Assert
    Assert.assertEquals(null, externs.getFirstChild());
    Assert.assertEquals(null, root.getFirstChild());
  }

  @Test
  public void testProcess_ExpressionStatementsWithNoSideEffects_NodesWithSideEffectsRemoved() {
    // Arrange
    Node parent = new Node(Token.ROOT);
    Node node1 = new Node(Token.EXPR_RESULT);
    Node node2 = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node1);
    parent.addChildToFront(node2);

    // Act
    removeConstantExpressions.process(externs, parent);

    // Assert
    Assert.assertEquals(0, parent.getChildCount());
  }

  @Test
  public void testProcess_ExpressionStatementsWithSideEffects_NodesRemainedUnchanged() {
    // Arrange
    Node parent = new Node(Token.ROOT);
    Node node1 = new Node(Token.EXPR_RESULT);
    Node node2 = new Node(Token.EXPR_RESULT);
    Node exprBody1 = new Node(Token.VAR);
    Node exprBody2 = new Node(Token.FUNCTION);
    node1.addChildToFront(exprBody1);
    node2.addChildToFront(exprBody2);
    parent.addChildToFront(node1);
    parent.addChildToFront(node2);

    // Act
    removeConstantExpressions.process(externs, parent);

    // Assert
    Assert.assertEquals(2, parent.getChildCount());
    Assert.assertEquals(node1, parent.getFirstChild());
    Assert.assertEquals(node2, parent.getLastChild());
  }

  @Test
  public void testRemoveConstantRValuesCallback_Visit_ExpressionStatementNode_SideEffectNodesRemoved() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node(Token.ROOT);
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.VAR);
    node.addChildToFront(exprBody);
    parent.addChildToFront(node);

    // Act
    callback.visit(null, node, parent);

    // Assert
    Assert.assertEquals(0, parent.getChildCount());
  }

  @Test
  public void testRemoveConstantRValuesCallback_Visit_ExpressionStatementNode_SideEffectNodesRemainedUnchanged() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node(Token.ROOT);
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.FUNCTION);
    node.addChildToFront(exprBody);
    parent.addChildToFront(node);

    // Act
    callback.visit(null, node, parent);

    // Assert
    Assert.assertEquals(1, parent.getChildCount());
    Assert.assertEquals(node, parent.getFirstChild());
  }

  @Test
  public void testRemoveConstantRValuesCallback_GetSideEffectNodes_ExpressionBodyWithSideEffects_CorrectSideEffectNodesReturned() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node node1 = new Node(Token.EXPR_RESULT);
    Node node2 = new Node(Token.EXPR_RESULT);
    Node exprBody1 = new Node(Token.VAR);
    Node exprBody2 = new Node(Token.FUNCTION);
    node1.addChildToFront(exprBody1);
    node2.addChildToFront(exprBody2);
    Node node = new Node(Token.ROOT);
    node.addChildToFront(node1);
    node.addChildToFront(node2);

    // Act
    List<Node> sideEffectNodes = callback.getSideEffectNodes(node);

    // Assert
    Assert.assertEquals(2, sideEffectNodes.size());
    Assert.assertEquals(node1, sideEffectNodes.get(0));
    Assert.assertEquals(node2, sideEffectNodes.get(1));
  }

  @Test
  public void testRemoveConstantRValuesCallback_GetSideEffectNodes_ExpressionBodyWithNoSideEffects_NoSideEffectNodesReturned() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.STRING);
    node.addChildToFront(exprBody);

    // Act
    List<Node> sideEffectNodes = callback.getSideEffectNodes(node);

    // Assert
    Assert.assertEquals(0, sideEffectNodes.size());
  }

  @Test
  public void testRemoveConstantRValuesCallback_GetResult_ResultReturned() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    // Act
    RemoveConstantExpressions.Result result = callback.getResult();

    // Assert
    Assert.assertNotNull(result);
  }
}