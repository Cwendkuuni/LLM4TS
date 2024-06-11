package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

/**
 * Test cases for ProcessCommonJSModules class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CompilerInput ci = Mockito.mock(CompilerInput.class);
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(compiler, "");
    pcjm.process(ci, Mockito.mock(Node.class));
    Mockito.verify(compiler).reportCodeChange();
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "src/main/java/com/example/ModuleA.js";
    String expected = "com_example_ModuleA";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
  }

  @Test
  public void testGuessCJSModuleNameRelativePath() {
    String filename = "src/main/java/com/example/ModuleB.js";
    String currentFilename = "src/main/java/com/example/ModuleA.js";
    String expected = "com_example_ModuleB";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename, currentFilename));
  }

  @Test
  public void testGuessCJSModuleNameAbsolutePath() {
    String filename = "/path/to/ModuleC.js";
    String expected = "ModuleC";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "src/main/java/com/example/ModuleD.js";
    String expected = "com/example/ModuleD.js";
    assertEquals(expected, ProcessCommonJSModules.normalizeSourceName(filename));
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitRequireCall() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CompilerInput ci = Mockito.mock(CompilerInput.class);
    Node root = Mockito.mock(Node.class);
    String requiredModule = "moduleA";
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(compiler, "", true);
    Node requireCall = Mockito.mock(Node.class);
    Node moduleRef = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    // Set up require call node
    Mockito.when(requireCall.getChildCount()).thenReturn(2);
    Mockito.when(requireCall.getChildAtIndex(0)).thenReturn(
        Mockito.mock(Node.class));
    Mockito.when(requireCall.getChildAtIndex(1)).thenReturn(
        Mockito.mock(Node.class).withArguments("..", requiredModule)
            .withReturnValues(Mockito.mock(NodeString.class).withString(requiredModule)));

    Mockito.when(parent.getParent()).thenReturn(root);

    // Call visitRequireCall method
    pcjm.processCommonJSModulesCallback().visit(
        Mockito.mock(NodeTraversal.class).withArguments(compiler, root),
        requireCall, parent);

    // Verify goog.require call was added to root node
    Mockito.verify(root, Mockito.times(1)).addChild(
        Mockito.argThat(n -> "goog.require".equals(n.getQualifiedName())));

    // Verify require call was replaced with module reference
    Mockito.verify(requireCall, Mockito.times(1)).replaceChild(
        Mockito.eq(0), moduleRef);
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitScriptNode() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CompilerInput ci = Mockito.mock(CompilerInput.class);
    Node root = Mockito.mock(Node.class);
    String moduleName = "moduleE";
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(compiler, "", true);
    Node scriptNode = Mockito.mock(Node.class);

    Mockito.when(scriptNode.getSourceFileName()).thenReturn("src/main/java/com/example/ModuleE.js");

    // Call visitScriptNode method
    pcjm.processCommonJSModulesCallback().visit(
        Mockito.mock(NodeTraversal.class).withArguments(compiler, root),
        scriptNode);

    // Verify goog.provide and module variable were added to script node
    Mockito.verify(scriptNode, Mockito.times(1)).addChild(
        Mockito.argThat(n -> "goog.provide".equals(n.getQualifiedName())));
    Mockito.verify(scriptNode, Mockito.times(1)).addChild(
        Mockito.argThat(n -> "module".equals(n.getString())));

    // Verify module exports were handled correctly
    String moduleExportsName = "module$exports";
    Node moduleExports = Mockito.mock(Node.class);
    Node moduleExportsProp = Mockito.mock(Node.class);
    Mockito.when(moduleExports.getSourceFileName()).thenReturn(moduleName);
    Mockito.when(moduleExportsProp.getQualifiedName()).thenReturn("module.exports");
    Mockito.when(scriptNode.getChildAtIndex(scriptNode.getChildCount() - 1)).thenReturn(moduleExportsProp);

    // Call visitScriptNode method with module exports
    pcjm.processCommonJSModulesCallback().visit(
        Mockito.mock(NodeTraversal.class).withArguments(compiler, root),
        scriptNode);

    // Verify if statement was added to check for module exports
    Mockito.verify(scriptNode, Mockito.times(1)).addChild(
        Mockito.argThat(n -> n.getClass() == Node.IfNode.class));

    // Verify module exports were renamed
    Mockito.verify(moduleExports).replaceChild(
        Mockito.eq(0), Mockito.argThat(n -> moduleExportsName.equals(n.getString())));
  }

  @Test
  public void testSuffixVarsCallback() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    String filename = "src/main/java/com/example/ModuleF.js";
    String suffix = "ModuleF";
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(compiler, filename);

    Node scriptNode = Mockito.mock(Node.class);
    scriptNode.setString("var a = 1;");

    // Call traverse method with suffixVarsCallback
    NodeTraversal.traverse(compiler, scriptNode, pcjm.suffixVarsCallback(suffix));

    // Verify variable name was changed
    Mockito.verify(scriptNode, Mockito.times(1)).setString(
        Mockito.argThat(n -> n.getString().equals("a$$ModuleF")));
  }
}