import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Create a simple test case with no escaping local variables
    String testCode = "function testFunction() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  if (x > y) {" +
        "    x += 1;" +
        "  } else {" +
        "    y += 1;" +
        "  }" +
        "  return x;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.SCRIPT), null);
    AbstractCompiler compiler = new Compiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.analyze(new Node(Token.SCRIPT), testCode);
    Set<Var> expectedLiveVariables = Sets.newHashSet(
        jsScope.getVar("x"),
        jsScope.getVar("y")
    );
    Set<Var> actualLiveVariables = analysis.getEscapedLocals();
    assertEquals(expectedLiveVariables, actualLiveVariables);

    // Test a case with escaped local variables
    String testCodeWithEscape = "function testFunction() {" +
        "  function innerFunction() {" +
        "    var x = 1;" +
        "    return x;" +
        "  }" +
        "  var y = innerFunction();" +
        "  return y;" +
        "}";

    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.analyze(new Node(Token.SCRIPT), testCodeWithEscape);
    expectedLiveVariables = Sets.newHashSet(
        jsScope.getVar("x"),
        jsScope.getVar("y")
    );
    actualLiveVariables = analysis.getEscapedLocals();
    assertEquals(expectedLiveVariables, actualLiveVariables);

    // Test a case with dead code
    String testCodeWithDeadCode = "function testFunction() {" +
        "  var x = 1;" +
        "  if (false) {" +
        "    var y = 2;" +
        "  }" +
        "  return x;" +
        "}";

    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.analyze(new Node(Token.SCRIPT), testCodeWithDeadCode);
    expectedLiveVariables = Sets.newHashSet(jsScope.getVar("x"));
    actualLiveVariables = analysis.getEscapedLocals();
    assertEquals(expectedLiveVariables, actualLiveVariables);

    // Test a case with a loop
    String testCodeWithLoop = "function testFunction() {" +
        "  var x = 0;" +
        "  while (x < 5) {" +
        "    x += 1;" +
        "  }" +
        "  return x;" +
        "}";

    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.analyze(new Node(Token.SCRIPT), testCodeWithLoop);
    expectedLiveVariables = Sets.newHashSet(jsScope.getVar("x"));
    actualLiveVariables = analysis.getEscapedLocals();
    assertEquals(expectedLiveVariables, actualLiveVariables);
  }
}