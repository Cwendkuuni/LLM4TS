package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

 private ProcessCommonJSModules processor;

 @Test
 public void testProcessCommonJSModules() {
 // Given a ProcessCommonJSModules instance
 processor = new ProcessCommonJSModules(new TestCompiler(), "");

 // When processing a Node with a require call
 Node requireCall = IR.call(IR.name("require"), IR.string("module"));
 Node script = IR.script();
 script.addChildToBack(requireCall);

 // Then the require call is replaced by a reference to the module
 processor.process(IR.nullNode(), script);
 assertNotNull(script.getChildAtIndex(script.getChildCount() - 1)
 .getQualifiedName());
 assertEquals("module", script.getChildAtIndex(script.getChildCount() - 1)
 .getString());
 }

 @Test
 public void testProcessCommonJSModulesWithDependenciesReporting() {
 // Given a ProcessCommonJSModules instance with dependencies reporting enabled
 processor = new ProcessCommonJSModules(new TestCompiler(), "", true);

 // Given a script file with multiple require calls
 Node script = IR.script();
 Node requireModule1 = IR.call(IR.name("require"), IR.string("module1"));
 Node requireModule2 = IR.call(IR.name("require"), IR.string("module2"));
 script.addChildToBack(requireModule1);
 script.addChildToBack(requireModule2);

 // When processing the script file
 processor.process(IR.nullNode(), script);

 // Then the require calls are replaced by references to the modules
 // and the input is updated with the dependencies
 assertEquals(2, processor.getCompiler().getInput().getRequireNames().size());
 assertEquals("module1", processor.getCompiler().getInput()
 .getRequireNames().get(0));
 assertEquals("module2", processor.getCompiler().getInput()
 .getRequireNames().get(1));
 }

 @Test
 public void testProcessCommonJSModulesWithModuleExports() {
 // Given a script file with a module.exports assignment
 Node script = IR.script();
 Node module = IR.name("module");
 Node exports = IR.objectlit();
 Node exportsProp = IR.getprop(module, IR.string("exports"));
 Node assign = IR.assign(exportsProp, IR.objectlit());
 script.addChildToBack(module);
 script.addChildToBack(exports);
 script.addChildToBack(assign);

 // When processing the script file
 processor = new ProcessCommonJSModules(new TestCompiler(), "", true);
 processor.process(IR.nullNode(), script);

 // Then the module.exports assignment is transformed into
 // moduleName.module$exports assignment
 assertNotNull(script.getChildAtIndex(script.getChildCount() - 3)
 .getQualifiedName());
 assertEquals("module$exports", script.getChildAtIndex(script.getChildCount() - 3)
 .getString());
 }

 @Test
 public void testProcessCommonJSModulesWithExistingModuleExports() {
 // Given a script file with a module.exports assignment and an existing
 // global variable with the same name
 Node script = IR.script();
 Node module = IR.name("module");
 Node exports = IR.objectlit();
 Node exportsProp = IR.getprop(module, IR.string("exports"));
 Node assign = IR.assign(exportsProp, IR.objectlit());
 Node existingExport = IR.name("exports");
 script.addChildToBack(module);
 script.addChildToBack(exports);
 script.addChildToBack(assign);
 script.addChildToBack(existingExport);

 // When processing the script file
 processor = new ProcessCommonJSModules(new TestCompiler(), "", true);
 processor.process(IR.nullNode(), script);

 // Then the module.exports assignment is transformed into
 // moduleName.module$exports assignment and the existing global variable
 // is renamed to avoid conflicts
 assertNotNull(script.getChildAtIndex(script.getChildCount() - 3)
 .getQualifiedName());
 assertEquals("module$exports", script.getChildAtIndex(script.getChildCount() - 3)
 .getString());
 assertEquals("exports$$module", script.getChildAtIndex(script.getChildCount() - 4)
 .getString());
 }

 @Test
 public void testProcessCommonJSModulesWithNoModuleExports() {
 // Given a script file without a module.exports assignment
 Node script = IR.script();
 Node module = IR.name("module");
 script.addChildToBack(module);

 // When processing the script file
 processor = new ProcessCommonJSModules(new TestCompiler(), "", true);
 processor.process(IR.nullNode(), script);

 // Then no module$exports assignment is added
 assertNull(script.getChildAtIndex(script.getChildCount() - 1)
 .getQualifiedName());
 }

 @Test
 public void testProcessCommonJSModulesWithNestedModules() {
 // Given a script file with multiple nested require calls
 Node script = IR.script();
 Node module1 = IR.name("module1");
 Node requireModule2 = IR.call(IR.name("require"), IR.string("module2"));
 Node assignModule1 = IR.assign(module1, requireModule2);
 Node module2 = IR.name("module2");
 Node requireModule3 = IR.call(IR.name("require"), IR.string("module3"));
 Node assignModule2 = IR.assign(module2, requireModule3);
 script.addChildToBack(module1);
 script.addChildToBack(assignModule1);
 script.addChildToBack(module2);
 script.addChildToBack(assignModule2);

 // When processing the script file
 processor = new ProcessCommonJSModules(new TestCompiler(), "", true);
 processor.process(IR.nullNode(), script);

 // Then the require calls are replaced by references to the modules
 // in the correct order
 assertEquals(3, processor.getCompiler().getInput().getRequireNames().size());
 assertEquals("module2", processor.getCompiler().getInput()
 .getRequireNames().get(0));
 assertEquals("module3", processor.getCompiler().getInput()
 .getRequireNames().get(1));
 assertEquals("module1", processor.getCompiler().getInput()
 .getRequireNames().get(2));
 }

 @Test
 public void testProcessCommonJSModulesWithCyclicDependencies() {
 // Given a script file with cyclic dependencies
 Node script = IR.script();
 Node module1 = IR.name("module1");
 Node requireModule2 = IR.call(IR.name("require"), IR.string("module2"));
 Node assignModule1 = IR.assign(module1, requireModule2);
 Node module2 = IR.name("module2");
 Node requireModule1 = IR.call(IR.name("require"), IR.string("module1"));
 Node assignModule2 = IR.assign(module2, requireModule1);
 script.addChildToBack(module1);
 script.addChildToBack(assignModule1);
 script.addChildToBack(module2);
 script.addChildToBack(assignModule2);

 // When processing the script file
 processor = new ProcessCommonJSModules(new TestCompiler(), "", true);
 try {
 processor.process(IR.nullNode(), script);
 fail("Expected RuntimeException due to cyclic dependencies");
 } catch (RuntimeException e) {
 // Then a RuntimeException is thrown
 }
 }
}

 class TestCompiler implements Compiler {
 @Override
 public void compile(Nodes nodes, CompilationResult result) {
 // Do nothing
 }

 @Override
 public void reportCodeChange() {
 // Do nothing
 }

 @Override
 public void addProvide(String name) {
 // Do nothing
 }

 @Override
 public void addRequire(String name) {
 // Do nothing
 }

 @Override
 public void addRequire(String name, int lineNumber, int lineOffset, int columnNumber) {
 // Do nothing
 }

 @Override
 public void addRequire(String name, int lineNumber, int lineOffset, int columnNumber,
 Node node) {
 // Do nothing
 }
}