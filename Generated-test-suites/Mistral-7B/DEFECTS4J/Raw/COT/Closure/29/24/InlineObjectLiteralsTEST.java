package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Rewriter;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerOptions options;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    options = new CompilerOptions();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, options.getSafeNameIdSupplier());
  }

  @Test
  public void testInlineObjectLiteral() {
    String sourceCode = "function f() { var o = {a: 1, b: 2}; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0, b = JSCompiler_object_inline_b_0;" +
            "o = {};" +
            "a = 1;" +
            "b = 2;" +
            "o.a = a;" +
            "o.b = b;" +
            "return o;" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithInitialValues() {
    String sourceCode = "function f() { var o = {a: 1, b: 2}; o.c = 3; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0, b = JSCompiler_object_inline_b_0, c = JSCompiler_object_inline_c_0;" +
            "o = {};" +
            "a = 1;" +
            "b = 2;" +
            "c = 3;" +
            "o.a = a;" +
            "o.b = b;" +
            "o.c = c;" +
            "return o;" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    String sourceCode = "function f() { var o = {a: {b: o}}; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0, o = JSCompiler_object_inline_o_0;" +
            "o = {};" +
            "a = {b: o};" +
            "o.a = a;" +
            "return o;" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithFunctionProperty() {
    String sourceCode = "function f() { var o = {a: function() {}}; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    // This test should fail because InlineObjectLiterals does not support
    // inlining function properties.
  }

  @Test
  public void testInlineObjectLiteralWithMultipleOccurrences() {
    String sourceCode = "function f() { var o1 = {a: 1}, o2 = {a: 2}; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "function f() {" +
            "var a_1 = JSCompiler_object_inline_a_1_0, a_2 = JSCompiler_object_inline_a_2_0;" +
            "var o1 = {}, o2 = {};" +
            "a_1 = 1;" +
            "o1.a = a_1;" +
            "a_2 = 2;" +
            "o2.a = a_2;" +
            "return o1;" +
            "return o2;" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithCyclicReference() {
    String sourceCode = "function f() { var o1 = {a: {b: o1}}; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    // This test should not fail because InlineObjectLiterals does not follow
    // cyclic references.
  }

  @Test
  public void testInlineObjectLiteralWithUsedAsFunctionContext() {
    String sourceCode = "function f(o) { o.m(); }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "function f(o) {" +
            "o = {m: function() {}};" +
            "o.m();" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithUsedAsObjectInitializer() {
    String sourceCode = "function f() { return {a: 1}; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0;" +
            "return {" +
                "a: a" +
            "};" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {
    String sourceCode = "exports.o = {a: 1};";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "exports.o = {" +
            "a: JSCompiler_object_inline_a_0" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithExternVariable() {
    String sourceCode = "var o = {a: 1};";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(sourceCode);

    assertEquals("InlineObjectLiterals should not modify extern variables",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithGlobalVariable() {
    String sourceCode = "var o = {a: 1};";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(sourceCode);

    assertEquals("InlineObjectLiterals should not modify global variables",
        expectedRoot, compiler.getRootNode());
  }

  @Test
  public void testInlineObjectLiteralWithVariableAssignment() {
    String sourceCode = "function f() { var o = {}; o = {a: 1}; }";

    Node root = nodeFactory.parseTree(sourceCode);
    inlineObjectLiterals.process(null, root);

    Node expectedRoot = nodeFactory.parseTree(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0;" +
            "var o = {};" +
            "o.a = a;" +
            "return o;" +
        "}");

    assertEquals("InlineObjectLiterals processed incorrectly",
        expectedRoot, compiler.getRootNode());
  }
}