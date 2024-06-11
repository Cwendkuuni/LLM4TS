package org.apache.commons.cli; // Replace with the correct package name for MaybeReachingVariableUse

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleUse() {
    Scope jsScope = new Scope();
    Node defNode = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.process(defNode);
    analysis.computeMayReach(defNode, useNode);

    Collection<Node> uses = analysis.getUses("x", defNode);
    assertNotNull(uses);
    assertTrue(uses.contains(useNode));
  }

  @Test
  public void testNoUse() {
    Scope jsScope = new Scope();
    Node defNode = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.process(defNode);
    analysis.computeMayReach(defNode, useNode);

    Collection<Node> uses = analysis.getUses("x", defNode);
    assertNull(uses);
  }

  @Test
  public void testConditionalUse() {
    Scope jsScope = new Scope();
    Node defNode = Node.newIfStatement(
        Node.newName("x"), Node.newName("y"), Node.newScopeNode(Token.SCOPE, new Node[0]));
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.process(defNode);
    analysis.computeMayReach(defNode, useNode);

    Collection<Node> uses = analysis.getUses("x", defNode);
    assertNotNull(uses);
    assertTrue(uses.contains(defNode));
  }

  @Test
  public void testConditionalNoUse() {
    Scope jsScope = new Scope();
    Node defNode = Node.newIfStatement(
        Node.newName("x"), Node.newName("y"), Node.newScopeNode(Token.SCOPE, new Node[0]));
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.process(defNode);
    analysis.computeMayReach(defNode, useNode);

    Collection<Node> uses = analysis.getUses("x", defNode);
    assertNull(uses);
  }

  @Test
  public void testMultipleDefinitions() {
    Scope jsScope = new Scope();
    Node defNode1 = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newName("y"));
    Node defNode2 = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node defNode1Parent = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node defNode1Node = Node.newBlock(new Node[]{defNode1, defNode1Parent});
    analysis.process(defNode1Node);

    Node defNode2Parent = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node defNode2Node = Node.newBlock(new Node[]{defNode2, defNode2Parent});
    analysis.process(defNode2Node);

    analysis.computeMayReach(defNode1Node, useNode);
    analysis.computeMayReach(defNode2Node, useNode);

    Collection<Node> uses = analysis.getUses("x", defNode1Node);
    assertNotNull(uses);
    assertTrue(uses.contains(defNode1Node));
  }

  @Test
  public void testMultipleDefinitionsWithUse() {
    Scope jsScope = new Scope();
    Node defNode1 = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newName("y"));
    Node defNode2 = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node defNode1Parent = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node defNode1Node = Node.newBlock(new Node[]{defNode1, defNode1Parent});
    analysis.process(defNode1Node);

    Node defNode2Parent = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node defNode2Node = Node.newBlock(new Node[]{defNode2, defNode2Parent});
    analysis.process(defNode2Node);

    analysis.computeMayReach(defNode1Node, useNode);
    analysis.computeMayReach(defNode2Node, useNode);

    Collection<Node> uses = analysis.getUsages(useNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(defNode1Node.hashCode(), uses.iterator().next().hashCode());
  }

  @Test
  public void testEscapedVariable() {
    Scope jsScope = new Scope();
    Node defNode = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newName("eval"));
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.process(defNode);
    analysis.computeMayReach(defNode, useNode);

    Collection<Node> uses = analysis.getUses("x", defNode);
    assertNull(uses);
  }

  @Test
  public void testNegatedConditionalUse() {
    Scope jsScope = new Scope();
    Node defNode = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newName("y"));
    Node useNode = Node.newName("!"); + Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node defNodeParent = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node defNodeNode = Node.newBlock(new Node[]{defNode, defNodeParent});
    analysis.process(defNodeNode);

    analysis.computeMayReach(defNodeNode, useNode);

    Collection<Node> uses = analysis.getUsages(useNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(defNodeNode.hashCode(), uses.iterator().next().hashCode());
  }

  @Test
  public void testLoopUse() {
    Scope jsScope = new Scope();
    Node defNode = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newName("y"));
    Node loopStart = Node.newForStatement(
        "i", Node.newName("x"), Node.newName("i", Token.ADD, Node.newNumber(1)), Node.newName("y"));
    Node useNode = Node.newName("x");
    Node cfgRoot = Node.newBlock(new Node[0]);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(cfgRoot);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node defNodeParent = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node defNodeNode = Node.newBlock(new Node[]{defNode, defNodeParent});
    analysis.process(defNodeNode);

    Node loopStartParent = Node.newScopeNode(Token.SCOPE, new Node[0]);
    Node loopStartNode = Node.newBlock(new Node[]{loopStart, defNodeNode, loopStartParent});
    analysis.process(loopStartNode);

    analysis.computeMayReach(defNodeNode, useNode);
    analysis.computeMayReach(loopStartNode, useNode);

    Collection<Node> uses = analysis.getUsages(useNode);
    assertNotNull(uses);
    assertEquals(2, uses.size());
    assertTrue(uses.contains(defNodeNode));
    assertTrue(uses.contains(loopStartNode));
  }
}