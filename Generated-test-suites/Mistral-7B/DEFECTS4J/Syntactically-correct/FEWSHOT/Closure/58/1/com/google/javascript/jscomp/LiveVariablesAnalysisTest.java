package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    jsScope = new Scope();
    jsScope.declareVariable("x", int.class);
    jsScope.declareVariable("y", int.class);
    jsScope.declareVariable("z", int.class);

    Node rootNode = new Node(Token.SCRIPT, 0);
    cfg = new ControlFlowGraph<>();
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testEmptyProgram() {
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeLattice(rootNode, liveVariablesAnalysis.createEntryLattice());
    Preconditions.checkState(result.liveSet.isEmpty());
  }

  @Test
  public void testVariableAssignment() {
    Node assignNode = new Node(Token.SET, 1);
    assignNode.setFirstChild(NodeUtil.makeNode("x", Node.NAME, 0));
    assignNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 10, 0));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assignNode, entryLattice);

    assertThat(result.isLive(jsScope.getVar("x"))).isTrue();
  }

  @Test
  public void testVariableAssignmentWithDeadVariable() {
    Node assignNode = new Node(Token.SET, 1);
    assignNode.setFirstChild(NodeUtil.makeNode("z", Node.NAME, 0));
    assignNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 10, 0));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assignNode, entryLattice);

    assertNull(result.isLive(jsScope.getVar("z")));
  }

  @Test
  public void testVariableAssignmentWithConditionalExpression() {
    Node conditionNode = NodeUtil.makeNode(Token.IF, 0);
    conditionNode.setNext(NodeUtil.makeNode(Token.NAME, "x", 0));
    conditionNode.setNext(NodeUtil.makeNode(Token.INT_LITERAL, 10, 0));
    conditionNode.setNext(NodeUtil.makeNode(Token.NAME, "x", 0));
    conditionNode.setNext(NodeUtil.makeNode(Token.INT_LITERAL, 5, 0));

    Node assignNode = new Node(Token.SET, 1);
    assignNode.setFirstChild(NodeUtil.makeNode("y", Node.NAME, 0));
    assignNode.setNext(NodeUtil.makeName(conditionNode));
    assignNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 20, 0));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assignNode, entryLattice);

    assertThat(result.isLive(jsScope.getVar("x"))).isTrue();
    assertThat(result.isLive(jsScope.getVar("y"))).isTrue();
  }

  @Test
  public void testVariableAssignmentWithDeadAssignment() {
    Node assign1Node = new Node(Token.SET, 1);
    assign1Node.setFirstChild(NodeUtil.makeNode("z", Node.NAME, 0));
    assign1Node.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 10, 0));

    Node assign2Node = new Node(Token.SET, 1);
    assign2Node.setFirstChild(NodeUtil.makeNode("x", Node.NAME, 0));
    assign2Node.setNext(NodeUtil.makeName(assign1Node));
    assign2Node.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 20, 0));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assign2Node, entryLattice);

    assertNull(result.isLive(jsScope.getVar("z")));
  }

  @Test
  public void testVariableAssignmentWithLoop() {
    Node loopNode = new Node(Token.FOR, 1);
    loopNode.setNext(NodeUtil.makeNode(Token.NAME, "i", 0));
    loopNode.setNext(NodeUtil.makeNode(Token.INT_LITERAL, 0, 0));
    loopNode.setNext(NodeUtil.makeNode(Token.INT_LITERAL, 10, 0));
    loopNode.setNext(NodeUtil.makeName(loopNode));

    Node assignNode = new Node(Token.SET, 1);
    assignNode.setFirstChild(NodeUtil.makeNode("x", Node.NAME, 0));
    assignNode.setNext(NodeUtil.makeName(loopNode));
    assignNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 20, 0));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assignNode, entryLattice);

    assertThat(result.isLive(jsScope.getVar("i"))).isTrue();
    assertThat(result.isLive(jsScope.getVar("x"))).isTrue();
  }

  @Test
  public void testVariableAssignmentWithFunctionCall() {
    Node functionCallNode = new Node(Token.CALL, 1);
    functionCallNode.setNext(NodeUtil.makeNode(Token.NAME, "myFunction", 0));
    functionCallNode.setNext(createArgumentsNode());

    Node assignNode = new Node(Token.SET, 1);
    assignNode.setFirstChild(NodeUtil.makeNode("x", Node.NAME, 0));
    assignNode.setNext(NodeUtil.makeName(assignNode));
    assignNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 20, 0));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assignNode, entryLattice);

    // myFunction doesn't have any local variables, so none should be killed.
    Set<Var> functionLocals = Sets.newHashSet();
    assertThat(liveVariablesAnalysis.computeLattice(functionCallNode, liveVariablesAnalysis.createEntryLattice()).liveSet).isEqualTo(functionLocals);

    // x is live because it is assigned after the function call.
    assertThat(result.isLive(jsScope.getVar("x"))).isTrue();
  }

  @Test
  public void testVariableAssignmentWithFunctionCallAndArgumentsEscaped() {
    Node functionCallNode = new Node(Token.CALL, 1);
    functionCallNode.setNext(NodeUtil.makeNode(Token.NAME, "myFunction", 0));
    functionCallNode.setNext(createArgumentsNode());

    Node assignNode = new Node(Token.SET, 1);
    assignNode.setFirstChild(NodeUtil.makeNode("x", Node.NAME, 0));
    assignNode.setNext(NodeUtil.makeName(assignNode));
    assignNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 20, 0));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    liveVariablesAnalysis.markAllParametersEscaped();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assignNode, entryLattice);

    // All arguments are considered escaped, so none of them should be live.
    Set<Var> functionArguments = Sets.newHashSet(jsScope.getVar("arg1"), jsScope.getVar("arg2"));
    assertThat(liveVariablesAnalysis.getEscapedLocals()).containsAll(functionArguments);

    // x is still live because it is assigned after the function call.
    assertThat(result.isLive(jsScope.getVar("x"))).isTrue();
  }

  @Test
  public void testVariableAssignmentWithFunctionCallAndLocalVariableEscaped() {
    Node functionCallNode = new Node(Token.CALL, 1);
    functionCallNode.setNext(NodeUtil.makeNode(Token.NAME, "myFunction", 0));
    functionCallNode.setNext(createArgumentsNode());

    Node functionBodyNode = new Node(Token.BLOCK, 0);
    Node localVariableNode = new Node(Token.VAR, 1);
    localVariableNode.setNext(NodeUtil.makeNode("x", Node.NAME, 0));
    localVariableNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 10, 0));
    functionBodyNode.setFirstChild(localVariableNode);

    Node assignNode = new Node(Token.SET, 1);
    assignNode.setFirstChild(NodeUtil.makeNode("x", Node.NAME, 0));
    assignNode.setNext(NodeUtil.makeName(assignNode));
    assignNode.setNext(NodeUtil.makeNode(Node.INT_LITERAL, 20, 0));
    assignNode.setNext(NodeUtil.makeName(functionBodyNode));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(assignNode, entryLattice);

    // x is still live because it is assigned after the function call.
    assertThat(result.isLive(jsScope.getVar("x"))).isTrue();

    // The local variable x in the function body is considered escaped,
    // so it should not be live.
    Set<Var> functionLocals = Sets.newHashSet(localVariableNode);
    assertThat(result.liveSet).doesNotContain(localVariableNode);
  }

  private Node createArgumentsNode() {
    Node argumentsNode = new Node(Token.NAME, "arguments", 0);
    Node argumentsListNode = new Node(Token.NAME, "callee", 0);
    argumentsListNode.setNext(argumentsNode);
    return argumentsListNode;
  }
}