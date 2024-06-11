package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMakeLocalNamesUnique() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newInt(0)),
        NodeUtil.newReturn(NodeUtil.newVarNode("x"))));

    mutator.makeLocalNamesUnique(fnNode, false);

    int xCount = 0;
    for (Node c = fnNode.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getType() == Token.VAR) {
        if (NodeUtil.isVarName(c, "x")) {
          xCount++;
        }
      }
    }

    assertEquals(1, xCount);
  }

  @Test
  public void testReplaceReturns() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newInt(0)),
        NodeUtil.newReturn(NodeUtil.newInt(1))));

    String labelName = "testLabel";
    Node resultNode = mutator.replaceReturns(fnNode.getBody(), null, labelName, false);

    assertNotNull(resultNode);
    assertEquals(NodeUtil.newLabel(labelName), resultNode);
  }

  @Test
  public void testReplaceReturnsWithResult() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newInt(0)),
        NodeUtil.newReturn(NodeUtil.newInt(1))));

    String labelName = "testLabel";
    String resultName = "result";
    Node resultNode = mutator.replaceReturns(fnNode.getBody(), resultName, labelName, true);

    assertNotNull(resultNode);
    assertEquals(NodeUtil.newAssign(NodeUtil.newVarNode(resultName), NodeUtil.newInt(1)), resultNode);

    assertEquals(NodeUtil.newLabel(labelName), fnNode.getBody().getLastChild());
  }

  @Test
  public void testAliasAndInlineArguments() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newInt(0)),
        NodeUtil.newVarNode("y", NodeUtil.newInt(1)),
        NodeUtil.newReturn(NodeUtil.newExpr(NodeUtil.newBinaryOperator(
            NodeUtil.newName("add"), NodeUtil.newName("x"), NodeUtil.newName("y"))))));

    LinkedHashMap<String, Node> arguments = Maps.newLinkedHashMap();
    arguments.put("y", NodeUtil.newInt(2));

    Node argsNode = mutator.aliasAndInlineArguments(fnNode.getBody(), arguments, null);

    assertNotNull(argsNode);
    assertEquals(NodeUtil.newExpr(NodeUtil.newBinaryOperator(
        NodeUtil.newName("add"), NodeUtil.newVarNode("x"), NodeUtil.newInt(2))), argsNode);
  }

  @Test
  public void testAliasAndInlineArgumentsWithAliases() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newInt(0)),
        NodeUtil.newVarNode("y", NodeUtil.newInt(1)),
        NodeUtil.newReturn(NodeUtil.newExpr(NodeUtil.newBinaryOperator(
            NodeUtil.newName("add"), NodeUtil.newName("x"), NodeUtil.newName("y"))))));

    Set<String> namesToAlias = Sets.newHashSet("x");
    LinkedHashMap<String, Node> arguments = Maps.newLinkedHashMap();
    arguments.put("y", NodeUtil.newInt(2));

    Node argsNode = mutator.aliasAndInlineArguments(fnNode.getBody(), arguments, namesToAlias);

    assertNotNull(argsNode);
    assertEquals(NodeUtil.newExpr(NodeUtil.newBinaryOperator(
        NodeUtil.newName("add"), NodeUtil.newVarNode("a"), NodeUtil.newInt(2))), argsNode);

    Node aNode = fnNode.getBody();
    for (Node c = aNode.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getType() == Token.VAR && NodeUtil.isVarName(c, "a")) {
        assertNotNull(c.getFirstChild());
        break;
      }
    }
  }

  @Test
  public void testFixUnitializedVarDeclarations() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newUndefined()),
        NodeUtil.newReturn(NodeUtil.newInt(1))));

    mutator.fixUnitializedVarDeclarations(fnNode.getBody());

    Node xNode = fnNode.getBody();
    for (Node c = xNode.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getType() == Token.ASSIGN) {
        assertEquals(NodeUtil.newInt(0), c.getFirstChild());
        assertNotNull(c.getLastChild());
        break;
      }
    }
  }

  @Test
  public void testReplaceReturnWithBreak() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newInt(0)),
        NodeUtil.newReturn(NodeUtil.newInt(1)),
        NodeUtil.newLabel("label")), null);

    String labelName = "label";
    Node resultNode = mutator.replaceReturns(fnNode.getBody(), null, labelName, false);

    assertNull(resultNode);

    assertEquals(NodeUtil.newLabel(labelName), fnNode.getBody().getLastChild());
  }

  @Test
  public void testReplaceReturnWithBreakAndResult() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newInt(0)),
        NodeUtil.newReturn(NodeUtil.newInt(1)),
        NodeUtil.newLabel("label")), null);

    String labelName = "label";
    String resultName = "result";
    Node resultNode = mutator.replaceReturns(fnNode.getBody(), resultName, labelName, true);

    assertNotNull(resultNode);
    assertEquals(NodeUtil.newAssign(NodeUtil.newVarNode(resultName), NodeUtil.newInt(1)), resultNode);

    assertEquals(NodeUtil.newLabel(labelName), fnNode.getBody().getLastChild());
  }

  @Test
  public void testLabelNameSupplier() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    LabelNameSupplier labelNameSupplier = new LabelNameSupplier(null);

    String labelName = mutator.getLabelNameForFunction(null);
    assertEquals("JSCompiler_inline_label_", labelName.substring(0, 24));

    labelName = mutator.getLabelNameForFunction("test");
    assertEquals("JSCompiler_inline_label_test_", labelName.substring(0, 24));
  }
}