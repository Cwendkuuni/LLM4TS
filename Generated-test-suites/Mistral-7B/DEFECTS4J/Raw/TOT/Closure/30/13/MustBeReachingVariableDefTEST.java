package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.javascript.jscomp.MustBeReachingVariableDef.Definition.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.MustDefJoin.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.MustDef.createEntryLattice;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.MustDef.createInitialEstimateLattice;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.MustDefJoin.apply;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.MustDefJoin.join;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.getConditionExpression;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.getFirstChild;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.getLastChild;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.isAssignmentOp;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.isDec;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.isForIn;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.isGet;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.isInc;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.isName;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.NodeUtil.isParamList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE =
      "function foo() {" +
      "  var a = 1;" +
      "  for (var i = 0; i < 10; i++) {" +
      "    var x = i;" +
      "    var b = a + i;" +
      "  }" +
      "  return a + 1;" +
      "}";

  private static final Scope MAIN_SCOPE = new Scope();
  private static final AbstractCompiler COMPILER = new AbstractCompiler() {};

  @org.junit.Test
  public void testMustDefAnalysis() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(Node.createAST(TEST_CODE), MAIN_SCOPE, COMPILER);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, MAIN_SCOPE, COMPILER);
    MustDef expected = createMustDef(MAIN_SCOPE.getVars());

    MustDef result = analyzer.compute();
    // Check that the final result is equal to the expected result.
    assertEquals(join(expected, expected), join(analyzer.createEntryLattice(), analyzer.createInitialEstimateLattice()));

    // Check that the variable lattice for each local variable is set correctly.
    ImmutableSet<Var> vars = Sets.newHashSet(MAIN_SCOPE.getVars());
    for (Var var : vars) {
      Node defNode = analyzer.getDef(var.getName(), cfg.getEntryNode());
      if (var.getName().equals("i")) {
        // The value of 'i' is undefined at the beginning and
        // is assigned a new value in every iteration of the for loop.
        assertNull(defNode);
      } else if (var.getName().equals("x")) {
        // 'x' is assigned a new value in every iteration of the for loop.
        assertEquals(var.getParentNode().getFirstChild(), defNode);
      } else {
        // 'a' is defined at the beginning and is not reassigned.
        assertEquals(var.getParentNode().getRootNode(), defNode);
      }
    }

    // Check that the analysis handles conditional branches correctly.
    Node ifCondition = getConditionExpression(cfg.getNode(9));
    MustDef ifDef = analyzer.createEntryLattice();
    analyzer.flowThrough(ifCondition, ifDef);
    MustDef ifResult = analyzer.compute();
    assertEquals(join(ifDef, ifResult), join(analyzer.createEntryLattice(), analyzer.createInitialEstimateLattice()));

    // Check that the analysis handles loops correctly.
    Node loopCondition = getConditionExpression(cfg.getNode(5));
    MustDef loopDef = createMustDef(MAIN_SCOPE.getVars());
    analyzer.flowThrough(loopCondition, loopDef);
    MustDef loopResult = analyzer.compute();
    assertEquals(join(loopDef, loopResult), join(analyzer.createEntryLattice(), analyzer.createInitialEstimateLattice()));

    // Check that the analysis handles assignments correctly.
    Node assignment = cfg.getNode(11);
    MustDef assignmentDef = createMustDef(MAIN_SCOPE.getVars());
    analyzer.flowThrough(assignment, assignmentDef);
    MustDef assignmentResult = analyzer.compute();
    Definition expectedAssignmentDef = new Definition(assignment.getLastChild());
    Definition actualAssignmentDef = assignmentResult.reachingDef.get(MAIN_SCOPE.getVar("b"));
    assertTrue(actualAssignmentDef != null);
    assertTrue(actualAssignmentDef.equals(expectedAssignmentDef));

    // Check that the analysis handles get expressions correctly.
    Node getExpression = getLastChild(getFirstChild(cfg.getNode(13)));
    MustDef getDef = createMustDef(MAIN_SCOPE.getVars());
    analyzer.flowThrough(getExpression, getDef);
    MustDef getResult = analyzer.compute();
    // 'a' is the target of the get expression and is defined at the beginning.
    Definition expectedGetDef = new Definition(cfg.getNode(1));
    Definition actualGetDef = getResult.reachingDef.get(MAIN_SCOPE.getVar("a"));
    assertTrue(actualGetDef != null);
    assertTrue(actualGetDef.equals(expectedGetDef));

    // Check that the analysis handles function calls correctly.
    Node functionCall = cfg.getNode(15);
    MustDef functionCallDef = createMustDef(MAIN_SCOPE.getVars());
    analyzer.flowThrough(functionCall, functionCallDef);
    MustDef functionCallResult = analyzer.compute();
    // The function call returns a value that depends on 'a', so 'a' should be
    // included in the result.
    Var returnVar = MAIN_SCOPE.getVar("a");
    Definition expectedReturnDef = new Definition(cfg.getNode(1));
    Definition actualReturnDef = functionCallResult.reachingDef.get(returnVar);
    assertTrue(actualReturnDef != null);
    assertTrue(actualReturnDef.equals(expectedReturnDef));

    // Check that the analysis handles escaped variables correctly.
    MustDef escapedDef = createMustDef(ImmutableSet.of(MAIN_SCOPE.getVar("arguments")));
    analyzer = new MustBeReachingVariableDef(cfg, MAIN_SCOPE, COMPILER) {
      @Override
      void escapeParameters(MustDef output) {
        output.reachingDef.putAll(escapedDef.reachingDef);
      }
    };
    MustDef escapedResult = analyzer.compute();
    // Since 'arguments' is an escaped variable, it should not be included in
    // the result.
    assertFalse(escapedResult.reachingDef.containsKey(MAIN_SCOPE.getVar("arguments")));

    // Check that the analysis handles variables that depend on escaped variables correctly.
    Var dependedVar = MAIN_SCOPE.getVar("x");
    MustDef dependedDef = createMustDef(ImmutableSet.of(MAIN_SCOPE.getVar("arguments"), dependedVar));
    analyzer = new MustBeReachingVariableDef(cfg, MAIN_SCOPE, COMPILER) {
      @Override
      void escapeParameters(MustDef output) {
        output.reachingDef.putAll(escapedDef.reachingDef);
      }
    };
    MustDef dependedResult = analyzer.compute();
    Definition expectedDependedDef = new Definition(cfg.getNode(6));
    Definition actualDependedDef = dependedResult.reachingDef.get(dependedVar);
    assertTrue(actualDependedDef != null);
    assertTrue(actualDependedDef.equals(expectedDependedDef));

    // Check that the analysis handles variables that depend on local variables properly.
    Var dependeeVar = MAIN_SCOPE.getVar("i");
    MustDef dependeeDef = createMustDef(ImmutableSet.of(MAIN_SCOPE.getVar("i")));
    analyzer = new MustBeReachingVariableDef(cfg, MAIN_SCOPE, COMPILER) {
      @Override
      void flowThrough(Node n, MustDef input) {
        super.flowThrough(n, input);
        if (n.isName() && isName("i")) {
          analyzer.addToDefIfLocal("i", null, null, input);
        }
      }
    };
    MustDef dependeeResult = analyzer.compute();
    Definition expectedDependeeDef = new Definition(cfg.getNode(5));
    Definition actualDependeeDef = dependeeResult.reachingDef.get(dependeeVar);
    assertTrue(actualDependeeDef != null);
    assertTrue(actualDependeeDef.equals(expectedDependeeDef));

    // Check that the analysis handles variables that depend on themselves properly.
    Var selfVar = MAIN_SCOPE.getVar("a");
    MustDef selfDef = createMustDef(ImmutableSet.of(selfVar));
    MustDef selfResult = analyzer.compute();
    Definition expectedSelfDef = new Definition(cfg.getNode(1));
    Definition actualSelfDef = selfResult.reachingDef.get(selfVar);
    assertTrue(actualSelfDef != null);
    assertTrue(actualSelfDef.equals(expectedSelfDef));

    // Check that the analysis handles variables that depend on variables that depend on themselves properly.
    Var grandSelfVar = MAIN_SCOPE.getVar("a");
    MustDef grandSelfDef = createMustDef(ImmutableSet.of(grandSelfVar));
    analyzer = new MustBeReachingVariableDef(cfg, MAIN_SCOPE, COMPILER) {
      @Override
      void flowThrough(Node n, MustDef input) {
        super.flowThrough(n, input);
        if (n.isName() && isName("a")) {
          analyzer.addToDefIfLocal("a", null, null, input);
        }
      }
    };
    MustDef grandSelfResult = analyzer.compute();
    Definition expectedGrandSelfDef = new Definition(cfg.getNode(1));
    Definition actualGrandSelfDef = grandSelfResult.reachingDef.get(grandSelfVar);
    assertTrue(actualGrandSelfDef != null);
    assertTrue(actualGrandSelfDef.equals(expectedGrandSelfDef));
  }

  private static MustDef createMustDef(Iterable<Var> vars) {
    return new MustDef(vars.iterator());
  }
}