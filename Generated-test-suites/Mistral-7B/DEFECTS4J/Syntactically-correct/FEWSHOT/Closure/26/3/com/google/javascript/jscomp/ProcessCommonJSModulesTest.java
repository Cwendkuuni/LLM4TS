package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CompilerInput inputMock = mock(CompilerInput.class);
    CompilerPassManager passManagerMock = mock(CompilerPassManager.class);

    when(compilerMock.getInput()).thenReturn(inputMock);
    when(compilerMock.getPassManager()).thenReturn(passManagerMock);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock,
        "prefix/");
    processor.process(inputMock, null);

    // No checks on the result, just ensure no exceptions are thrown.
  }

  @Test
  public void testToModuleName() {
    String filename = "module.js";
    assertEquals("module$prefixmodule.js", ProcessCommonJSModules.toModuleName(filename));

    filename = "./module.js";
    assertEquals("module$prefixmodule.js", ProcessCommonJSModules.toModuleName(filename));

    filename = "../module.js";
    try {
      URI uri = new URI("file:///prefix/module.js");
      filename = ProcessCommonJSModules.toModuleName(requiredFilename, uri.toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    assertEquals("module$prefix../module.js", filename);

    filename = "module.js-";
    assertEquals("module$prefixmodule.js--", ProcessCommonJSModules.toModuleName(filename));
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "module.js";
    assertEquals("module$prefixmodule.js", ProcessCommonJSModules.guessCJSModuleName(filename));

    filename = "./module.js";
    assertEquals("module$prefixmodule.js", ProcessCommonJSModules.guessCJSModuleName(filename));

    filename = "../module.js";
    try {
      URI uri = new URI("file:///prefix/module.js");
      filename = ProcessCommonJSModules.guessCJSModuleName(uri.toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    assertEquals("module$prefix../module.js", filename);

    filename = "module.js-";
    assertEquals("module$prefixmodule.js--", ProcessCommonJSModules.guessCJSModuleName(filename));
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "prefix/module.js";
    assertEquals("module.js", ProcessCommonJSModules.normalizeSourceName(filename));

    filename = "prefix/module.js-";
    assertEquals("module.js--", ProcessCommonJSModules.normalizeSourceName(filename));

    filename = "./prefix/module.js";
    assertEquals("prefix/module.js", ProcessCommonJSModules.normalizeSourceName(filename));

    filename = "./prefix/module.js-";
    assertEquals("prefix/module.js--", ProcessCommonJSModules.normalizeSourceName(filename));

    filename = "prefix/.";
    assertEquals(".", ProcessCommonJSModules.normalizeSourceName(filename));

    filename = "prefix/../";
    assertEquals("..", ProcessCommonJSModules.normalizeSourceName(filename));
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    Node root = IR.script(null);

    // Simple require call.
    Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("module"));
    root.addChildToBack(requireCall);

    // Test ProcessCommonJsModulesCallback with no module exports.
    NodeTraversal nodeTraversal = new NodeTraversal(root, new ProcessCommonJSModulesCallback());
    nodeTraversal.processAll();
    assertEquals(0, nodeTraversal.getNumTransformations());

    // Test ProcessCommonJsModulesCallback with module exports.
    requireCall.setChildAtIndex(1, IR.string("exports"));
    nodeTraversal = new NodeTraversal(root, new ProcessCommonJSModulesCallback());
    nodeTraversal.processAll();
    assertEquals(1, nodeTraversal.getNumTransformations());

    Node moduleExports = nodeTraversal.getOutput().getFirstDescendantWithName("exports");
    assertSame(requireCall.getChildAtIndex(1), moduleExports);
  }

  @Test
  public void testSuffixVarsCallback() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CompilerInput inputMock = mock(CompilerInput.class);
    Scope scopeMock = mock(Scope.class);

    when(compilerMock.getInput()).thenReturn(inputMock);
    when(inputMock.getScope()).thenReturn(scopeMock);

    ProcessCommonJSModules.SuffixVarsCallback suffixVarsCallback = new ProcessCommonJSModules.SuffixVarsCallback("module");
    NodeTraversal nodeTraversal = new NodeTraversal(null, suffixVarsCallback);
    nodeTraversal.setInput(inputMock);

    Node globalVar = IR.name("globalVar");
    Node globalVarAssignment = IR.exprResult(IR.assign(globalVar, IR.lit(42)));
    root.addChildToBack(globalVarAssignment);

    nodeTraversal.processAll();
    Node globalVarWithSuffix = root.getFirstDescendantWithName("globalVar$$module");
    assertSame(globalVar, globalVarWithSuffix);
  }

  @Test
  public void testProcess() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CompilerInput inputMock = mock(CompilerInput.class);
    CompilerPassManager passManagerMock = mock(CompilerPassManager.class);

    when(compilerMock.getInput()).thenReturn(inputMock);
    when(compilerMock.getPassManager()).thenReturn(passManagerMock);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "prefix/");

    CompilerPass pass = processor;
    passManagerMock.registerPass(pass);

    Node root = IR.script(null);
    inputMock.setSourceName("test.js");
    inputMock.setSourceFileName("test.js");

    // Simple require call.
    Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("module"));
    root.addChildToBack(requireCall);

    // Test ProcessCommonJSModules with no module exports.
    NodeTraversal nodeTraversal = new NodeTraversal(root, new ProcessCommonJSModulesCallback());
    nodeTraversal.processAll();
    assertEquals(0, nodeTraversal.getNumTransformations());

    // Test ProcessCommonJSModules with module exports.
    requireCall.setChildAtIndex(1, IR.string("exports"));
    nodeTraversal = new NodeTraversal(root, new ProcessCommonJSModulesCallback());
    nodeTraversal.processAll();
    assertEquals(1, nodeTraversal.getNumTransformations());

    Node moduleExports = nodeTraversal.getOutput().getFirstDescendantWithName("exports");
    assertSame(requireCall.getChildAtIndex(1), moduleExports);

    JSModule module = processor.getModule();
    assertSame(module, module);

    String moduleName = ProcessCommonJSModules.toModuleName("test.js");
    assertEquals("module$prefixtest.js", moduleName);

    // Test ProcessCommonJSModules with multiple require calls.
    Node requireCall2 = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("otherModule"));
    root.addChildToBack(requireCall2);

    nodeTraversal = new NodeTraversal(root, new ProcessCommonJSModulesCallback());
    nodeTraversal.processAll();
    assertEquals(2, nodeTraversal.getNumTransformations());

    Node otherModuleExports = nodeTraversal.getOutput().getFirstDescendantWithName("otherModule$exports");
    assertSame(requireCall2.getChildAtIndex(1), otherModuleExports);

    // Test ProcessCommonJSModules with dependent modules.
    requireCall.setChildAtIndex(1, IR.string("dep1"));
    requireCall2.setChildAtIndex(1, IR.string("dep2"));

    nodeTraversal = new NodeTraversal(root, new ProcessCommonJSModulesCallback());
    nodeTraversal.processAll();
    assertEquals(4, nodeTraversal.getNumTransformations());

    Set<String> dependencies = Sets.newHashSet();
    dependencies.add("dep1$prefixdep1.js");
    dependencies.add("dep2$prefixdep2.js");
    dependencies.add("module$prefixtest.js");
    dependencies.add("otherModule$prefixotherModule.js");

    assertEquals(dependencies, processor.getDependencySet());
  }
}