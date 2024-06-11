import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression("{}");
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node nodeFor = Node.newExpression("for (var i = 0; i < 10; i++) {}", Token.FOR);
    assertFalse(NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression("{}");
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression("1 + 1");
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newExpression("function foo() {}", FUNCTION);
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newExpression("function foo() {}", FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression("function foo() {}", FUNCTION_DECLARATION);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newBlock(Node.newExpression("function foo() {}", FUNCTION), null);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));

    Node nodeFunctionInFunction = Node.newBlock(
        Node.newExpression("function bar() {}", FUNCTION),
        Node.newBlock(
            Node.newExpression("function foo() {}", FUNCTION),
            null)
    );
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction.getFirstChild()));
  }
}