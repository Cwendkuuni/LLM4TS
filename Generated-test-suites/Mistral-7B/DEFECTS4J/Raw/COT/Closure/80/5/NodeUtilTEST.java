import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode("for (var i in array) { }");
    assertTrue(NodeUtil.isForIn(node));

    node = Node.newNode("for (var i = 0; i < array.length; i++) { }");
    assertFalse(NodeUtil.isForIn(node));

    node = Node.newNode("if (true) { for (var i in array) { } }");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.newNode("true");
    assertFalse(NodeUtil.isStatement(node));

    node = Node.newNode("{}");
    assertFalse(NodeUtil.isStatement(node));

    node = Node.newNode("function func() {}");
    assertFalse(NodeUtil.isStatement(node));

    node = Node.newNode("var x = 5;");
    assertTrue(NodeUtil.isStatement(node));

    node = Node.newNode("if (true) { var x = 5; }");
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementNullParent() {
    Node node = Node.newNode("{}");
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = Node.newNode("function func() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = Node.newNode("var func = function() {}");
    assertFalse(NodeUtil.isFunctionDeclaration(node));

    node = Node.newNode("function func() { return 5; }");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = Node.newNode("if (true) { function func() {} }");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationNullParent() {
    Node node = Node.newNode("function func() {}");
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isFunctionDeclaration(node);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = Node.newNode("function func() {}");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

    node = Node.newNode("function func() {}");
    node.setParent(Node.newNode("{}"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

    node = Node.newNode("function func() {}");
    node.setParent(Node.newNode("function foo() {}", Token.FUNCTION));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    node = Node.newNode("function func() {}");
    node.setParent(Node.newNode("function foo() {}", Token.FUNCTION));
    node.getParent().setParent(Node.newNode("{}"));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node node = Node.newNode("function func() {}");
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isHoistedFunctionDeclaration(node);
  }
}