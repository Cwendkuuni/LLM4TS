package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdgePredicate;
import com.google.javascript.rhino.Node;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private static final String ESCAPED_VAR_1 = "var1";
  private static final String ESCAPED_VAR_2 = "var2";
  private static final String ESCAPED_VAR_3 = "var3";
  private static final String LOCAL_VAR_1 = "var4";
  private static final String LOCAL_VAR_2 = "var5";
  private static final String LOCAL_VAR_3 = "var6";

  private ControlFlowGraph<Node> cfg;
  private LiveVariablesAnalysis analysis;

  @Before
  public void setUp() {
    cfg = createControlFlowGraph();
    Scope jsScope = createScope();
    AbstractCompiler compiler = new AbstractCompiler();
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.getEscapedLocals().add(jsScope.getVar(ESCAPED_VAR_1));
    analysis.getEscapedLocals().add(jsScope.getVar(ESCAPED_VAR_2));
    analysis.getEscapedLocals().add(jsScope.getVar(ESCAPED_VAR_3));
  }

  @Test
  public void testFlowThrough_LocalVariableIsLive() {
    Node node = getNodeByType(Token.VAR, LOCAL_VAR_1);
    LiveVariablesAnalysis.LiveVariableLattice input = createLattice(LOCAL_VAR_1);
    LiveVariablesAnalysis.LiveVariableLattice expected = createLattice(LOCAL_VAR_1);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    assertEquals(expected, result);
  }

  @Test
  public void testFlowThrough_LocalVariableIsNotLive() {
    Node node = getNodeByType(Token.VAR, LOCAL_VAR_2);
    LiveVariablesAnalysis.LiveVariableLattice input = createLattice(LOCAL_VAR_1);
    LiveVariablesAnalysis.LiveVariableLattice expected = createLattice(LOCAL_VAR_1);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    assertEquals(expected, result);
  }

  @Test
  public void testFlowThrough_LocalVariableIsKilled() {
    Node node = getNodeByType(Token.VAR, LOCAL_VAR_1);
    LiveVariablesAnalysis.LiveVariableLattice input = createLattice(LOCAL_VAR_1, LOCAL_VAR_2);
    LiveVariablesAnalysis.LiveVariableLattice expected = createLattice(LOCAL_VAR_2);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    assertEquals(expected, result);
  }

  @Test
  public void testFlowThrough_LocalVariableIsGenAndKilled() {
    Node node = getNodeByType(Token.VAR, LOCAL_VAR_1);
    LiveVariablesAnalysis.LiveVariableLattice input = createLattice(LOCAL_VAR_1, LOCAL_VAR_2);
    LiveVariablesAnalysis.LiveVariableLattice expected = createLattice(LOCAL_VAR_1, LOCAL_VAR_2);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    assertEquals(expected, result);
  }

  private ControlFlowGraph<Node> createControlFlowGraph() {
    Node root = new Node(Token.SCRIPT);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new DiGraph<Node, Branch>(root));

    Node node1 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, ESCAPED_VAR_1)));
    Node node2 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, LOCAL_VAR_1)));
    Node node3 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, ESCAPED_VAR_2)));
    Node node4 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, LOCAL_VAR_2)));
    Node node5 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, ESCAPED_VAR_3)));
    Node node6 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, LOCAL_VAR_3)));

    root.addChildToBack(node1);
    root.addChildToBack(node2);
    root.addChildToBack(node3);
    root.addChildToBack(node4);
    root.addChildToBack(node5);
    root.addChildToBack(node6);

    cfg.connect(root, node1);
    cfg.connect(node1, node2);
    cfg.connect(root, node3);
    cfg.connect(root, node4);
    cfg.connect(node4, node5);
    cfg.connect(node5, node6);

    return cfg;
  }

  private Scope createScope() {
    Var escapedVar1 = new Var(0, ESCAPED_VAR_1, null, null);
    Var escapedVar2 = new Var(1, ESCAPED_VAR_2, null, null);
    Var escapedVar3 = new Var(2, ESCAPED_VAR_3, null, null);
    Var localVar1 = new Var(3, LOCAL_VAR_1, null, null);
    Var localVar2 = new Var(4, LOCAL_VAR_2, null, null);
    Var localVar3 = new Var(5, LOCAL_VAR_3, null, null);

    List<Var> vars = new ArrayList<>();
    vars.add(escapedVar1);
    vars.add(escapedVar2);
    vars.add(escapedVar3);
    vars.add(localVar1);
    vars.add(localVar2);
    vars.add(localVar3);

    Set<Var> knownConstants = new HashSet<>();

    return new Scope(null, vars, knownConstants);
  }

  private LiveVariablesAnalysis.LiveVariableLattice createLattice(String... liveVariables) {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(6);
    for (String var : liveVariables) {
      lattice.liveSet.set(getVarIndex(var));
    }
    return lattice;
  }

  private int getVarIndex(String var) {
    return analysis.getVarIndex(var);
  }

  private Node getNodeByType(int type, String name) {
    Deque<Node> stack = new ArrayDeque<>();
    stack.add(cfg.getControlFlowGraph().getRoot());
    while (!stack.isEmpty()) {
      Node node = stack.pop();
      if (node.getType() == type && node.getFirstChild().getString().equals(name)) {
        return node;
      }
      for (Node child : node.children()) {
        stack.push(child);
      }
    }
    return null;
  }

}