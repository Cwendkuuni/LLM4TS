import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE = "function foo() {" +
      "  var x = 1, y = 2;" +
      "  if (true) {" +
      "    var x = 3;" +
      "  } else {" +
      "    var y = 3;" +
      "  }" +
      "  return x + y;" +
      "}";

  private Scope jsScope;
  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setUp() {
    jsScope = new Scope();
    jsScope.setParent(null);
    jsScope.parse(TEST_CODE, 0, null, null);
    compiler = new AbstractCompiler(jsScope, null);
    cfg = new ControlFlowGraph<>(jsScope.getRootNode(), new EmptyGraphVisitor<Node>());
    analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testBasicFunctionality() {
    Node useNode = cfg.getNode(cfg.getEntryNode().getFirstSuccessor()).getFirstSuccessor();
    Node defNode = analyzer.getDef("x", useNode);
    assertEquals(cfg.getNode(1), defNode);
  }

  @Test
  public void testUseOfUndefinedVariable() {
    Node useNode = cfg.getNode(cfg.getEntryNode().getLastSuccessor());
    assertNull(analyzer.getDef("z", useNode));
  }

  @Test
  public void testUseOfEscapedVariable() {
    String escapedVariableName = "arguments";
    Node useNode = cfg.getNode(cfg.getEntryNode().getLastSuccessor());
    assertNull(analyzer.getDef(escapedVariableName, useNode));
  }

  @Test
  public void testUseOfVariableDefinedInAnotherScope() {
    String otherScopeVariableName = "y";
    Node useNode = cfg.getNode(cfg.getNode(1).getLastSuccessor());
    assertNull(analyzer.getDef(otherScopeVariableName, useNode));
  }

  @Test
  public void testUseOfVariableWithMultipleDefinitions() {
    Node useNode = cfg.getNode(cfg.getNode(2).getLastSuccessor());
    Node defNode1 = cfg.getNode(1);
    Node defNode2 = cfg.getNode(3);
    Node expectedDefNode = defNode1;

    if (analyzer.dependsOnOuterScopeVars("y", useNode)) {
      expectedDefNode = defNode2;
    }

    Node actualDefNode = analyzer.getDef("y", useNode);
    assertEquals(expectedDefNode, actualDefNode);
  }
}