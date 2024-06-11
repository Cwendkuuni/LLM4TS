import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.ScriptContext;
import com.google.javascript.rhino.SourceName;

import java.io.IOException;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    AbstractCompiler compiler = new NoOpCompiler();
    String filenamePrefix = "/test/unit/";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    // Test with a valid CommonJS module
    String moduleSource = "var exports = module.exports = {}; exports.sayHello = function() {" +
        "  console.log('Hello from the CommonJS module'); };";
    Node rootNode = NodeFactory.createScript(SourceName.create("test_module.js"), moduleSource);
    processor.process(null, rootNode);
    Node module = processor.getModule();
    Node expectedOutput = NodeFactory.createScript(SourceName.create("test_module_processed.js"),
        "goog.provide('test.unit.test_module'); test.unit.test_module.sayHello = function() {" +
        "  console.log('Hello from the CommonJS module'); };");

    Node actualOutput = module.getFirstChild();
    assertEquals(expectedOutput.toString(), actualOutput.toString());

    // Test with a CommonJS module that exports an object with a method
    String moduleSourceWithExport = "var module = { exports: {} }; module.exports.sayHello = function() {" +
        "  console.log('Hello from the CommonJS module with exports object'); };";
    Node rootNodeWithExport = NodeFactory.createScript(SourceName.create("test_module_export.js"),
        moduleSourceWithExport);
    processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    processor.process(null, rootNodeWithExport);
    module = processor.getModule();
    expectedOutput = NodeFactory.createScript(SourceName.create("test_module_export_processed.js"),
        "goog.provide('test.unit.test_module_export'); test.unit.test_module_export.sayHello = function() {" +
        "  console.log('Hello from the CommonJS module with exports object'); };");

    actualOutput = module.getFirstChild();
    assertEquals(expectedOutput.toString(), actualOutput.toString());

    // Test with a CommonJS module that has a dependency on another module
    String moduleSourceWithDependency = "require('other_module'); var exports = module.exports = {" +
        "  sayHello: function() {" +
        "    console.log('Hello from the CommonJS module with dependency'); }";
    String otherModuleSource = "exports.greeting = 'Hello from other_module';";
    Node rootNodeWithDependency = NodeFactory.createScript(SourceName.create("test_module_dependency.js"),
        moduleSourceWithDependency);
    Node otherRootNode = NodeFactory.createScript(SourceName.create("other_module.js"), otherModuleSource);
    Node dependencies = NodeFactory.createArrayLiteral();
    dependencies.addChild(NodeFactory.createStringLiteral("other_module"));
    processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    processor.process(null, otherRootNode);
    processor.process(null, rootNodeWithDependency, dependencies);
    module = processor.getModule();
    expectedOutput = NodeFactory.createScript(SourceName.create("test_module_dependency_processed.js"),
        "goog.provide('test.unit.test_module_dependency'); test.unit.test_module_dependency.sayHello = function() {" +
        "  console.log('Hello from the CommonJS module with dependency'); } test.unit.test_module_dependency.greeting = " +
        "'Hello from other_module';");

    actualOutput = module.getFirstChild();
    assertEquals(expectedOutput.toString(), actualOutput.toString());

    // Test with a CommonJS module that has a cyclic dependency
    String moduleSourceWithCyclicDependency = "require('./cyclic_dependency'); " +
        "var exports = module.exports = {" +
        "  sayHello: function() {" +
        "    console.log('Hello from the CommonJS module with cyclic dependency'); }};";
    String cyclicDependencySource = "require('./test_module_with_cyclic_dependency'); " +
        "var exports = module.exports = {" +
        "  sayHello: function() {" +
        "    console.log('Hello from the CommonJS module with cyclic dependency'); }};";
    Node rootNodeWithCyclicDependency = NodeFactory.createScript(SourceName.create("test_module_cyclic_dependency.js"),
        moduleSourceWithCyclicDependency);
    Node cyclicDependencyRootNode = NodeFactory.createScript(SourceName.create("cyclic_dependency.js"),
        cyclicDependencySource);
    Node dependenciesWithCyclicDependency = NodeFactory.createArrayLiteral();
    dependenciesWithCyclicDependency.addChild(NodeFactory.createStringLiteral("cyclic_dependency"));
    dependenciesWithCyclicDependency.addChild(NodeFactory.createStringLiteral("test_module_cyclic_dependency"));
    processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    processor.process(null, cyclicDependencyRootNode, dependenciesWithCyclicDependency);
    processor.process(null, rootNodeWithCyclicDependency, dependenciesWithCyclicDependency);
    module = processor.getModule();

    // Cyclic dependencies are not supported and should cause an error
    assertThrows(IllegalStateException.class, () -> processor.getModule());

    // Test with a CommonJS module that has invalid source name
    String invalidSourceNameModuleSource = "var exports = module.exports = {" +
        "  sayHello: function() {" +
        "    console.log('Hello from the CommonJS module with invalid source name'); }};";
    Node rootNodeWithInvalidSourceName = NodeFactory.createScript(null, invalidSourceNameModuleSource);
    assertThrows(NullPointerException.class, () -> processor.process(null, rootNodeWithInvalidSourceName, null));

    // Test with a CommonJS module that has a non-existent dependency
    String nonExistentDependencyModuleSource = "require('non_existent_dependency'); " +
        "var exports = module.exports = {" +
        "  sayHello: function() {" +
        "    console.log('Hello from the CommonJS module with non-existent dependency'); }};";
    Node rootNodeWithNonExistentDependency = NodeFactory.createScript(SourceName.create("test_module_non_existent_dependency.js"),
        nonExistentDependencyModuleSource);
    assertThrows(URISyntaxException.class, () -> processor.process(null, rootNodeWithNonExistentDependency, null));
  }

  private void assertThrows(Class<? extends Throwable> expectedExceptionClass, Runnable runnable) {
    try {
      runnable.run();
      fail("Expected an exception of type " + expectedExceptionClass.getName() + " but no exception was thrown.");
    } catch (Throwable t) {
      if (expectedExceptionClass.isInstance(t)) {
        return;
      }
      fail("Expected an exception of type " + expectedExceptionClass.getName() +
          " but received " + t.getClass().getName() + " instead.");
    }
  }
}