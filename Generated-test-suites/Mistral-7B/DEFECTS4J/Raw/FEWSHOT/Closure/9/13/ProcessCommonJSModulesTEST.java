package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String testFileName = "test.js";
    String moduleName = "test";

    CompilerInput input = new CompilerInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
    p.process(input, null);

    JSModule module = p.getModule();
    assertSame(moduleName, module.getName());
    assertNull(module.getParent());

    Node rootNode = input.getRoot();
    assertNull(rootNode.getFirstChild());
    assertEquals(1, rootNode.getChildCount());
    Node scriptNode = rootNode.getChildAtIndex(0);

    assertEquals(2, scriptNode.getChildCount());
    Node provideCall = scriptNode.getChildAtIndex(0);
    Node requireCall = scriptNode.getChildAtIndex(1);

    assertEquals("goog.provide", provideCall.getQualifiedName());
    assertEquals(moduleName, provideCall.getChildAtIndex(0).getString());

    assertEquals("goog.require", requireCall.getQualifiedName());
    assertEquals(moduleName, requireCall.getChildAtIndex(1).getString());

    assertEquals(1, scriptNode.getChildCount());

    Node exportsProp = scriptNode.getChildAtIndex(0);
    assertEquals("module$exports", exportsProp.getQualifiedName());

    assertEquals(1, exportsProp.getChildCount());
    Node exportsVar = exportsProp.getChildAtIndex(0);
    assertEquals("exports", exportsVar.getQualifiedName());
  }

  @Test
  public void testProcessCommonJSModulesWithDependency() {
    String testFileName = "test.js";
    String moduleName = "test";
    String dependencyName = "dependency";

    CompilerInput input = new CompilerInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
    p.process(input, null);

    JSModule module = p.getModule();
    assertSame(moduleName, module.getName());
    assertNull(module.getParent());

    Node rootNode = input.getRoot();
    assertNull(rootNode.getFirstChild());
    assertEquals(1, rootNode.getChildCount());
    Node scriptNode = rootNode.getChildAtIndex(0);

    assertEquals(3, scriptNode.getChildCount());

    Node provideCall = scriptNode.getChildAtIndex(0);
    Node requireCall = scriptNode.getChildAtIndex(1);
    Node dependencyRequireCall = scriptNode.getChildAtIndex(2);

    assertEquals("goog.provide", provideCall.getQualifiedName());
    assertEquals(moduleName, provideCall.getChildAtIndex(0).getString());

    assertEquals("goog.require", requireCall.getQualifiedName());
    assertEquals(moduleName, requireCall.getChildAtIndex(1).getString());

    assertEquals("goog.require", dependencyRequireCall.getQualifiedName());
    assertEquals(dependencyName, dependencyRequireCall.getChildAtIndex(1).getString());

    assertEquals(1, scriptNode.getChildCount());

    Node moduleExportsProp = scriptNode.getChildAtIndex(0);
    assertEquals("module$exports", moduleExportsProp.getQualifiedName());

    assertEquals(1, moduleExportsProp.getChildCount());
    Node exportsVar = moduleExportsProp.getChildAtIndex(0);
    assertEquals("exports", exportsVar.getQualifiedName());

    Node dependencyModuleExportsProp = scriptNode.getChildAtIndex(1);
    assertEquals("module$exports", dependencyModuleExportsProp.getQualifiedName());

    assertEquals(1, dependencyModuleExportsProp.getChildCount());
    Node dependencyExportsVar = dependencyModuleExportsProp.getChildAtIndex(0);
    assertEquals("exports", dependencyExportsVar.getQualifiedName());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    String testFileName = "test.js";
    String moduleName = "test";
    String dependencyName = "dependency";

    CompilerInput input = new CompilerInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
    p.process(input, null);

    JSModule module = p.getModule();
    assertSame(moduleName, module.getName());
    assertNull(module.getParent());

    Node rootNode = input.getRoot();
    assertNull(rootNode.getFirstChild());
    assertEquals(1, rootNode.getChildCount());
    Node scriptNode = rootNode.getChildAtIndex(0);

    assertEquals(3, scriptNode.getChildCount());

    Node provideCall = scriptNode.getChildAtIndex(0);
    Node requireCall = scriptNode.getChildAtIndex(1);
    Node dependencyRequireCall = scriptNode.getChildAtIndex(2);

    assertEquals("goog.provide", provideCall.getQualifiedName());
    assertEquals(moduleName, provideCall.getChildAtIndex(0).getString());

    assertEquals("goog.require", requireCall.getQualifiedName());
    assertEquals(moduleName, requireCall.getChildAtIndex(1).getString());

    assertEquals("goog.require", dependencyRequireCall.getQualifiedName());
    assertEquals(dependencyName, dependencyRequireCall.getChildAtIndex(1).getString());

    assertEquals(1, scriptNode.getChildCount());

    Node moduleExportsProp = scriptNode.getChildAtIndex(0);
    assertEquals("module$exports", moduleExportsProp.getQualifiedName());

    assertEquals(1, moduleExportsProp.getChildCount());
    Node exportsVar = moduleExportsProp.getChildAtIndex(0);
    assertEquals("test$exports", exportsVar.getQualifiedName());

    Node dependencyModuleExportsProp = scriptNode.getChildAtIndex(1);
    assertEquals("module$exports", dependencyModuleExportsProp.getQualifiedName());

    assertEquals(1, dependencyModuleExportsProp.getChildCount());
    Node dependencyExportsVar = dependencyModuleExportsProp.getChildAtIndex(0);
    assertEquals("dependency$exports", dependencyExportsVar.getQualifiedName());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExportsAndAssignment() {
    String testFileName = "test.js";
    String moduleName = "test";
    String dependencyName = "dependency";

    CompilerInput input = new CompilerInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
    p.process(input, null);

    JSModule module = p.getModule();
    assertSame(moduleName, module.getName());
    assertNull(module.getParent());

    Node rootNode = input.getRoot();
    assertNull(rootNode.getFirstChild());
    assertEquals(1, rootNode.getChildCount());
    Node scriptNode = rootNode.getChildAtIndex(0);

    assertEquals(3, scriptNode.getChildCount());

    Node provideCall = scriptNode.getChildAtIndex(0);
    Node requireCall = scriptNode.getChildAtIndex(1);
    Node dependencyRequireCall = scriptNode.getChildAtIndex(2);

    assertEquals("goog.provide", provideCall.getQualifiedName());
    assertEquals(moduleName, provideCall.getChildAtIndex(0).getString());

    assertEquals("goog.require", requireCall.getQualifiedName());
    assertEquals(moduleName, requireCall.getChildAtIndex(1).getString());

    assertEquals("goog.require", dependencyRequireCall.getQualifiedName());
    assertEquals(dependencyName, dependencyRequireCall.getChildAtIndex(1).getString());

    assertEquals(1, scriptNode.getChildCount());

    Node moduleExportsProp = scriptNode.getChildAtIndex(0);
    assertEquals("module$exports", moduleExportsProp.getQualifiedName());

    assertEquals(1, moduleExportsProp.getChildCount());
    Node exportsVar = moduleExportsProp.getChildAtIndex(0);
    assertEquals("test$exports", exportsVar.getQualifiedName());

    Node dependencyModuleExportsProp = scriptNode.getChildAtIndex(1);
    assertEquals("module$exports", dependencyModuleExportsProp.getQualifiedName());

    assertEquals(1, dependencyModuleExportsProp.getChildCount());
    Node dependencyExportsVar = dependencyModuleExportsProp.getChildAtIndex(0);
    assertEquals("dependency$exports", dependencyExportsVar.getQualifiedName());

    assertEquals(1, scriptNode.getChildCount());
    assertEquals(2, scriptNode.getChildAtIndex(0).getChildCount());

    Node moduleAssignment = scriptNode.getChildAtIndex(0).getChildAtIndex(1);
    assertEquals("test$exports", moduleAssignment.getQualifiedName());
    assertEquals("test$exports", moduleAssignment.getChildAtIndex(0).getString());

    Node dependencyAssignment = scriptNode.getChildAtIndex(1).getChildAtIndex(1);
    assertEquals("dependency$exports", dependencyAssignment.getQualifiedName());
    assertEquals("dependency$exports", dependencyAssignment.getChildAtIndex(0).getString());
  }
}