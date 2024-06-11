package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
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
    // Initialize the test environment
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    // Test simple cases
    testSimpleCases(analysis);

    // Test complex cases
    testComplexCases(analysis);
  }

  private void testSimpleCases(MaybeReachingVariableUse analysis) {
    Node script = Node.createTree(
        "var a = 1;",
        Token.SCRIPT,
        0,
        null,
        0
    );

    analysis.computeMayBeReachingVariables(script.getFirstChild());

    // Test getUses method for simple cases
    Var a = jsScope.getVar("a");
    Collection<Node> uses = analysis.getUses("a", script.getFirstChild());
    assertThat(uses).containsExactly(script.getFirstChild());
  }

  private void testComplexCases(MaybeReachingVariableUse analysis) {
    Node script = Node.createTree(
        "var a = 1;",
        Token.SCRIPT,
        0,
        null,
        0
    );

    Node ifStatement = Node.createTree(
        "if (x) {}",
        Token.IF,
        1,
        script.getFirstChild(),
        null
    );

    Node assignment = Node.createTree(
        "a = 2;",
        Token.ASSIGN,
        2,
        Node.createTree("x", Token.NAME, 0, null, 0),
        ifStatement
    );

    Node printStatement = Node.createTree(
        "print(a);",
        Token.PRINT,
        3,
        Node.createTree("a", Token.NAME, 0, null, 0),
        null
    );

    script.setFirstChild(ifStatement);
    ifStatement.setFirstChild(assignment);
    script.setLastChild(printStatement);

    analysis.computeMayBeReachingVariables(script.getFirstChild());

    // Test getUses method for complex cases
    Var a = jsScope.getVar("a");
    Collection<Node> uses = analysis.getUses("a", script.getFirstChild());
    assertThat(uses).containsExactly(assignment, printStatement);
  }
}