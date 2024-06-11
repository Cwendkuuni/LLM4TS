import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  @ org.junit.Test
  public void testLiveVariablesAnalysisSimpleFunction() {
    String jsSource = "function foo(x) {" +
        "  var y = 1;" +
        "  x = x + 1;" +
        "  return y + x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("y")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithIfStatement() {
    String jsSource = "function foo(x) {" +
        "  var y = 1;" +
        "  if (x > 0) {" +
        "    var z = 2;" +
        "  }" +
        "  x = x + 1;" +
        "  return y + x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("y"), new Var("x")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithReturnStatement() {
    String jsSource = "function foo(x) {" +
        "  var y = 1;" +
        "  return y;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("y")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithWhileStatement() {
    String jsSource = "function foo(x) {" +
        "  var y = 1;" +
        "  while (x > 0) {" +
        "    x--;" +
        "  }" +
        "  return y + x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("y")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithForStatement() {
    String jsSource = "function foo(x) {" +
        "  for (var i = 0; i < x; i++) {" +
        "    i = i + 1;" +
        "  }" +
        "  return x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("i")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("x")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithAssignmentStatement() {
    String jsSource = "function foo(x) {" +
        "  var y = 1;" +
        "  x = x + 1;" +
        "  y = y + x;" +
        "  return y + x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithConditionalAssignmentStatement() {
    String jsSource = "function foo(x) {" +
        "  var y = 1;" +
        "  if (x > 0) {" +
        "    y = x;" +
        "  } else {" +
        "    y = 1;" +
        "  }" +
        "  return y + x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithTryCatchStatement() {
    String jsSource = "function foo(x) {" +
        "  try {" +
        "    var y = 1;" +
        "    x = x + 1;" +
        "    throw new Error();" +
        "  } catch (e) {" +
        "    var z = 2;" +
        "  }" +
        "  return y + x;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    // In the presence of exceptions, we can't determine the liveness of variables accurately.
    assertNotEquals(Sets.newHashSet(new Var("y")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithFunctionDeclaration() {
    String jsSource = "function foo(x) {" +
        "  function bar(y) {" +
        "    return y + 1;" +
        "  }" +
        "  return bar(x);" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("x")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }

  @ org.junit.Test
  public void testLiveVariablesAnalysisFunctionWithVarDeclaration() {
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  var y = 1;" +
        "  return x + y;" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(new ControlFlowGraph.Builder(), jsScope);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(compiler.compile(jsSource)), jsScope, compiler);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getEscapedLocals());
    assertEquals(Sets.newHashSet(new Var("x"), new Var("y")), liveVariablesAnalysis.getLiveVariablesAtIndex(0));
  }
}