package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;

import junit.sourcefiles.CompilerEnvironment;
import junit.sourcefiles.TestCompilationResult;
import junit.sourcefiles.TestCompilationUnit;

@RunWith(JUnit4.class)
public class VarCheckTest {

  @Test
  public void testUndefinedVar() {
    CompilerEnvironment env = new CompilerEnvironment();
    String sourceCode = "var x; function f() { x = 1; }";
    TestCompilationUnit unit = new TestCompilationUnit(sourceCode);
    VarCheck checker = new VarCheck(env.getCompiler());

    TestCompilationResult result = unit.testCompile(checker);
    assertNull(result.getDiagnostics().get(0).getMessage());

    sourceCode = "function f() { var x; }";
    unit = new TestCompilationUnit(sourceCode);
    result = unit.testCompile(checker);
    assertNull(result.getDiagnostics().get(0).getMessage());

    sourceCode = "var x;";
    unit = new TestCompilationUnit(sourceCode);
    result = unit.testCompile(checker);
    assertNull(result.getDiagnostics().get(0).getMessage());

    sourceCode = "function f() { var x; }";
    unit = new TestCompilationUnit(sourceCode);
    result = unit.testCompile(checker);
    assertEquals("JSC_UNDEFINED_VARIABLE", result.getDiagnostics().get(0).getType().getName());
    assertEquals("x is undefined", result.getDiagnostics().get(0).getMessage());

    sourceCode = "function f() { var x; x = 1; }";
    unit = new TestCompilationUnit(sourceCode);
    result = unit.testCompile(checker);
    assertNull(result.getDiagnostics().get(0).getMessage());

    sourceCode = "function f() { var x; x = 1; }";
    unit = new TestCompilationUnit(sourceCode, null, Sets.newHashSet("m1"));
    result = unit.testCompile(checker);
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", result.getDiagnostics().get(0).getType().getName());
    assertEquals("module m1 cannot reference x, defined in file:1:1, since file:1:1 loads after m1", result.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testDuplicateVar() {
    CompilerEnvironment env = new CompilerEnvironment();
    String sourceCode = "function f() { var x; var x; }";
    TestCompilationUnit unit = new TestCompilationUnit(sourceCode);
    VarCheck checker = new VarCheck(env.getCompiler());

    TestCompilationResult result = unit.testCompile(checker);
    assertEquals("JSC_UNDEFINED_VARIABLE", result.getDiagnostics().get(0).getType().getName());
    assertEquals("x is undefined", result.getDiagnostics().get(0).getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", result.getDiagnostics().get(1).getType().getName());
    assertEquals("x is undefined", result.getDiagnostics().get(1).getMessage());
  }

  @Test
  public void testDuplicateVarInFunction() {
    CompilerEnvironment env = new CompilerEnvironment();
    String sourceCode = "function f() { function g() { var x; var x; } }";
    TestCompilationUnit unit = new TestCompilationUnit(sourceCode);
    VarCheck checker = new VarCheck(env.getCompiler());

    TestCompilationResult result = unit.testCompile(checker);
    assertEquals("JSC_UNDEFINED_VARIABLE", result.getDiagnostics().get(0).getType().getName());
    assertEquals("x is undefined", result.getDiagnostics().get(0).getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", result.getDiagnostics().get(1).getType().getName());
    assertEquals("x is undefined", result.getDiagnostics().get(1).getMessage());
  }

  @Test
  public void testDuplicateVarInGlobalScope() {
    CompilerEnvironment env = new CompilerEnvironment();
    String sourceCode = "var x; var x;";
    TestCompilationUnit unit = new TestCompilationUnit(sourceCode);
    VarCheck checker = new VarCheck(env.getCompiler());

    TestCompilationResult result = unit.testCompile(checker);
    assertEquals("JSC_UNDEFINED_VARIABLE", result.getDiagnostics().get(0).getType().getName());
    assertEquals("x is undefined", result.getDiagnostics().get(0).getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", result.getDiagnostics().get(1).getType().getName());
    assertEquals("x is undefined", result.getDiagnostics().get(1).getMessage());
  }

  @Test
  public void testMissingModuleDependency() {
    CompilerEnvironment env = new CompilerEnvironment();
    String module1SourceCode = "function m1() { var x = 1; }";
    TestCompilationUnit module1Unit = new TestCompilationUnit(module1SourceCode);
    CompilerInput module1Input = module1Unit.testCompile(env.getCompiler());

    String sourceCode = "function f() { var x; }";
    TestCompilationUnit unit = new TestCompilationUnit(sourceCode, null, Sets.newHashSet("m1"));
    VarCheck checker = new VarCheck(env.getCompiler());

    TestCompilationResult result = unit.testCompile(checker);
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", result.getDiagnostics().get(0).getType().getName());
    assertEquals("missing module dependency; module f should depend on module m1 because it references x", result.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testStrictModuleDependency() {
    CompilerEnvironment env = new CompilerEnvironment();
    String module1SourceCode = "function m1() { var x = 1; }";
    TestCompilationUnit module1Unit = new TestCompilationUnit(module1SourceCode);
    CompilerInput module1Input = module1Unit.testCompile(env.getCompiler());

    String sourceCode = "function f() { var x; }";
    TestCompilationUnit unit = new TestCompilationUnit(sourceCode, null, Sets.newHashSet("m1"));
    VarCheck checker = new VarCheck(env.getCompiler(), true);

    TestCompilationResult result = unit.testCompile(checker);
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", result.getDiagnostics().get(0).getType().getName());
    assertEquals("module f cannot reference x, defined in module m1", result.getDiagnostics().get(0).getMessage());
  }
}