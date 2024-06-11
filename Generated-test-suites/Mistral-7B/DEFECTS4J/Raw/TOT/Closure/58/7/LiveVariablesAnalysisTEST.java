import static com.google.javascript.jscomp.LiveVariablesAnalysis.ARGUMENT_ARRAY_ALIAS;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testSimpleLocalVariables() {
    String jsCode = "function f(x, y) {\n" +
        "  var a = x + y;\n" +
        "  var b = a * 2;\n" +
        "  return b;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariableLattice result = liveVariablesAnalysis.compute(entryLattice, initialLattice);

    // Check that variables a and b are live
    assertTrue(result.isLive(liveVariablesAnalysis.getVarIndex("a")));
    assertTrue(result.isLive(liveVariablesAnalysis.getVarIndex("b")));

    // Check that x and y are not live since they are not assigned inside the function
    assertFalse(result.isLive(liveVariablesAnalysis.getVarIndex("x")));
    assertFalse(result.isLive(liveVariablesAnalysis.getVarIndex("y")));
  }

  @Test
  public void testEscapedLocalVariables() {
    String jsCode = "function f(x) {\n" +
        "  var a = x;\n" +
        "  function g() {\n" +
        "    return a;\n" +
        "  }\n" +
        "  return g;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

    // Check that the local variable a is escaping the function
    assertTrue(escapedLocals.contains(scope.getVar("a")));
  }

  @Test
  public void testConditionalStatements() {
    String jsCode = "function f(x) {\n" +
        "  var a = x;\n" +
        "  if (Math.random() > 0.5) {\n" +
        "    a *= 2;\n" +
        "  } else {\n" +
        "    a *= 3;\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

    // Check that the local variable a is live since we don't know which branch is taken
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariableLattice result = liveVariablesAnalysis.compute(entryLattice, initialLattice);

    assertTrue(result.isLive(liveVariablesAnalysis.getVarIndex("a")));
  }

  @Test
  public void testExceptionHandling() {
    String jsCode = "function f(x) {\n" +
        "  try {\n" +
        "    var a = x;\n" +
        "  } catch (e) {\n" +
        "    // do nothing\n" +
        "  }\n" +
        "  return a * 2;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

    // Check that the local variable a is live since an exception might not be thrown
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariableLattice result = liveVariablesAnalysis.compute(entryLattice, initialLattice);

    assertTrue(result.isLive(liveVariablesAnalysis.getVarIndex("a")));
  }

  @Test
  public void testReturnWithoutAssignment() {
    String jsCode = "function f() {\n" +
        "  return x;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

    // Check that the local variable x is not live since it is not assigned a value
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariableLattice result = liveVariablesAnalysis.compute(entryLattice, initialLattice);

    assertFalse(result.isLive(liveVariablesAnalysis.getVarIndex("x")));
  }

  @Test
  public void testMultipleReturnStatements() {
    String jsCode = "function f(x) {\n" +
        "  if (x > 0) {\n" +
        "    return x;\n" +
        "  } else {\n" +
        "    return -x;\n" +
        "  }\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

    // Check that the local variable x is live since one of the return statements assigns a value to it
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariableLattice result = liveVariablesAnalysis.compute(entryLattice, initialLattice);

    assertTrue(result.isLive(liveVariablesAnalysis.getVarIndex("x")));
  }
}