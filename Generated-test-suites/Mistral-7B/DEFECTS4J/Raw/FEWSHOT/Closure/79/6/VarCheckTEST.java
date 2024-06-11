package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Token;

public class VarCheckTest {
  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    varCheck = new VarCheck(compiler);
    input = new CompilerInput("test.js", null, null);
    root = Node.root();
  }

  @Test
  public void testUndefinedVar() {
    input.setData("let x;");
    NodeTraversal.traverse(compiler, input, root, varCheck);
    assertEquals("JSC_UNDEFINED_VARIABLE", varCheck.getFirstError().getType().getName());
  }

  @Test
  public void testDuplicateVar() {
    input.setData("let x; let x;");
    NodeTraversal.traverse(compiler, input, root, varCheck);
    assertEquals("JSC_UNDEFINED_VARIABLE", varCheck.getFirstError().getType().getName());
  }

  @Test
  public void testNoDuplicateVar() {
    input.setData("let x; let y;");
    NodeTraversal.traverse(compiler, input, root, varCheck);
    assertNull(varCheck.getFirstError());
  }

  @Test
  public void testVarInSameFile() {
    input.setData("let x = 1; function foo() { let x = 2; }");
    NodeTraversal.traverse(compiler, input, root, varCheck);
    assertNull(varCheck.getFirstError());
  }

  @Test
  public void testModuleDependency() {
    JSModule moduleA = new JSModule("moduleA", Lists.newArrayList("moduleB.js"));
    JSModule moduleB = new JSModule("moduleB", Lists.newArrayList());
    CompilerInput inputA = new CompilerInput("moduleA.js", null, moduleA);
    CompilerInput inputB = new CompilerInput("moduleB.js", null, moduleB);
    inputA.setData("import foo from 'moduleB';");
    NodeTraversal.traverse(compiler, inputA, root, varCheck);
    assertFalse(varCheck.hasErrors());

    inputB.setData("export let foo = 1;");
    NodeTraversal.traverse(compiler, inputB, root, varCheck);
    assertFalse(varCheck.hasErrors());

    inputA.setData("let x = 1; function foo() { let x = 2; }");
    NodeTraversal.traverse(compiler, inputA, root, varCheck);
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", varCheck.getFirstError().getType().getName());
  }

  @Test
  public void testMissingModuleDependency() {
    JSModule moduleA = new JSModule("moduleA", Lists.newArrayList("missingModule.js"));
    JSModule moduleMissing = new JSModule("missingModule", Lists.newArrayList());
    CompilerInput inputA = new CompilerInput("moduleA.js", null, moduleA);
    CompilerInput inputMissing = new CompilerInput("missingModule.js", null, moduleMissing);
    inputA.setData("import foo from 'missingModule';");
    NodeTraversal.traverse(compiler, inputA, root, varCheck);
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", varCheck.getFirstError().getType().getName());
  }

  @Test
  public void testStrictModuleDependency() {
    JSModule moduleA = new JSModule("moduleA", Lists.newArrayList("moduleB.js"));
    JSModule moduleB = new JSModule("moduleB", Lists.newArrayList());
    CompilerInput inputA = new CompilerInput("moduleA.js", null, moduleA);
    CompilerInput inputB = new CompilerInput("moduleB.js", null, moduleB);
    inputA.setData("export let foo = 1;");
    inputB.setData("import foo from 'moduleA';");
    NodeTraversal.traverse(compiler, inputA, root, varCheck);
    assertFalse(varCheck.hasErrors());

    NodeTraversal.traverse(compiler, inputB, root, varCheck);
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", varCheck.getFirstError().getType().getName());
  }

  @Test
  public void testNameReferenceInExterns() {
    CompilerInput input = new CompilerInput("test.js", null, null);
    input.setData("//@extern extern function foo();");
    NodeTraversal.traverse(compiler, input, root, varCheck);
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", varCheck.getFirstError().getType().getName());
  }

  @Test
  public void testUndefinedExternalVar() {
    CompilerInput input = new CompilerInput("test.js", null, null);
    input.setData("//@extern extern let x;");
    NodeTraversal.traverse(compiler, input, root, varCheck);
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", varCheck.getFirstError().getType().getName());
  }

  @Test
  public void testEmptyNameFunctionDeclaration() {
    input.setData("function() {}");
    NodeTraversal.traverse(compiler, input, root, varCheck);
    assertEquals("JSC_INVALID_FUNCTION_DECL", varCheck.getFirstError().getType().getName());
  }

  private void assertEquals(String expectedErrorType, String actualErrorType) {
    assertSame(expectedErrorType, actualErrorType);
  }
}