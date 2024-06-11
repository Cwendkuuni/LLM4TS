import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  private static ControlFlowGraph<Node> createSampleControlFlowGraph() {
    // Simplified control flow graph for testing.
    // The graph has one entry node and two exit nodes.
    // The first exit node represents the normal execution path.
    // The second exit node represents the error path.

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entryNode = new Node(Token.BLOCK, null, null);
    Node ifConditionNode = new Node(Token.IF, null, null);
    Node ifTruePathNode = new Node(Token.BLOCK, null, null);
    Node ifFalsePathNode = new Node(Token.BLOCK, null, null);
    Node errorNode = new Node(Token.BLOCK, null, null);
    Node exitNormalNode = new Node(Token.RETURN, null, null);
    Node exitErrorNode = new Node(Token.THROW, null, null);

    cfg.addNodes(entryNode, ifConditionNode, ifTruePathNode, ifFalsePathNode,
        errorNode, exitNormalNode, exitErrorNode);

    cfg.addEdge(entryNode, ifConditionNode);
    cfg.addEdge(ifConditionNode, ifTruePathNode);
    cfg.addEdge(ifConditionNode, errorNode);
    cfg.addEdge(ifTruePathNode, exitNormalNode);
    cfg.addEdge(errorNode, exitErrorNode);

    return cfg;
  }

  private static Scope createSampleScope() {
    Scope scope = new Scope();

    Var a = scope.addVar("a");
    Var b = scope.addVar("b");
    Var c = scope.addVar("c");
    Var d = scope.addVar("d");

    scope.setRootNode(new Node(Token.BLOCK, null, null));

    return scope;
  }

  private static MustBeReachingVariableDef createSampleAnalysis(
      ControlFlowGraph<Node> cfg, Scope jsScope) {

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg,
        jsScope, new NoOpCompiler());

    return analysis;
  }

  @org.junit.Test
  public void testGetDef() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope jsScope = createSampleScope();

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, jsScope);

    Node entryNode = cfg.getEntryNode();
    Node aAssignmentNode = new Node(Token.ASSIGN, null, new Node(Token.NAME,
        "a", null));
    Node useNodeA = new Node(Token.GETPROP, null, new Node(Token.NAME, "a", null));

    assertEquals(aAssignmentNode, analysis.getDef("a", useNodeA));
    assertNull(analysis.getDef("b", useNodeA));
    assertNull(analysis.getDef("c", useNodeA));
    assertNull(analysis.getDef("d", useNodeA));

    analysis.dependsOnOuterScopeVars("a", useNodeA);
    assertSame(analysis.getDef("a", useNodeA), aAssignmentNode);
  }

  @org.junit.Test
  public void testGetDef_EscapedVariable() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope jsScope = createSampleScope();

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, jsScope);

    Node entryNode = cfg.getEntryNode();
    Node globalVariableAssignmentNode = new Node(Token.ASSIGN, null,
        new Node(Token.NAME, "window", null));
    Node windowPropertyAccessNode = new Node(Token.GETPROP, null,
        new Node(Token.NAME, "window", null));

    jsScope.setVar("window", new Var(null, globalVariableAssignmentNode, null));

    assertEquals(globalVariableAssignmentNode, analysis.getDef("window",
        windowPropertyAccessNode));
  }

  @org.junit.Test
  public void testGetDef_UseNodeDoesntExist() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope jsScope = createSampleScope();

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, jsScope);

    Node entryNode = cfg.getEntryNode();
    Node nonExistentUseNode = new Node(Token.BLOCK, null, null);

    assertNull(analysis.getDef("a", nonExistentUseNode));
  }

  @org.junit.Test
  public void testGetDef_OuterScopeVariable() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope outerScope = createSampleScope();
    Scope innerScope = createSampleScope();

    Var outerScopeVariable = outerScope.addVar("outerVariable");
    Var innerScopeVariable = innerScope.addVar("innerVariable");

    outerScope.setParentScope(outerScope);
    innerScope.setParentScope(outerScope);

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, innerScope);

    Node entryNode = cfg.getEntryNode();
    Node outerScopeVariableAssignmentNode = new Node(Token.ASSIGN, null,
        new Node(Token.NAME, "outerVariable", null));
    Node innerScopeVariableUseNode = new Node(Token.GETPROP, null,
        new Node(Token.NAME, "innerVariable", null));

    assertEquals(outerScopeVariableAssignmentNode, analysis.getDef(
        "outerVariable", innerScopeVariableUseNode));
  }

  @org.junit.Test
  public void testGetDef_UseNodeIsInErrorPath() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope jsScope = createSampleScope();

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, jsScope);

    Node entryNode = cfg.getEntryNode();
    Node errorThrowNode = new Node(Token.THROW, null, null);
    Node useNodeA = new Node(Token.GETPROP, null, new Node(Token.NAME, "a", null));

    assertEquals(null, analysis.getDef("a", useNodeA));
  }

  @org.junit.Test
  public void testDependsonOuterScopeVars() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope jsScope = createSampleScope();

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, jsScope);

    Node entryNode = cfg.getEntryNode();
    Node globalVariableAssignmentNode = new Node(Token.ASSIGN, null,
        new Node(Token.NAME, "window", null));
    Node windowPropertyAccessNode = new Node(Token.GETPROP, null,
        new Node(Token.NAME, "window", null));

    jsScope.setVar("window", new Var(null, globalVariableAssignmentNode, null));

    analysis.dependsOnOuterScopeVars("a", new Node(Token.NAME, "a", null));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNodeA));
    assertTrue(analysis.dependsOnOuterScopeVars("window", windowPropertyAccessNode));
  }

  @org.junit.Test
  public void testDependsonOuterScopeVars_ErrorPath() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope jsScope = createSampleScope();

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, jsScope);

    Node entryNode = cfg.getEntryNode();
    Node errorThrowNode = new Node(Token.THROW, null, null);
    Node useNodeA = new Node(Token.GETPROP, null, new Node(Token.NAME, "a", null));

    analysis.dependsOnOuterScopeVars("a", useNodeA);
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNodeA));
  }

  @org.junit.Test
  public void testDependsonOuterScopeVars_Null() {
    ControlFlowGraph<Node> cfg = createSampleControlFlowGraph();
    Scope jsScope = createSampleScope();

    MustBeReachingVariableDef analysis = createSampleAnalysis(cfg, jsScope);

    Node entryNode = cfg.getEntryNode();

    analysis.dependsOnOuterScopeVars(null, entryNode);
    assertFalse(analysis.dependsOnOuterScopeVars(null, entryNode));
  }
}