package com.google.javascript.jscomp;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE = "function f() {" +
      "  var a = 1;" +
      "  var b = 2;" +
      "  if (x > 0) {" +
      "    var c = a + b;" +
      "  } else {" +
      "    var c = 3;" +
      "  }" +
      "  return c;" +
      "}";

  private MustBeReachingVariableDef analyzer;
  private Scope scope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = ControlFlowGraph.create(NodeUtil.parse(TEST_CODE), null);
    scope = new Scope(null, null, Sets.newHashSet("x"));
    compiler = new AbstractCompiler(new CompilerOptions());
    analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
  }

  @Test
  public void testSimplePath() {
    Node useNode = NodeUtil.getFirstReturnNode(cfg.getNodes()).getNodeValue();
    Node defNode = analyzer.getDef("c", useNode);
    assertEquals(cfg.getNode(NodeUtil.getFirstStatementNode(cfg.getNodes())).getNodeValue(), defNode);
  }

  @Test
  public void testConditionalPaths() {
    Node useNodeIfTrue = NodeUtil.getBranchTrue(
        NodeUtil.getIfStatementNode(cfg.getNodes())).getNodeValue();
    Node defNodeIfTrue = analyzer.getDef("c", useNodeIfTrue);
    assertEquals(cfg.getNode(NodeUtil.getAssignmentNode(cfg.getNodes(), 3)).getNodeValue(), defNodeIfTrue);

    Node useNodeIfFalse = NodeUtil.getBranchFalse(
        NodeUtil.getIfStatementNode(cfg.getNodes())).getNodeValue();
    Node defNodeIfFalse = analyzer.getDef("c", useNodeIfFalse);
    assertEquals(cfg.getNode(NodeUtil.getAssignmentNode(cfg.getNodes(), 5)).getNodeValue(), defNodeIfFalse);
  }

  @Test
  public void testNoDefinition() {
    Node useNode = NodeUtil.getAssignmentNode(cfg.getNodes(), 1);
    assertNull(analyzer.getDef("x", useNode));
  }

  @Test
  public void testVariableNotInScope() {
    String name = "outerVar";
    Node useNode = NodeUtil.parse("return " + name + ";").getNodeValue();
    assertNull(analyzer.getDef(name, useNode));
  }

  @Test
  public void testDependenceOnOuterScopeVars() {
    String outerName = "outerVar";
    Node useNode = NodeUtil.parse("var a = " + outerName + "; return a;").getNodeValue();
    assertNull(analyzer.getDef("a", useNode));
    assertTrue(analyzer.dependsOnOuterScopeVars(outerName, useNode));
  }

  @Test
  public void testEscapedVariables() {
    String name = "arguments";
    Node useNode = NodeUtil.getFirstStatementNode(cfg.getNodes());
    analyzer.escaped.clear();
    assertEquals(cfg.getNode(useNode).getNodeValue(), analyzer.getDef(name, useNode));
    assertTrue(analyzer.escaped.containsAll(Arrays.asList(Scope.getExternVars("arguments"))));
    analyzer.escaped.clear();
    assertNull(analyzer.getDef(name, useNode));
  }

  @Test
  public void testParameters() {
    String name = "x";
    Node useNode = cfg.getNodes().get(0).getNodeValue();
    Node defNode = cfg.getNode(NodeUtil.getFirstStatementNode(cfg.getNodes())).getNodeValue();
    analyzer.escaped.clear();
    assertEquals(defNode, analyzer.getDef(name, useNode));
    assertTrue(analyzer.dependsOnOuterScopeVars(name, useNode));
  }

  @Test
  public void testLoop() {
    String code = "function f() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    a = a + b;" +
        "  }" +
        "  return a;" +
        "}";
    ControlFlowGraph<Node> loopCfg = ControlFlowGraph.create(NodeUtil.parse(code), null);
    MustBeReachingVariableDef loopAnalyzer = new MustBeReachingVariableDef(loopCfg, scope, compiler);
    Node useNode = loopCfg.getNodes().get(loopCfg.getNodes().size() - 1).getNodeValue();
    Node defNode = loopAnalyzer.getDef("a", useNode);
    assertEquals(loopCfg.getNode(NodeUtil.getAssignmentNode(loopCfg.getNodes(), loopCfg.getNodes().size() - 5)).getNodeValue(), defNode);
  }
}