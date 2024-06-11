package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.rhino.IR;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String filename = "input.js";
    String inputCode = "require('module1');\n" +
        "function foo() {\n" +
        "  require('module2');\n" +
        "  exports.bar = 42;\n" +
        "}\n" +
        "exports.foo = foo;\n" +
        "module.exports = exports;\n";

    String expectedOutput = "goog.provide('module1');\n" +
        "var module1 = module.exports;\n" +
        "goog.require('module2');\n" +
        "function module1_foo() {\n" +
        "  module1.module2 = module2;\n" +
        "  module1.exports.bar = 42;\n" +
        "}\n" +
        "module1.exports.foo = module1_foo;\n" +
        "module.exports = module1;\n" +
        "goog.provide('module2');\n" +
        "var module2 = module.exports;\n" +
        "module1_foo.call(null);\n";

    CompilerInput input = new CompilerInput(new SourceFile(filename, inputCode));
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filename);
    processor.process(input.getRootNode(), input.getRootNode());

    String outputCode = IR.toJavaScript(input.getRootNode());
    assertEquals(expectedOutput, outputCode);

    JSModule module = processor.getModule();
    assertNotNull(module);
  }

  @Test
  public void testToModuleName() {
    String filename = "module1.js";
    String expected = "module1$";
    String result = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, result);

    filename = "module/submodule.js";
    expected = "module$submodule$";
    result = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, result);

    filename = "module1/submodule.js";
    expected = "module1$submodule$";
    result = ProcessCommonJSModules.toModuleName(filename, "module1.js");
    assertEquals(expected, result);
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "module1.js";
    String expected = "module1$";
    String result = new ProcessCommonJSModules(null, filename).guessCJSModuleName(filename);
    assertEquals(expected, result);

    filename = "module/submodule.js";
    expected = "module$submodule$";
    result = new ProcessCommonJSModules(null, filename).guessCJSModuleName(filename);
    assertEquals(expected, result);

    filename = "module1/submodule.js";
    expected = "module1$submodule$";
    result = new ProcessCommonJSModules(null, filename).guessCJSModuleName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testToModuleNameWithRelativePath() {
    String currentFilename = "input.js";
    String requiredFilename = "module1.js";
    String expected = "module1$";
    String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);

    currentFilename = "module/input.js";
    requiredFilename = "../module1.js";
    expected = "module1$";
    result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);

    currentFilename = "module/input.js";
    requiredFilename = "module/submodule.js";
    expected = "module$submodule$";
    result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "input.js";
    String expected = "input";
    String result = new ProcessCommonJSModules(null, filename).normalizeSourceName(filename);
    assertEquals(expected, result);

    filename = "input" + ProcessCommonJSModules.MODULE_SLASH + "module.js";
    expected = "inputmodule";
    result = new ProcessCommonJSModules(null, filename).normalizeSourceName(filename);
    assertEquals(expected, result);
  }
}