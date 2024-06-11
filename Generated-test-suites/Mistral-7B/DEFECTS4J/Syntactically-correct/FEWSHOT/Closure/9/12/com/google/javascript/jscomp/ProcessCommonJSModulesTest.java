package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.test.testcases.TestCompiler;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {
  @Test
  public void testNormalizeSourceName() {
    String filename = "src/commonjs/module1.js";
    String expected = "commonjs/module1";
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, ".");
    String result = pcjm.normalizeSourceName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testToModuleName() {
    String filename = "src/commonjs/module1.js";
    String expected = "module$commonjs_module1";
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, ".");
    String result = pcjm.toModuleName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "src/commonjs/module1.js";
    String expected = "module$commonjs_module1";
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, ".");
    String result = pcjm.guessCJSModuleName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testToModuleNameRelative() {
    String currentFilename = "src/commonjs/module2.js";
    String requiredFilename = "module1.js";
    String expected = "module$commonjs_module1";
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, ".");
    String result = pcjm.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);
  }

  @Test
  public void testProcess() throws Exception {
    CompilerInput input = TestCompiler.getInputFromString(
        "function main() {\n" +
        "  require('module1');\n" +
        "  module.exports = 'Hello World';\n" +
        "}\n" +
        "// module1.js\n" +
        "var x = 1;\n" +
        "module.exports = function() {\n" +
        "  return x;\n" +
        "};",
        "module1.js");

    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "");
    pcjm.process(input.getExterns(), input.getRootNode());

    NodeTraversal nt = new NodeTraversal(input.getRootNode());
    Node main = nt.findFunctionByName("main");

    Node requireCall = main.getFirstChild().getFirstChild();
    Node requireModule = requireCall.getChildAtIndex(1);
    String moduleName = requireModule.getString();
    String expectedModuleName = "module$";

    assertEquals(expectedModuleName + "commonjs_module1", moduleName);

    Node requireNode = main.getChildAtIndex(1);
    Node moduleExports = requireNode.getChildAtIndex(1);
    String actualModuleName = moduleExports.getQualifiedName();
    String expectedModuleName2 = "module$commonjs_module1";

    assertEquals(expectedModuleName2, actualModuleName);
  }

  @Test
  public void testProcessWithReportDependencies() throws Exception {
    CompilerInput input = TestCompiler.getInputFromString(
        "function main() {\n" +
        "  require('module1');\n" +
        "  module.exports = 'Hello World';\n" +
        "}\n" +
        "// module1.js\n" +
        "var x = 1;\n" +
        "module.exports = function() {\n" +
        "  return x;\n" +
        "};",
        "module1.js");

    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "");
    pcjm.reportDependencies = true;
    CompilerInput expected = new CompilerInput("", null, null, null, null);

    expected.addProvide("module$commonjs_module1");
    expected.addRequire("module$commonjs_module1");

    pcjm.process(input.getExterns(), input.getRootNode());

    NodeTraversal nt = new NodeTraversal(input.getRootNode());
    Node main = nt.findFunctionByName("main");

    Node requireCall = main.getFirstChild().getFirstChild();
    Node requireModule = requireCall.getChildAtIndex(1);
    String moduleName = requireModule.getString();
    String expectedModuleName = "module$commonjs_module1";

    assertEquals(expectedModuleName, moduleName);

    Node requiredModule = nt.getInput().getProvidedModule("module$commonjs_module1");
    Node expectedScript = requiredModule.getFirstChild();
    Node actualScript = main.getChildAtIndex(1);

    assertEquals(expectedScript.getSourceFileName(), actualScript.getSourceFileName());
    assertEquals(expectedScript.getChildCount(), actualScript.getChildCount());

    Node expectedModuleExports = expectedScript.getChildAtIndex(1);
    Node actualModuleExports = actualScript.getChildAtIndex(1);

    assertEquals(expectedModuleExports.getSourceFileName(), actualModuleExports.getSourceFileName());
    assertEquals(expectedModuleExports.getChildCount(), actualModuleExports.getChildCount());
    assertEquals(expectedModuleExports.getString(), actualModuleExports.getString());
  }
}