package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collections;
import java.util.List;

public class DevirtualizePrototypeMethodsTest extends CompilerPassTestBase {

  @Override
  protected List<CompilerPass> getCompilerPasses() {
    return ImmutableList.of(new DevirtualizePrototypeMethods(compiler));
  }

  @Override
  protected void assertCompilesToExpected(String input, String expectedOutput) {
    // Your custom assertions here
  }

  public void testDevirtualizePrototypeMethods_SimpleMethod() {
    String input = "function A() {} A.prototype.foo = function(a) { return a; }";
    String expectedOutput = "var foo = function(self, a) { return a; }";

    String actualOutput = compile(input);

    assertTrue("Expected: " + expectedOutput, actualOutput.contains(expectedOutput));
  }

  public void testDevirtualizePrototypeMethods_MultipleMethods() {
    String input = "function A() {} A.prototype.foo = function(a) { return a; }; A.prototype.bar = function(a, b) { return a + b; }";
    String expectedOutput = "var foo = function(self, a) { return a; }; var bar = function(self, a, b) { return a + b; }";

    String actualOutput = compile(input);

    assertTrue("Expected: " + expectedOutput, actualOutput.contains(expectedOutput));
  }

  public void testDevirtualizePrototypeMethods_FunctionWithVarArgs() {
    String input = "function A() {} A.prototype.foo = function() { return arguments.length; }";
    // Below output is just for illustration purposes,
    // as it is not possible to test it with JUnit 4.
    // String expectedOutput = "var foo = function(self, argsArray) { return argsArray.length; }";

    String actualOutput = compile(input);

    assertFalse("Expected failure, as function with varargs cannot be devirtualized.", actualOutput.contains("JSCompiler_StaticMethods_"));
  }

  public void testDevirtualizePrototypeMethods_MethodAccessInControlStructure() {
    String input = "for (var i = 0; A.prototype.foo(i); i++) {}";
    String expectedOutput = "for (var i = 0; foo(A, i); i++) {}";

    String actualOutput = compile(input);

    assertTrue("Expected: " + expectedOutput, actualOutput.contains(expectedOutput));
  }

  public void testDevirtualizePrototypeMethods_MethodAccessOutsideFunctionCallContext() {
    String input = "A.prototype.foo();";
    String expectedOutput = "foo(A);";

    String actualOutput = compile(input);

    assertTrue("Expected: " + expectedOutput, actualOutput.contains(expectedOutput));
  }

  public void testDevirtualizePrototypeMethods_MethodAccessInModuleLoadedBeforeDefinition() {
    String input1 = "A.prototype.foo = function() {}; module1.foo();";
    String input2 = "var module1 = { foo: function() {} };";

    String expectedOutput = "module1.foo = function(self) {}; module1.foo();";

    String actualOutput = compile(input1, input2);

    assertTrue("Expected: " + expectedOutput, actualOutput.contains(expectedOutput));
  }

  public void testDevirtualizePrototypeMethods_MultipleDefinitions() {
    String input = "function A() {} A.prototype.foo = function() {}; A.prototype.foo = function() {};";

    String actualOutput = compile(input);

    assertFalse("Expected failure, as there should be only one definition for a given method.", actualOutput.contains("JSCompiler_StaticMethods_foo"));
  }

  public void testDevirtualizePrototypeMethods_MethodAccessInModuleLoadedAfterDefinition() {
    String input1 = "A.prototype.foo = function() {}; module1.foo();";
    String input2 = "var module1 = { foo: function() {} };";

    String expectedOutput = "A.prototype.foo = function(self) {}; module1.foo();";

    String actualOutput = compile(input1, input2);

    assertTrue("Expected: " + expectedOutput, actualOutput.contains(expectedOutput));
  }

  public void testDevirtualizePrototypeMethods_MethodAccessInCyclicDependency() {
    String input1 = "A.prototype.foo = function() { B.prototype.bar(); };";
    String input2 = "B.prototype.bar = function() { A.prototype.foo(); };";

    String expectedOutput = "A.prototype.foo = function(self) { B.prototype.bar(self); };";
    String expectedOutput2 = "B.prototype.bar = function(self) { A.prototype.foo(self); };";

    String actualOutput = compile(input1, input2);

    assertTrue("Expected: " + expectedOutput, actualOutput.contains(expectedOutput));
    assertTrue("Expected: " + expectedOutput2, actualOutput.contains(expectedOutput2));
  }
}