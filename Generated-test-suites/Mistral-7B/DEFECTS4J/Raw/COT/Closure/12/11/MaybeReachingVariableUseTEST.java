package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private static final String TEST_CODE = "function foo() {" +
      "  var x = 1;" +
      "  if (true) {" +
      "    x = 2;" +
      "  }" +
      "  return x;" +
      "}";

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private MaybeReachingVariableUse analysis;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsScope = compiler.getTopLevelScope();
    cfg = new ControlFlowGraph<>(Token.EMPTY_NODE, null);
    analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testBasicFunctionality() {
    analysis.analyze();
    Node defNode = Token.makeNode(Token.FUNCTION, 0, "foo", null, null);
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(Sets.newHashSet(Token.makeNode(Token.ASSIGN, 1, null, "x",
        Token.makeNode(Token.NUM, 0, null, 2))), Sets.newHashSet(uses.iterator().next()));
  }

  @Test
  public void testEmptyFunction() {
    String emptyFunctionCode = "function foo() {}";
    AnalysisManager analysisManager = new AnalysisManager();
    analysisManager.setSourceFile("TestEmptyFunction.js", emptyFunctionCode);
    Scope scope = analysisManager.getOrCreateTopLevelScope();
    ControlFlowGraph<Node> cfg = analysisManager.getOrCreateControlFlowGraph();
    MaybeReachingVariableUse analysis =
        new MaybeReachingVariableUse(cfg, scope, analysisManager.getCompiler());
    analysis.analyze();
  }

  @Test(expected = AssertionError.class)
  public void testNullInput() {
    analysis.flowThrough(null, null);
  }

  @Test
  public void testNullOutput() {
    ReachingUses output = null;
    analysis.flowThrough(Token.makeNode(Token.NAME, 0, null, "x", null), output);
    fail("Should throw NullPointerException");
  }

  @Test
  public void testNullCondition() {
    Node conditionNode = null;
    analysis.computeMayUse(conditionNode, null, null, false);
  }

  @Test
  public void testBlockNode() {
    Node blockNode = Token.makeNode(Token.BLOCK, 0, null, null, null);
    ReachingUses output = analysis.createEntryLattice();
    analysis.flowThrough(blockNode, output);
    assertEquals(output, analysis.createEntryLattice());
  }

  @Test
  public void testFunctionNode() {
    Node functionNode = Token.makeNode(Token.FUNCTION, 0, "foo", null, null);
    ReachingUses output = analysis.createEntryLattice();
    analysis.flowThrough(functionNode, output);
    assertEquals(output, analysis.createEntryLattice());
  }

  @Test
  public void testNameNodeWithLocalVariable() {
    Node nameNode = Token.makeNode(Token.NAME, 1, null, "x", null);
    Node defNode = Token.makeNode(Token.ASSIGN, 2, null, "x", Token.makeNode(Token.NUM, 0, null, 1));
    ReachingUses input = analysis.createEntryLattice();
    input.mayUseMap.put(jsScope.getVar("x"), defNode);

    ReachingUses output = analysis.flowThrough(nameNode, input);
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(Sets.newHashSet(nameNode), Sets.newHashSet(uses.iterator().next()));
  }

  @Test
  public void testNameNodeWithEscapedVariable() {
    Node nameNode = Token.makeNode(Token.NAME, 1, null, "Math", null);
    Node defNode = Token.makeNode(Token.ASSIGN, 2, null, "x", Token.makeNode(Token.NUM, 0, null, 1));
    ReachingUses input = analysis.createEntryLattice();
    input.mayUseMap.put(jsScope.getVar("x"), defNode);

    analysis.escaped.add(jsScope.getVar("Math"));
    ReachingUses output = analysis.flowThrough(nameNode, input);
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testNameNodeWithGlobalVariable() {
    Node nameNode = Token.makeNode(Token.NAME, 1, null, "Math", null);
    Node defNode = Token.makeNode(Token.ASSIGN, 2, null, "Math", null);
    ReachingUses input = analysis.createEntryLattice();
    ReachingUses output = analysis.flowThrough(nameNode, input);
    Collection<Node> uses = analysis.getUses("Math", defNode);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testWhileNode() {
    Node whileNode = Token.makeNode(Token.WHILE, 0, null, null, null);
    Node conditionNode = Token.makeNode(Token.NAME, 1, null, "x", null);
    Node assignmentNode = Token.makeNode(Token.ASSIGN, 2, null, "x",
        Token.makeNode(Token.NUM, 0, null, 1));
    Node bodyNode = Token.makeNode(Token.BLOCK, 3, null, null, null);

    ReachingUses input = analysis.createEntryLattice();
    input.mayUseMap.put(jsScope.getVar("x"), assignmentNode);

    analysis.flowThrough(conditionNode, input);
    analysis.computeMayUse(conditionNode, null, null, false);
    analysis.flowThrough(bodyNode, input);
    analysis.flowThrough(assignmentNode, input);

    Collection<Node> uses = analysis.getUses("x", assignmentNode);
    assertEquals(Sets.newHashSet(conditionNode, bodyNode, assignmentNode), Sets.newHashSet(uses.iterator().next()));
  }

  @Test
  public void testIfNode() {
    Node ifNode = Token.makeNode(Token.IF, 0, null, null, null);
    Node conditionNode = Token.makeNode(Token.NAME, 1, null, "x", null);
    Node trueBranchNode = Token.makeNode(Token.BLOCK, 2, null, null, null);
    Node falseBranchNode = Token.makeNode(Token.BLOCK, 3, null, null, null);
    Node assignmentNode1 = Token.makeNode(Token.ASSIGN, 4, null, "x",
        Token.makeNode(Token.NUM, 0, null, 1));
    Node assignmentNode2 = Token.makeNode(Token.ASSIGN, 5, null, "x",
        Token.makeNode(Token.NUM, 0, null, 2));

    ReachingUses input = analysis.createEntryLattice();
    input.mayUseMap.put(jsScope.getVar("x"), assignmentNode1);

    analysis.flowThrough(conditionNode, input);
    analysis.computeMayUse(conditionNode, null, null, false);
    analysis.flowThrough(trueBranchNode, input);
    analysis.flowThrough(assignmentNode1, input);

    analysis.flowThrough(conditionNode, input);
    analysis.computeMayUse(conditionNode, null, null, false);
    analysis.flowThrough(falseBranchNode, input);
    analysis.flowThrough(assignmentNode2, input);

    Collection<Node> usesTrueBranch = analysis.getUses("x", assignmentNode1);
    Collection<Node> usesFalseBranch = analysis.getUses("x", assignmentNode2);

    assertEquals(Sets.newHashSet(conditionNode, trueBranchNode, assignmentNode1), Sets.newHashSet(usesTrueBranch.iterator().next()));
    assertEquals(Sets.newHashSet(conditionNode, falseBranchNode, assignmentNode2), Sets.newHashSet(usesFalseBranch.iterator().next()));
  }

  @Test
  public void testForNode() {
    Node forNode = Token.makeNode(Token.FOR, 0, null, null, null);
    Node conditionNode = Token.makeNode(Token.NAME, 1, null, "i", null);
    Node initializationNode = Token.makeNode(Token.VAR, 2, null, "i", null);
    Node incrementNode = Token.makeNode(Token.ASSIGN, 3, null, "i",
        Token.makeNode(Token.ADD, 4, null, "i", Token.makeNode(Token.NUM, 0, null, 1)));
    Node assignmentNode = Token.makeNode(Token.ASSIGN, 5, null, "x",
        Token.makeNode(Token.NAME, 6, null, "i", null));

    Node arrayNode = Token.makeNode(Token.NAME, 7, null, "arr", null);
    Node lengthNode = Token.makeNode(Token.CALL, 8, null, "arr.length", null);

    ReachingUses input = analysis.createEntryLattice();

    analysis.flowThrough(conditionNode, input);
    analysis.computeMayUse(conditionNode, null, null, false);
    analysis.flowThrough(initializationNode, input);
    analysis.computeMayUse(initializationNode, null, null, false);
    analysis.flowThrough(incrementNode, input);
    analysis.computeMayUse(incrementNode, null, null, false);
    analysis.flowThrough(assignmentNode, input);
    analysis.computeMayUse(assignmentNode, null, null, false);

    analysis.flowThrough(arrayNode, input);
    analysis.computeMayUse(lengthNode, arrayNode, input, false);
    analysis.flowThrough(conditionNode, input);
    analysis.computeMayUse(conditionNode, null, null, false);

    Collection<Node> usesForNode = analysis.getUses("i", initializationNode);
    Collection<Node> usesArrayLength = analysis.getUses("i", lengthNode);

    assertEquals(Sets.newHashSet(arrayNode, lengthNode, initializationNode), Sets.newHashSet(usesForNode.iterator().next()));
    assertEquals(Sets.newHashSet(arrayNode, conditionNode), Sets.newHashSet(usesArrayLength.iterator().next()));
  }

  @Test
  public void testAndNode() {
    Node andNode = Token.makeNode(Token.AND, 0, null, null, null);
    Node leftChildNode = Token.makeNode(Token.NAME, 1, null, "x", null);
    Node rightChildNode = Token.makeNode(Token.NAME, 2, null, "y", null);

    ReachingUses leftInput = analysis.createEntryLattice();
    leftInput.mayUseMap.put(jsScope.getVar("x"), Token.makeNode(Token.NUM, 3, null, 1, null));

    ReachingUses rightInput = analysis.createEntryLattice();
    rightInput.mayUseMap.put(jsScope.getVar("y"), Token.makeNode(Token.NUM, 4, null, 2, null));

    ReachingUses input = analysis.createEntryLattice();
    input.mayUseMap.putAll(leftInput.mayUseMap);
    input.mayUseMap.putAll(rightInput.mayUseMap);

    analysis.flowThrough(leftChildNode, input);
    analysis.computeMayUse(leftChildNode, null, null, false);
    analysis.flowThrough(rightChildNode, input);
    analysis.computeMayUse(rightChildNode, null, null, false);
    analysis.flowThrough(andNode, input);

    Collection<Node> usesLeftChild = analysis.getUses("x", leftChildNode);
    Collection<Node> usesRightChild = analysis.getUses("y", rightChildNode);

    assertEquals(Sets.newHashSet(leftChildNode, andNode, rightChildNode), Sets.newHashSet(usesLeftChild.iterator().next()));
    assertEquals(Sets.newHashSet(rightChildNode, andNode, leftChildNode), Sets.newHashSet(usesRightChild.iterator().next()));
  }

  @Test
  public void testOrNode() {
    Node orNode = Token.makeNode(Token.OR, 0, null, null, null);
    Node leftChildNode = Token.makeNode(Token.NAME, 1, null, "x", null);
    Node rightChildNode = Token.makeNode(Token.NAME, 2, null, "y", null);

    ReachingUses leftInput = analysis.createEntryLattice();
    leftInput.mayUseMap.put(jsScope.getVar("x"), Token.makeNode(Token.NUM, 3, null, 1, null));

    ReachingUses rightInput = analysis.createEntryLattice();
    rightInput.mayUseMap.put(jsScope.getVar("y"), Token.makeNode(Token.NUM, 4, null, 2, null));

    ReachingUses input = analysis.createEntryLattice();
    input.mayUseMap.putAll(leftInput.mayUseMap);
    input.mayUseMap.putAll(rightInput.mayUseMap);

    analysis.flowThrough(leftChildNode, input);
    analysis.computeMayUse(leftChildNode, null, null, false);
    analysis.flowThrough(rightChildNode, input);
    analysis.computeMayUse(rightChildNode, null, null, false);
    analysis.flowThrough(orNode, input);

    Collection<Node> usesLeftChild = analysis.getUsages("x");
    Collection<Node> usesRightChild = analysis.getUsages("y");

    assertEquals(Sets.newHashSet(leftChildNode, orNode, rightChildNode), Sets.newHashSet(usesLeftChild.iterator().next()));
    assertEquals(Sets.newHashSet(rightChildNode, orNode, leftChildNode), Sets.newHashSet(usesRightChild.iterator().next()));
  }

  @Test
  public void testHookNode() {
    Node hookNode = Token.makeNode(Token.HOOK, 0, null, null, null);
    Node conditionNode1 = Token.makeNode(Token.NAME, 1, null, "x", null);
    Node conditionNode2 = Token.makeNode(Token.NAME, 2, null, "y", null);
    Node conditionNode3 = Token.makeNode(Token.NAME, 3, null, "z", null);

    ReachingUses input = analysis.createEntryLattice();

    analysis.flowThrough(conditionNode1, input);
    analysis.computeMayUse(conditionNode1, null, null, false);
    analysis.flowThrough(hookNode, input);
    analysis.flowThrough(conditionNode2, input);
    analysis.computeMayUse(conditionNode2, null, null, false);
    analysis.flowThrough(hookNode, input);
    analysis.flowThrough(conditionNode3, input);
    analysis.computeMayUse(conditionNode3, null, null, false);
    analysis.flowThrough(hookNode, input);

    Collection<Node> usesConditionNode1 = analysis.getUsages("x");
    Collection<Node> usesConditionNode2 = analysis.getUsages("y");
    Collection<Node> usesConditionNode3 = analysis.getUsages("z");

    assertEquals(Sets.newHashSet(conditionNode1, hookNode, conditionNode2), Sets.newHashSet(usesConditionNode1.iterator().next()));
    assertEquals(Sets.newHashSet(conditionNode2, hookNode, conditionNode3), Sets.newHashSet(usesConditionNode2.iterator().next()));
    assertEquals(Sets.newHashSet(conditionNode3, hookNode, conditionNode1), Sets.newHashSet(usesConditionNode3.iterator().next()));
  }
}