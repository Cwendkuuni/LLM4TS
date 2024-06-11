import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleAssignment() {
    String jsCode = "function f() {" + "  var x = 1; " + "  var y = x; " + "  return y; " + "}";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = cfg.getEntryNode().getLastChild();
    Node defNode = analysis.getDef("x", useNode);
    assertEquals(analysis.getCfg().getNode(cfg.getEntryNode().getFirstChild())
        .getNode(), defNode);
  }

  @Test
  public void testMultipleAssignments() {
    String jsCode = "function f() {" + "  var x = 1, y = 2; " + "  x = y; " + "  y = x; " + "  return x; " + "}";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = cfg.getEntryNode().getLastChild();
    Node defNode = analysis.getDef("x", useNode);
    assertEquals(analysis.getCfg().getNode(cfg.getEntryNode().get(1))
        .getNode(), defNode);
  }

  @Test
  public void testConditionalReachingDefinitions() {
    String jsCode = "function f() {" + "  var x = 1; " + "  if (true) {" + "    var y = 2;" + "  } " + "  if (false) {" + "    var z = 3;" + "  } " + "  return x; " + "}";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = cfg.getEntryNode().getLastChild();
    assertNull(analysis.getDef("y", useNode));
    assertNull(analysis.getDef("z", useNode));
    assertEquals(analysis.getCfg().getNode(cfg.getEntryNode().getFirstChild())
        .getNode(), analysis.getDef("x", useNode));
  }

  @Test
  public void testReachingDefinitionsAcrossFunctions() {
    String jsCode = "function f() {" + "  var x = 1;" + "}" + "function g() {" + "  var y = f();" + "  return y; " + "} " + "function h() {" + "  return g();" + "}";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = cfg.getNode(cfg.getFunctionNodes().get(2).getNode())
        .getNode();
    assertEquals(cfg.getNode(cfg.getFunctionNodes().get(0).getNode())
        .getNode(), analysis.getDef("x", useNode));
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidArgument() {
    String jsCode = "function f() {" + "  var x = 1; " + "  return x; " + "}";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = cfg.getNode(cfg.getFunctionNodes().get(0).getNode())
        .getNode(); // This node does not have a use.
    analysis.getDef("x", useNode);
  }

  @Test
  public void testParameters() {
    String jsCode = "function f(x, y) {" + "  return x + y; " + "}";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = cfg.getEntryNode().getLastChild();
    assertNull(analysis.getDef("x", useNode));
    assertNull(analysis.getDef("y", useNode));
  }

  @Test
  public void testOuterScopeVariable() {
    String jsCode = "function f() {" + "  var x = 1;" + "  function g() {" + "    var y = x;" + "  }" + "  g();" + "  return x; " + "}";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = cfg.getEntryNode().getLastChild();
    assertNull(analysis.getDef("y", useNode));
    assertEquals(analysis.getCfg().getNode(cfg.getFunctionNodes().get(0).getNode())
        .getNode(), analysis.getDef("x", useNode));
  }
}