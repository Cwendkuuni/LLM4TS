package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

import java.util.Set;

public class ProcessCommonJSModulesTest extends CompilerPassTestCase {

  public ProcessCommonJSModulesTest() {
    super();
  }

  @Override
  protected CompilerPass getProcessor() {
    return new ProcessCommonJSModules(compiler, "test_", true);
  }

  public void testSimpleModule() throws Exception {
    String input = "var m = require('module');\n" +
        "m.exports = 1;\n";
    String expected = "var module$m = require('module$test_module');\n" +
        "goog.provide('test_module');\n" +
        "goog.module.exports = 1;\n";
    testRoundtrip(input, expected);
  }

  public void testModuleWithRequire() throws Exception {
    String input = "var m = require('module1');\n" +
        "m.exports = require('module2');\n";
    String expected = "var module$m = require('module$test_module1');\n" +
        "goog.provide('test_module1');\n" +
        "goog.module.exports = function() {\n" +
        "  return require('module$test_module2');\n" +
        "};\n" +
        "goog.require('module$test_module2');\n";
    testRoundtrip(input, expected);
  }

  public void testModuleWithModuleExports() throws Exception {
    String input = "var m = require('module1');\n" +
        "m.exports.foo = 1;\n";
    String expected = "var module$m = require('module$test_module1');\n" +
        "goog.provide('test_module1');\n" +
        "goog.module.exports = {\n" +
        "  foo: 1\n" +
        "};\n";
    testRoundtrip(input, expected);
  }

  public void testModuleWithRequireAndModuleExports() throws Exception {
    String input = "var m = require('module1');\n" +
        "m.exports.foo = require('module2');\n";
    String expected = "var module$m = require('module$test_module1');\n" +
        "goog.provide('test_module1');\n" +
        "goog.module.exports = {\n" +
        "  foo: function() {\n" +
        "    return require('module$test_module2');\n" +
        "  }\n" +
        "};\n" +
        "goog.require('module$test_module2');\n";
    testRoundtrip(input, expected);
  }

  public void testModuleWithMultipleScripts() throws Exception {
    String input = "var m1 = require('module1');\n" +
        "m1.exports = 1;\n" +
        "\n" +
        "var m2 = require('module2');\n" +
        "m2.exports = 2;\n";
    String expected = "var module$m1 = require('module$test_module1');\n" +
        "goog.provide('test_module1');\n" +
        "goog.module.exports = 1;\n" +
        "\n" +
        "var module$m2 = require('module$test_module2');\n" +
        "goog.provide('test_module2');\n" +
        "goog.module.exports = 2;\n";
    testRoundtrip(input, expected);
  }

  public void testModuleWithRelativeRequire() throws Exception {
    String input = "var m = require('./module1');\n" +
        "m.exports = 1;\n" +
        "require('module2');\n";
    String expected = "var module$m = require('module$test_module1');\n" +
        "goog.provide('test_module1');\n" +
        "goog.module.exports = 1;\n" +
        "require('module$test_module2');\n";
    testRoundtrip(input, expected);
  }

  public void testModuleWithRelativeRequireAndAbsoluteRequire() throws Exception {
    String input = "var m1 = require('./module1');\n" +
        "m1.exports = 1;\n" +
        "require('module2.js');\n";
    String expected = "var module$m1 = require('module$test_module1');\n" +
        "goog.provide('test_module1');\n" +
        "goog.module.exports = 1;\n" +
        "require('module$test_module2');\n";
    testRoundtrip(input, expected);
  }

  public void testModuleWithRequireCycle() throws Exception {
    String input = "var m1 = require('module1');\n" +
        "m1.exports = require('module2');\n" +
        "\n" +
        "var m2 = require('module2');\n" +
        "m2.exports = require('module1');\n";
    String errorMessage = "Error: Circular require dependency: module1, " +
        "module2";
    try {
      testRoundtrip(input, null);
      fail("Expected compiler error for circular require dependency");
    } catch (AssertionError e) {
      assertEquals(errorMessage, e.getMessage());
    }
  }

  public void testModuleWithDuplicateRequires() throws Exception {
    String input = "var m1 = require('module1');\n" +
        "m1.exports = require('module1');\n";
    String errorMessage = "Error: Duplicate require for 'module1'";
    try {
      testRoundtrip(input, null);
      fail("Expected compiler error for duplicate require");
    } catch (AssertionError e) {
      assertEquals(errorMessage, e.getMessage());
    }
  }

  public void testModuleWithMissingRequire() throws Exception {
    String input = "var m = require('module1');\n" +
        "m.exports = 1;\n" +
        "require('module2'); // missing require statement\n";
    String expected = "var module$m = require('module$test_module1');\n" +
        "goog.provide('test_module1');\n" +
        "goog.module.exports = 1;\n";
    testRoundtrip(input, expected);
  }

  private void testRoundtrip(String input, String expectedOutput) throws Exception {
    testRoundtrip(input, expectedOutput, null);
  }

  private void testRoundtrip(String input, String expectedOutput,
      String sourceName) throws Exception {
    ProcessCommonJSModules processor = (ProcessCommonJSModules) getProcessor();
    Node ast = parse(input, sourceName);
    processor.process(null, ast);
    assertCompiledEqualsExpectedOutput(getSourceFileName(sourceName),
        getPrintedCode(ast));
    if (expectedOutput != null) {
      assertCompiledEqualsExpectedSource(getSourceFileName(sourceName),
          ast, expectedOutput);
    }
  }

  private String getSourceFileName(String sourceName) {
    if (sourceName == null) {
      return "input.js";
    }
    return sourceName;
  }
}