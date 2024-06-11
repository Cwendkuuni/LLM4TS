package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  @Test
  public void testBasicFunctions() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    assertNull(mayUseMap);
  }

  @Test
  public void testAssignment() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newNumber(1));

    Node entryNode = Node.newBlock(null, null, null, null, null);
    cfg.addEdge(entryNode, functionNode, new DiGraphEdge());
    cfg.addEdge(functionNode, entryNode, new DiGraphEdge());

    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    Set<Node> uses = mayUseMap.values();

    assertNotNull(uses);
    assertTrue(uses.size() > 0);
    assertTrue(uses.contains(assignmentNode));
  }

  @Test
  public void testConditional() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    Node conditionalNode = Node.newIfNode(Node.newBoolean(true), Node.newBlock(null, null, null, null, null), null, null, null);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newNumber(1));

    Node entryNode = Node.newBlock(null, null, null, null, null);
    cfg.addEdge(entryNode, functionNode, new DiGraphEdge());
    cfg.addEdge(functionNode, entryNode, new DiGraphEdge());
    cfg.addEdge(functionNode, conditionalNode, new DiGraphEdge());
    cfg.addEdge(conditionalNode, Node.newBlock(null, null, null, null, null), new DiGraphEdge());
    cfg.addEdge(conditionalNode, Node.newBlock(null, null, null, null, null), new DiGraphEdge());

    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    Set<Node> uses = mayUseMap.values();

    assertNotNull(uses);
    assertTrue(uses.size() > 0);
    assertTrue(uses.contains(assignmentNode));
  }

  @Test
  public void testLoop() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    Node loopNode = Node.newForInNode(Node.newName("a"), Node.newName("b"), Node.newBlock(null, null, null, null, null));
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newNumber(1));

    Node entryNode = Node.newBlock(null, null, null, null, null);
    cfg.addEdge(entryNode, functionNode, new DiGraphEdge());
    cfg.addEdge(functionNode, entryNode, new DiGraphEdge());
    cfg.addEdge(functionNode, loopNode, new DiGraphEdge());
    cfg.addEdge(loopNode, Node.newBlock(null, null, null, null, null), new DiGraphEdge());
    cfg.addEdge(loopNode, loopNode, new DiGraphEdge());

    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    Set<Node> uses = mayUseMap.values();

    assertNotNull(uses);
    assertTrue(uses.size() > 0);
    assertTrue(uses.contains(assignmentNode));
  }

  @Test
  public void testFunctionWithEscapedVariable() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    Node escapeNode = Node.newVarIn("escape", Node.NEW, null, null, null);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newName("escape"));

    Node entryNode = Node.newBlock(null, null, null, null, null);
    cfg.addEdge(entryNode, functionNode, new DiGraphEdge());
    cfg.addEdge(functionNode, entryNode, new DiGraphEdge());
    cfg.addEdge(functionNode, escapeNode, new DiGraphEdge());
    cfg.addEdge(escapeNode, assignmentNode, new DiGraphEdge());

    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    assertNull(mayUseMap);
  }

  @Test
  public void testFunctionWithLocalVariable() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    Node localNode = Node.newVar("a", Node.VAR_DECL, Node.NORMAL, null, null, null);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newNumber(1));

    Node entryNode = Node.newBlock(null, null, null, null, null);
    cfg.addEdge(entryNode, functionNode, new DiGraphEdge());
    cfg.addEdge(functionNode, entryNode, new DiGraphEdge());
    cfg.addEdge(functionNode, localNode, new DiGraphEdge());
    cfg.addEdge(localNode, assignmentNode, new DiGraphEdge());

    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    Set<Node> uses = mayUseMap.values();

    assertNotNull(uses);
    assertTrue(uses.size() > 0);
    assertTrue(uses.contains(assignmentNode));
  }

  @Test
  public void testFunctionWithLocalAndEscapedVariable() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    Node escapeNode = Node.newVarIn("escape", Node.NEW, null, null, null);
    Node localNode = Node.newVar("a", Node.VAR_DECL, Node.NORMAL, null, null, null);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newName("escape"));

    Node entryNode = Node.newBlock(null, null, null, null, null);
    cfg.addEdge(entryNode, functionNode, new DiGraphEdge());
    cfg.addEdge(functionNode, entryNode, new DiGraphEdge());
    cfg.addEdge(functionNode, escapeNode, new DiGraphEdge());
    cfg.addEdge(escapeNode, localNode, new DiGraphEdge());
    cfg.addEdge(localNode, assignmentNode, new DiGraphEdge());

    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    Set<Node> uses = mayUseMap.values();
    assertNull(mayUseMap);
  }

  @Test
  public void testFunctionWithFunctionParameter() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Scope(jsScope));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler() {});

    Node functionNode = Node.newFunctionNode("myFunction", null, null, null, null, null);
    Node functionParameterNode = Node.newVarIn("param", Node.NORMAL, null, null, null);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newName("param"));

    Node entryNode = Node.newBlock(null, null, null, null, null);
    cfg.addEdge(entryNode, functionNode, new DiGraphEdge());
    cfg.addEdge(functionNode, entryNode, new DiGraphEdge());
    cfg.addEdge(functionNode, functionParameterNode, new DiGraphEdge());
    cfg.addEdge(functionParameterNode, assignmentNode, new DiGraphEdge());

    analysis.analyze(functionNode);

    Multimap<Var, Node> mayUseMap = analysis.getUses("a", functionNode);
    Set<Node> uses = mayUseMap.values();

    assertNotNull(uses);
    assertTrue(uses.size() > 0);
    assertTrue(uses.contains(assignmentNode));
  }
}