import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsSource = "function test() {\n" +
        "  var a = 1;\n" +
        "  if (true) {\n" +
        "    var b = 2;\n" +
        "  } else {\n" +
        "    var c = 3;\n" +
        "  }\n" +
        "  var d = a + b + c;\n" +
        "  return d;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, null);
    JsCompiler compiler = new JsCompiler(cfg);
    compiler.compile(jsSource);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice resultLattice = analysis.analyze();

    // All variables should be live at the entry point.
    assertEquals(entryLattice.toString(), resultLattice.toString());

    // Test variable liveness at return point.
    LiveVariableLattice returnLattice = analysis.getLatticeAt(analysis.getReturnNode());
    Set<Var> liveVariables = returnLattice.liveSet.stream()
        .map(l -> (Var) scope.getVar(Integer.toString(l.index)))
        .collect(Collectors.toSet());
    assertTrue(liveVariables.contains(scope.getVar("a")));
    assertTrue(liveVariables.contains(scope.getVar("b")));
    assertTrue(liveVariables.contains(scope.getVar("c")));
    assertTrue(liveVariables.contains(scope.getVar("d")));
  }

  @Test
  public void testEscapedVariables() {
    String jsSource = "function test(x) {\n" +
        "  function innerFunction() {\n" +
        "    var a = x;\n" +
        "  }\n" +
        "  innerFunction();\n" +
        "  return x;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, null);
    JsCompiler compiler = new JsCompiler(cfg);
    compiler.compile(jsSource);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    Set<Var> escapedVariables = analysis.getEscapedLocals();
    assertTrue(escapedVariables.contains(scope.getVar("x")));
  }

  @Test
  public void testSimpleAssignment() {
    String jsSource = "function test() {\n" +
        "  var a = 1;\n" +
        "  a = 2;\n" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, null);
    JsCompiler compiler = new JsCompiler(cfg);
    compiler.compile(jsSource);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice resultLattice = analysis.analyze();

    LiveVariableLattice assignmentLattice = analysis.getLatticeAt(analysis.getAssignNode());
    Set<Var> liveVariables = assignmentLattice.liveSet.stream()
        .map(l -> (Var) scope.getVar(Integer.toString(l.index)))
        .collect(Collectors.toSet());
    assertTrue(liveVariables.contains(scope.getVar("a")));
  }
}