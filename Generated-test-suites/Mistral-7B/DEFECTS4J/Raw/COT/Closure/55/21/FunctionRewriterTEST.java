package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

/**
 * Test class for FunctionRewriter.
 */
@RunWith(JUnit4.class)
public class FunctionRewriterTest {
  // test helper
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
  }

  @Test
  public void testEmptyFunctionReducer() {
    // given
    Node root = createNode(
        "function testEmptyFunctionReducer() {" +
            "  function f() {}" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testEmptyFunctionReducer() {" +
            "  emptyFn();" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testIdentityReducer() {
    // given
    Node root = createNode(
        "function testIdentityReducer(arg) {" +
            "  function f(value) {return value} " +
            "  return f(arg);" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testIdentityReducer(arg) {" +
            "  identityFn(arg);" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testIdentityReducer_ThisIsArg() {
    // given
    Node root = createNode(
        "function testIdentityReducer() {" +
            "  function f(value) {return value} " +
            "  return f(this);" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testIdentityReducer() {" +
            "  identityFn(this);" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testIdentityReducer_NoArg() {
    // given
    Node root = createNode(
        "function testIdentityReducer() {" +
            "  function f() {return this} " +
            "  return f();" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testIdentityReducer() {" +
            "  identityFn();" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testIdentityReducer_NoReturnStatement() {
    // given
    Node root = createNode(
        "function testIdentityReducer() {" +
            "  function f() {return this} " +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // then
    assertNull("Expected no change for function without return statement.",
               findNodeByType(root, Token.FUNCTION));
  }

  @Test
  public void testReturnConstantReducer() {
    // given
    Node root = createNode(
        "function testReturnConstantReducer() {" +
            "  function f() {return 10}" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testReturnConstantReducer() {" +
            "  returnconst(10);" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testReturnConstantReducer_WithArg() {
    // given
    Node root = createNode(
        "function testReturnConstantReducer(arg) {" +
            "  function f() {return arg * 2}" +
            "  return f();" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testReturnConstantReducer(arg) {" +
            "  returnconst(" + arg.toString() + " * 2);" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testGetterReducer() {
    // given
    Node root = createNode(
        "function testGetterReducer() {" +
            "  function getX() {return this.x_}" +
            "  return getX();" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testGetterReducer() {" +
            "  getter(\"x_\");" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test(expected = AssertionError.class)
  public void testGetterReducer_NoGetProp() {
    // given
    Node root = createNode(
        "function testGetterReducer() {" +
            "  function getX() {return this.x}" +
            "  return getX();" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // then
    assertThrows(AssertionError.class, () -> {
      Node actual = findNodeByType(root, Token.FUNCTION);
      assertEquals("Expected getter method.",
                   "identityFn", actual.getLastChild().getString());
    });
  }

  @Test
  public void testSetterReducer() {
    // given
    Node root = createNode(
        "function testSetterReducer(value) {" +
            "  function setX(x) {this.x_ = x}" +
            "  setX(value);" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // when
    Node expected = createNode(
        "function testSetterReducer(value) {" +
            "  setter(\"x_\", value);" +
            "}", 1, 1);

    // then
    Node actual = findNodeByType(root, Token.FUNCTION);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testSetterReducer_NoSetProp() {
    // given
    Node root = createNode(
        "function testSetterReducer(value) {" +
            "  function setX(x) {this.x = x}" +
            "  setX(value);" +
            "}", 1, 1);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);

    // then
    assertNull("Expected no change for function without setter.",
               findNodeByType(root, Token.FUNCTION));
  }

  /**
   * Helper method to create a Node with the given code and line number.
   */
  private Node createNode(String code, int lineNumber, int charOffset) {
    NodeFactory nodeFactory = new NodeFactory();
    return nodeFactory.compile(code, null, null, null, null).getTree();
  }

  /**
   * Helper method to find a Node by its type in the given Node tree.
   */
  private Node findNodeByType(Node node, int type) {
    if (node.getType() == type) {
      return node;
    }

    for (Node child : node.getChildren()) {
      Node found = findNodeByType(child, type);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  /**
   * Helper method to throw an AssertionError with the given message.
   */
  private void assertThrows(Class<? extends Throwable> expectedExceptionType,
                            Runnable runner) {
    try {
      runner.run();
    } catch (Throwable e) {
      if (expectedExceptionType.isInstance(e)) {
        throw (AssertionError) e;
      }
    }
    throw new AssertionError("Expected " + expectedExceptionType.getName() +
                             " but was: " + e.getClass().getName());
  }
}