package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Sets;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.NodeMatcher;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations class.
 */
public class CollapseVariableDeclarationsTest {
  private static final NodeFactory NODE_FACTORY = new NodeFactory();

  @Test
  public void testCollapseSingleVariable() {
    String input = "function foo() { var a; }";
    String expectedOutput = "function foo() { var a; }";
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseMultipleVariables() {
    String input = "function foo() { var a; var b; }";
    String expectedOutput = "function foo() { var a, b; }";
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseMultipleVariablesWithAssignments() {
    String input = "function foo() { var a = 1; var b = 2; }";
    String expectedOutput = "function foo() { var a = 1, b = 2; }";
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithSameName() {
    String input = "function foo() { var a; var a; }";
    String expectedOutput = "function foo() { var a; }"; // Only one declaration should remain
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinCondition() {
    String input = "function foo() { if (true) { var a; } }";
    String expectedOutput = "function foo() { if (true) { var a; } }";
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinIfElse() {
    String input = "function foo() { if (true) { var a; } else { var a; } }";
    String expectedOutput = "function foo() { if (true) { var a; } }"; // Only one declaration should remain
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinIfElseIf() {
    String input = "function foo() { if (true) { var a; } else if (true) { var a; } }";
    String expectedOutput = "function foo() { if (true) { var a; } }"; // Only one declaration should remain
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinLoop() {
    String input = "function foo() { for (var i = 0; i < 10; i++) { var a; } }";
    String expectedOutput = "function foo() { for (var i = 0, a; i < 10; i++) { } }";
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinFunctionDeclaration() {
    String input = "function foo(x) { function bar() { var a; } }";
    String expectedOutput = "function foo(x) { function bar() { var a; } }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinFunctionCall() {
    String input = "function foo() { bar(); } function bar() { var a; }";
    String expectedOutput = "function foo() { bar(); } function bar() { var a; }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinClassDeclaration() {
    String input = "class Foo { constructor() { var a; } }";
    String expectedOutput = "class Foo { constructor() { var a; } }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinClassExpression() {
    String input = "const Foo = class { constructor() { var a; } }";
    String expectedOutput = "const Foo = class { constructor() { var a; } }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinClassMethod() {
    String input = "class Foo { method() { var a; } }";
    String expectedOutput = "class Foo { method() { var a; } }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinStaticClassMethod() {
    String input = "class Foo { static method() { var a; } }";
    String expectedOutput = "class Foo { static method() { var a; } }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinFunctionExpression() {
    String input = "function foo() { return (function() { var a; })(); }";
    String expectedOutput = "function foo() { return (function() { var a; })(); }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinMethodExpression() {
    String input =
        "function foo() { return (function bar() {})(); } function bar() { var a; }";
    String expectedOutput = "function foo() { return (function bar() {})(); } function bar() { var a; }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinBlock() {
    String input = "function foo() { { var a; var b; } }";
    String expectedOutput = "function foo() { { var a, b; } }";
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinBlockWithAssignments() {
    String input = "function foo() { { var a = 1; var b = 2; } }";
    String expectedOutput = "function foo() { { var a = 1, b = 2; } }";
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinBlockWithFunctions() {
    String input = "function foo() { { function a() {} function b() {} } }";
    String expectedOutput = "function foo() { { function a() {}, function b() {} } }"; // No change expected
    testCollapse(input, expectedOutput);
  }

  @Test
  public void testCollapseVariablesWithinBlockWithVariableDeclarations() {
    String input = "function foo() { { var a; var b; var a; } }";
    String expectedOutput = "function foo() { { var a, b; } }";
    testCollapse(input, expectedOutput);
  }

  private void testCollapse(String input, String expectedOutput) {
    String testInput = "function foo() {" + input + " }";
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new Compiler(options);
    Node root = Parser.parseCompilationUnit(testInput, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    String output = Printer.printCompilationUnit(root);
    checkState(output.equals(expectedOutput), "Expected output doesn't match\nInput:\n%s\nExpected:\n%s\nOutput:\n%s",
        input, expectedOutput, output);
  }
}