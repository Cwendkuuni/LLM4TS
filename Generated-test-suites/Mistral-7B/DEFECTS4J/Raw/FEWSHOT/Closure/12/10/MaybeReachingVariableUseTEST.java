package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {

  @Test
  public void testAddToUseIfLocal() {
    String name = "x";
    Node defNode = Node.newIntLiteral(1);
    Scope scope = new Scope(Token.NO_SOURCE_FILE, null);
    ControlFlowGraph cfg = new ControlFlowGraph(null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);
    analysis.addToUseIfLocal(name, defNode, analysis.createEntryLattice());
    Multimap<Var, Node> mayUseMap = analysis.createEntryLattice().mayUseMap;
    Var var = scope.getVar(name);
    Preconditions.checkState(var != null, "Var should exist");
    List<Node> uses = new ArrayList<>(mayUseMap.get(var));
    assertThat(uses).hasSize(1);
    assertThat(uses.get(0)).isSameInstanceAs(defNode);
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    String name = "x";
    Node defNode = Node.newIntLiteral(1);
    Scope scope = new Scope(Token.NO_SOURCE_FILE, null);
    ControlFlowGraph cfg = new ControlFlowGraph(null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);
    analysis.addToUseIfLocal(name, defNode, analysis.createEntryLattice());
    analysis.removeFromUseIfLocal(name, analysis.createEntryLattice());
    Multimap<Var, Node> mayUseMap = analysis.createEntryLattice().mayUseMap;
    Var var = scope.getVar(name);
    Preconditions.checkState(var != null, "Var should exist");
    List<Node> uses = new ArrayList<>(mayUseMap.get(var));
    assertNull(uses);
  }

  @Test
  public void testGetUses() {
    String name = "x";
    Node defNode = Node.newIntLiteral(1);
    Node useNode = Node.newIntLiteral(2);
    Scope scope = new Scope(Token.NO_SOURCE_FILE, null);
    ControlFlowGraph cfg = new ControlFlowGraph(null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);
    analysis.addToUseIfLocal(name, defNode, analysis.createEntryLattice());
    analysis.addToUseIfLocal(name, useNode, analysis.createEntryLattice());
    Collection<Node> uses = analysis.getUses(name, defNode);
    List<Node> useList = new ArrayList<>(uses);
    assertThat(useList).hasSize(1);
    assertThat(useList.get(0)).isSameInstanceAs(useNode);
  }

  @Test
  public void testEmptyGetUses() {
    String name = "x";
    Node defNode = Node.newIntLiteral(1);
    Scope scope = new Scope(Token.NO_SOURCE_FILE, null);
    ControlFlowGraph cfg = new ControlFlowGraph(null, null, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);
    Collection<Node> uses = analysis.getUses(name, defNode);
    assertNull(uses);
  }

  @Test
  public void testMayUseMapEquals() {
    String name1 = "x";
    Node defNode1 = Node.newIntLiteral(1);
    String name2 = "y";
    Node defNode2 = Node.newIntLiteral(2);
    Scope scope = new Scope(Token.NO_SOURCE_FILE, null);
    ControlFlowGraph cfg = new ControlFlowGraph(null, null, null);
    MaybeReachingVariableUse analysis1 = new MaybeReachingVariableUse(cfg, scope, null);
    MaybeReachingVariableUse analysis2 = new MaybeReachingVariableUse(cfg, scope, null);
    analysis1.addToUseIfLocal(name1, defNode1, analysis1.createEntryLattice());
    analysis2.addToUseIfLocal(name2, defNode2, analysis2.createEntryLattice());
    Multimap<Var, Node> mayUseMap1 = analysis1.createEntryLattice().mayUseMap;
    Multimap<Var, Node> mayUseMap2 = analysis2.createEntryLattice().mayUseMap;
    assertNull(mayUseMap1.equals(mayUseMap2));
  }

  @Test
  public void testMayUseMapHashCode() {
    String name1 = "x";
    Node defNode1 = Node.newIntLiteral(1);
    String name2 = "y";
    Node defNode2 = Node.newIntLiteral(2);
    Scope scope = new Scope(Token.NO_SOURCE_FILE, null);
    ControlFlowGraph cfg = new ControlFlowGraph(null, null, null);
    MaybeReachingVariableUse analysis1 = new MaybeReachingVariableUse(cfg, scope, null);
    MaybeReachingVariableUse analysis2 = new MaybeReachingVariableUse(cfg, scope, null);
    analysis1.addToUseIfLocal(name1, defNode1, analysis1.createEntryLattice());
    analysis2.addToUseIfLocal(name2, defNode2, analysis2.createEntryLattice());
    Multimap<Var, Node> mayUseMap1 = analysis1.createEntryLattice().mayUseMap;
    Multimap<Var, Node> mayUseMap2 = analysis2.createEntryLattice().mayUseMap;
    int hashCode1 = mayUseMap1.hashCode();
    int hashCode2 = mayUseMap2.hashCode();
    assertNull(hashCode1 == hashCode2);
  }
}