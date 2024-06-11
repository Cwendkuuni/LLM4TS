import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.BLOCK;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.SCRIPT;
import static com.google.javascript.rhino.TokenType.FOR;

import com.google.common.base.Preconditions;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (i in array) { }");
    assertTrue(NodeUtil.isForIn(node));

    node = new Node("for (; i < 10; i++) { }");
    assertFalse(NodeUtil.isForIn(node));

    node = new Node("function foo() { }");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("1 + 1");
    assertFalse(NodeUtil.isStatement(node));

    node = new Node("if (x > 5) { }");
    assertTrue(NodeUtil.isStatement(node));

    node = new Node("function foo() { }");
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function foo() { }");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node("function foo(x) { }");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node("1 + function foo() { }");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function foo() { }");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

    node = new Node("function foo() { }", new Node("function bar() { }", node));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

    node = new Node("function foo() { }", new Node("script", node));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    node = new Node("function foo() { }", new Node("function bar() { }", new Node("script", node)));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test(expected = AssertionError.class)
  public void testInvalidParentNodeType() {
    Node node = new Node("function foo() { }");
    Node parent = new Node("div");
    NodeUtil.isStatement(node);
    Preconditions.checkState(parent != null);
    NodeUtil.isStatement(node, parent);
  }
}