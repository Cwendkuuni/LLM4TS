package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {
  

  @Test
  public void testUnreachableCodeElimination() {
    String jsCode = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "    alert('unreachable');" +
        "  }" +
        "  var x = 1;" +
        "}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  var x = 1;" +
        "}", toplevel.toString());
  }

  @Test
  public void testRemoveNoOpStatements() {
    String jsCode = "function foo() {" +
        "  var x = a.b.MyClass.prototype.propertyName;" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {}" +
        "function MyClass() {}", toplevel.toString());
  }

  @Test(expected = AssertionError.class)
  public void testRemoveForIn() {
    String jsCode = "function foo() {" +
        "  for (var i in [1]) {" +
        "    return;" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);
  }

  @Test
  public void testRemoveBreak() {
    String jsCode = "function foo() {" +
        "  for (var i = 0; i < 1; i++) {" +
        "    if (i === 1) {" +
        "      break;" +
        "    }" +
        "    alert('i: " + i + "');" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {" +
        "  for (var i = 0; i < 1; i++) {" +
        "    alert('i: " + i + "');" +
        "  }" +
        "}", toplevel.toString());
  }

  @Test
  public void testRemoveContinue() {
    String jsCode = "function foo() {" +
        "  for (var i = 0; i < 3; i++) {" +
        "    if (i % 2 === 0) {" +
        "      continue;" +
        "    }" +
        "    alert('i: " + i + "');" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {" +
        "  for (var i = 0; i < 3; i++) {" +
        "    if (i % 2 === 0) {" +
        "    }" +
        "    alert('i: " + i + "');" +
        "  }" +
        "}", toplevel.toString());
  }

  @Test
  public void testRemoveReturn() {
    String jsCode = "function foo() {" +
        "  return;" +
        "  alert('unreachable');" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {}" +
        "function MyClass() {}", toplevel.toString());
  }

  @Test
  public void testRemoveReturnInsideLoop() {
    String jsCode = "function foo() {" +
        "  for (var i = 0; i < 3; i++) {" +
        "    if (i === 1) {" +
        "      return;" +
        "    }" +
        "    alert('i: " + i + "');" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {" +
        "  for (var i = 0; i < 3; i++) {" +
        "    alert('i: " + i + "');" +
        "  }" +
        "}", toplevel.toString());
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    String jsCode = "function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      continue;" +
        "      break;" +
        "    case 2:" +
        "      return;" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      break;" +
        "    case 2:" +
        "      return;" +
        "  }" +
        "}", toplevel.toString());
  }

  @Test
  public void testRemoveUnconditionalBranchingRecursively() {
    String jsCode = "function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      switch (1) {" +
        "        case 1:" +
        "          continue;" +
        "          break;" +
        "        case 2:" +
        "          return;" +
        "      }" +
        "    case 2:" +
        "      return;" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      switch (1) {" +
        "        case 1:" +
        "        case 2:" +
        "      }" +
        "    case 2:" +
        "      return;" +
        "  }" +
        "}", toplevel.toString());
  }

  @Test
  public void testRemoveUnconditionalBranchingWithUnreachableCode() {
    String jsCode = "function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      if (true) {" +
        "        return;" +
        "      }" +
        "      continue;" +
        "      break;" +
        "    case 2:" +
        "      return;" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);

    assertEquals("function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      continue;" +
        "      break;" +
        "    case 2:" +
        "      return;" +
        "  }" +
        "}", toplevel.toString());
  }

  @Test(expected = AssertionError.class)
  public void testRemoveUnreachableCodeInsideTryCatch() {
    String jsCode = "function foo() {" +
        "  try {" +
        "    throw 1;" +
        "  } catch (e) {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}" +
        "function MyClass() {}";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node externs = null;
    Node toplevel = compiler.parseTree(jsCode, null, null, options);
    unreachableCodeElimination.process(externs, toplevel);
  }

  
}