import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;
import com.google.javascript.jscomp.token.Token;
import com.google.common.base.Preconditions;

@RunWith(JUnit4.class)
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newFunction(null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newText("function foo() {}");
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newAssign(Node.newName("x"), Node.newNumber(42)));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, Node.newName("foo"));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newName("foo"), null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newText("function foo() {}");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript));

    Node nodeFunctionInsideFunction = Node.newFunction(Node.newName("outer"), null, Node.newFunction(Node.newName("foo"), null, Node.newName("bar")));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction.getFirstChild()));

    Node nodeFunctionNotInsideFunction = Node.newFunction(Node.newName("outer"), null, Node.newFunction(Node.newName("foo"), null, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionNotInsideFunction.getFirstChild()));

    Node nodeFunctionInsideScript = Node.newText("function outer() {" + "function foo() {}" + "}");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(Node.newFunction("foo", null, null).getFirstChild()));
  }
}