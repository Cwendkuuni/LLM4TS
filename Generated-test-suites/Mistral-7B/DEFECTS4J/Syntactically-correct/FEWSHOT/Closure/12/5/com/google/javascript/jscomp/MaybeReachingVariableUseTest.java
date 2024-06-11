package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private static final Scope JS_SCOPE = mock(Scope.class);
  private static final ControlFlowGraph<Node> CFG = mock(ControlFlowGraph.class);
  private static final AbstractCompiler COMPILER = mock(AbstractCompiler.class);

  private MaybeReachingVariableUse maybeReachingVariableUse;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    maybeReachingVariableUse = new MaybeReachingVariableUse(
        CFG, JS_SCOPE, COMPILER);
  }

  @Test
  public void testEmpty() {
    ReachingUses empty = maybeReachingVariableUse.createEntryLattice();
    assertNull(empty.mayUseMap.keys().toArray(new Var[0]));
  }

  @Test
  public void testSingleLocalVariable() {
    String variableName = "x";
    Var variable = mock(Var.class);
    when(JS_SCOPE.getVar(variableName)).thenReturn(variable);

    Node variableNode = mock(Node.class);
    when(variableNode.getType()).thenReturn(Token.NAME);
    when(variableNode.getString()).thenReturn(variableName);

    ReachingUses result = new ReachingUses();
    addToUseIfLocal(variableName, variableNode, result);

    Multimap<Var, Node> expected = HashMultimap.create();
    expected.put(variable, variableNode);

    assertThat(result.mayUseMap).isEqualTo(expected);
  }

  @Test
  public void testMultipleLocalVariables() {
    String variableName1 = "x";
    Var variable1 = mock(Var.class);
    when(JS_SCOPE.getVar(variableName1)).thenReturn(variable1);

    String variableName2 = "y";
    Var variable2 = mock(Var.class);
    when(JS_SCOPE.getVar(variableName2)).thenReturn(variable2);

    Node variableNode1 = mock(Node.class);
    when(variableNode1.getType()).thenReturn(Token.NAME);
    when(variableNode1.getString()).thenReturn(variableName1);

    Node variableNode2 = mock(Node.class);
    when(variableNode2.getType()).thenReturn(Token.NAME);
    when(variableNode2.getString()).thenReturn(variableName2);

    ReachingUses result = new ReachingUses();
    addToUseIfLocal(variableName1, variableNode1, result);
    addToUseIfLocal(variableName2, variableNode2, result);

    Multimap<Var, Node> expected = HashMultimap.create();
    expected.putAll(result.mayUseMap);

    assertThat(expected.keys()).containsExactly(variable1, variable2);
    assertThat(expected.asMap().get(variable1)).containsExactly(variableNode1);
    assertThat(expected.asMap().get(variable2)).containsExactly(variableNode2);
  }

  @Test
  public void testEscapedVariable() {
    String variableName = "x";
    Var variable = mock(Var.class);
    when(JS_SCOPE.getVar(variableName)).thenReturn(variable);

    Node variableNode = mock(Node.class);
    when(variableNode.getType()).thenReturn(Token.NAME);
    when(variableNode.getString()).thenReturn(variableName);

    when(escaped.contains(variable)).thenReturn(true);

    ReachingUses result = new ReachingUses();
    addToUseIfLocal(variableName, variableNode, result);

    assertNull(result.mayUseMap.keys());
  }

  @Test
  public void testConditional() {
    String variableName = "x";
    Var variable = mock(Var.class);
    when(JS_SCOPE.getVar(variableName)).thenReturn(variable);

    Node conditionNode = mock(Node.class);
    when(conditionNode.getType()).thenReturn(Token.IF);

    Node variableNode = mock(Node.class);
    when(variableNode.getType()).thenReturn(Token.NAME);
    when(variableNode.getString()).thenReturn(variableName);

    ReachingUses result = new ReachingUsages();
    computeMayUse(conditionNode, null, result, false);
    addToUseIfLocal(variableName, variableNode, result);

    Multimap<Var, Node> expected = HashMultimap.create();
    expected.put(variable, variableNode);

    assertThat(result.mayUseMap).isEqualTo(expected);
  }

  @Test
  public void testWhileLoop() {
    String variableName = "x";
    Var variable = mock(Var.class);
    when(JS_SCOPE.getVar(variableName)).thenReturn(variable);

    Node conditionNode = mock(Node.class);
    when(conditionNode.getType()).thenReturn(Token.WHILE);

    Node variableNode = mock(Node.class);
    when(variableNode.getType()).thenReturn(Token.NAME);
    when(variableNode.getString()).thenReturn(variableName);

    ReachingUses result = new ReachingUsages();
    computeMayUse(conditionNode, null, result, false);
    addToUseIfLocal(variableName, variableNode, result);

    Multimap<Var, Node> expected = HashMultimap.create();
    expected.put(variable, variableNode);

    assertThat(result.mayUseMap).isEqualTo(expected);
  }

  @Test
  public void testForLoop() {
    String variableName = "x";
    Var variable = mock(Var.class);
    when(JS_SCOPE.getVar(variableName)).thenReturn(variable);

    Node lhsNode = mock(Node.class);
    when(lhsNode.getType()).thenReturn(Token.VAR);
    when(lhsNode.getString()).thenReturn(variableName);

    Node rhsNode = mock(Node.class);
    when(rhsNode.getType()).thenReturn(Token.NAME);
    when(rhsNode.getString()).thenReturn("y");

    Node assignmentNode = mock(Node.class);
    when(assignmentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignmentNode.getFirstChild()).thenReturn(lhsNode);
    when(assignmentNode.getLastChild()).thenReturn(rhsNode);

    Node forLoopNode = mock(Node.class);
    when(forLoopNode.getType()).thenReturn(Token.FOR);
    when(forLoopNode.getFirstChild()).thenReturn(lhsNode);
    when(forLoopNode.getLastChild()).thenReturn(assignmentNode);

    ReachingUsages result = new ReachingUsages();
    computeMayUse(forLoopNode, null, result, false);

    Multimap<Var, Node> expected = HashMultimap.create();
    expected.putAll(result.mayUseMap);

    assertThat(expected.keys()).containsExactly(variable);
    assertThat(expected.asMap().get(variable)).isEmpty();
  }

  @Test
  public void testJoin() {
    String variableName1 = "x";
    Var variable1 = mock(Var.class);
    when(JS_SCOPE.getVar(variableName1)).thenReturn(variable1);

    String variableName2 = "y";
    Var variable2 = mock(Var.class);
    when(JS_SCOPE.getVar(variableName2)).thenReturn(variable2);

    Node variableNode1 = mock(Node.class);
    when(variableNode1.getType()).thenReturn(Token.NAME);
    when(variableNode1.getString()).thenReturn(variableName1);

    Node variableNode2 = mock(Node.class);
    when(variableNode2.getType()).thenReturn(Token.NAME);
    when(variableNode2.getString()).thenReturn(variableName2);

    ReachingUses left = new ReachingUsages();
    addToUseIfLocal(variableName1, variableNode1, left);
    addToUseIfLocal(variableName2, variableNode2, left);

    ReachingUsages right = new ReachingUsages();
    addToUseIfLocal(variableName1, variableNode1, right);

    ReachingUsages expected = new ReachingUsages();
    addToUseIfLocal(variableName1, variableNode1, expected);
    addToUseIfLocal(variableName2, variableNode2, expected);

    ReachingUsages result = maybeReachingVariableUse.join(List.of(left, right));

    assertThat(result.mayUseMap).isEqualTo(expected.mayUseMap);
  }

  private void addToUseIfLocal(String name, Node node, ReachingUsages uses) {
    maybeReachingVariableUse.addToUseIfLocal(name, node, uses);
  }
}