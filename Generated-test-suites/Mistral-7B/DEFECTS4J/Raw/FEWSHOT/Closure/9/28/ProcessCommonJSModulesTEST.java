package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NodeTraversal.class})
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerInput input = new CompilerInput("input.js", null);
    String filenamePrefix = "test_";
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node root = IR.program();
    pass.process(input, root);

    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals("test_module$", module.getName());

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    Node moduleNode = traversal.findChildEmpPath("test_module$");
    assertNotNull(moduleNode);

    // Check that global variables have suffixes.
    Node globalVar = IR.getGlobal("oldVar");
    assertEquals("oldVar$$test_module$", globalVar.getString());

    Node moduleExports = moduleNode.getChildAtIndex(0);
    Node exports = moduleExports.getChildAtIndex(1);
    assertEquals("module$exports", exports.getString());
  }

  @Test
  public void testProcessCommonJSModulesWithRequire() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerInput input = new CompilerInput("input.js", null);
    String filenamePrefix = "test_";
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix, false);

    Node root = IR.program();
    root.addChildToFront(IR.script(IR.string("require('other.js');"), null));
    pass.process(input, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    Node requireCall = traversal.findChildEmpPath("require")[0];
    assertNotNull(requireCall);

    Node moduleRef = requireCall.getChildAtIndex(1);
    assertEquals("other$", moduleRef.getString());

    Node script = traversal.findChildEmpPath("other$")[0];
    assertNotNull(script);

    // Check that goog.require is emitted.
    Node googRequireCall = traversal.findChildEmpPath("goog.require")[0];
    assertNotNull(googRequireCall);
  }

  @Test
  public void testProcessCommonJSModulesNoModuleExports() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerInput input = new CompilerInput("input.js", null);
    String filenamePrefix = "test_";
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix, false);

    Node root = IR.program();
    root.addChildToFront(IR.script(IR.string("// No module.exports"), null));
    pass.process(input, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    Node moduleNode = traversal.findChildEmpPath("test_module$")[0];
    assertNull(moduleNode);
  }
}