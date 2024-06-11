package com.google.javascript.jscomp.test.inlineobjectliterals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class InlineObjectLiteralsTest {

  private static final String TEST_CASE =
      "function Foo() {\n" +
      "  this.bar = {\n" +
      "    baz: function() {\n" +
      "      alert('hello');\n" +
      "    }\n" +
      "  };\n" +
      "}\n" +
      "\n" +
      "Foo.prototype.qux = function() {\n" +
      "  this.bar.baz();\n" +
      "};\n";

  @org.junit.Test
  public void testInlineObjectLiterals() throws Exception {
    String sourceCode = TEST_CASE;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    Node expected = NodeFactory.instance().parseTree(
        "function Foo() {\n" +
        "  var bar_baz = function() {\n" +
        "    alert('hello');\n" +
        "  };\n" +
        "  this.bar_baz = bar_baz;\n" +
        "  var bar_baz_this = this;\n" +
        "  var bar = {\n" +
        "    baz: bar_baz\n" +
        "  };\n" +
        "  this.bar = bar;\n" +
        "  this.bar_baz = bar_baz;\n" +
        "  var bar_this = this;\n" +
        "  this.qux = function() {\n" +
        "    bar_this.bar_baz();\n" +
        "  };\n" +
        "}\n");

    Node result = node;
    for (CompilerPass pass : passes) {
      result = pass.getProcessedTree(node);
    }

    NodeUtil.deepEquals(expected, result);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithSelfReference() throws Exception {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = {\n" +
        "    baz: this\n" +
        "  };\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    String expectedErrorMessage =
        "InlineObjectLiterals: self-referential object literal assignments are not allowed.";

    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(expectedErrorMessage);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithCircularReference() throws Exception {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = {\n" +
        "    baz: this.qux\n" +
        "  };\n" +
        "  this.qux = function() {\n" +
        "    return this.bar.baz;\n" +
        "  };\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    String expectedErrorMessage =
        "InlineObjectLiterals: circular object literal references are not allowed.";

    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(expectedErrorMessage);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithEmptyObjectLiteral() throws Exception {
    String sourceCode = "function Foo() { this.bar = {}; }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    Node expected = NodeFactory.instance().parseTree(
        "function Foo() {\n" +
        "  var bar = {};\n" +
        "  this.bar = bar;\n" +
        "}");

    Node result = node;
    for (CompilerPass pass : passes) {
      result = pass.getProcessedTree(node);
    }

    NodeUtil.deepEquals(expected, result);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithFunctionAsValue() throws Exception {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = function() {\n" +
        "    return 'hello';\n" +
        "  };\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    Node expected = NodeFactory.instance().parseTree(
        "function Foo() {\n" +
        "  var bar = function() {\n" +
        "    return 'hello';\n" +
        "  };\n" +
        "  this.bar = bar;\n" +
        "}");

    Node result = node;
    for (CompilerPass pass : passes) {
      result = pass.getProcessedTree(node);
    }

    NodeUtil.deepEquals(expected, result);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithNestedObjects() throws Exception {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = {\n" +
        "    baz: {\n" +
        "      qux: 1\n" +
        "    }\n" +
        "  };\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    Node expected = NodeFactory.instance().parseTree(
        "function Foo() {\n" +
        "  var bar = {\n" +
        "    baz = {\n" +
        "      qux: 1\n" +
        "    }\n" +
        "  };\n" +
        "  this.bar = bar;\n" +
        "}");

    Node result = node;
    for (CompilerPass pass : passes) {
      result = pass.getProcessedTree(node);
    }

    NodeUtil.deepEquals(expected, result);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithObjectWithFunctionAsKey() throws Exception {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = {\n" +
        "    'qux': function() {\n" +
        "      return 'hello';\n" +
        "    }\n" +
        "  };\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    Node expected = NodeFactory.instance().parseTree(
        "function Foo() {\n" +
        "  var bar = {\n" +
        "    'qux': function qux() {\n" +
        "      return 'hello';\n" +
        "    }\n" +
        "  };\n" +
        "  this.bar = bar;\n" +
        "}");

    Node result = node;
    for (CompilerPass pass : passes) {
      result = pass.getProcessedTree(node);
    }

    NodeUtil.deepEquals(expected, result);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithObjectWithFunctionMethods() throws Exception {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = {\n" +
        "    baz: function() {\n" +
        "      return 'hello';\n" +
        "    }\n" +
        "  };\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.qux = function() {\n" +
        "  return this.bar.baz();\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackFrames(false);
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);

    List<CompilerPass> passes = Lists.newArrayList();
    passes.add(new InlineObjectLiterals(compiler, NodeFactory.instance()));

    Node node = compiler.parseTree(sourceCode, sourceCode, options);
    for (CompilerPass pass : passes) {
      pass.process(null, node);
    }

    Node expected = NodeFactory.instance().parseTree(
        "function Foo() {\n" +
        "  var bar_baz = function() {\n" +
        "    return 'hello';\n" +
        "  };\n" +
        "  this.bar = bar_baz;\n" +
        "  var bar_baz_this = this;\n" +
        "  this.bar_baz = bar_baz;\n" +
        "  var bar = {\n" +
        "    baz: bar_baz\n" +
        "  };\n" +
        "  this.bar = bar;\n" +
        "  this.bar_baz = bar_baz;\n" +
        "  var bar_this = this;\n" +
        "  this.qux = function() {\n" +
        "    return bar_this.bar_baz();\n" +
        "  };\n" +
        "}");

    Node result = node;
    for (CompilerPass pass : passes) {
      result = pass.getProcessedTree(node);
    }

    NodeUtil.deepEquals(expected, result);
  }
}