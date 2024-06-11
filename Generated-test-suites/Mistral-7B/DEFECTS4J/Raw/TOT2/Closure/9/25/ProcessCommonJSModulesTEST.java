package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.ProcessCommonJSModules.toModuleName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions.OutputFileAsModule;
import com.google.javascript.jscomp.JSCompiler;
import com.google.javascript.jscomp.JSFile;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.SuffixVarsCallback;

import org.junit.BeforeClass;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  private static AbstractCompiler compiler;
  private static CompilerInput input;

  @BeforeClass
  public static void setUp() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setCompilationLevel(CompilationLevel.SIMPLE);
    options.setOutputFileAsModule(OutputFileAsModule.NONE);
    JSCompiler compiler = new JSCompiler();
    compiler.setOptions(options);
    compiler.addPass(new ProcessCommonJSModules(compiler, "."));
    compiler.addPass(new SuffixVarsCallback("test"));
    input = new CompilerInput("test.js", "test.js");
  }

  @Test
  public void testProcessCommonJSModules() {
    Node root = JSFile.parseTree("test.js", getClass().getResourceAsStream("/test.js"));
    CompilerInput ci = new CompilerInput("test.js", "test-output.js");
    compiler.process(input, root);
    JSModule module = new JSModule("test");
    JSFile testOutput = new JSFile("test-output.js");
    assertEquals(testOutput.getText(), module.getCode());
  }

  @Test
  public void testProcessRelativeRequire() {
    String testFile = "test.js";
    String requireFile = "relative/require.js";
    String expectedOutput = "module$require('relative/require')";

    Node root = JSFile.parseTree(testFile, getClass().getResourceAsStream("/" + testFile));
    Node requireCall = root.findFirstChildByName("require")
        .getFirstChild();
    String requiredModule = requireCall.getChildAtIndex(1).getString();

    String actualOutput = processCommonJSModules(testFile, requireFile);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessAbsoluteRequire() {
    String testFile = "test.js";
    String requireFile = "/absolute/require.js";
    String expectedOutput = "require('absolute/require')";

    Node root = JSFile.parseTree(testFile, getClass().getResourceAsStream("/" + testFile));
    Node requireCall = root.findFirstChildByName("require")
        .getFirstChild();
    String requiredModule = requireCall.getChildAtIndex(1).getString();

    String actualOutput = processCommonJSModules(testFile, requireFile);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessModuleExports() {
    String testFile = "test.js";
    String moduleFile = "module.js";

    Node root = JSFile.parseTree(testFile, getClass().getResourceAsStream("/" + testFile));
    root.getChildAtIndex(0).setString("var test = require('module')");

    String actualOutput = processCommonJSModules(testFile, moduleFile);
    String expectedOutput = "module$exports.test";
    assertEquals(expectedOutput, actualOutput);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessInvalidURI() {
    String testFile = "test.js";
    String requireFile = "data:text/plain,invalid";

    Node root = JSFile.parseTree(testFile, getClass().getResourceAsStream("/" + testFile));
    Node requireCall = root.findFirstChildByName("require")
        .getFirstChild();
    String requiredModule = requireCall.getChildAtIndex(1).getString();

    String actualOutput = processCommonJSModules(testFile, requireFile);
    fail("Expected RuntimeException but got: " + actualOutput);
  }

  @Test
  public void testProcessEmptyFile() {
    String testFile = "test.js";

    Node root = JSFile.parseTree(testFile, getClass().getResourceAsStream("/" + testFile));
    String actualOutput = processCommonJSModules(testFile, null);
    assertNull(actualOutput);
  }

  private String processCommonJSModules(String testFile, String moduleFile) {
    CompilerInput input = new CompilerInput(testFile, "test-output.js");
    Node root = JSFile.parseTree(testFile, getClass().getResourceAsStream("/" + testFile));
    compiler.process(input, root);
    if (moduleFile != null) {
      Node requireCall = root.findFirstChildByName("require")
          .getFirstChild();
      String requiredModule = requireCall.getChildAtIndex(1).getString();
      String actualOutput = processCommonJSModules(requiredModule, null);
      return actualOutput;
    }
    JSFile output = new JSFile("test-output.js");
    return output.getText();
  }
}