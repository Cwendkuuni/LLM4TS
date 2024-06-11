package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckSideEffects.USELESS_CODE_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.jscomp.compiler.CompilerInput;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeOperations;

import java.util.List;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);
    CheckSideEffects sideEffectsChecker = new CheckSideEffects(compiler, CheckLevel.ERROR, false);

    String sourceCode = "function foo() { return 1; } " +
        "var x = 2; " +
        "foo(); x = 3; " +
        "var unused = 4; " +
        "unused = foo(); " +
        "var s = \"this string is \"; " +
        "s += \"continued on the next line but you forgot the +\"; " +
        "var n = 1 + 1; " +
        "var a = [1, 2]; " +
        "var b = a; " +
        "var c = a.slice(); " +
        "for (var i = 0; i < 10; i++) {" +
        "  var j = i; " +
        "} " +
        "function bar() {" +
        "  // do something " +
        "  return 42;" +
        "} " +
        "var z = [1]; " +
        "z[0] = function() { return 42; }; ";

    CompilerInput input = new CompilerInput(sourceCode, "test.js");
    Node root = compiler.parse(input, null);
    NodeTraversal.traverse(compiler, root, sideEffectsChecker);

    assertEquals(0, sideEffectsChecker.problemNodes.size());

    sourceCode = "function foo() { return 1; } " +
        "var x = 2; " +
        "x = foo(); " +
        "var unused = 4; " +
        "unused = foo(); " +
        "var s = \"this string is \"; " +
        "s += \"continued on the next line but you forgot the +\"; " +
        "var n = 1 + 1; " +
        "var a = [1, 2]; " +
        "var b = a; " +
        "var c = a.slice(); " +
        "for (var i = 0; i < 10; i++) {" +
        "  var j = i; " +
        "} " +
        "function bar() {" +
        "  // do something " +
        "} " +
        "var z = [1]; " +
        "z[0] = function() { return 42; }; ";

    input = new CompilerInput(sourceCode, "test.js");
    root = compiler.parse(input, null);
    NodeTraversal.traverse(compiler, root, sideEffectsChecker);

    assertEquals(1, sideEffectsChecker.problemNodes.size());
    Node problemNode = sideEffectsChecker.problemNodes.get(0);
    assertEquals("function bar() {", problemNode.toString());

    // Test the reportCodeChange() method
    sideEffectsChecker.protectSideEffects();
    assertEquals(1, sideEffectsChecker.problemNodes.size());
    problemNode = sideEffectsChecker.problemNodes.get(0);
    assertEquals("JSCOMPILER_PRESERVE(function bar() {", problemNode.toString());

    // Test the StripProtection class
    StripProtection stripProtection = new StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripProtection);
    assertEquals(1, sideEffectsChecker.problemNodes.size());
    problemNode = sideEffectsChecker.problemNodes.get(0);
    assertNull(problemNode);

    // Test the remove side-effect sync functions case
    sourceCode = "function foo() { return 1; } " +
        "function bar() {" +
        "  // do something " +
        "  return foo(); " +
        "}";

    input = new CompilerInput(sourceCode, "test.js");
    root = compiler.parse(input, null);
    NodeTraversal.traverse(compiler, root, sideEffectsChecker);
    assertEquals(1, sideEffectsChecker.problemNodes.size());
    problemNode = sideEffectsChecker.problemNodes.get(0);
    assertEquals("function bar() {", problemNode.toString());

    input = new CompilerInput(sourceCode, "test.js");
    root = compiler.parse(input, null);
    NodeTraversal.traverse(compiler, root, sideEffectsChecker);
    NodeTraversal.traverse(compiler, root, stripProtection);
    assertEquals(0, sideEffectsChecker.problemNodes.size());

    // Test the protectSideEffects() method with side-effect free statements
    sourceCode = "function foo() { return 1; } " +
        "var x = 2; " +
        "x = foo(); ";

    input = new CompilerInput(sourceCode, "test.js");
    root = compiler.parse(input, null);
    sideEffectsChecker = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    NodeTraversal.traverse(compiler, root, sideEffectsChecker);
    assertEquals(0, sideEffectsChecker.problemNodes.size());

    input = new CompilerInput(sourceCode, "test.js");
    root = compiler.parse(input, null);
    sideEffectsChecker.protectSideEffects();
    assertEquals(1, sideEffectsChecker.problemNodes.size());
    problemNode = sideEffectsChecker.problemNodes.get(0);
    assertEquals("JSCOMPILER_PRESERVE(x =)", problemNode.toString());
  }
}