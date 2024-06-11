package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    // Initialize test data
    ImmutableSet<String> functionNames = ImmutableSet.of("testFunction");
    jsScope = new Scope(functionNames);
    compiler = new AbstractCompiler(jsScope, null);
    // Create a control flow graph for testing
    cfg = new ControlFlowGraph<Node>(functionNames.iterator().next(), null);
  }

  @Test
  public void testMaybeReachingVariableUse_SimpleTest() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node testFunction = Node.newFunctionNode(Token.FUNCTION, "testFunction", ImmutableSet.of());
    Node entryNode = Node.newBlockNode(Token.BLOCK, null);
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName(Token.NAME, "A"), Node.INT_LITERAL, 1);
    Node useA = Node.newName(Token.NAME, "A");
    Node printA = Node.newCall(Token.CALL, Node.newName(Token.NAME, "print"), ImmutableList.of(useA));

    // Add nodes to the control flow graph
    cfg.addNode(entryNode);
    cfg.addNode(testFunction);
    cfg.addEdge(new DiGraphEdge(entryNode, testFunction, null));
    cfg.addEdge(new DiGraphEdge(testFunction, useA, null));
    cfg.addEdge(new DiGraphEdge(testFunction, printA, null));
    cfg.addEdge(new DiGraphEdge(testFunction, assignA, null));
    cfg.addEdge(new DiGraphEdge(assignA, useA, null));

    // Run the analysis
    FlowState<MaybeReachingVariableUse.ReachingUses> state = maybeReachingVariableUse.compute(entryNode, null);

    // Check the results
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", assignA);
    assertTrue(uses.contains(useA));
  }

  @Test
  public void testMaybeReachingVariableUse_NoUses() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node testFunction = Node.newFunctionNode(Token.FUNCTION, "testFunction", ImmutableSet.of());
    Node entryNode = Node.newBlockNode(Token.BLOCK, null);
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName(Token.NAME, "A"), Node.INT_LITERAL, 1);
    Node useB = Node.newName(Token.NAME, "B");

    // Add nodes to the control flow graph
    cfg.addNode(entryNode);
    cfg.addNode(testFunction);
    cfg.addEdge(new DiGraphEdge(entryNode, testFunction, null));
    cfg.addEdge(new DiGraphEdge(testFunction, assignA, null));
    cfg.addEdge(new DiGraphEdge(testFunction, useB, null));

    // Run the analysis
    FlowState<MaybeReachingVariableUse.ReachingUses> state = maybeReachingVariableUse.compute(entryNode, null);

    // Check the results
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", assignA);
    assertNull(uses);
  }

  @Test
  public void testMaybeReachingVariableUse_Conditional() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node testFunction = Node.newFunctionNode(Token.FUNCTION, "testFunction", ImmutableSet.of());
    Node entryNode = Node.newBlockNode(Token.BLOCK, null);
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName(Token.NAME, "A"), Node.INT_LITERAL, 1);
    Node ifCond = Node.newIf(Token.IF, Node.newName(Token.NAME, "x"), null, null);
    Node useAIfTrue = Node.newName(Token.NAME, "A");
    Node useAIfFalse = Node.newName(Token.NAME, "A");
    Node assignAUseAIfTrue = Node.newAssign(Token.ASSIGN, Node.newName(Token.NAME, "A"), useAIfTrue);
    Node assignAUseAIfFalse = Node.newAssign(Token.ASSIGN, Node.newName(Token.NAME, "A"), useAIfFalse);
    Node printA = Node.newCall(Token.CALL, Node.newName(Token.NAME, "print"), ImmutableList.of(useA));

    // Add nodes to the control flow graph
    cfg.addNode(entryNode);
    cfg.addNode(testFunction);
    cfg.addEdge(new DiGraphEdge(entryNode, testFunction, null));
    cfg.addEdge(new DiGraphEdge(testFunction, ifCond, null));
    cfg.addEdge(new DiGraphEdge(testFunction, assignA, null));
    cfg.addEdge(new DiGraphEdge(ifCond, useAIfTrue, null));
    cfg.addEdge(new DiGraphEdge(ifCond, assignAUseAIfTrue, null));
    cfg.addEdge(new DiGraphEdge(ifCond, useAIfFalse, null));
    cfg.addEdge(new DiGraphEdge(ifCond, assignAUseAIfFalse, null));
    cfg.addEdge(new DiGraphEdge(testFunction, printA, null));

    // Run the analysis
    FlowState<MaybeReachingVariableUse.ReachingUses> state = maybeReachingVariableUse.compute(entryNode, null);

    // Check the results
    Collection<Node> usesIfTrue = maybeReachingVariableUse.getUses("A", assignAUseAIfTrue);
    assertTrue(usesIfTrue.contains(useAIfTrue));

    Collection<Node> usesIfFalse = maybeReachingVariableUse.getUses("A", assignAUseAIfFalse);
    assertTrue(usesIfFalse.contains(useAIfFalse));

    Collection<Node> uses = maybeReachingVariableUse.getUses("A", assignA);
    assertTrue(uses.containsAll(ImmutableList.of(useAIfTrue, useAIfFalse)));
  }

  @Test
  public void testMaybeReachingVariableUse_EscapedVariable() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node testFunction = Node.newFunctionNode(Token.FUNCTION, "testFunction", ImmutableSet.of());
    Node entryNode = Node.newBlockNode(Token.BLOCK, null);
    Node escVar = Node.newVar(Token.VAR, Token.IDENT, "escVar");
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName(Token.NAME, "A"), Node.INT_LITERAL, 1);
    Node useA = Node.newName(Token.NAME, "A");
    Node useEscVar = Node.newName(Token.NAME, "escVar");
    Node printA = Node.newCall(Token.CALL, Node.newName(Token.NAME, "print"), ImmutableList.of(useA));

    // Add nodes to the control flow graph
    cfg.addNode(entryNode);
    cfg.addNode(testFunction);
    cfg.addEdge(new DiGraphEdge(entryNode, testFunction, null));
    cfg.addEdge(new DiGraphEdge(testFunction, escVar, null));
    cfg.addEdge(new DiGraphEdge(testFunction, assignA, null));
    cfg.addEdge(new DiGraphEdge(testFunction, useA, null));
    cfg.addEdge(new DiGraphEdge(testFunction, useEscVar, null));
    cfg.addEdge(new DiGraphEdge(testFunction, printA, null));

    // Set escVar as an escaped variable
    Set<Var> escaped = Sets.newHashSet();
    Var escVarVar = jsScope.getVar("escVar");
    Preconditions.checkNotNull(escVarVar);
    escaped.add(escVarVar);

    // Run the analysis
    MaybeReachingVariableUse maybeReachingVariableUseWithEscaped = new MaybeReachingVariableUse(cfg, jsScope, compiler) {
      @Override
      Set<Var> escaped() {
        return escaped;
      }
    };
    FlowState<MaybeReachingVariableUse.ReachingUses> state = maybeReachingVariableUseWithEscaped.compute(entryNode, null);

    // Check the results
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", assignA);
    assertTrue(uses.contains(useA));

    Collection<Node> usesEscVar = maybeReachingVariableUse.getUses("escVar", escVar);
    assertFalse(usesEscVar.contains(useA));
  }
}