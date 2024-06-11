package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  @Test
  public void testMaybeReachingVariableUse() {
    // Given a simple scope
    Scope scope = new Scope(null, null, null, null, null);
    Var var1 = new Var("var1", null, null, null, null, 0, 0, 0, null, null, null, null);
    scope.putVar(var1.name, var1);

    // When computing "may be" reaching uses for the variable "var1"
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, null);

    // Then the variable "var1" should not have any upward exposed uses by default
    Collection<Node> uses = maybeReachingVariableUse.getUses(var1.name, null);
    assertNull(uses);

    // Given a simple assignment statement
    Node assignment = Token.makeNode(Token.ASSIGN, "x", null, null);
    Node value = Token.makeNode(Token.NUMBER, 1, null, null);
    Node statement = new Node(Token.BLOCK, null, List.of(assignment, value), null);

    // When adding the assignment statement to the control flow graph and re-computing "may be" reaching uses for the variable "var1"
    GraphNode<Node, Branch> node = cfg.addNode(statement);
    FlowState<MaybeReachingVariableUse.ReachingUses> flowState = new FlowState<>(maybeReachingVariableUse.createEntryLattice());
    node.setAnnotation(flowState);
    maybeReachingVariableUse.computeMayUse(assignment, statement, flowState.getEntry(), false);

    // Then the variable "var1" should have an upward exposed use by the assignment statement
    uses = maybeReachingVariableUse.getUses(var1.name, statement);
    assertThat(uses).hasSize(1);
    assertThat(uses.iterator().next()).isSameInstanceAs(assignment);
  }

  @Test
  public void testMaybeReachingVariableUseWithConditional() {
    // Given a simple scope
    Scope scope = new Scope(null, null, null, null, null);
    Var var1 = new Var("var1", null, null, null, null, 0, 0, 0, null, null, null, null);
    scope.putVar(var1.name, var1);

    // Given a simple conditional statement
    Node ifStatement = Token.makeNode(Token.IF, null, null, null);
    Node condition = Token.makeNode(Token.NAME, "x", null, null);
    Node thenBranch = new Node(Token.BLOCK, null, null, null);
    Node elseBranch = new Node(Token.BLOCK, null, null, null);
    Node assignment = Token.makeNode(Token.ASSIGN, "x", null, null);
    Node value = Token.makeNode(Token.NUMBER, 1, null, null);

    ifStatement.setFirstChild(condition);
    ifStatement.setLastChild(thenBranch);
    thenBranch.addChild(assignment);
    thenBranch.addChild(value);
    if (eligibleForTesting(ifStatement)) {
      GraphNode<Node, Branch> node = cfg.addNode(ifStatement);
      FlowState<MaybeReachingVariableUse.ReachingUses> flowState = new FlowState<>(maybeReachingVariableUse.createEntryLattice());
      node.setAnnotation(flowState);

      // When re-computing "may be" reaching uses for the variable "var1" in the conditional branch
      maybeReachingVariableUse.computeMayUse(condition, ifStatement, flowState.getEntry(), false);
      maybeReachingVariableUse.computeMayUse(assignment, thenBranch, flowState.getEntry(), false);

      // Then the variable "var1" should not have any upward exposed uses in the conditional branch by default
      Collection<Node> uses = maybeReachingVariableUse.getUses(var1.name, condition);
      assertNull(uses);

      // But it should have an upward exposed use by the assignment statement in the then branch
      uses = maybeReachingVariableUse.getUses(var1.name, thenBranch);
      assertThat(uses).hasSize(1);
      assertThat(uses.iterator().next()).isSameInstanceAs(assignment);

      // When re-computing "may be" reaching uses for the variable "var1" in the else branch
      FlowState<MaybeReachingVariableUse.ReachingUsages> flowStateElse = new FlowState<>(maybeReachingVariableUse.createEntryLattice());
      node.setAnnotation(flowStateElse);
      maybeReachingVariableUse.computeMayUse(elseBranch, ifStatement, flowStateElse.getEntry(), false);

      // Then the variable "var1" should not have any upward exposed uses in the else branch
      uses = maybeReachingVariableUse.getUses(var1.name, elseBranch);
      assertNull(uses);
    }
  }

  @Test
  public void testMaybeReachingVariableUseWithLoop() {
    // Given a simple scope
    Scope scope = new Scope(null, null, null, null, null);
    Var var1 = new Var("var1", null, null, null, null, 0, 0, 0, null, null, null, null);
    scope.putVar(var1.name, var1);

    // Given a simple for loop statement
    Node forStatement = Token.makeNode(Token.FOR, null, null, null);
    Node initialization = new Node(Token.BLOCK, null, null, null);
    Node condition = Token.makeNode(Token.NAME, "x", null, null);
    Node increment = Token.makeNode(Token.ASSIGN, "x", null, null);
    increment.setLeft(Token.makeNode(Token.NAME, "x", null, null));
    increment.setRight(Token.makeNode(Token.PLUS, null, List.of(increment.getLeft(), Token.makeNode(Token.NUMBER, 1, null, null)), null));

    forStatement.setFirstChild(initialization);
    forStatement.setLastChild(condition);
    initialization.addChild(Token.makeNode(Token.VAR, "x", null, null));
    initialization.addChild(Token.makeNode(Token.ASSIGN, "x", null, null));
    initialization.addChild(Token.makeNode(Token.NUMBER, 0, null, null));

    GraphNode<Node, Branch> node = cfg.addNode(forStatement);
    FlowState<MaybeReachingVariableUse.ReachingUses> flowState = new FlowState<>(maybeReachingVariableUse.createEntryLattice());
    node.setAnnotation(flowState);

    // When re-computing "may be" reaching uses for the variable "var1" in the initialization part of the loop
    maybeReachingVariableUse.computeMayUse(Token.makeNode(Token.VAR, "x", null, null), initialization, flowState.getEntry(), false);

    // Then the variable "var1" should not have any upward exposed uses by default
    Collection<Node> uses = maybeReachingVariableUse.getUses(var1.name, initialization);
    assertNull(uses);

    // But it should have an upward exposed use by the assignment statement
    uses = maybeReachingVariableUse.getUsages(flowState.getOut());
    assertThat(uses).hasSize(1);
    assertThat(uses.iterator().next().getFirstChild()).isSameInstanceAs(Token.makeNode(Token.VAR, "x", null, null));

    // When re-computing "may be" reaching uses for the variable "var1" in the loop condition
    FlowState<MaybeReachingVariableUse.ReachingUsages> flowStateCondition = new FlowState<>(maybeReachingVariableUse.createEntryLattice());
    node.setAnnotation(flowStateCondition);
    maybeReachingVariableUse.computeMayUse(condition, forStatement, flowStateCondition.getEntry(), false);

    // Then the variable "var1" should not have any upward exposed uses in the loop condition by default
    uses = maybeReachingVariableUse.getUses(var1.name, condition);
    assertNull(uses);

    // But it should have an upward exposed use by the loop condition itself
    uses = maybeReachingVariableUse.getUsages(flowStateCondition.getOut());
    assertThat(uses).hasSize(1);
    assertThat(uses.iterator().next().getFirstChild()).isSameInstanceAs(condition);
  }

  @Test
  public void testMaybeReachingVariableUseWithFunctionCall() {
    // Given a simple scope
    Scope scope = new Scope(null, null, null, null, null);
    Var var1 = new Var("var1", null, null, null, null, 0, 0, 0, null, null, null, null);
    scope.putVar(var1.name, var1);

    // Given a simple function call statement
    Node functionCall = Token.makeNode(Token.CALL, null, null, null);
    Node callee = Token.makeNode(Token.NAME, "foo", null, null);
    functionCall.setFirstChild(callee);

    GraphNode<Node, Branch> node = cfg.addNode(functionCall);
    FlowState<MaybeReachingVariableUse.ReachingUses> flowState = new FlowState<>(maybeReachingVariableUse.createEntryLattice());
    node.setAnnotation(flowState);

    // When re-computing "may be" reaching uses for the variable "var1" in the called function
    MaybeReachingVariableUse maybeReachingVariableUseFunction = new MaybeReachingVariableUse(cfg, scope, null);
    FlowState<MaybeReachingVariableUse.ReachingUses> flowStateFunction = new FlowState<>(maybeReachingVariableUseFunction.createEntryLattice());
    node.setAnnotation(flowStateFunction);
    maybeReachingVariableUseFunction.computeMayUse(callee, functionCall, flowState.getEntry(), false);
    maybeReachingVariableUseFunction.computeMayUse(
        NodeUtil.getFunctionBody(callee), functionCall, flowStateFunction.getEntry(), false);

    // Then the variable "var1" should not have any upward exposed uses by default
    Collection<Node> uses = maybeReachingVariableUse.getUses(var1.name, functionCall);
    assertNull(uses);

    // But it should have an upward exposed use by the called function itself
    uses = maybeReachingVariableUseFunction.getUsages(flowStateFunction.getOut());
    assertThat(uses).hasSize(1);
    assertThat(uses.iterator().next().getFirstChild()).isSameInstanceAs(callee);
  }

  private static boolean eligibleForTesting(Node node) {
    if (node == null) {
      return false;
    }

    switch (node.getType()) {
      case Token.IF:
      case Token.FOR:
        return true;
      default:
        return false;
    }
  }
}