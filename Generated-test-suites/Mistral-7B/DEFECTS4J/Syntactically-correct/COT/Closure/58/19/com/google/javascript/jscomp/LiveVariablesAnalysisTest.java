import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Create a simple test case with no exceptions or inner functions.
    String sourceCode = "function test() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  x = 2;" +
        "  return y; // should return 1" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(sourceCode, jsScope);

    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        cfg, jsScope, compiler);

    LiveVariableLattice expectedResult = new LiveVariableLattice(jsScope.getVarCount());
    expectedResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariableLattice actualResult = liveVariablesAnalysis.compute(cfg.getEntryNode(),
        liveVariablesAnalysis.createEntryLattice());

    assertTrue(expectedResult.equals(actualResult));

    // Test a case with an exception.
    sourceCode = "function test() {" +
        "  try {" +
        "    var x = 1;" +
        "    throw new Error('Exception');" +
        "  } catch (e) {" +
        "    var y = x;" +
        "  }" +
        "  return y; // should return 1" +
        "}";

    jsScope = new Scope();
    compiler = new Compiler();
    cfg = compiler.getCFG(sourceCode, jsScope);

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    LiveVariableLattice expectedResultWithException = new LiveVariableLattice(jsScope.getVarCount());
    expectedResultWithException.liveSet.set(jsScope.getVar("x").index);

    actualResult = liveVariablesAnalysis.compute(cfg.getEntryNode(),
        liveVariablesAnalysis.createEntryLattice());

    assertTrue(expectedResultWithException.equals(actualResult));

    // Test a case with inner functions.
    sourceCode = "function test() {" +
        "  function innerFunction() {" +
        "    var x = 1;" +
        "  }" +
        "  var y = x;" +
        "  innerFunction();" +
        "  return y; // should return 1" +
        "}";

    jsScope = new Scope();
    compiler = new Compiler();
    cfg = compiler.getCFG(sourceCode, jsScope);

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    LiveVariableLattice expectedResultWithInnerFunction = new LiveVariableLattice(jsScope.getVarCount());
    expectedResultWithInnerFunction.liveSet.set(jsScope.getVar("x").index);

    actualResult = liveVariablesAnalysis.compute(cfg.getEntryNode(),
        liveVariablesAnalysis.createEntryLattice());

    assertTrue(expectedResultWithInnerFunction.equals(actualResult));
  }
}

class LiveVariableLattice {
  // You don't need to modify this class.
}