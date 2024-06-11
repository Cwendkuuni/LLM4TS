package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerInput input = new CompilerInput("test.js", null, null);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, null);
    CompilerPass pass = new ProcessCommonJSModules(compiler, "");

    JSModule module = new JSModule("test");
    module.addAndOverrideModule(input);

    Node root = Node.externalCall("goog.require", IR.string("test"));
    Node result = pass.process(null, root).getChildAtIndex(0);

    assertNotNull(module.getModule$exports());
    assertEquals("test", module.getName());

    Node exports = result.getChildAtIndex(0);
    Node actualModuleExports = exports.getChildAtIndex(0);
    assertEquals("test", actualModuleExports.getString());

    Node expectedModuleExports = result.getChildAtIndex(1);
    Node actualGlobalVariables = expectedModuleExports.getChildAtIndex(0);
    assertEquals("test$$exports", actualGlobalVariables.getString());

    // Check that the original exports variable is still accessible
    Node originalExports = result.getChildAtIndex(2);
    assertEquals("exports", originalExports.getString());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    CompilerInput input1 = new CompilerInput("test1.js", null, null);
    CompilerInput input2 = new CompilerInput("test2.js", null, null);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, null);
    CompilerPass pass = new ProcessCommonJSModules(compiler, "");

    JSModule module1 = new JSModule("test1");
    module1.addAndOverrideModule(input1);

    JSModule module2 = new JSModule("test2");
    module2.addAndOverrideModule(input2);

    // test1.js
    Node test1Script = Node.script("var a = require('./test2');");
    module1.addChild(test1Script);

    Node root = Node.externalCall("goog.require", IR.string("test1"));
    Node result = pass.process(null, root).getChildAtIndex(0);

    // test2.js
    Node test2Script = Node.script("module.exports = {b: 2};");
    module2.addChild(test2Script);

    Node expectedModuleExports = result.getChildAtIndex(1);
    Node actualGlobalVariables = expectedModuleExports.getChildAtIndex(0);
    assertEquals("test1$$exports", actualGlobalVariables.getString());

    Node actualA = result.getChildAtIndex(2);
    assertEquals("test2", actualA.getString());

    Node actualB = actualA.getChildAtIndex(0);
    assertEquals(2, actualB.getInt());
  }
}