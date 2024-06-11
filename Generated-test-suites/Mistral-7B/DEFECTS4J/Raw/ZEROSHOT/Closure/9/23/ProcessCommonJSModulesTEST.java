package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// input.js\n" +
        "var a = require('./a.js');\n" +
        "var b = require('./b.js');\n" +
        "exports.c = function() {\n" +
        "  return a.x + b.y;\n" +
        "};\n";

    String expectedOutput = "// input.js\n" +
        "goog.provide('module$input');\n" +
        "goog.require('module$a');\n" +
        "goog.require('module$b');\n" +
        "module$input.exports = function() {\n" +
        "  return module$a.module$exports.x + module$b.module$exports.y;\n" +
        "};\n" +
        "//# sourceURL=input.js";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node root = NodeOperations.parse(input);
    ProcessCommonJSModules processCommonJSModules =
        new ProcessCommonJSModules(compiler, ".", false);
    processCommonJSModules.process(null, root);

    String output = compiler.getDiagnostics().messages().get(0).getMessage();
    assertThat(output).isEqualTo(expectedOutput);
  }
}