package com.google.javascript.jscomp.test;

import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class LiveVariablesAnalysisTest {

  private static final String TEST_CODE =
      "function foo(a, b) {" +
      "  var x = 1;" +
      "  if (a > b) {" +
      "    x = a;" +
      "  } else {" +
      "    x = b;" +
      "    for (var i = 0; i < 10; i++) {" +
      "      x += i;" +
      "    }" +
      "  }" +
      "  return x;" +
      "}";

  @org.junit.Test
  public void testLiveVariablesAnalysis() {
    Scope jsScope = new Scope();
    jsScope.declareVar("a", Node.INT_TYPE);
    jsScope.declareVar("b", Node.INT_TYPE);
    jsScope.declareVar("x", Node.INT_TYPE);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new NoOpCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node rootNode = new Node(Token.SCRIPT, 0, null);
    rootNode.setSourceName("test.js");
    Node functionNode = NodeUtil.createFunctionDeclaration(
        "foo", Node.FUNCTION_DECLARATION, rootNode, jsScope, null);
    rootNode.setFirstChild(functionNode);
    Node bodyNode = functionNode.getFirstChild();
    Node scriptNode = bodyNode.getFirstChild();
    scriptNode.setString(TEST_CODE);

    analysis.compute(scriptNode);
    Set<Var> expectedEscaped = Sets.newHashSet(jsScope.getVar("a"), jsScope.getVar("b"));
    Set<Var> actualEscaped = analysis.getEscapedLocals();
    assertThat(actualEscaped).containsExactlyElementsIn(expectedEscaped);

    BitSet liveVariables = analysis.getEntryLattice().liveSet;
    assertTrue(liveVariables.get(jsScope.getVar("x").index));

    // Testing a dead variable
    jsScope.declareVar("deadVariable", Node.INT_TYPE);
    jsScope = jsScope.withChild(new Scope());
    jsScope.declareVar("deadVariable", Node.INT_TYPE);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(scriptNode);
    liveVariables = analysis.getEntryLattice().liveSet;
    assertFalse(liveVariables.get(jsScope.getVar("deadVariable").index));

    // Testing a function with a return statement
    jsScope = new Scope();
    jsScope.declareVar("x", Node.INT_TYPE);
    functionNode = NodeUtil.createFunctionDeclaration(
        "foo", Node.FUNCTION_DECLARATION, rootNode, jsScope, null);
    rootNode.setFirstChild(functionNode);
    bodyNode = functionNode.getFirstChild();
    bodyNode.setLastChild(new Node(Token.RETURN, 0, null));
    scriptNode = bodyNode.getFirstChild();
    scriptNode.setString(TEST_CODE.substring(0, TEST_CODE.lastIndexOf("return")));
    analysis.compute(scriptNode);
    liveVariables = analysis.getEntryLattice().liveSet;
    assertTrue(liveVariables.get(jsScope.getVar("x").index));
  }

  private static class NoOpCompiler implements Compiler {
    @Override
    public void process(Node program, CompilationUnitTree tree, Node externs,
        Node globals, Node sourceFileNode) {
      // No-op
    }
  }
}