import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testutils.CompilationTestHelper;

public class LiveVariablesAnalysisTest {

  @Test
  public void testEmptyFunction() {
    CompilationTestHelper helper = new CompilationTestHelper("testemptyfunction.js")
        .addInputFile("testemptyfunction.js")
        .setOutputFile("testemptyfunction-out.js")
        .compile();

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        helper.getControlFlowGraph(), helper.getScope(), null);
    Set<Var> escaped = analysis.getEscapedLocals();
    assertEquals(Collections.emptySet(), escaped);
  }

  @Test
  public void testSimpleVariableAssignment() {
    String jsCode = "function foo() { var x = 1; x = 2; }";

    CompilationTestHelper helper = new CompilationTestHelper("testsimplevariableassignment.js")
        .addInputFile("testsimplevariableassignment.js", jsCode)
        .setOutputFile("testsimplevariableassignment-out.js")
        .compile();

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        helper.getControlFlowGraph(), helper.getScope(), null);
    Set<Var> escaped = analysis.getEscapedLocals();
    Set<Var> expected = ImmutableSet.of(helper.getScope().getVar("x"));
    assertEquals(expected, escaped);
  }

  @Test
  public void testVariableAssignmentInConditionalBlock() {
    String jsCode = "function foo() { if (true) { var x = 1; x = 2; } }";

    CompilationTestHelper helper = new CompilationTestHelper("testvariableassignmentinconditionalblock.js")
        .addInputFile("testvariableassignmentinconditionalblock.js", jsCode)
        .setOutputFile("testvariableassignmentinconditionalblock-out.js")
        .compile();

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        helper.getControlFlowGraph(), helper.getScope(), null);
    Set<Var> escaped = analysis.getEscapedLocals();
    Set<Var> expected = Collections.emptySet();
    assertEquals(expected, escaped);
  }

  @Test
  public void testVariableAssignmentInLoop() {
    String jsCode = "function foo() { for (var i = 0; i < 10; i++) { i = i + 1; } }";

    CompilationTestHelper helper = new CompilationTestHelper("testvariableassignmentinloop.js")
        .addInputFile("testvariableassignmentinloop.js", jsCode)
        .setOutputFile("testvariableassignmentinloop-out.js")
        .compile();

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        helper.getControlFlowGraph(), helper.getScope(), null);
    Set<Var> escaped = analysis.getEscapedLocals();
    Set<Var> expected = ImmutableSet.of(helper.getScope().getVar("i"));
    assertEquals(expected, escaped);
  }

  @Test
  public void testFunctionCallWithArguments() {
    String jsCode = "function foo() { return Math.pow(2, 3); } function bar(x) { return foo(x); }";

    CompilationTestHelper helper = new CompilationTestHelper("testfunctioncallwitharguments.js")
        .addInputFile("testfunctioncallwitharguments.js", jsCode)
        .setOutputFile("testfunctioncallwitharguments-out.js")
        .compile();

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        helper.getControlFlowGraph(), helper.getScope(), null);
    Set<Var> escaped = analysis.getEscapedLocals();
    Set<Var> expected = ImmutableSet.of(helper.getScope().getVar("x"));
    assertEquals(expected, escaped);
  }

  @Test
  public void testVariableAssignmentInTryCatchBlock() {
    String jsCode = "function foo() { try { var x = 1; } catch (e) { x = 2; } }";

    CompilationTestHelper helper = new CompilationTestHelper("testvariableassignmentintrycatchblock.js")
        .addInputFile("testvariableassignmentintrycatchblock.js", jsCode)
        .setOutputFile("testvariableassignmentintrycatchblock-out.js")
        .compile();

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        helper.getControlFlowGraph(), helper.getScope(), null);
    Set<Var> escaped = analysis.getEscapedLocals();
    Set<Var> expected = Collections.emptySet();
    assertEquals(expected, escaped);
  }
}