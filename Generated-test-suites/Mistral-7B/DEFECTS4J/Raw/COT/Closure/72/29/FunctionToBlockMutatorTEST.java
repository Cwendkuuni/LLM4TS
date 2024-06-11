package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Supplier;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    safeNameIdSupplier = () -> "test";
    functionToBlockMutator = new FunctionToBlockMutator(null, safeNameIdSupplier);
  }

  @Test
  public void testMutate_NoArguments() {
    Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunc", fnNode, Lists.newArrayList());
    Node resultNode = functionToBlockMutator.mutate("testFunc", fnNode, callNode,
        "result", false, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testMutate_SingleArgument() {
    Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, null);
    Node argNode = NodeUtil.newNumberNode(42);
    Node callNode = NodeUtil.newCallNode("testFunc", fnNode, Lists.newArrayList(argNode));
    Node resultNode = functionToBlockMutator.mutate("testFunc", fnNode, callNode,
        "result", false, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testMutate_MultipleArguments() {
    Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, null);
    Node arg1Node = NodeUtil.newNumberNode(42);
    Node arg2Node = NodeUtil.newStringNode("hello");
    Node callNode = NodeUtil.newCallNode("testFunc", fnNode, Lists.newArrayList(
        arg1Node, arg2Node));
    Node resultNode = functionToBlockMutator.mutate("testFunc", fnNode, callNode,
        "result", false, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testAliasAndInlineArguments_NoAliases() {
    Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, null);
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    Node resultNode = functionToBlockMutator.aliasAndInlineArguments(fnNode,
        argMap, null);
    assertNotNull(resultNode);
  }

  @Test
  public void testAliasAndInlineArguments_AliasesPresent() {
    Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, null);
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    Set<String> namesToAlias = Lists.newLinkedHashSet("a", "b");
    Node resultNode = functionToBlockMutator.aliasAndInlineArguments(fnNode,
        argMap, namesToAlias);
    assertNotNull(resultNode);
  }

  @Test(expected = AssertionError.class)
  public void testMutate_ArgumentNotInCallNode() {
    Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, null);
    Node argNode = NodeUtil.newNumberNode(42);
    Node callNode = NodeUtil.newCallNode("testFunc", fnNode, Lists.newArrayList());
    Node resultNode = functionToBlockMutator.mutate("testFunc", fnNode, callNode,
        "result", false, false);
    assertNull(resultNode);
  }

  @Test(expected = AssertionError.class)
  public void testMutate_InvalidFunctionNode() {
    Node fnNode = NodeUtil.newNumberNode(1);
    Node callNode = NodeUtil.newCallNode("testFunc", fnNode, Lists.newArrayList());
    Node resultNode = functionToBlockMutator.mutate("testFunc", fnNode, callNode,
        "result", false, false);
    assertNull(resultNode);
  }

  @Test(expected = AssertionError.class)
  public void testMutate_InvalidCallNode() {
    Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, null);
    Node callNode = Node.newUndefinedNode();
    Node resultNode = functionToBlockMutator.mutate("testFunc", fnNode, callNode,
        "result", false, false);
    assertNull(resultNode);
  }
}