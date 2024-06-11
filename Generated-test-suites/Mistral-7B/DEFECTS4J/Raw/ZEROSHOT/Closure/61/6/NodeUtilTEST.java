import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(1);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(1, new int[]{}, Node.newName("f"), null, null);
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    Node emptyParent = Node.newExpressionStatement(Node.newName(""));
    Preconditions.checkState(nodeStatement.getParent() != null);
    NodeUtil.isStatement(nodeStatement);
    Preconditions.checkState(emptyParent.getParent() == null);
    NodeUtil.isStatement(emptyParent);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, new int[]{}, Node.newName("f"), null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(1, new int[]{}, Node.newName("f"), null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeTopLevelFunction = Node.newFunction(1, new int[]{}, Node.newName("f"), null, null);
    Node nodeFunctionInsideFunction = Node.newFunction(1, new int[]{}, Node.newName("g"), null, null);
    Node nodeFunctionInsideBlock = Node.newFunction(1, new int[]{}, Node.newName("h"), null, null);
    Node nodeFunctionInsideExpression = Node.newExpressionStatement(Node.newFunction(1, new int[]{}, Node.newName("i"), null, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeTopLevelFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideExpression));
  }
}