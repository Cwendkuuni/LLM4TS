package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMakeLocalNamesUnique() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node fnNode = NodeUtil.newFunctionNode(); // A dummy function node

    mutator.makeLocalNamesUnique(fnNode, false);

    NodeTraversal.traverse(null, fnNode, new NodeNameCounter());

    NodeTraversal.traverse(null, fnNode, new NodeNameChecker());
  }

  @Test
  public void testAliasAndInlineArguments() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnTemplateRoot = NodeUtil.newFunctionNode(); // A dummy function node
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newStringNode("arg1"));
    argMap.put("arg2", NodeUtil.newStringNode("arg2"));

    Set<String> namesToAlias = Set.of("arg1");

    Node newFnNode = mutator.aliasAndInlineArguments(fnTemplateRoot, argMap, namesToAlias);

    NodeTraversal.traverse(null, fnTemplateRoot, new NodeChecker());
    NodeTraversal.traverse(null, newFnNode, new NodeChecker());
  }

  @Test
  public void testReplaceReturns() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node block = NodeUtil.newBlockNode();
    String labelName = "label";
    String resultName = "result";

    Node result = mutator.replaceReturns(block, resultName, labelName, true);

    NodeTraversal.traverse(null, block, new NodeChecker());
    NodeTraversal.traverse(null, result, new NodeChecker());
  }

  @Test
  public void testReplaceReturnsWithNoResult() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node block = NodeUtil.newBlockNode();
    String labelName = "label";

    Node result = mutator.replaceReturns(block, null, labelName, false);

    NodeTraversal.traverse(null, block, new NodeChecker());
    NodeTraversal.traverse(null, result, new NodeChecker());
  }

  static class NodeChecker implements NodeVisitor<Void> {
    @Override
    public Void visit(Node node, Void arg) {
      if (node.getType() == Token.RETURN) {
        assertNull(node.getNext());
      }
      return null;
    }
  }

  static class NodeNameChecker implements NodeVisitor<Void> {
    private int counter = 0;

    @Override
    public Void visit(Node node, Void arg) {
      if (node.getType() == Token.NAME) {
        String name = node.getString().getValue();
        assertNotNull(name);
        assertEquals(Integer.toString(++counter), name);
      }
      return null;
    }
  }

  static class NodeNameCounter implements NodeVisitor<Void> {
    private int counter = 0;

    @Override
    public Void visit(Node node, Void arg) {
      if (node.getType() == Token.NAME) {
        String name = node.getString().getValue();
        assertEquals(Integer.toString(counter++), name);
      }
      return null;
    }
  }
}