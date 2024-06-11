package com.google.javascript.jscomp.test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class InlineObjectLiteralsTest {

  public static void main(String[] args) {
    CompilerOptions options = new CompilerOptions(
        CompilationLevel.ADVANCED);
    Compiler compiler = new Compiler(options);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_var_");

    String originalCode = "" +
        "function Foo() {" +
        "  this.a = {b: 1, c: 2};" +
        "  this.d = 3;" +
        "  this.e = function() { return this.a.b + this.d; };" +
        "}" +
        "Foo.prototype.f = function() { return this.a.c; };";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    compiler.process(root, errorCollector);

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;
    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "" +
        "function Foo() {" +
        "  var a = {};" +
        "  a.b = 1;" +
        "  a.c = 2;" +
        "  this.a = a;" +
        "  this.d = 3;" +
        "  this.e = function() { return a.b + this.d; };" +
        "  this.f = function() { return a.c; };" +
        "}" +
        "Foo.prototype.f = function() { return a.c; };";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that an object literal with a single property is inlined correctly.
   */
  public static void testSinglePropertyObject() {
    String originalCode = "function Foo() { this.a = {b: 1}; }";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_single_property_object_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() { var a = {}; a.b = 1; this.a = a; }";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that an object literal with multiple properties is inlined correctly.
   */
  public static void testMultiplePropertiesObject() {
    String originalCode = "function Foo() { this.a = {b: 1, c: 2}; }";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_multiple_properties_object_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() {" +
        "  var a = {};" +
        "  a.b = 1;" +
        "  a.c = 2;" +
        "  this.a = a;" +
        "}";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that an object literal with a property that is itself an object
   * is inlined correctly.
   */
  public static void testNestedObject() {
    String originalCode = "function Foo() {" +
        "  this.a = {" +
        "    b: 1," +
        "    c: {" +
        "      d: 2" +
        "    }" +
        "  };" +
        "}";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_nested_object_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() {" +
        "  var a = {};" +
        "  a.b = 1;" +
        "  a.c = {" +
        "    d: 2" +
        "  };" +
        "  this.a = a;" +
        "}";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that the InlineObjectLiterals pass does not inline a declared
   * but undefined object literal.
   */
  public static void testUndefinedObjectLiteral() {
    String originalCode = "function Foo() {" +
        "  this.a = {};" +
        "}";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_undefined_object_literal_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    // The object literal should not be inlined.
    Node expectedResult = root;

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that the InlineObjectLiterals pass does not inline an object
   * literal if it is used as the callee of a function.
   */
  public static void testObjectLiteralAsFunctionCallee() {
    String originalCode = "function Foo() {" +
        "  this.a = function(x) { return x + this.b; };" +
        "  this.b = 1;" +
        "}";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_object_literal_as_function_callee_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() {" +
        "  this.a = function(x) { return x + this.b; };" +
        "  var a = {};" +
        "  a.b = 1;" +
        "  this.a.fn = function(x) { return x + a.b; };" +
        "}";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that the InlineObjectLiterals pass does not inline an object
   * literal if it is used as a getter.
   */
  public static void testObjectLiteralAsGetter() {
    String originalCode = "function Foo() {" +
        "  this.getA = function() { return this.a.b; };" +
        "  this.a = {b: 1};" +
        "}";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_object_literal_as_getter_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() {" +
        "  var a = {};" +
        "  a.b = 1;" +
        "  this.getA = function() { return a.b; };" +
        "}";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that the InlineObjectLiterals pass does not inline an object
   * literal if it is used as a setter.
   */
  public static void testObjectLiteralAsSetter() {
    String originalCode = "function Foo() {" +
        "  this.setA = function(x) { this.a.b = x; };" +
        "  this.a = {b: 1};" +
        "}";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_object_literal_as_setter_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() {" +
        "  this.setA = function(x) { var a = {}; a.b = x; this.a = a; };" +
        "  this.a = {}" +
        "}";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that the InlineObjectLiterals pass does not inline an object
   * literal if it is used in a for statement.
   */
  public static void testObjectLiteralInForStatement() {
    String originalCode = "function Foo() {" +
        "  for (var i in this.a) {" +
        "    // Do something with i." +
        "  }" +
        "  this.a = {b: 1, c: 2};" +
        "}";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_object_literal_in_for_statement_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() {" +
        "  var a = {};" +
        "  a.b = 1;" +
        "  a.c = 2;" +
        "  for (var i in a) {" +
        "    // Do something with i." +
        "  }" +
        "  this.a = a;" +
        "}";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }

  /**
   * Test that the InlineObjectLiterals pass does not inline an object
   * literal if it is used in a loop condition.
   */
  public static void testObjectLiteralInLoopCondition() {
    String originalCode = "function Foo() {" +
        "  for (; this.a.b && this.a.c.d;) {" +
        "    // Do something in the loop body." +
        "  }" +
        "  this.a = {b: true, c: {d: true}};" +
        "}";

    Node root = Node.parse(originalCode);

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler(new CompilerOptions(
        CompilationLevel.ADVANCED));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "JSCompiler_inline_test_object_literal_in_loop_condition_var_");

    List<CompilerPass> passes = Lists.newArrayList(inlineObjectLiterals);

    Node result = Node.NULL;

    for (CompilerPass pass : passes) {
      pass.process(root, result);
    }

    String expectedCode = "function Foo() {" +
        "  var a = {};" +
        "  a.b = true;" +
        "  a.c = {d: true};" +
        "  for (; a.b && a.c.d;) {" +
        "    // Do something in the loop body." +
        "  }" +
        "  this.a = a;" +
        "}";

    Node expectedResult = Node.parse(expectedCode);

    NodeUtil.assertNodesEqual(
        "Unexpected code after InlineObjectLiterals pass",
        expectedResult, result);
  }
}