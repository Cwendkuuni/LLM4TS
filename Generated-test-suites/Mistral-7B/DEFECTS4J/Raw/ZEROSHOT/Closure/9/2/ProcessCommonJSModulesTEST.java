package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.processing.JSErrorReporter;
import com.google.javascript.rhino.IR;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  public static void main(String[] args) throws Exception {
    testProcessCommonJSModules();
  }

  public static void testProcessCommonJSModules() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Sets.newHashSet(new CompilerPass[] {
      new ProcessCommonJSModules(new TestAbstractCompiler(), ".")
    }));

    CompilerInput input = new CompilerInput();
    input.setFileName("test.js");
    input.setContent(
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      console.log('bar');\n" +
        "    } \n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "require('./module');\n" +
        "module.exports = require('./module').exports;\n" +
        "\n" +
        "function test() {\n" +
        "  module.exports.foo();\n" +
        "}\n" +
        "\n" +
        "module.exports = test;\n" +
        "\n" +
        "// module.js\n" +
        "exports.foo = function() {\n" +
        "  console.log('baz');\n" +
        "};\n" +
        "");

    AbstractCompiler compiler = new TestAbstractCompiler(options);
    JSErrorReporter errorReporter = new JSErrorReporter();
    compiler.setErrorReporter(errorReporter);

    CompilerPass processor = new ProcessCommonJSModules(compiler, ".");
    processor.process(input, null);

    Node rootNode = input.getRootNode();
    NodeTraversal traversal = NodeTraversal.create(rootNode, compiler);
    Node module = traversal.getModule();

    assertEquals("test", module.getQualifiedName());

    Node fooFunction = traversal.get("test.exports.foo");
    assertNull(errorReporter.getErrors().isEmpty());
    assertEquals(1, errorReporter.getErrors().size());
    assertEquals("ReferenceError: module is not defined", errorReporter.getErrors().get(0).getMessage());
    assertEquals("test.exports.foo", fooFunction.getQualifiedName());

    Node testFunction = traversal.get("test");
    assertEquals("test", testFunction.getQualifiedName());
    assertEquals("function test() {\n  " +
        "module.exports.foo();\n}\n", testFunction.toString());

    Node fooCall = traversal.get("test.foo");
    assertEquals("test.foo", fooCall.getQualifiedName());
    assertEquals("console.log('baz');", fooCall.toString());

    Node testModule = traversal.get("module");
    assertEquals("module", testModule.getQualifiedName());
    assertEquals("{\n  exports: {\n    foo: function() {\n      console.log('baz');\n    }\n  }\n}", testModule.toString());

    Node exports = traversal.get("module.exports");
    assertEquals("module.exports", exports.getQualifiedName());
    assertEquals("test", exports.getChildAtIndex(0).getQualifiedName());

    Node exportsFoo = traversal.get("module.exports.foo");
    assertEquals("module.exports.foo", exportsFoo.getQualifiedName());
    assertEquals("function () {\n  console.log('baz');\n}", exportsFoo.toString());

    Node testCall = traversal.get("test()");
    assertEquals("test()", testCall.getQualifiedName());
    assertEquals("test()", testCall.toString());
  }

  static class TestAbstractCompiler extends AbstractCompiler {

    TestAbstractCompiler(CompilerOptions options) {
      super(options);
    }

    @Override
    public void process(CompilerInput input, Node root) throws Exception {
      super.process(input, root);
      if (getErrors().isEmpty()) {
        printResultToStandardOutput(getOutputFile(), getRootNode());
      }
    }
  }
}