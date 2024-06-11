package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.parser.bare.Parser;
import com.google.javascript.jscomp.test.CompilerTestCase;
import com.google.javascript.rhino.NodeFactory;

import org.apache.commons.cli.FunctionRewriter;
import org.apache.commons.cli.FunctionRewriter.Reducer;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {

  private static String getTestCode(String name) {
    return "class TestFunctionRewriter " +
        "{" +
        "  @@functionRewriter;" +
        "  " + name +
        "}" +
        "TestFunctionRewriter.";
  }

  @Test
  public void testEmptyFunction() {
    String code = getTestCode("prototype.testEmptyFunction = function() {}");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    Node expected = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_emptyFn"), null);
    Node actual = traversal.getFunction("testFunctionRewriter.prototype.testEmptyFunction");
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testIdentityFunction() {
    String code = getTestCode("prototype.testIdentityFunction = function(x) { return x; }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    Node expected = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_identityFn"), NodeFactory.makeString("x"));
    Node actual = traversal.getFunction("testFunctionRewriter.prototype.testIdentityFunction");
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testGetterWithoutThis() {
    String code = getTestCode("prototype.testGetterWithoutThis = function() { return 1; }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    assertNull(traversal.getFunction("testFunctionRewriter.prototype.testGetterWithoutThis"));
  }

  @Test
  public void testGetterWithThis() {
    String code = getTestCode("prototype.testGetterWithThis = function() { return this.a; }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    Node expected = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_get"), NodeFactory.makeString("a_"));
    Node actual = traversal.getFunction("testFunctionRewriter.prototype.testGetterWithThis");
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testSetterWithoutThis() {
    String code = getTestCode("prototype.testSetterWithoutThis = function(x) { this.a = x; }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    assertNull(traversal.getFunction("testFunctionRewriter.prototype.testSetterWithoutThis"));
  }

  @Test
  public void testSetterWithThis() {
    String code = getTestCode("prototype.testSetterWithThis = function(x) { this.a = x; }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    Node expected = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_set"), NodeFactory.makeString("a_"));
    Node actual = traversal.getFunction("testFunctionRewriter.prototype.testSetterWithThis");
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testMultipleFunctions() {
    String code = getTestCode(
        "prototype.testFunction1 = function() { return 1; }" +
        "prototype.testFunction2 = function() { return 2; }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    Node expected1 = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_returnArg"), NodeFactory.makeInt(1));
    Node expected2 = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_returnArg"), NodeFactory.makeInt(2));
    Node actual1 = traversal.getFunction("testFunctionRewriter.prototype.testFunction1");
    Node actual2 = traversal.getFunction("testFunctionRewriter.prototype.testFunction2");

    assertEquals(expected1.toString(), actual1.toString());
    assertEquals(expected2.toString(), actual2.toString());
  }

  @Test
  public void testReducerOrder() {
    String code = getTestCode(
        "prototype.testReducerOrder1 = function() { return this.a; }" +
        "prototype.testReducerOrder2 = function() { return 1; }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    Node expected1 = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_get"), NodeFactory.makeString("a_"));
    Node actual1 = traversal.getFunction("testFunctionRewriter.prototype.testReducerOrder1");
    assertEquals(expected1.toString(), actual1.toString());

    Node expected2 = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_returnArg"), NodeFactory.makeInt(1));
    Node actual2 = traversal.getFunction("testFunctionRewriter.prototype.testReducerOrder2");
    assertEquals(expected2.toString(), actual2.toString());
  }

  @Test
  public void testReducerNesting() {
    String code = getTestCode(
        "prototype.testReducerNesting1 = function() { return this.a; }" +
        "prototype.testReducerNesting2 = function() { return this.b; }" +
        "prototype.testReducerNesting3 = function() { return this.testReducerNesting1(); }");
    FunctionRewriter funcRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new TestVisitor());
    funcRewriter.process(null, root);

    Node expected1 = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_get"), NodeFactory.makeString("a_"));
    Node actual1 = traversal.getFunction("testFunctionRewriter.prototype.testReducerNesting1");
    assertEquals(expected1.toString(), actual1.toString());

    Node expected2 = NodeFactory.makeCall(
        NodeFactory.makeString("JSCompiler_get"), NodeFactory.makeString("a_"));
    Node expected3 = NodeFactory.makeCall(expected1, NodeFactory.makeNull());
    Node actual2 = traversal.getFunction("testFunctionRewriter.prototype.testReducerNesting2");
    Node actual3 = traversal.getFunction("testFunctionRewriter.prototype.testReducerNesting3");

    assertEquals(expected2.toString(), actual2.toString());
    assertEquals(expected3.toString(), actual3.toString());
  }

  private static class TestVisitor implements NodeTraversal.Callback {
    private final FunctionRewriter.Reducer[] reducers = new FunctionRewriter.Reducer[] {
        new FunctionRewriter.EmptyFunctionReducer(),
        new FunctionRewriter.IdentityReducer(),
        new FunctionRewriter.GetterReducer(),
        new FunctionRewriter.SetterReducer()
    };

    @Override
    public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
      for (FunctionRewriter.Reducer reducer : reducers) {
        Node replacement = reducer.reduce(node);
        if (replacement != node) {
          traversal.setFunction(node, replacement);
          return false;
        }
      }
      return true;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
    }
  }
}