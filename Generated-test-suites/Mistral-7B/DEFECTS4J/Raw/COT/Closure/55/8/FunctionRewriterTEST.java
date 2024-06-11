package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {

  private FunctionRewriter functionRewriter;
  private Node testRoot;

  @Before
  public void setUp() {
    functionRewriter = new FunctionRewriter(null);
    testRoot = functionRewriter.parseSyntheticCode(
        FunctionRewriterTest.class.toString() + ":test",
        "function test() {}");
  }

  @Test
  public void testEmptyFunctionReducer() {
    Node emptyFunctionNode = NodeUtil.newFunctionExpression(
        "testEmpty", Token.ANY, ImmutableList.of(), null, null);
    Node originalEmptyFunction = testRoot.addChild(emptyFunctionNode);

    functionRewriter.process(null, testRoot);

    Node reducedEmptyFunction = NodeUtil.findNodeInTree(testRoot,
        "testEmpty:emptyFn");
    assertEquals(emptyFunctionNode, reducedEmptyFunction);
    assertNull(originalEmptyFunction);
  }

  @Test
  public void testIdentityReducer() {
    Node identityFunctionNode = NodeUtil.newFunctionExpression(
        "testIdentity", Token.ANY, ImmutableList.of(), null, null);
    identityFunctionNode.setLastChild(
        NodeUtil.newReturnStatement(NodeUtil.newName("arg0")));

    Node originalIdentityFunction = testRoot.addChild(identityFunctionNode);

    functionRewriter.process(null, testRoot);

    Node reducedIdentityFunction = NodeUtil.findNodeInTree(testRoot,
        "testIdentity:identityFn");
    assertEquals(identityFunctionNode, reducedIdentityFunction);
    assertNull(originalIdentityFunction);
  }

  @Test
  public void testGetterReducer() {
    Node getterFunctionNode = NodeUtil.newFunctionExpression(
        "testGetter", Token.ANY, ImmutableList.of(), null, null);
    getterFunctionNode.setLastChild(
        NodeUtil.newReturnStatement(
            NodeUtil.newGetProperty(NodeUtil.newThis(),
                NodeUtil.newName("propertyName"))));

    Node originalGetterFunction = testRoot.addChild(getterFunctionNode);

    functionRewriter.process(null, testRoot);

    Node reducedGetterFunction = NodeUtil.findNodeInTree(testRoot,
        "testGetter:getter");
    assertEquals(getterFunctionNode, reducedGetterFunction);
    assertNull(originalGetterFunction);
  }

  @Test
  public void testSetterReducer() {
    Node setterFunctionNode = NodeUtil.newFunctionExpression(
        "testSetter", Token.ANY, ImmutableList.of("value"), null, null);
    setterFunctionNode.setLastChild(
        NodeUtil.newStatement(
            NodeUtil.newAssignment(
                NodeUtil.newGetProperty(NodeUtil.newThis(),
                    NodeUtil.newName("propertyName")),
                NodeUtil.newName("value"))));

    Node originalSetterFunction = testRoot.addChild(setterFunctionNode);

    functionRewriter.process(null, testRoot);

    Node reducedSetterFunction = NodeUtil.findNodeInTree(testRoot,
        "testSetter:setter");
    assertEquals(setterFunctionNode, reducedSetterFunction);
    assertNull(originalSetterFunction);
  }

  @Test
  public void testProcessEmpty() {
    functionRewriter.process(null, testRoot);
    assertEquals(testRoot, NodeUtil.getRoot(testRoot));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidFunction() {
    Node invalidFunctionNode = NodeUtil.newFunctionExpression(
        "testInvalid", Token.ANY, ImmutableList.of(), null, null);
    invalidFunctionNode.setLastChild(
        NodeUtil.newStatement(NodeUtil.newInvalidToken(Token.TYPE_ERROR)));

    testRoot.addChild(invalidFunctionNode);
    functionRewriter.process(null, testRoot);
  }
}