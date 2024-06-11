import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  x = y;" +
        "  alert(x);" +
        "}";

    ControlFlowGraph cfg = new ControlFlowGraph(new Scope(null, null, jsCode, null));
    Scope jsScope = cfg.getFunctionScope(0);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, new NoOpCompiler());
    Node useNode = jsScope.getRootNode().getLastChild();

    Node expectedDef = jsScope.getRootNode().getChildAt(1);
    Node actualDef = analysis.getDef("x", useNode);

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testMustBeReachingVariableDefWithCycle() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  x = y;" +
        "}";

    ControlFlowGraph cfg = new ControlFlowGraph(new Scope(null, null, jsCode, null));
    Scope jsScope = cfg.getFunctionScope(0);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, new NoOpCompiler());
    Node useNode = jsScope.getRootNode().getLastChild();

    try {
      analysis.getDef("x", useNode);
      fail("Expected AnalysisException");
    } catch (AnalysisException e) {
      // Expected
    }
  }

  @Test
  public void testMustBeReachingVariableDefWithEscapedVariable() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  x = y;" +
        "  function bar(z) {" +
        "    x = z;" +
        "  }" +
        "  bar(3);" +
        "  alert(x);" +
        "}" +
        "function main() {" +
        "  foo();" +
        "}" +
        "main();";

    ControlFlowGraph cfg = new ControlFlowGraph(new Scope(null, null, jsCode, null));
    Scope jsScope = cfg.getFunctionScope(1);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, new NoOpCompiler());
    Node useNode = jsScope.getRootNode().getLastChild();

    try {
      analysis.getDef("x", useNode);
      fail("Expected AnalysisException");
    } catch (AnalysisException e) {
      // Expected
    }
  }

  @Test
  public void testMustBeReachingVariableDefWithParameter() {
    String jsCode = "function foo(x) {" +
        "  alert(x);" +
        "}" +
        "function main() {" +
        "  foo(1);" +
        "}" +
        "main();";

    ControlFlowGraph cfg = new ControlFlowGraph(new Scope(null, null, jsCode, null));
    Scope jsScope = cfg.getFunctionScope(1);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, new NoOpCompiler());
    Node useNode = jsScope.getRootNode().getLastChild();

    try {
      analysis.getDef("x", useNode);
      fail("Expected AnalysisException");
    } catch (AnalysisException e) {
      // Expected
    }
  }

  @Test
  public void testMustBeReachingVariableDefWithOuterScopeVariable() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  function bar() {" +
        "    x = 2;" +
        "    alert(x);" +
        "  }" +
        "  bar();" +
        "}" +
        "function main() {" +
        "  foo();" +
        "}" +
        "main();";

    ControlFlowGraph cfg = new ControlFlowGraph(new Scope(null, null, jsCode, null));
    Scope jsScope = cfg.getFunctionScope(0);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, new NoOpCompiler());
    Node useNode = jsScope.getRootNode().getLastChild();

    assertTrue(analysis.dependsOnOuterScopeVars("x", useNode));
  }
}

class NoOpCompiler implements AbstractCompiler {
  @Override
  public void analyze(Node root) {}
}