package org.apache.commons.cli;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    assertEquals("module$foo", processor.guessCJSModuleName("foo.js"));
    assertEquals("module$bar/baz.js", processor.guessCJSModuleName("bar/baz.js"));
    assertEquals("module$bar-baz.js", processor.guessCJSModuleName("bar-baz.js"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$foo", ProcessCommonJSModules.toModuleName("foo.js"));
    assertEquals("module$bar/baz.js", ProcessCommonJSModules.toModuleName("bar/baz.js"));
    assertEquals("module$bar-baz.js", ProcessCommonJSModules.toModuleName("bar-baz.js"));
    assertEquals("module$./foo.js", ProcessCommonJSModules.toModuleName("./foo.js"));
    assertEquals("module$../bar/baz.js", ProcessCommonJSModules.toModuleName("../bar/baz.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals("foo.js", ProcessCommonJSModules.normalizeSourceName("foo.js"));
    assertEquals("bar/baz.js", ProcessCommonJSModules.normalizeSourceName("bar/baz.js"));
    assertEquals(".", ProcessCommonJSModules.normalizeSourceName("."));
  }

  @Test
  public void testProcessCommonJSModulesCallback_visitRequireCall() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();
    NodeTraversal traversal = NodeTraversal.forTree(root);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    callback.visit(NodeTraversal.forTree(root), IR.call(IR.getprop(IR.name("require"),
        IR.string("foo")), null), root);

    assertEquals(1, callback.scriptNodeCount);
    assertEquals("module$foo", input.requireMap.get("foo"));

    NodeModule module = (NodeModule) input.modules.get(0);
    assertEquals("test.js", module.getSourceFileName());
    assertEquals("module$foo", module.getName());
    assertNotSame(module, module.getParentModule());
  }

  @Test
  public void testProcessCommonJSModulesCallback_visitScript() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();
    NodeTraversal traversal = NodeTraversal.forTree(root);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    Node script = IR.scriptNode("var foo = require('foo')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    callback.visit(NodeTraversal.forTree(root), script, root);

    assertEquals(1, callback.scriptNodeCount);
    assertEquals("module$foo", input.requireMap.get("foo"));

    NodeModule module = (NodeModule) input.modules.get(0);
    assertEquals("test.js", module.getSourceFileName());
    assertEquals("module$foo", module.getName());
    assertNotSame(module, module.getParentModule());
  }

  @Test
  public void testProcessCommonJSModulesCallback_visitModuleExports() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();
    NodeTraversal traversal = NodeTraversal.forTree(root);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    Node script = IR.scriptNode("var module = {}; module.exports = {}; var foo = require('foo')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    callback.process(new CompilerInput("externs.js", "externs.js"), root);

    NodeModule module = (NodeModule) input.modules.get(0);
    assertEquals("test.js", module.getSourceFileName());
    assertEquals("module$foo", module.getName());
    assertNotSame(module, module.getParentModule());

    Node exports = IR.getprop(module, IR.string("exports"));
    assertEquals("module$foo.exports", exports.getString());
  }

  @Test
  public void testSuffixVarsCallback() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node root = IR.programNode();
    NodeTraversal traversal = NodeTraversal.forTree(root);

    ProcessCommonJSModules.SuffixVarsCallback suffixVarsCallback =
        new ProcessCommonJSModules.SuffixVarsCallback("foo");

    suffixVarsCallback.visit(NodeTraversal.forTree(root), IR.name("foo"), root);

    Node module = IR.name("module$$foo");
    assertEquals(module, root.getChildAtIndex(0));
  }

  @Test
  public void testProcessCommonJSModules_visit() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = IR.programNode();

    Node script1 = IR.scriptNode("var foo = require('foo')", null);
    script1.setSourceFileName("test.js");
    root.addChildToFront(script1);

    Node script2 = IR.scriptNode("var bar = require('bar')", null);
    script2.setSourceFileName("test.js");
    root.addChildToFront(script2);

    Node script3 = IR.scriptNode("var baz = require('baz')", null);
    script3.setSourceFileName("test.js");
    root.addChildToFront(script3);

    processor.process(input, root);

    NodeModule module1 = (NodeModule) input.modules.get(0);
    assertEquals("test.js", module1.getSourceFileName());
    assertEquals("module$foo", module1.getName());
    assertNotSame(module1, module1.getParentModule());

    NodeModule module2 = (NodeModule) input.modules.get(1);
    assertEquals("test.js", module2.getSourceFileName());
    assertEquals("module$bar", module2.getName());
    assertNotSame(module1, module2.getParentModule());

    NodeModule module3 = (NodeModule) input.modules.get(2);
    assertEquals("test.js", module3.getSourceFileName());
    assertEquals("module$baz", module3.getName());
    assertNotSame(module1, module3.getParentModule());

    assertEquals("module$foo.exports", input.exportsMap.get("foo"));
    assertEquals("module$bar.exports", input.exportsMap.get("bar"));
    assertEquals("module$baz.exports", input.exportsMap.get("baz"));
  }

  @Test
  public void testProcessCommonJSModules_emitOptionalModuleExportsOverride() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = IR.programNode();

    Node script = IR.scriptNode("var foo = require('foo')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    Node moduleExports = IR.getprop(IR.name("module$foo"), IR.string("exports"));
    root.addChildToBack(moduleExports);

    processor.process(input, root);

    assertEquals("module$foo.exports", input.exportsMap.get("foo"));
    assertEquals("module$foo", input.requireMap.get("foo"));
  }

  @Test
  public void testProcessCommonJSModules_emitOptionalModuleExportsOverride_noModuleExports() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = IR.programNode();

    Node script = IR.scriptNode("var foo = require('foo')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    processor.process(input, root);

    assertEquals(null, input.exportsMap.get("foo"));
    assertEquals("module$foo", input.requireMap.get("foo"));
  }

  @Test
  public void testProcessCommonJSModules_reportDependencies() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "test");

    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();

    Node script = IR.scriptNode("var foo = require('foo')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    processor.process(input, root);

    assertEquals("test/foo.js", input.requireMap.get("foo"));
    assertEquals("test", input.filenamePrefix);
  }

  @Test
  public void testProcessCommonJSModules_reportDependencies_noEntryPoint() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();

    Node script = IR.scriptNode("require('foo')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    processor.process(input, root);

    assertNull(input.requireMap.get("foo"));
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_normalizeSourceName_uriSyntaxException() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();

    Node script = IR.scriptNode("require('./../test2/test.js')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    processor.process(input, root);
  }

  @Test
  public void testProcessCommonJSModules_visitRequireCall_noModule() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();

    Node script = IR.scriptNode("require('foo')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    callback.visit(NodeTraversal.forTree(root), script, root);

    assertNull(input.requireMap.get("foo"));
  }

  @Test
  public void testProcessCommonJSModules_visitRequireCall_invalidModuleName() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();

    Node script = IR.scriptNode("require('foo/bar/baz')", null);
    script.setSourceFileName("test.js");
    root.addChildToFront(script);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    callback.visit(NodeTraversal.forTree(root), script, root);

    assertNull(input.requireMap.get("foo/bar/baz"));
  }

  @Test
  public void testProcessCommonJSModules_visitRequireCall_cyclicDependency() {
    AbstractCompiler compiler = new CompilerPassContext().getCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");

    Node root = IR.programNode();

    Node moduleA = IR.scriptNode("require('./testB')", null);
    moduleA.setSourceFileName("testA.js");
    root.addChildToFront(moduleA);

    Node moduleB = IR.scriptNode("require('./testA')", null);
    moduleB.setSourceFileName("testB.js");
    root.addChildToFront(moduleB);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    callback.visit(NodeTraversal.forTree(root), moduleA, root);

    assertEquals("testB.js", input.requireMap.get("testA"));
    assertEquals("testA.js", input.requireMap.get("testB"));
  }
}