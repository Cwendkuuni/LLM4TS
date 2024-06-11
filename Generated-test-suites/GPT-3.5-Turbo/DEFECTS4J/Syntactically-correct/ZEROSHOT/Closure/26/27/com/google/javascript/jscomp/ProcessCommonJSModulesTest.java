package com.google.javascript.jscomp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {
  
  private ProcessCommonJSModules processCommonJSModules;
  
  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), "");
  }
  
  @After
  public void tearDown() {
    processCommonJSModules = null;
  }
  
  @Test
  public void testGuessCJSModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String result = processCommonJSModules.guessCJSModuleName(filename);
    assertEquals(expected, result);
  }
  
  @Test
  public void testToModuleName() {
    String filename = "test/file.js";
    String expected = "module$test$file";
    String result = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, result);
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./file.js";
    String currentFilename = "test.js";
    String expected = "module$test$file";
    String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressingParent() {
    String requiredFilename = "../file.js";
    String currentFilename = "test/test.js";
    String expected = "module$test$file";
    String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);
  }
  
  @Test
  public void testNormalizeSourceName() {
    String filename = "test/file.js";
    String expected = "file.js";
    String result = processCommonJSModules.normalizeSourceName(filename);
    assertEquals(expected, result);
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node();
    Node root = new Node();
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), "");
    processCommonJSModules.process(externs, root);
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
    callback.visit(t, n, parent);
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackVisitRequireCall() {
    NodeTraversal t = new NodeTraversal();
    Node require = new Node();
    Node parent = new Node();
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
    callback.visitRequireCall(t, require, parent);
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackVisitScript() {
    NodeTraversal t = new NodeTraversal();
    Node script = new Node();
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
    callback.visitScript(t, script);
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackEmitOptionalModuleExportsOverride() {
    Node script = new Node();
    String moduleName = "module$test";
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
    callback.emitOptionalModuleExportsOverride(script, moduleName);
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackVisitModuleExports() {
    Node prop = new Node();
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
    callback.visitModuleExports(prop);
  }
  
  @Test
  public void testSuffixVarsCallbackVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback("test");
    callback.visit(t, n, parent);
  }
}