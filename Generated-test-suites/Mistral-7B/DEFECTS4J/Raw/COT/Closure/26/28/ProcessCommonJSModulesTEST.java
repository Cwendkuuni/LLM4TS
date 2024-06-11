import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private CompilerInput createCompilerInput(String fileName) {
    CompilerInput ci = new CompilerInput(fileName);
    ci.setSourceName(fileName);
    return ci;
  }

  private AbstractCompiler createCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getFactory()).thenReturn(nodeFactory);
    return compiler;
  }

  @org.junit.Test
  public void testProcessCommonJSModules() {
    String filename = "commonJSModule.js";
    String moduleName = "commonJSModule";
    String expectedModuleName = "module$commonJSModule";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ci = createCompilerInput(filename);
    Node root = Node.root();

    pass.process(ci, root);

    // Test module name
    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals(expectedModuleName, module.getName());
  }

  @org.junit.Test
  public void testProcessCommonJSModules_require() {
    String filename = "commonJSModuleA.js";
    String moduleNameA = "commonJSModuleA";
    String requiredModuleName = "commonJSModuleB";
    String expectedModuleNameA = "module$commonJSModuleA";
    String expectedModuleNameB = "module$commonJSModuleB";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ciA = createCompilerInput(filename);
    Node rootA = Node.root();

    rootA.addChildToBack(IR.script(
        IR.block(IR.exprResult(IR.call(IR.getprop(IR.name("require"),
            IR.string(requiredModuleName)))),
            IR.returnStatement(IR.undefined()))));

    pass.process(ciA, rootA);

    // Test module names
    JSModule moduleA = pass.getModule();
    assertNotNull(moduleA);
    assertEquals(expectedModuleNameA, moduleA.getName());

    CompilerInput ciB = createCompilerInput(requiredModuleName + ".js");
    Node rootB = Node.root();

    pass.process(ciB, rootB);

    JSModule moduleB = pass.getModule();
    assertNotNull(moduleB);
    assertEquals(expectedModuleNameB, moduleB.getName());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_require_invalidUri() {
    String filename = "commonJSModule.js";
    String requiredModuleName = "invalid:Uri";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ci = createCompilerInput(filename);
    Node root = Node.root();

    root.addChildToBack(IR.script(
        IR.block(IR.exprResult(IR.call(IR.getprop(IR.name("require"),
            IR.string(requiredModuleName)))),
            IR.returnStatement(IR.undefined()))));

    pass.process(ci, root);
  }

  @org.junit.Test
  public void testProcessCommonJSModules_moduleExports() {
    String filename = "commonJSModule.js";
    String moduleName = "commonJSModule";
    String exportName = "exportName";
    String expectedModuleName = "module$commonJSModule";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ci = createCompilerInput(filename);
    Node root = Node.root();

    root.addChildToBack(IR.script(
        IR.block(IR.assign(
            IR.getprop(IR.name(moduleName), "module$exports"),
            IR.objectlit(IR.string(exportName).name("value"))),
            IR.returnStatement(IR.undefined()))));

    pass.process(ci, root);

    // Test module name
    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals(expectedModuleName, module.getName());

    // Test exported name
    Node exports = module.getChildAtIndex(0);
    assertNotNull(exports);
    assertEquals(exportName, exports.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModules_moduleExports_multiple() {
    String filename = "commonJSModule.js";
    String moduleName = "commonJSModule";
    String exportName1 = "exportName1";
    String exportName2 = "exportName2";
    String expectedModuleName = "module$commonJSModule";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ci = createCompilerInput(filename);
    Node root = Node.root();

    root.addChildToBack(IR.script(
        IR.block(IR.assign(
            IR.getprop(IR.name(moduleName), "module$exports"),
            IR.objectlit(IR.string(exportName1).name("value1"),
                IR.string(exportName2).name("value2"))),
            IR.returnStatement(IR.undefined()))));

    pass.process(ci, root);

    // Test module name
    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals(expectedModuleName, module.getName());

    // Test exported names
    Set<String> exportNames = Sets.newHashSet();
    for (int i = 0; i < module.getChildCount(); i++) {
      Node child = module.getChildAtIndex(i);
      if (child.isObject()) {
        for (int j = 0; j < child.getChildCount(); j++) {
          Node export = child.getChildAtIndex(j);
          if (export.isName()) {
            exportNames.add(export.getString());
          }
        }
      }
    }
    assertEquals(Sets.newHashSet(exportName1, exportName2), exportNames);
  }

  @org.junit.Test
  public void testProcessCommonJSModules_moduleExports_noExport() {
    String filename = "commonJSModule.js";
    String moduleName = "commonJSModule";
    String expectedModuleName = "module$commonJSModule";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ci = createCompilerInput(filename);
    Node root = Node.root();

    pass.process(ci, root);

    // Test module name
    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals(expectedModuleName, module.getName());

    // Test no exported names
    Set<String> exportNames = Sets.newHashSet();
    for (int i = 0; i < module.getChildCount(); i++) {
      Node child = module.getChildAtIndex(i);
      if (child.isObject()) {
        for (int j = 0; j < child.getChildCount(); j++) {
          Node export = child.getChildAtIndex(j);
          if (export.isName()) {
            exportNames.add(export.getString());
          }
        }
      }
    }
    assertNull(exportNames.iterator().next());
  }

  @org.junit.Test
  public void testProcessCommonJSModules_globalVariables() {
    String filename = "commonJSModule.js";
    String moduleName = "commonJSModule";
    String variableName = "variableName";
    String expectedModuleName = "module$commonJSModule";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ci = createCompilerInput(filename);
    Node root = Node.root();

    root.addChildToBack(IR.script(
        IR.block(IR.assign(IR.name(variableName), IR.integerLiteral(42)),
            IR.returnStatement(IR.undefined()))));

    pass.process(ci, root);

    // Test module name
    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals(expectedModuleName, module.getName());

    // Test renamed global variable
    Node variable = module.getChildAtIndex(0);
    assertNotNull(variable);
    assertEquals(variableName + "$$", variable.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModules_globalVariables_multiple() {
    String filename = "commonJSModule.js";
    String moduleName = "commonJSModule";
    String variableName1 = "variableName1";
    String variableName2 = "variableName2";
    String expectedModuleName = "module$commonJSModule";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "", true);

    CompilerInput ci = createCompilerInput(filename);
    Node root = Node.root();

    root.addChildToBack(IR.script(
        IR.block(IR.assign(IR.name(variableName1), IR.integerLiteral(42)),
            IR.assign(IR.name(variableName2), IR.integerLiteral(24)),
            IR.returnStatement(IR.undefined()))));

    pass.process(ci, root);

    // Test module name
    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals(expectedModuleName, module.getName());

    // Test renamed global variables
    Set<String> variableNames = Sets.newHashSet();
    for (int i = 0; i < module.getChildCount(); i++) {
      Node child = module.getChildAtIndex(i);
      if (child.isName()) {
        variableNames.add(child.getString());
      }
    }
    assertEquals(Sets.newHashSet(variableName1 + "$$", variableName2 + "$$"),
        variableNames);
  }
}