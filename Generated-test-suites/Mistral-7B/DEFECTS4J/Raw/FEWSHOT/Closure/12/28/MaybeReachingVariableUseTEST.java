package org.apache.commons.cli; // for consistency with the given example

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testEmptyAnalysis() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new NoOpCompiler(); // A dummy compiler for the test
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertTrue(result.mayUseMap.isEmpty());
  }

  @Test
  public void testSimpleVariableUse() {
    String jsCode = "var a = 1; alert(a);";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
    AbstractCompiler compiler = new NoOpCompiler();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("a")));
    assertEquals(cfg.getNode(1), result.mayUseMap.get(jsScope.getVar("a")));
  }

  @Test
  public void testMultipleVariableUses() {
    String jsCode = "var a = 1; var b = a; alert(b);";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
    AbstractCompiler compiler = new NoOpCompiler();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("a")));
    assertEquals(cfg.getNode(1), result.mayUseMap.get(jsScope.getVar("a")));
    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("b")));
    assertEquals(cfg.getNode(1), result.mayUseMap.get(jsScope.getVar("b")));
  }

  @Test
  public void testVariableUseInCondition() {
    String jsCode = "var a = 1; if (a) { alert(1); } alert(a);";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
    AbstractCompiler compiler = new NoOpCompiler();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("a")));
    assertEquals(cfg.getNode(3), result.mayUseMap.get(jsScope.getVar("a")));
  }

  @Test
  public void testVariableUseInLoop() {
    String jsCode = "for (var i = 0; i < 5; ++i) { alert(i); }";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
    AbstractCompiler compiler = new NoOpCompiler();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    Multimap<Var, Node> uses = result.mayUseMap;
    for (int i = 0; i < 5; ++i) {
      Var var = jsScope.getVar("i");
      assertEquals(cfg.getNode(i + 3), uses.get(var).get(0));
    }
  }

  @Test
  public void testVariableEscape() {
    String jsCode = "function foo(x) { var y = x; return x; }";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
    AbstractCompiler compiler = new NoOpCompiler();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    Var var = jsScope.getVar("x");
    assertNull(result.mayUseMap.get(var));
  }

  @Test
  public void testVariableUseInFunctionParameter() {
    String jsCode = "function foo(x) { alert(x); } foo(1);";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
    AbstractCompiler compiler = new NoOpCompiler();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    Var var = jsScope.getVar("x");
    assertNotNull(result.mayUseMap.get(var));
    assertEquals(cfg.getNode(4), result.mayUseMap.get(var));
  }

  @Test
  public void testVariableUseInCatchBlock() {
    String jsCode = "try { throw 1; } catch (e) { alert(e); }";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
    AbstractCompiler compiler = new NoOpCompiler();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(cfg.getEntryNode());
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    Var var = jsScope.getVar("e");
    assertNotNull(result.mayUseMap.get(var));
    assertEquals(cfg.getNode(7), result.mayUseMap.get(var));
  }

  private static class NoOpCompiler implements Compiler {
    @Override
    public void visit(Node node, Scope scope) {}

    @Override
    public void visitEnd(Scope scope) {}
  }
}