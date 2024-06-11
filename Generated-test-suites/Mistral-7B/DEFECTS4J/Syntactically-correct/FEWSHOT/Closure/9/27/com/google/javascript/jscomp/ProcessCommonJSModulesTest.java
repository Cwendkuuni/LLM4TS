package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.ProcessCommonJSModules.toModuleName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// Input file.\n" +
        "require('./a.js');\n" +
        "function foo() {\n" +
        "  alert('Hello, World!');\n" +
        "}\n" +
        "\n" +
        "// a.js file.\n" +
        "module.exports = {\n" +
        "  'exports': function() {\n" +
        "    alert('Hello, a.js!');\n" +
        "  }\n" +
        "};\n";

    String expectedOutput = "// Input file.\n" +
        "goog.require('a');\n" +
        "function foo() {\n" +
        "  alert('Hello, World!');\n" +
        "}\n" +
        "\n" +
        "// a.js file.\n" +
        "var a$exports = (function() {\n" +
        "  function a$exports() {\n" +
        "    alert('Hello, a.js!');\n" +
        "  }\n" +
        "  return this; // Enable Closure Compiler to set 'this'.\n" +
        "})();\n" +
        "goog.provide('a');\n";

    CompilerInput inputNode = Processor.parseOneFile(input, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new AbstractCompiler(), "");
    pass.process(inputNode, null);
    String output = Processor.emitCode(inputNode, null);
    assertEquals(expectedOutput, output);

    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals("a", module.getName());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequirements()
      throws Exception {
    String input = "// Input file.\n" +
        "require('./a/b.js'); // Requires b.js from a/ directory.\n" +
        "require('../c.js'); // Requires c.js from parent directory.\n";

    String expectedOutput = "// Input file.\n" +
        "goog.require('c');\n" +
        "goog.require('a.b');\n";

    CompilerInput inputNode = Processor.parseOneFile(input, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new AbstractCompiler(), "");
    pass.process(inputNode, null);
    String output = Processor.emitCode(inputNode, null);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() throws Exception {
    String input = "// Input file.\n" +
        "module.exports = require('./a.js').exports;\n";

    String expectedOutput = "// Input file.\n" +
        "var a$exports = (function() {\n" +
        "  function a$exports() {\n" +
        "  }\n" +
        "  return this; // Enable Closure Compiler to set 'this'.\n" +
        "})();\n" +
        "goog.provide('a');\n" +
        "goog.require('a');\n" +
        "goog.exportSymbol('exports', a$exports.exports, 'a');\n";

    CompilerInput inputNode = Processor.parseOneFile(input, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new AbstractCompiler(), "");
    pass.process(inputNode, null);
    String output = Processor.emitCode(inputNode, null);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithModulesWithSameName()
      throws Exception {
    String input = "// a.js\n" +
        "module.exports = {\n" +
        "  'exports': function() {\n" +
        "    alert('Hello, a.js 1!');\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// b.js\n" +
        "module.exports = {\n" +
        "  'exports': function() {\n" +
        "    alert('Hello, b.js 1!');\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Input file.\n" +
        "require('./a.js').exports(); // Calls a.js\n" +
        "require('./b.js').exports(); // Calls b.js\n";

    String expectedOutput = "// a.js\n" +
        "var a$exports = (function() {\n" +
        "  function a$exports() {\n" +
        "    alert('Hello, a.js 1!');\n" +
        "  }\n" +
        "  return this; // Enable Closure Compiler to set 'this'.\n" +
        "})();\n" +
        "goog.provide('a');\n" +
        "\n" +
        "// b.js\n" +
        "var b$exports = (function() {\n" +
        "  function b$exports() {\n" +
        "    alert('Hello, b.js 1!');\n" +
        "  }\n" +
        "  return this; // Enable Closure Compiler to set 'this'.\n" +
        "})();\n" +
        "goog.provide('b');\n" +
        "\n" +
        "// Input file.\n" +
        "goog.require('a');\n" +
        "goog.require('b');\n" +
        "a.exports(); // Calls a.js\n" +
        "b.exports(); // Calls b.js\n";

    CompilerInput inputNode = Processor.parseTwoFiles("a.js", input, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new AbstractCompiler(), "");
    pass.process(inputNode, null);
    String output = Processor.emitCode(inputNode, null);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithDifferentFilenames()
      throws Exception {
    String input = "// a.js\n" +
        "module.exports = {\n" +
        "  'exports': function() {\n" +
        "    alert('Hello, a.js 1!');\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// b.js\n" +
        "module.exports = {\n" +
        "  'exports': function() {\n" +
        "    alert('Hello, b.js 1!');\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Input file.\n" +
        "require('./a.js').exports(); // Calls a.js\n" +
        "require('../b.js').exports(); // Calls b.js from parent directory\n";

    String expectedOutput = "// a.js\n" +
        "var a$exports = (function() {\n" +
        "  function a$exports() {\n" +
        "    alert('Hello, a.js 1!');\n" +
        "  }\n" +
        "  return this; // Enable Closure Compiler to set 'this'.\n" +
        "})();\n" +
        "goog.provide('a');\n" +
        "\n" +
        "// b.js\n" +
        "var b$exports = (function() {\n" +
        "  function b$exports() {\n" +
        "    alert('Hello, b.js 1!');\n" +
        "  }\n" +
        "  return this; // Enable Closure Compiler to set 'this'.\n" +
        "})();\n" +
        "goog.provide('b');\n" +
        "\n" +
        "// Input file.\n" +
        "goog.require('a');\n" +
        "goog.require('b');\n" +
        "a.exports(); // Calls a.js\n" +
        "b.exports(); // Calls b.js\n";

    CompilerInput inputNode = Processor.parseTwoFiles("a.js", "b.js", input);
    CompilerPass pass = new ProcessCommonJSModules(new AbstractCompiler(), "");
    pass.process(inputNode, null);
    String output = Processor.emitCode(inputNode, null);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithDifferentSourceNames()
      throws Exception {
    String input = "// a.js\n" +
        "module.exports = {\n" +
        "  'exports': function() {\n" +
        "    alert('Hello, a.js 1!');\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Input file.\n" +
        "require('./a.js').exports(); // Calls a.js\n";

    String expectedOutput = "// a.js\n" +
        "var a$exports = (function() {\n" +
        "  function a$exports() {\n" +
        "    alert('Hello, a.js 1!');\n" +
        "  }\n" +
        "  return this; // Enable Closure Compiler to set 'this'.\n" +
        "})();\n" +
        "goog.provide('a');\n" +
        "\n" +
        "// Input file.\n" +
        "goog.require('a');\n" +
        "a.exports(); // Calls a.js\n";

    CompilerInput inputNode = Processor.parseTwoFiles("input.js", "a.js", null);
    CompilerPass pass = new ProcessCommonJSModules(new AbstractCompiler(), "");
    pass.process(inputNode, null);
    String output = Processor.emitCode(inputNode, null);
    assertEquals(expectedOutput, output);
  }
}