package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.babel.Parser;
import com.google.javascript.rhino.NodeFactory;

public class FunctionRewriterTest {

  private static Node parseNode(String jsCode) {
    Parser parser = new Parser();
    Node root = parser.parse(jsCode, null, null);
    NodeFactory nodeFactory = parser.getContext().getFactory();
    root = nodeFactory.makeTreeConsistent(root);
    return root;
  }

  @Test
  public void testEmptyFunction() {
    String jsCode = "function() {}";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("emptyFn()");
    assertEquals(expected, node);
  }

  @Test
  public void testIdentityFunction() {
    String jsCode = "function(x) { return x }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function(x) { return x }; " +
        "JSCompiler_identityFn()");
    assertEquals(expected, node);
  }

  @Test
  public void testReturnConstantFunction() {
    String jsCode = "function() { return 1 }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("returnconst(1)");
    assertEquals(expected, node);
  }

  @Test
  public void testGetterFunction() {
    String jsCode = "function() { return this.x }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function() { return getter('x'); }");
    assertEquals(expected, node);
  }

  @Test
  public void testSetterFunction() {
    String jsCode = "function(x) { this.x = x }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function(x) { getter('x') = x }");
    assertEquals(expected, node);
  }

  @Test
  public void testUnreducedFunction() {
    String jsCode = "function(x, y) { return x + y }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function(x, y) { return x + y }");
    assertEquals(expected, node);
  }

  @Test
  public void testNestedFunction() {
    String jsCode = "function foo() { return function(x) { return x + 1 } }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { return JSCompiler_get('JSCompiler_get' + '_11'); }" +
        "function(x) { return x + 1 }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunction() {
    String jsCode = "function foo() { return foo() }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { return emptyFn(); }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithArguments() {
    String jsCode = "function foo(x, y) { return foo(x, y) }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo(x, y) { return emptyFn(); }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithVoidReturn() {
    String jsCode = "function foo() { }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithArgumentList() {
    String jsCode = "function foo(x, y) {}";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo(x, y) { }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithReturnStatement() {
    String jsCode = "function foo() { return 0 }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { return emptyFn(); }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithBlockStatement() {
    String jsCode = "function foo() { var x = 1 }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { emptyFn(); }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithExpressionStatement() {
    String jsCode = "function foo() { return 1 + 1 }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { return emptyFn() + emptyFn(); }");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithExpressionStatementAndReturnStatement() {
    String jsCode = "function foo() { return 1 + 1; }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { return emptyFn() + emptyFn(); }" +
        "return emptyFn();");
    assertEquals(expected, node);
  }

  @Test
  public void testRedundantFunctionWithExpressionStatementAndReturnStatementWithArguments() {
    String jsCode = "function foo(x) { return x + 1; }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo(x) { return emptyFn() + emptyFn(); " +
        "return emptyFn(); }");
    assertEquals(expected, node);
  }

  @Test
  public void testGetPropertyName() {
    Node node = parseNode("function foo(x) { this.x = x }");
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node getter = node.getFirstChild();
    Node propertyName = getSetPropertyName(getter);
    assertEquals("x", propertyName.getString());
  }

  @Test
  public void testSetPropertyName() {
    Node node = parseNode("function foo(x) { this.x = x }");
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node setter = node.getLastChild();
    Node propertyName = getSetPropertyName(setter);
    assertEquals("x", propertyName.getString());
  }

  @Test
  public void testGetPropertyNameWithInvalidNode() {
    Node node = parseNode("function foo() { return foo(); }");
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node getter = node.getFirstChild();
    assertNull(getSetPropertyName(getter));
  }

  @Test
  public void testSetPropertyNameWithInvalidNode() {
    Node node = parseNode("function foo() { return foo(); }");
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node setter = node.getLastChild();
    assertNull(getSetPropertyName(setter));
  }

  @Test
  public void testReduceNestedFunctions() {
    String jsCode = "function foo() { return function(x) { return x + 1 } }" +
        "function bar() { return foo()(2) }";
    Node node = parseNode(jsCode);
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, node);
    Node expected = parseNode("function foo() { return JSCompiler_get('JSCompiler_get' + '_11'); }" +
        "function(x) { return x + 1 }" +
        "function bar() { return JSCompiler_get('JSCompiler_get' + '_11')(2); }");
    assertEquals(expected, node);
  }

  private Node getSetPropertyName(Node node) {
    if (node.getType() != Token.CALL) {
      return null;
    }

    Node methodCall = node.getFirstChild();
    if (methodCall.getType() != Token.GETPROP ||
        methodCall.getFirstChild().getType() != Token.NAME ||
        !methodCall.getFirstChild().getString().equals("this")) {
      return null;
    }

    Node propertyName = methodCall.getLastChild();
    return propertyName;
  }
}