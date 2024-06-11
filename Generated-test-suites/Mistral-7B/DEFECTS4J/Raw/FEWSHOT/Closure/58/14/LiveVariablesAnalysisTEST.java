package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import java.util.BitSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis analysis;

  @Before
  public void setUp() {
    // Assuming you have a way to create a ControlFlowGraph and Scope instance,
    // replace the following lines with your implementation.
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope();
    analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testEmptyProgram() {
    LiveVariablesAnalysis emptyAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice entryLattice = emptyAnalysis.createEntryLattice();

    LiveVariablesAnalysis.LiveVariableLattice result = emptyAnalysis.flowThrough(cfg.getStartNode(), entryLattice);

    assertThat(result.isLive(0)).isTrue();
  }

  @Test
  public void testSimpleAssignment() {
    Node assignmentNode = Node.newNode(Token.ASSIGN, new Node[] {
        Node.newName("x"),
        Node.newNumber(1)
    });

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.flowThrough(assignmentNode, entryLattice);

    assertThat(result.isLive(0)).isTrue();
  }

  @Test
  public void testConditionalAssignment() {
    Node conditionNode = Node.newNode(Token.IF, new Node[] {
        Node.newNode(Token.ASSIGN, new Node[] {
          Node.newName("x"),
          Node.newNumber(1)
        }),
        Node.newBoolean(true)
    });

    Node falseBranchNode = Node.newBlock();
    Node trueBranchNode = Node.newBlock();
    Node ifNode = Node.newNode(Token.IF, new Node[] { conditionNode, falseBranchNode, trueBranchNode });

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.flowThrough(ifNode, entryLattice);

    // Assuming x is the first local variable in the scope.
    assertThat(result.isLive(0)).isTrue();
  }

  @Test
  public void testForLoop() {
    Node forNode = Node.newNode(Token.FOR, new Node[] {
        Node.newName("i"),
        Node.newNode(Token.ASSIGN, new Node[] { Node.newName("i"), Node.newNumber(0) }),
        Node.newNode(Token.WHILE, new Node[] { Node.newNode(Token.LEQ, new Node[] { Node.newName("i"), Node.newNumber(10) }), Node.newBoolean(true) }),
        Node.newBlock()
    });

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.flowThrough(forNode, entryLattice);

    // Assuming i is the first local variable in the scope.
    assertThat(result.isLive(0)).isTrue();
  }

  @Test
  public void testFunctionCall() {
    Node functionCallNode = Node.newNode(Token.CALL, new Node[] {
        Node.newName("functionName")
    });

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.flowThrough(functionCallNode, entryLattice);

    // Assuming functionName has a local variable with index 1.
    assertThat(result.isLive(1)).isTrue();
  }

  @Test
  public void testEscapedVariables() {
    // Assuming you have a way to create a Scope instance with declared variables.
    // Replace the following lines with your implementation.
    jsScope = new Scope();
    jsScope.declare("x");
    jsScope.declare("y");
    analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    analysis.markAllParametersEscaped();

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.flowThrough(cfg.getStartNode(), entryLattice);

    // Assuming x and y have indices 0 and 1 respectively.
    assertThat(result.isLive(0)).isFalse();
    assertThat(result.isLive(1)).isFalse();

    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertThat(escapedLocals.size()).isEqualTo(2);
    // Assuming x and y have indices 2 and 3 respectively in the escaped locals set.
    assertThat(escapedLocals.contains(jsScope.getVar(2))).isTrue();
    assertThat(escapedLocals.contains(jsScope.getVar(3))).isTrue();
  }
}