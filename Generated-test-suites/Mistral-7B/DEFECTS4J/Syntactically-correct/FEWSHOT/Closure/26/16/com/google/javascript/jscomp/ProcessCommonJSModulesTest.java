package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = new CompilerPassAdapter();
    String filenamePrefix = "test_";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    CompilerInput input = new CompilerInput();
    input.setSourceFileName("main.js");
    Node root = IR.program();

    Node script1 = IR.script(
        IR.string("var a = require('module1');"),
        IR.string("console.log(a.exports);"));
    script1.setSourceFileName("script1.js");
    root.addChildToBack(script1);

    Node script2 = IR.script(
        IR.string("var b = require('module2');"),
        IR.string("console.log(b.exports);"));
    script2.setSourceFileName("script2.js");
    root.addChildToBack(script2);

    Node script3 = IR.script(
        IR.string("goog.require('module1');"),
        IR.string("goog.require('module2');"),
        IR.string("console.log(module1.module$exports);"),
        IR.string("console.log(module2.module$exports);"));
    script3.setSourceFileName("script3.js");
    root.addChildToBack(script3);

    processor.process(input, root);

    JSModule module1 = processor.getModule();
    assertNotNull(module1);
    assertEquals("module1", module1.getName());
    Set<String> requires = module1.getRequires();
    assertEquals(1, requires.size());
    assertEquals("module2", requires.iterator().next());

    JSModule module2 = module1.getModule(input, "module2");
    assertNotNull(module2);
    assertEquals("module2", module2.getName());
    assertNull(module2.getRequires());

    Node mainJs = input.getMainSource();
    assertNotNull(mainJs);
    Node mainJsModule = mainJs.getFirstChild();
    assertEquals(IR.CALL, mainJsModule.getType());
    assertEquals("goog.require", mainJsModule.getFirstChild().getString());
    assertEquals("module1", mainJsModule.getChildAtIndex(1).getString());
    assertEquals(IR.CALL, mainJsModule.getNextSibling().getType());
    assertEquals("goog.require", mainJsModule.getNextSibling().getFirstChild()
        .getString());
    assertEquals("module2", mainJsModule.getNextSibling().getChildAtIndex(1)
        .getString());

    Node script1Js = root.getLastChild().getLastChild();
    assertNotNull(script1Js);
    Node script1Module = script1Js.getFirstChild();
    assertEquals(IR.NAME, script1Module.getType());
    assertEquals("module1", script1Module.getString());
    Node script1Require = script1Module.getNextSibling();
    assertEquals(IR.CALL, script1Require.getType());
    assertEquals("require", script1Require.getFirstChild().getString());
    assertEquals("module1", script1Require.getChildAtIndex(1).getString());

    Node script2Js = root.getLastChild().getPreviousSibling().getLastChild();
    assertNotNull(script2Js);
    Node script2Module = script2Js.getFirstChild();
    assertEquals(IR.NAME, script2Module.getType());
    assertEquals("module2", script2Module.getString());
    Node script2Require = script2Module.getNextSibling();
    assertEquals(IR.CALL, script2Require.getType());
    assertEquals("require", script2Require.getFirstChild().getString());
    assertEquals("module2", script2Require.getChildAtIndex(1).getString());
  }
}