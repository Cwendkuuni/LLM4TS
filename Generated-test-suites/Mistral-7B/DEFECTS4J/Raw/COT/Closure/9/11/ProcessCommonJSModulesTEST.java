package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Provider;
import com.google.javascript.rhino.Nodeops;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerPass processCommonJSModules;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.init();

    processCommonJSModules = new ProcessCommonJSModules(compiler, "");
  }

  @Test
  public void testProcess() {
    rootNode = Nodeops.parseTree("function main() { require('module1'); }");
    compiler.process(rootNode, null);
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) compiler.getPassManager().getPass(
            ProcessCommonJSModules.class);
    processCommonJSModules.process(null, rootNode);

    assertEquals("module1", processCommonJSModules.getModule().getName());
  }

  @Test(expected = RuntimeException.class)
  public void testUriSyntaxException() {
    rootNode = Nodeops.parseTree("require('././test.js')");
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) new ProcessCommonJSModules(compiler, "");
    processCommonJSModules.process(null, rootNode);
  }

  @Test
  public void testRequireCall() {
    rootNode = Nodeops.parseTree("function main() { require('module1'); }");
    compiler.process(rootNode, null);
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) compiler.getPassManager().getPass(
            ProcessCommonJSModules.class);
    Node requireCallNode = processCommonJSModules.getModule().getCallNode();
    assertNotNull(requireCallNode);
    Node moduleRefNode = requireCallNode.getChildAtIndex(1);
    assertEquals("module1", moduleRefNode.getString());
  }

  @Test
  public void testModuleExports() {
    rootNode = Nodeops.parseTree("exports.foo = 'bar'");
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) new ProcessCommonJSModules(compiler, "");
    processCommonJSModules.process(null, rootNode);
    Node moduleExportsNode = processCommonJSModules.getModule().getChild();
    Node exportsNode = moduleExportsNode.getChildAtIndex(0);
    Node fooNode = exportsNode.getChildAtIndex(0);
    assertEquals("foo", fooNode.getString());
    assertEquals("bar", fooNode.getFirstChild().getString());
  }

  @Test
  public void testScriptNode() {
    rootNode = Nodeops.parseTree("// module: module1\nfunction main() { console.log('hello'); }");
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) new ProcessCommonJSModules(compiler, "", false);
    processCommonJSModules.process(null, rootNode);
    assertEquals("module1", processCommonJSModules.getModule().getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModuleName() {
    rootNode = Nodeops.parseTree("// module: module:module");
    new ProcessCommonJSModules(compiler, "", false).process(null, rootNode);
  }

  @Test
  public void testProvideStatement() {
    rootNode = Nodeops.parseTree("// module: module1\n" +
        "goog.provide('module1');\n" +
        "function main() { console.log('hello'); }");
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) new ProcessCommonJSModules(compiler, "", false).process(null, rootNode);
    assertEquals("module1", processCommonJSModules.getModule().getName());
  }

  @Test
  public void testMultipleModuleInSameFile() {
    rootNode = Nodeops.parseTree("// module: module1\n" +
        "goog.provide('module1');\n" +
        "function module1() {}\n" +
        "// module: module2\n" +
        "goog.provide('module2');\n" +
        "function module2() {}\n" +
        "function main() { module1(); module2(); }");
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) new ProcessCommonJSModules(compiler, "", false).process(null, rootNode);
    JSModule module1 = processCommonJSModules.getModule();
    JSModule module2 = processCommonJSModules.getModule();
    assertNotSame(module1, module2);
    assertEquals("module1", module1.getName());
    assertEquals("module2", module2.getName());
  }

  @Test(expected = AssertionError.class)
  public void testRequireCircularDependency() {
    rootNode = Nodeops.parseTree("function module1() { require('./module2'); }" +
        "function module2() { require('./module1'); }");
    new ProcessCommonJSModules(compiler, "", false).process(null, rootNode);
  }

  @Test
  public void testRequireRelativePath() {
    rootNode = Nodeops.parseTree("function main() { require('./module1'); }");
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) new ProcessCommonJSModules(compiler, "test.js", false).process(null, rootNode);
    String expectedModuleName = "test.js/module1";
    assertEquals(expectedModuleName, processCommonJSModules.getModule().getName());
  }

  @Test
  public void testRequireAbsolutePath() {
    rootNode = Nodeops.parseTree("function main() { require('/path/to/module1'); }");
    ProcessCommonJSModules processCommonJSModules =
        (ProcessCommonJSModules) new ProcessCommonJSModules(compiler, "", false).process(null, rootNode);
    String expectedModuleName = "/path/to/module1";
    assertEquals(expectedModuleName, processCommonJSModules.getModule().getName());
  }
}

class JSModule {
  private String name;
  private Node callNode;
  private Node moduleExports;

  JSModule(String name) {
    this.name = name;
  }

  String getName() {
    return name;
  }

  Node getCallNode() {
    return callNode;
  }

  Node getChild() {
    if (moduleExports == null) {
      Node exportsNode = new Node(Nodeops.SCOPE);
      moduleExports = new Node(Nodeops.OBJECT_LITERAL, exportsNode);
      callNode.addChildToFront(moduleExports);
    }
    return moduleExports;
  }

  void addAndOverrideModule(CompilerInput ci) {
    Provider provider = ci.getProvider(name);
    if (provider != null) {
      provider.replaceWith(this);
    }
  }
}