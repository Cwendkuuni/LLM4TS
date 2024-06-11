package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private CompilerPass processor;
  private Node rootNode;
  private ProcessCommonJSModules moduleProcessor;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = mock(CompilerInput.class);
    rootNode = mock(Node.class);
    when(input.getRoot()).thenReturn(rootNode);
    processor = new ProcessCommonJSModules(compiler, "");
    moduleProcessor = new ProcessCommonJSModules(compiler, "", true);
  }

  @Test
  public void testToModuleName() {
    assertThat(ProcessCommonJSModules.toModuleName("test.js")).isEqualTo("test");
    assertThat(ProcessCommonJSModules.toModuleName("./test.js")).isEqualTo("test");
    assertThat(ProcessCommonJSModules.toModuleName("../test.js")).isEqualTo("test");
    assertThat(ProcessCommonJSModules.toModuleName("test.js/subfolder")).isEqualTo("test_subfolder");
    assertThat(ProcessCommonJSModules.toModuleName("test.js/subfolder/subsubfolder")).isEqualTo("test_subfolder_subsubfolder");
  }

  @Test
  public void testGuessCJSModuleName() {
    assertThat(moduleProcessor.guessCJSModuleName("test.js")).isEqualTo("test");
    assertThat(moduleProcessor.guessCJSModuleName("./test.js")).isEqualTo("test");
    assertThat(moduleProcessor.guessCJSModuleName("../test.js")).isEqualTo("test");
    assertThat(moduleProcessor.guessCJSModuleName("test.js/subfolder")).isEqualTo("test_subfolder");
    assertThat(moduleProcessor.guessCJSModuleName("test.js/subfolder/subsubfolder")).isEqualTo("test_subfolder_subsubfolder");
  }

  @Test
  public void testProcess() {
    when(input.getSourceName()).thenReturn("test.js");
    when(compiler.getSourceFileName()).thenReturn("test.js");

    moduleProcessor.process(input, rootNode);

    ArgumentCaptor<NodeTraversal.Callback> callbackCaptor = ArgumentCaptor.forClass(NodeTraversal.Callback.class);
    Mockito.verify(compiler).traverse(input, rootNode, callbackCaptor.capture());
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = (ProcessCommonJSModules.ProcessCommonJsModulesCallback) callbackCaptor.getValue();
    assertThat(callback.scriptNodeCount).isEqualTo(1);

    JSModule module = moduleProcessor.getModule();
    assertSame(module, callback.module);
  }

  @Test
  public void testProcessWithMultipleScripts() {
    when(input.getSourceName()).thenReturn("test.js");
    when(compiler.getSourceFileName()).thenReturn("test.js");

    Node script1 = mock(Node.class);
    Node script2 = mock(Node.class);
    when(rootNode.getChildAtIndex(0)).thenReturn(script1);
    when(rootNode.getChildAtIndex(1)).thenReturn(script2);

    moduleProcessor.process(input, rootNode);

    ArgumentCaptor<NodeTraversal.Callback> callbackCaptor1 = ArgumentCaptor.forClass(NodeTraversal.Callback.class);
    ArgumentCaptor<NodeTraversal.Callback> callbackCaptor2 = ArgumentCaptor.forClass(NodeTraversal.Callback.class);
    Mockito.verify(compiler, Mockito.times(2))
        .traverse(input, rootNode, callbackCaptor1.capture(), callbackCaptor2.capture());

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback1 = (ProcessCommonJSModules.ProcessCommonJsModulesCallback) callbackCaptor1.getValue();
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback2 = (ProcessCommonJSModules.ProcessCommonJsModulesCallback) callbackCaptor2.getValue();

    JSModule module1 = moduleProcessor.getModule();
    assertSame(module1, callback1.module);
    JSModule module2 = moduleProcessor.getModule();
    assertSame(module1, module2);
  }

  @Test
  public void testProcessWithNoScript() {
    when(input.getSourceName()).thenReturn("test.js");
    when(compiler.getSourceFileName()).thenReturn("test.js");

    moduleProcessor.process(input, rootNode);

    ArgumentCaptor<NodeTraversal.Callback> callbackCaptor = ArgumentCaptor.forClass(NodeTraversal.Callback.class);
    Mockito.verify(compiler, Mockito.times(1))
        .traverse(input, rootNode, callbackCaptor.capture());
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = (ProcessCommonJSModules.ProcessCommonJsModulesCallback) callbackCaptor.getValue();
    assertThat(callback.scriptNodeCount).isZero();
  }

  @Test
  public void testVisitRequireCall() {
    String requiredModule = "required_module";
    String currentModule = "current_module";

    Node requireCall = createRequireCallNode("required_module.js");
    Node parent = createScriptNode();

    moduleProcessor.visitRequireCall(Mockito.mock(NodeTraversal.class), requireCall, parent);

    Node expectedModuleRef = IR.name("required_module");
    Node actualModuleRef = requireCall.getChildAtIndex(0);
    assertSame(expectedModuleRef, actualModuleRef);

    Node expectedScript = createScriptNode();
    Node actualScript = parent.getChildAtIndex(0);
    assertSame(expectedScript, actualScript);

    Node expectedRequireCall = IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
        IR.string("required_module"));
    Node actualRequireCall = requireCall.getChildAtIndex(1);
    assertSame(expectedRequireCall, actualRequireCall);

    Node expectedModuleExportsAssignment = IR.assign(IR.name("required_module"),
        IR.getprop(IR.name("goog"), IR.string("require")).apply("required_module"));
    Node actualModuleExportsAssignment = expectedScript.getLastChild();
    assertSame(expectedModuleExportsAssignment, actualModuleExportsAssignment);
  }

  @Test
  public void testVisitModuleExports() {
    String moduleName = "module_name";

    Node moduleExportsProp = IR.getprop(IR.name("module_name"), IR.string("module$exports"));
    Node parent = createScriptNode();

    moduleProcessor.visitModuleExports(moduleExportsProp);

    Node expectedModule = IR.name("module_name");
    Node actualModule = moduleExportsProp.getChildAtIndex(0);
    assertSame(expectedModule, actualModule);

    Node expectedexports = IR.name("module_name$exports");
    Node actualexports = moduleExportsProp.getChildAtIndex(1);
    assertSame(expectedexports, actualexports);
  }

  @Test
  public void testVisitScript() {
    String moduleName = "module_name";

    Node script = createScriptNode();

    moduleProcessor.visitScript(Mockito.mock(NodeTraversal.class), script);

    Node expectedModuleDeclaration = IR.var(IR.name(moduleName), IR.objectlit());
    Node actualModuleDeclaration = script.getChildAtIndex(0);
    assertSame(expectedModuleDeclaration, actualModuleDeclaration);

    Node expectedProvideCall = IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
        IR.string(moduleName));
    Node actualProvideCall = script.getChildAtIndex(1);
    assertSame(expectedProvideCall, actualProvideCall);

    Node expectedModuleExportsOverride = IR.ifNode(
        IR.getprop(IR.name(moduleName), IR.string("module$exports")),
        IR.block(IR.assign(IR.name(moduleName), IR.getprop(IR.name(moduleName), IR.string("module$exports"))))).cloneTree();
    Node actualModuleExportsOverride = script.getLastChild();
    assertSame(expectedModuleExportsOverride, actualModuleExportsOverride);
  }

  @Test
  public void testVisitSuffixVarsCallback() {
    String suffix = "suffix";
    String originalName = "original_name";

    Node nameNode = IR.name(originalName);
    Node parent = createScriptNode();

    moduleProcessor.traverse(compiler, parent, new ProcessCommonJSModules.SuffixVarsCallback(suffix));

    Node expectedNameNode = IR.name(originalName + "$$" + suffix);
    Node actualNameNode = parent.getChildAtIndex(0);
    assertSame(expectedNameNode, actualNameNode);
  }

  private Node createRequireCallNode(String moduleName) {
    Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("")), IR.string(moduleName));
    Node script = IR.script(IR.string(""));
    script.addChildToBack(requireCall);
    return script;
  }

  private Node createScriptNode() {
    Node script = IR.script(IR.string(""));
    return script;
  }
}