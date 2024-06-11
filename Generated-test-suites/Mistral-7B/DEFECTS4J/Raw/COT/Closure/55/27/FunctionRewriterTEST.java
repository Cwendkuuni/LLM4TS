package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

public class FunctionRewriterTest {

  private NodeFactory nodeFactory = new NodeFactory();
  private Node root;

  @org.junit.Before
  public void setUp() {
    root = nodeFactory.parse("function test() {}");
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(new FunctionRewriter.ReductionGatherer(
        ImmutableList.of(new FunctionRewriter.EmptyFunctionReducer()),
        null));
    traversal.traverse();

    int emptyFunctionCallCountBefore = countNodesWithName("JSCompiler_emptyFn", root);
    functionRewriter.process(null, root);
    int emptyFunctionCallCountAfter = countNodesWithName("JSCompiler_emptyFn", root);

    assertEquals("EmptyFunctionReducer should apply and reduce empty functions",
        1, emptyFunctionCallCountAfter - emptyFunctionCallCountBefore);
  }

  @org.junit.Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node identityFunctionNode = nodeFactory.parse("function identity(x) { return x; }");
    Node testNode = nodeFactory.parse("function test() {" +
            " identity(1) + identity(2) + identity(3);" +
            "}");

    NodeTraversal traversal = new NodeTraversal(testNode, null);
    traversal.addCallback(new FunctionRewriter.ReductionGatherer(
        ImmutableList.of(new FunctionRewriter.IdentityReducer()),
        null));
    traversal.traverse();

    int identityFunctionCallCountBefore = countNodesWithName(
        "JSCompiler_identityFn", testNode);
    functionRewriter.process(null, testNode);
    int identityFunctionCallCountAfter = countNodesWithName(
        "JSCompiler_identityFn", testNode);

    assertEquals("IdentityReducer should apply and reduce identity functions",
        3, identityFunctionCallCountAfter - identityFunctionCallCountBefore);
  }

  @org.junit.Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node getterFunctionNode = nodeFactory.parse("function getter(x) { return this.x; }");
    Node testNode = nodeFactory.parse("function test() {" +
            " test.x = 1;" +
            " test.getter();" +
            "}");

    NodeTraversal traversal = new NodeTraversal(testNode, null);
    traversal.addCallback(new FunctionRewriter.ReductionGatherer(
        ImmutableList.of(new FunctionRewriter.GetterReducer()),
        null));
    traversal.traverse();

    int getterFunctionCallCountBefore = countNodesWithName(
        "JSCompiler_get", testNode);
    functionRewriter.process(null, testNode);
    int getterFunctionCallCountAfter = countNodesWithName(
        "JSCompiler_get", testNode);

    assertEquals("GetterReducer should apply and reduce getter functions",
        1, getterFunctionCallCountAfter - getterFunctionCallCountBefore);
  }

  @org.junit.Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node setterFunctionNode = nodeFactory.parse("function setter(x, value) {" +
            " this.x = value;" +
            "}");
    Node testNode = nodeFactory.parse("function test() {" +
            " test.setter(1, 2);" +
            "}");

    NodeTraversal traversal = new NodeTraversal(testNode, null);
    traversal.addCallback(new FunctionRewriter.ReductionGatherer(
        ImmutableList.of(new FunctionRewriter.SetterReducer()),
        null));
    traversal.traverse();

    int setterFunctionCallCountBefore = countNodesWithName(
        "JSCompiler_set", testNode);
    functionRewriter.process(null, testNode);
    int setterFunctionCallCountAfter = countNodesWithName(
        "JSCompiler_set", testNode);

    assertEquals("SetterReducer should apply and reduce setter functions",
        1, setterFunctionCallCountAfter - setterFunctionCallCountBefore);
  }

  @org.junit.Test
  public void testNoReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node testNode = nodeFactory.parse("function test() {" +
            " function inner() { return 1; } }");

    NodeTraversal traversal = new NodeTraversal(testNode, null);
    traversal.addCallback(new FunctionRewriter.ReductionGatherer(
        ImmutableList.of(new FunctionRewriter.EmptyFunctionReducer(),
            new FunctionRewriter.IdentityReducer(),
            new FunctionRewriter.GetterReducer(),
            new FunctionRewriter.SetterReducer()),
        null));
    traversal.traverse();

    int functionCallCountBefore = countNodesWithName("JSCompiler_", testNode);
    functionRewriter.process(null, testNode);
    int functionCallCountAfter = countNodesWithName("JSCompiler_", testNode);

    assertEquals("No reduction should be applied", functionCallCountBefore,
        functionCallCountAfter);
  }

  private int countNodesWithName(String name, Node node) {
    int count = 0;
    for (Node child = node; child != null; child = child.getNext()) {
      if (NodeUtil.isName(child) && child.getString().startsWith(name)) {
        count++;
      }
    }
    return count;
  }
}