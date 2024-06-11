package org.apache.commons.cli;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp. Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.JSModule;
import com.google.common.collect.Sets;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");
 processor.process(input, null);
 JSModule module = processor.getModule();
 assertSame(module, processor.getModule());
 assertNotNull(module.getName());
 }

 @Test
 public void testGuessCJSModuleName() {
 String filename = "module1.js";
 String moduleName = ProcessCommonJSModules.toModuleName(filename);
 assertEquals("module1", moduleName);
 }

 @Test
 public void testToModuleNameWithRelativePath() {
 String requiredFilename = "module2.js";
 String currentFilename = "module1.js";
 String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
 assertEquals("module2", moduleName);
 }

 @Test
 public void testNormalizeSourceName() {
 String filename = "module" + java.io.File.separator + "module1.js";
 String normalizedSourceName = ProcessCommonJSModules.normalizeSourceName(filename);
 assertEquals("modulemodule1", normalizedSourceName);
 }

 @Test
 public void testProcessScriptNode() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 Node rootNode = Node.newTopLevelNode(new IR());
 processor.process(input, rootNode);
 Node scriptNode = rootNode.getFirstChild();
 Preconditions.checkArgument(scriptNode.isScript(), "Expected script node");
 JSModule module = processor.getModule();
 String expectedModuleName = "module1";
 String expectedSourceName = "test.js";
 assertEquals(expectedModuleName, module.getName());
 assertEquals(expectedSourceName, scriptNode.getSourceFileName());
 }

 @Test
 public void testProcessRequireCall() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 Node rootNode = Node.newTopLevelNode(new IR());
 Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
 IR.string("module2"));
 Node scriptNode = Node.newTopLevelNode(new IR());
 scriptNode.addChildToFront(requireCall);
 Node scriptNodeParent = Node.newTopLevelNode(new IR());
 scriptNodeParent.addChildToFront(scriptNode);
 rootNode.addChildToFront(scriptNodeParent);
 processor.process(input, rootNode);
 Node moduleRef = scriptNode.getFirstChild();
 assertNotSame(requireCall, moduleRef);
 assertSame(moduleRef, scriptNode.getChildAtIndex(0));
 }

 @Test
 public void testProcessModuleExports() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 Node rootNode = Node.newTopLevelNode(new IR());
 Node moduleNode = IR.newVar("module", IR.objectlit());
 Node exportsNode = IR.newVar("exports", IR.objectlit());
 Node moduleExports = IR.getprop(moduleNode, "exports");
 Node scriptNode = Node.newTopLevelNode(new IR());
 scriptNode.addChildToBack(moduleNode);
 scriptNode.addChildToBack(exportsNode);
 scriptNode.addChildToBack(moduleExports);
 rootNode.addChildToFront(scriptNode);
 processor.process(input, rootNode);
 Set<String> expectedModules = Sets.newHashSet("module1", "module2");
 Set<String> actualModules = processor.getModule().getAllModuleNames();
 assertEquals(expectedModules, actualModules);
 }

 @Test
 public void testProcessModuleExportsWithEmptySet() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 Node rootNode = Node.newTopLevelNode(new IR());
 Node scriptNode = Node.newTopLevelNode(new IR());
 rootNode.addChildToFront(scriptNode);
 processor.process(input, rootNode);
 Set<String> actualModules = processor.getModule().getAllModuleNames();
 assertEquals(Sets.newHashSet(), actualModules);
 }

 @Test
 public void testProcessModuleExportsWithSingleModule() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 Node rootNode = Node.newTopLevelNode(new IR());
 Node moduleNode = IR.newVar("module", IR.objectlit());
 Node exportsNode = IR.newVar("exports", IR.objectlit());
 Node moduleExports = IR.getprop(moduleNode, "exports");
 Node scriptNode = Node.newTopLevelNode(new IR());
 scriptNode.addChildToBack(moduleNode);
 scriptNode.addChildToBack(exportsNode);
 scriptNode.addChildToBack(moduleExports);
 rootNode.addChildToFront(scriptNode);
 processor.process(input, rootNode);
 Set<String> expectedModules = Sets.newHashSet("module1");
 Set<String> actualModules = processor.getModule().getAllModuleNames();
 assertEquals(expectedModules, actualModules);
 }

 @Test
 public void testProcessModuleExportsWithMultipleModules() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 Node rootNode = Node.newTopLevelNode(new IR());
 Node module1Node = IR.newVar("module1", IR.objectlit());
 Node exports1Node = IR.newVar("exports1", IR.objectlit());
 Node module1Exports = IR.getprop(module1Node, "exports");
 Node module2Node = IR.newVar("module2", IR.objectlit());
 Node exports2Node = IR.newVar("exports2", IR.objectlit());
 Node module2Exports = IR.getprop(module2Node, "exports");
 Node scriptNode = Node.newTopLevelNode(new IR());
 scriptNode.addChildToBack(module1Node);
 scriptNode.addChildToBack(exports1Node);
 scriptNode.addChildToBack(module1Exports);
 scriptNode.addChildToBack(module2Node);
 scriptNode.addChildToBack(exports2Node);
 scriptNode.addChildToBack(module2Exports);
 rootNode.addChildToFront(scriptNode);
 processor.process(input, rootNode);
 Set<String> expectedModules = Sets.newHashSet("module1", "module2");
 Set<String> actualModules = processor.getModule().getAllModuleNames();
 assertEquals(expectedModules, actualModules);
 }

 @Test
 public void testProcessModuleExportsWithEmptyObjectLiterals() throws Exception {
 AbstractCompiler compiler = new AbstractCompiler() {};
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 Node rootNode = Node.newTopLevelNode(new IR());
 Node moduleNode = IR.newVar("module", IR.objectlit());
 Node exportsNode = IR.newVar("exports", IR.objectlit());
 Node moduleExports = IR.getprop(moduleNode, "exports");
 Node scriptNode = Node.newTopLevelNode(new IR());
 scriptNode.addChildToBack(moduleNode);
 scriptNode.addChildToBack(exportsNode);
 scriptNode.addChildToBack(moduleExports);
 rootNode.addChildToFront(scriptNode);
 processor.process(input, rootNode);
 Set<String> expectedModules = Sets.newHashSet("module1");
 Set<String> actualModules = processor.getModule().getAllModuleNames();
 assertEquals(expectedModules, actualModules);
 }
}