import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function foo(x, y) {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var z;\n" +
        "  if (x > y) {\n" +
        "    z = a;\n" +
        "  } else {\n" +
        "    z = b;\n" +
        "  }\n" +
        "  return z;\n" +
        "}\n";

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(new NodeFactory(), new Signature(null, Arrays.asList("x", "y"), Arrays.asList(Node.INT_TYPE, Node.INT_TYPE), Node.FUNCTION_TYPE));
    AbstractCompiler compiler = new Compiler();
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis.analyze(rootNode);

    Set<Var> expectedEscapedLocals = Sets.newHashSet(jsScope.getVar("x"), jsScope.getVar("y"));
    Set<Var> actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariableLattice entryLattice = liveAnalysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = liveAnalysis.createInitialEstimateLattice();

    List<LiveVariableLattice> lattices = liveAnalysis.getLattices();
    LiveVariableLattice lattice = lattices.get(lattices.size() - 1);

    // Check that all variables are live at the end of the function.
    for (Var var : jsScope.getAllVars()) {
      assertTrue(lattice.isLive(var));
    }

    // Check that no variables are live at the beginning of the function.
    for (Var var : jsScope.getAllVars()) {
      assertFalse(entryLattice.isLive(var));
    }

    // Check that all variables are initially assumed to be live.
    for (Var var : jsScope.getAllVars()) {
      assertTrue(initialEstimateLattice.isLive(var));
    }

    // Test a simple assignment
    jsCode = "function foo(x) {\n" +
        "  var a = 1;\n" +
        "  x = a;\n" +
        "  return x;\n" +
        "}\n";

    rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    liveAnalysis.analyze(rootNode);

    expectedEscapedLocals = Sets.newHashSet();
    actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    lattice = lattices.get(lattices.size() - 1);
    Var xVar = jsScope.getVar("x");

    // The variable 'x' should be live at the end of the function.
    assertTrue(lattice.isLive(xVar));

    // The variable 'a' should not be live at the end of the function.
    Var aVar = jsScope.getVar("a");
    assertFalse(lattice.isLive(aVar));

    // Test a simple if statement
    jsCode = "function foo(x) {\n" +
        "  var a = 1;\n" +
        "  if (x > 0) {\n" +
        "    a = 2;\n" +
        "  }\n" +
        "  return a;\n" +
        "}\n";

    rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    liveAnalysis.analyze(rootNode);

    expectedEscapedLocals = Sets.newHashSet();
    actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    lattice = lattices.get(lattices.size() - 1);

    // The variable 'a' should be live when x > 0.
    Var aVarInIf = jsScope.getVar("a");
    assertTrue(lattice.isLive(aVarInIf));

    // The variable 'a' should not be live when x <= 0.
    Var aVarOutsideIf = jsScope.getVar("a");
    assertFalse(lattice.isLive(aVarOutsideIf));

    // Test a while loop
    jsCode = "function foo(x) {\n" +
        "  var a = 1;\n" +
        "  while (x > 0) {\n" +
        "    x--;\n" +
        "    a = x;\n" +
        "  }\n" +
        "  return a;\n" +
        "}\n";

    rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    liveAnalysis.analyze(rootNode);

    expectedEscapedLocals = Sets.newHashSet();
    actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    lattice = lattices.get(lattices.size() - 1);

    // The variable 'x' should be live inside the while loop.
    Var xVarInWhile = jsScope.getVar("x");
    assertTrue(lattice.isLive(xVarInWhile));

    // The variable 'x' should not be live outside the while loop.
    Var xVarOutsideWhile = jsScope.getVar("x");
    assertFalse(lattice.isLive(xVarOutsideWhile));

    // The variable 'a' should be live inside the while loop.
    Var aVarInWhile = jsScope.getVar("a");
    assertTrue(lattice.isLive(aVarInWhile));

    // The variable 'a' should not be live outside the while loop.
    Var aVarOutsideWhile = jsScope.getVar("a");
    assertFalse(lattice.isLive(aVarOutsideWhile));

    // Test a for loop
    jsCode = "function foo(x) {\n" +
        "  for (var i = 0; i < x; i++) {\n" +
        "    var a = i;\n" +
        "  }\n" +
        "  return a;\n" +
        "}\n";

    rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    liveAnalysis.analyze(rootNode);

    expectedEscapedLocals = Sets.newHashSet();
    actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    lattice = lattices.get(lattices.size() - 1);

    // The variable 'i' should be live inside the for loop.
    Var iVarInFor = jsScope.getVar("i");
    assertTrue(lattice.isLive(iVarInFor));

    // The variable 'i' should not be live outside the for loop.
    Var iVarOutsideFor = jsScope.getVar("i");
    assertFalse(lattice.isLive(iVarOutsideFor));

    // The variable 'a' should be live inside the for loop.
    Var aVarInFor = jsScope.getVar("a");
    assertTrue(lattice.isLive(aVarInFor));

    // The variable 'a' should not be live outside the for loop.
    Var aVarOutsideFor = jsScope.getVar("a");
    assertFalse(lattice.isLive(aVarOutsideFor));

    // Test an if-else statement
    jsCode = "function foo(x) {\n" +
        "  var a = 1;\n" +
        "  if (x > 0) {\n" +
        "    a = 2;\n" +
        "  } else {\n" +
        "    a = 3;\n" +
        "  }\n" +
        "  return a;\n" +
        "}\n";

    rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    liveAnalysis.analyze(rootNode);

    expectedEscapedLocals = Sets.newHashSet();
    actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    lattice = lattices.get(lattices.size() - 1);

    // The variable 'a' should be live when x > 0.
    Var aVarInIf = jsScope.getVar("a");
    assertTrue(lattice.isLive(aVarInIf));

    // The variable 'a' should not be live when x <= 0.
    Var aVarOutsideIf = jsScope.getVar("a");
    assertFalse(lattice.isLive(aVarOutsideIf));
  }

  @Test
  public void testLiveVariablesAnalysisWithArguments() {
    String jsCode = "function foo(x, y) {\n" +
        "  return x + y;\n" +
        "}";

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(new NodeFactory(), new Signature(null, Arrays.asList("x", "y"), Arrays.asList(Node.INT_TYPE, Node.INT_TYPE), Node.FUNCTION_TYPE));
    AbstractCompiler compiler = new Compiler();
    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis.analyze(rootNode);

    Set<Var> expectedEscapedLocals = Sets.newHashSet(jsScope.getVar("x"), jsScope.getVar("y"));
    Set<Var> actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariableLattice entryLattice = liveAnalysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = liveAnalysis.createInitialEstimateLattice();

    List<LiveVariableLattice> lattices = liveAnalysis.getLattices();
    LiveVariableLattice lattice = lattices.get(lattices.size() - 1);

    // Check that all variables are live at the end of the function.
    for (Var var : jsScope.getAllVars()) {
      assertTrue(lattice.isLive(var));
    }

    // Check that no variables are live at the beginning of the function.
    for (Var var : jsScope.getAllVars()) {
      assertFalse(entryLattice.isLive(var));
    }

    // Check that all variables are initially assumed to be live.
    for (Var var : jsScope.getAllVars()) {
      assertTrue(initialEstimateLattice.isLive(var));
    }

    // Test a simple function call
    jsCode = "function foo(x, y) {\n" +
        "  return x + y;\n" +
        "}\n" +
        "function bar() {\n" +
        "  var z = foo(1, 2);\n" +
        "  return z;\n" +
        "}";

    rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    liveAnalysis.analyze(rootNode);

    expectedEscapedLocals = Sets.newHashSet(jsScope.getVar("x"), jsScope.getVar("y"));
    actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    lattice = lattices.get(lattices.size() - 1);

    // Check that 'x' and 'y' are live in the 'foo' function.
    Var xVarInFoo = jsScope.getVar("x");
    Var yVarInFoo = jsScope.getVar("y");
    assertTrue(lattice.isLive(xVarInFoo));
    assertTrue(lattice.isLive(yVarInFoo));

    // Check that 'z' is live in the 'bar' function.
    Var zVarInBar = jsScope.getVar("z");
    assertTrue(lattice.isLive(zVarInBar));

    // Check that 'x' and 'y' are dead outside the 'foo' function.
    Var xVarOutsideFoo = jsScope.getVar("x");
    Var yVarOutsideFoo = jsScope.getVar("y");
    assertFalse(lattice.isLive(xVarOutsideFoo));
    assertFalse(lattice.isLive(yVarOutsideFoo));

    // Test a function call with an inner function
    jsCode = "function foo(x, y) {\n" +
        "  function inner() {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "  return inner();\n" +
        "}";

    rootNode = new NodeFactory().createScript((Node) ParserUtil.parseCompilationUnit(jsCode));
    liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    liveAnalysis.analyze(rootNode);

    expectedEscapedLocals = Sets.newHashSet(jsScope.getVar("x"), jsScope.getVar("y"));
    actualEscapedLocals = liveAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    lattice = lattices.get(lattices.size() - 1);

    // Check that 'x' and 'y' are live inside the 'inner' function.
    Var xVarInInner = jsScope.getVar("x");
    Var yVarInInner = jsScope.getVar("y");
    assertTrue(lattice.isLive(xVarInInner));
    assertTrue(lattice.isLive(yVarInInner));

    // Check that 'x' and 'y' are dead outside the 'foo' function.
    assertFalse(lattice.isLive(xVarOutsideFoo));
    assertFalse(lattice.isLive(yVarOutsideFoo));

    // Check that 'z' is live inside the 'foo' function.
    Var zVarInFoo = jsScope.getVar("z");
    assertTrue(lattice.isLive(zVarInFoo));

    // Check that 'z' is dead outside the 'foo' function.
    assertFalse(lattice.isLive(zVarInBar));
  }
}