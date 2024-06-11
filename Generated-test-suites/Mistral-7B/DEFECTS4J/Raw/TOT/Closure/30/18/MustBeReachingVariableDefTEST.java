package com.google.javascript.jscomp;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE = "function foo() {" +
      "  var a = 1;" +
      "  var b = 2;" +
      "  var c = 3;" +
      "  if (true) {" +
      "    var d = 4;" +
      "  }" +
      "  return a + b + c;" +
      "}";

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    jsScope = new Scope();
    cfg = ControlFlowGraph.create(jsScope, null);
    compiler = new AbstractCompiler(cfg, jsScope);
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    compiler.compile(new TextSource("test.js", TEST_CODE), options, null, null);
  }

  @Test
  public void testSimpleFunction() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useA = cfg.getEntryNode().getFirstChild();
    Node defA = analysis.getDef("a", useA);
    assertEquals(cfg.getNode(1), defA);

    Node useB = useA.getNext();
    Node defB = analysis.getDef("b", useB);
    assertEquals(cfg.getNode(3), defB);

    Node useC = useB.getNext();
    Node defC = analysis.getDef("c", useC);
    assertEquals(cfg.getNode(5), defC);

    Node useD = cfg.getNode(11).getFirstChild();
    assertNull(analysis.getDef("d", useD));

    Node useE = cfg.getNode(12).getFirstChild();
    assertNull(analysis.getDef("e", useE));

    Node useF = cfg.getNode(13).getFirstChild();
    assertNull(analysis.getDef("f", useF));

    Node useG = cfg.getNode(14).getFirstChild();
    assertNull(analysis.getDef("g", useG));

    Node useH = cfg.getNode(15).getFirstChild();
    assertNull(analysis.getDef("h", useH));

    Node returnNode = cfg.getNode(16);
    assertNull(analysis.getDef("a", returnNode));
    assertNull(analysis.getDef("b", returnNode));
    assertNull(analysis.getDef("c", returnNode));
  }

  @Test
  public void testConditionalBranch() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node ifNode = cfg.getNode(7);
    Node useA = ifNode.getFirstChild();
    Node defA = analysis.getDef("a", useA);
    assertEquals(cfg.getNode(1), defA);

    Node useB = useA.getNext();
    Node defB = analysis.getDef("b", useB);
    assertNull(defB);

    Node useC = useB.getNext();
    Node defC = analysis.getDef("c", useC);
    assertEquals(cfg.getNode(5), defC);

    Node useD = cfg.getNode(11).getFirstChild();
    assertNull(analysis.getDef("a", useD));
    assertNull(analysis.getDef("b", useD));
    assertNull(analysis.getDef("c", useD));
    assertNull(analysis.getDef("d", useD));
  }

  @Test
  public void testEscapedVariable() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Set<Var> escapedVars = Sets.newHashSet(jsScope.getVar("arguments"));
    analysis.escaped = escapedVars;

    Node useArg = cfg.getNode(16).getFirstChild();
    assertNull(analysis.getDef("a", useArg));
    assertNull(analysis.getDef("b", useArg));
    assertNull(analysis.getDef("c", useArg));
  }

  @Test
  public void testDependencyOnOuterScopeVariables() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useX = cfg.getNode(0).getLastChild();
    assertNull(analysis.getDef("x", useX));

    Node useY = cfg.getNode(1).getLastChild();
    assertNull(analysis.getDef("x", useY));

    Node useZ = useY.getNext();
    assertNull(analysis.getDef("x", useZ));

    Node useXInsideFunction = cfg.getNode(8).getFirstChild();
    assertTrue(analysis.dependsOnOuterScopeVars("x", useXInsideFunction));

    Node useYInsideFunction = useXInsideFunction.getNext();
    assertTrue(analysis.dependsOnOuterScopeVars("y", useYInsideFunction));
  }
}