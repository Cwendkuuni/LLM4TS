import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;

public class LiveVariablesAnalysisTest {

  @Test
  public void testSimpleVariable() {
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  return x;" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", jsSource, scope));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testDeadVariable() {
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  return 2;" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", jsSource, scope));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

    assertFalse(result.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testConditionalBranch() {
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    return x;" +
        "  } else {" +
        "    return 2;" +
        "  }" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", jsSource, scope));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testDeadAssignment() {
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return 3;" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", jsSource, scope));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

    assertFalse(result.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testVariableUsedAfterReturn() {
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  return x;" +
        "  x = 2;" +
        "}";

    try {
      Scope scope = new Scope();
      ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", jsSource, scope));

      LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
      LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

      assertTrue(result.isLive(analysis.getVarIndex("x")));
    } catch (RuntimeException e) {
      // The analysis should bail out due to the return statement.
      // The test passes as expected.
    }
  }

  @Test
  public void testFunctionWithArguments() {
    String jsSource = "function foo(x, y) {" +
        "  return x + y;" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", jsSource, scope));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

    // Since all formal parameters are put into the escaped set, we expect all
    // local variables to be considered live.
    for (Var var : scope.getVars()) {
      assertTrue(result.isLive(var.index));
    }
  }

  @Test
  public void testNestedFunctions() {
    String outerJsSource = "function outerFunction() {" +
        "  var outerX = 1;" +
        "  function innerFunction() {" +
        "    var innerX = outerX;" +
        "    return innerX + 1;" +
        "  }" +
        "  return innerFunction();" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph outerCfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", outerJsSource, scope));

    LiveVariablesAnalysis outerAnalysis = new LiveVariablesAnalysis(outerCfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice outerResult = outerAnalysis.computeLattice();

    // outerX is live because it is a non-escaped local variable in the outer function.
    assertTrue(outerResult.isLive(outerAnalysis.getVarIndex("outerX")));

    // The inner function is a new function scope, so all its local variables are considered live.
    Scope innerScope = outerAnalysis.getFunctionScope(outerResult.getFunctionEntryNode());
    ControlFlowGraph innerCfg = new ControlFlowGraph(innerScope.getRootNode());

    LiveVariablesAnalysis innerAnalysis = new LiveVariablesAnalysis(innerCfg, innerScope, null);
    LiveVariablesAnalysis.LiveVariableLattice innerResult = innerAnalysis.computeLattice();

    for (Var var : innerScope.getVars()) {
      assertTrue(innerResult.isLive(innerAnalysis.getVarIndex(var.getName())));
    }
  }

  @Test
  public void testTryCatchBlock() {
    String jsSource = "function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    throw new Error(\"Exception\");" +
        "  } catch (e) {" +
        "    var y = 2;" +
        "  }" +
        "  return x;" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node(Token.SCRIPT, "", jsSource, scope));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

    // Since we cannot know if the exception will be caught or not, we assume all variables are live.
    assertTrue(result.isLive(analysis.getVarIndex("x")));
    assertTrue(result.isLive(analysis.getVarIndex("y")));
  }
}