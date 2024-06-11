import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement(1, 2, 3, 4, 5, null);
    Node nodeIf = Node.newExpressionStatement(Node.newIfStatement(1, Node.newLiteral(true), Node.newLiteral(1)), null);
    Node nodeFunction = Node.newFunction(1, "test", new String[]{"x"}, Node.newExpressionStatement(Node.newLiteral(1), null), null);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeFor = Node.newForStatement(1, 2, 3, 4, 5, null);
    Node nodeIf = Node.newExpressionStatement(Node.newIfStatement(1, Node.newLiteral(true), Node.newLiteral(1)), null);
    Node nodeFunction = Node.newFunction(1, "test", new String[]{"x"}, Node.newExpressionStatement(Node.newLiteral(1), null), null);
    Node nodeScript = Node.newScript(new int[]{}, null, new Node[]{}, null);

    Node parentFor = Node.newBlock(1, new Node[]{}, nodeFor, null);
    Node parentIf = Node.newExpressionStatement(nodeIf, null);
    Node parentFunction = Node.newBlock(1, new Node[]{}, nodeFunction, null);

    assertTrue(NodeUtil.isStatement(nodeFor));
    assertTrue(NodeUtil.isStatement(parentFor));
    assertTrue(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeScript));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, "test", new String[]{"x"}, Node.newExpressionStatement(Node.newLiteral(1), null), null);
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(1, "test", new String[]{"x"}, Node.newExpressionStatement(Node.newLiteral(1), null), null), null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionRoot = Node.newFunction(1, "test", new String[]{"x"}, Node.newExpressionStatement(Node.newLiteral(1), null), null);
    Node nodeFunctionNested = Node.newFunction(2, "test", new String[]{"x"}, Node.newExpressionStatement(Node.newLiteral(1), null), null);

    Node nodeScript = Node.newScript(new int[]{}, null, new Node[]{}, nodeFunctionRoot);
    Node nodeBlock = Node.newBlock(1, new Node[]{}, nodeFunctionNested, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionRoot));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionNested));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeBlock));
  }
}