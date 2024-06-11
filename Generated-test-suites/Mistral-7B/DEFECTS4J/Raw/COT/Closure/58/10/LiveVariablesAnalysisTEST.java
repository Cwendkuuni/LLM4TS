import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    Node rootNode = Node.createTree("function f() { var x = 1; var y = 1; x += y; }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariableLattice latticeEntry = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice latticeInitialEstimate = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(latticeEntry, latticeInitialEstimate);

    LiveVariableLattice lattice = liveVariablesAnalysis.computeLattice(rootNode, latticeInitialEstimate);
    assertTrue(lattice.isLive(scope.getVar("x")));
    assertTrue(lattice.isLive(scope.getVar("y")));
  }

  @Test
  public void testEmptyFunction() {
    Node rootNode = Node.createTree("function f() {}");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariableLattice latticeEntry = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice latticeInitialEstimate = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(latticeEntry, latticeInitialEstimate);

    LiveVariableLattice lattice = liveVariablesAnalysis.computeLattice(rootNode, latticeInitialEstimate);
    assertTrue(lattice.isLive(scope.getVar("x"))); // x is assumed to be live
  }

  @Test(expected = IllegalStateException.class)
  public void testNullScope() {
    Node rootNode = Node.createTree("function f() { var x = 1; }");
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    Node rootNode = Node.createTree("function f() { var x = 1; }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullRootNode() {
    Scope scope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeVarIndex() {
    Node rootNode = Node.createTree("function f() { var x = 1; }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    int varIndex = -1;
    assertFalse(liveVariablesAnalysis.isLive(varIndex));
  }

  @Test
  public void testEscapedLocals() {
    Node rootNode = Node.createTree("function f() { function g() {} }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    rootNode = Node.createTree("function f() { function g() { var x = 1; } }");
    scope = new Scope(rootNode);
    cfg = new ControlFlowGraph<>(rootNode);
    compiler = new AbstractCompiler();

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(1, escapedLocals.size());
    Var escapedLocal = escapedLocals.iterator().next();
    assertEquals("x", escapedLocal.getName());
  }

  @Test
  public void testBasicAssignment() {
    Node rootNode = Node.createTree("function f() { var x = 1; x = x + 1; }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariableLattice latticeEntry = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice latticeInitialEstimate = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(latticeEntry, latticeInitialEstimate);

    LiveVariableLattice lattice = liveVariablesAnalysis.computeLattice(rootNode, latticeInitialEstimate);
    assertTrue(lattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testConditionalAssignment() {
    Node rootNode = Node.createTree("function f() { var x = 1; if (1) x = x + 1; }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariableLattice latticeEntry = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice latticeInitialEstimate = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(latticeEntry, latticeInitialEstimate);

    LiveVariableLattice lattice = liveVariablesAnalysis.computeLattice(rootNode, latticeInitialEstimate);
    assertTrue(lattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testConditionalAssignmentWithException() {
    Node rootNode = Node.createTree("function f() { try { var x = 1; if (1) x = x + 1; } catch (e) {} }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariableLattice latticeEntry = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice latticeInitialEstimate = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(latticeEntry, latticeInitialEstimate);

    LiveVariableLattice lattice = liveVariablesAnalysis.computeLattice(rootNode, latticeInitialEstimate);
    assertTrue(lattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testAssignmentInCondition() {
    Node rootNode = Node.createTree("function f() { var x = 1; if (x = x + 1) {} }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariableLattice latticeEntry = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice latticeInitialEstimate = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(latticeEntry, latticeInitialEstimate);

    LiveVariableLattice lattice = liveVariablesAnalysis.computeLattice(rootNode, latticeInitialEstimate);
    assertFalse(lattice.isLive(scope.getVar("x")));
  }

  @Test
  public void testArgumentsName() {
    Node rootNode = Node.createTree("function f(x) { var y = 1; x = x + 1; }");
    Scope scope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    AbstractCompiler compiler = new AbstractCompiler();

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    rootNode = Node.createTree("function f(x) { function g() { var y = 1; x = x + 1; } }");
    scope = new Scope(rootNode);
    cfg = new ControlFlowGraph<>(rootNode);
    compiler = new AbstractCompiler();

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(1, escapedLocals.size());
    Var escapedLocal = escapedLocals.iterator().next();
    assertEquals("arguments", escapedLocal.getName());
  }
}