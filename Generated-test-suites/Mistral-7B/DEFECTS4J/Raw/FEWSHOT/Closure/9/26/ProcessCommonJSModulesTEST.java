package org.apache.commons.cli.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerInput input = new CompilerInput("test.js", "test.js");
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, null);
    JSModule module = processor.getModule();
    assertNotNull(module);
    Set<String> dependencies = module.getDependencies();
    assertNotNull(dependencies);
    assertFalse(dependencies.isEmpty());
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "/path/to/module.js";
    String expected = "module$module_js";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitRequireCall() {
    NodeTraversal traversal = new NodeTraversal();
    Node root = new Node();
    Node require = new NodeCall("require", new NodeString("module"));
    root.addChild(require);
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    traversal.traverse(new Compiler(), root, callback);
    Node moduleRef = root.getChild(0);
    assertNotNull(moduleRef);
    assertEquals("module$module", moduleRef.getQualifiedName());
  }

  @Test
  public void testVisitScript() {
    NodeTraversal traversal = new NodeTraversal();
    Node root = new NodeScript();
    root.setSourceFileName("path/to/module.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");
    processor.process(null, root);
    JSModule module = processor.getModule();
    assertNotNull(module);
    String moduleName = module.getName();
    assertEquals("module$module_js", moduleName);
    Node script = root.getChild(0);
    assertEquals(1, script.getChildCount());
    Node moduleVar = script.getChild(0);
    assertEquals("module$module_js", moduleVar.getQualifiedName());
    assertEquals(2, script.getChildCount());
    Node provideCall = script.getChild(1);
    assertEquals("goog.provide", provideCall.getFirstChild().getQualifiedName());
    Node provideString = provideCall.getChildAtIndex(1);
    assertEquals("module$module_js", provideString.getString());
  }

  @Test
  public void testVisitModuleExports() {
    Node script = new NodeScript();
    script.setSourceFileName("path/to/module.js");
    Node module = new NodeName("module");
    Node exports = new NodeName("exports");
    Node exportsAssignment = new NodeAssign(module, exports);
    script.addChild(module);
    script.addChild(exportsAssignment);
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal();
    traversal.traverse(new Compiler(), script, callback);
    assertEquals("module$exports", script.getChildAtIndex(1).getQualifiedName());
  }

  @Test
  public void testSuffixVarsCallback() {
    Node root = new NodeScript();
    Node variable1 = new NodeName("foo");
    Node variable2 = new NodeName("bar");
    Node variable3 = new NodeName("baz");
    root.addChild(variable1);
    root.addChild(variable2);
    root.addChild(variable3);
    ProcessCommonJSModules.SuffixVarsCallback suffixVarsCallback =
        new ProcessCommonJSModules.SuffixVarsCallback("suffix");
    NodeTraversal traversal = new NodeTraversal();
    traversal.traverse(new Compiler(), root, suffixVarsCallback);
    assertEquals("foo$$suffix", variable1.getQualifiedName());
    assertEquals("bar$$suffix", variable2.getQualifiedName());
    assertEquals("baz$$suffix", variable3.getQualifiedName());
  }

  @Test
  public void testVisitModuleExportsOverride() {
    Node script = new NodeScript();
    Node module = new NodeName("module");
    Node exports = new NodeName("exports");
    Node exportsAssignment = new NodeAssign(exports, IR.nullNode());
    Node moduleExportsProp = new NodeGetProp(module, "module$exports");
    Node ifNode = new NodeIfNode(moduleExportsProp, new NodeBlock(), null);
    Node assignNode = new NodeAssign(module, exports);
    Node block = new NodeBlock();
    block.addChild(assignNode);
    script.addChild(ifNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal();
    traversal.traverse(new Compiler(), script, callback);

    assertEquals("module$module_exports", script.getChildAtIndex(1).getQualifiedName());
  }
}