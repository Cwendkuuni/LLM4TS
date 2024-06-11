package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  private CompilerInput input;
  private CompilerOptions options;
  private NodeFactory nodeFactory;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    nodeFactory = new NodeFactory();
    input = new CompilerInput("test.js");
  }

  @Test
  public void testProcessCommonJSModules_SimpleModule() throws Exception {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new CompilerPassAdapter(input, options), "");
    processor.process(input, null);

    Node root = input.getRootNode();
    Node expectedRoot = nodeFactory.createScript(
        "goog.provide('module1');\n" +
        "module1.module$exports = {'foo': 'bar'};\n");
    assertEquals(expectedRoot, root);
  }

  @Test
  public void testProcessCommonJSModules_ModuleWithDependency()
      throws Exception {
    String dependencies = "module2";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new CompilerPassAdapter(input, options), "", false);
    input.setInputFileSearchPath(Sets.newHashSet("/test"));
    input.setSourceFileName("test.js");
    input.addRequire(dependencies);

    Node depModuleScript = nodeFactory.createScript("goog.provide('module2');");
    Node depModuleExports = nodeFactory.createGetProp(
        nodeFactory.createName("module2"), "module$exports");
    Node depModuleExportsValue = nodeFactory.createObjectLit();
    depModuleExportsValue.setProperty("foo", nodeFactory.createString("baz"));
    depModuleExports.setProperty("value", depModuleExportsValue);

    Node requireCall = nodeFactory.createCall(
        nodeFactory.createGetprop(nodeFactory.createName("require"), "require"),
        nodeFactory.createString(dependencies));
    Node moduleScript = nodeFactory.createScript(
        "goog.provide('module1');\n" +
        "module1.module$exports = require('" + dependencies + "');\n");
    Node requireStatement = nodeFactory.createCall(
        nodeFactory.createName("require"),
        nodeFactory.createArray(nodeFactory.createString(dependencies)));

    Node expectedRoot = nodeFactory.createScript(
        "goog.provide('module1');\n" +
        "module1.module$exports = {\n" +
        "  'foo': 'baz'\n" +
        "};\n" +
        "goog.require('module1');\n" +
        "goog.provide('module2');\n" +
        "module2.module$exports = {\n" +
        "  'foo': 'baz'\n" +
        "};\n");

    input.addCompilationUnit(depModuleScript);
    input.addCompilationUnit(requireCall);
    input.addCompilationUnit(moduleScript);
    input.addCompilationUnit(nodeFactory.createCall(
        nodeFactory.createName("goog.require"), nodeFactory.createArray(
            nodeFactory.createName("module1"))));

    processor.process(input, null);

    Node root = input.getRootNode();
    assertEquals(expectedRoot, root);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_URISyntaxException()
      throws Exception {
    String filename = "test%2Ftest.js";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new CompilerPassAdapter(input, options), "");
    input.setSourceFileName(filename);

    Node script = nodeFactory.createScript("require('test.js');");
    Node requireCall = nodeFactory.createCall(
        nodeFactory.createName("require"), nodeFactory.createString("test.js"));

    input.addCompilationUnit(script);
    input.addCompilationUnit(requireCall);

    processor.process(input, null);
  }

  @Test(expected = JSError.class)
  public void testProcessCommonJSModules_MissingDependency()
      throws Exception {
    String dependencies = "missingModule";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new CompilerPassAdapter(input, options), "");
    input.setInputFileSearchPath(Sets.newHashSet("/test"));
    input.setSourceFileName("test.js");
    input.addRequire(dependencies);

    Node requireCall = nodeFactory.createCall(
        nodeFactory.createName("require"),
        nodeFactory.createArray(nodeFactory.createString(dependencies)));

    Node expectedError = nodeFactory.createCall(
        nodeFactory.createName("JSError"),
        Set.of(nodeFactory.createString("Module not found: missingModule")));

    input.addCompilationUnit(requireCall);

    processor.process(input, null);
  }
}