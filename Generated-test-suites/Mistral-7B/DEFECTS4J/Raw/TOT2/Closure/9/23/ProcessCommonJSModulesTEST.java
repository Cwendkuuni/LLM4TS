import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler.CompilerFlags;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

 private CompilerInput createTestInput(String source) throws URISyntaxException {
 CompilerInput input = new CompilerInput("test.js", null, null, null, null, null);
 input.setSource(source);
 return input;
 }

 @Test
 public void testProcessCommonJSModule() throws IOException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input = createTestInput(
 "require('./module1').foo();\n" +
 "function module1() {\n" +
 "    goog.provide('module1');\n" +
 "    exports.foo = function() {\n" +
 "        alert('Hello World');\n" +
 "    };\n" +
 "}");
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
 pass.process(input, null);
 Node result = input.getOutput();
 Node expected = NodeFactory.createScript(
 "goog.provide('module1');\n" +
 "module1.module$exports = module1;\n" +
 "exports.foo = module1.module$exports.foo;\n" +
 "require('module1').foo();" +
 "module1.foo();");
 NodeTraversal.traverse(compiler, result, new AssertTraversalCallback(expected));
 }

 @Test
 public void testProcessCommonJSModuleWithDependencies() throws IOException, URISyntaxException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input1 = createTestInput(
 "require('./module1').foo();\n" +
 "function module1() {\n" +
 "    goog.provide('module1');\n" +
 "    exports.foo = function() {\n" +
 "        alert('Hello World');\n" +
 "    };\n" +
 "}");
 ProcessCommonJSModules pass1 = new ProcessCommonJSModules(compiler, ".", true);
 pass1.process(input1, null);

 CompilerInput input2 = createTestInput(
 "require('./module1').foo();\n" +
 "function module2() {\n" +
 "    goog.require('module1');\n" +
 "    module2.module$exports = module2;\n" +
 "}");
 ProcessCommonJSModules pass2 = new ProcessCommonJSModules(compiler, ".", true);
 pass2.process(input2, null);

 Node result1 = input1.getOutput();
 Node expected1 = NodeFactory.createScript(
 "goog.provide('module1');\n" +
 "module1.module$exports = module1;\n" +
 "exports.foo = module1.module$exports.foo;\n" +
 "require('module1').foo();" +
 "module1.foo();");
 NodeTraversal.traverse(compiler, result1, new AssertTraversalCallback(expected1));

 Node result2 = input2.getOutput();
 Node expected2 = NodeFactory.createScript(
 "goog.require('module1');\n" +
 "module2.module$exports = module2;\n");
 NodeTraversal.traverse(compiler, result2, new AssertTraversalCallback(expected2));

 String module1Name = toModuleName(new URI(".").getPath(), "test.js");
 Node module1Call1 = IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
 IR.string(module1Name));
 Node module1Call2 = IR.call(IR.getprop(IR.name(module1Name), IR.string("foo")), null);
 Node expectedModule1Call = IR.call(module1Call1, module1Call2);
 Node expectedResult = IR.call(expectedModule1Call, null);
 Node result = NodeTraversal.deepTraverse(compiler, result1, result2)[0];
 NodeTraversal.traverse(compiler, result, new AssertTraversalCallback(expectedResult));
 }

 @Test
 public void testProcessCommonJSModuleWithModuleExports() throws IOException, URISyntaxException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input = createTestInput(
 "require('./module1').exports.foo();\n" +
 "function module1() {\n" +
 "    goog.provide('module1');\n" +
 "    exports.foo = function() {\n" +
 "        alert('Hello World');\n" +
 "    };\n" +
 "}");
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
 pass.process(input, null);
 Node result = input.getOutput();
 Node expected = NodeFactory.createScript(
 "goog.provide('module1');\n" +
 "module1.module$exports = module1;\n" +
 "exports.foo = module1.module$exports.foo;\n" +
 "require('./module1').exports.foo();\n" +
 "module1.foo();");
 NodeTraversal.traverse(compiler, result, new AssertTraversalCallback(expected));
 }

 @Test
 public void testProcessCommonJSModuleWithCycle() throws IOException, URISyntaxException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input1 = createTestInput(
 "require('./module1').foo();\n" +
 "function module1() {\n" +
 "    goog.provide('module1');\n" +
 "    exports.foo = function() {\n" +
 "        require('./module2');\n" +
 "    };\n" +
 "}");
 ProcessCommonJSModules pass1 = new ProcessCommonJSModules(compiler, ".", true);
 pass1.process(input1, null);

 CompilerInput input2 = createTestInput(
 "require('./module2').foo();\n" +
 "function module2() {\n" +
 "    goog.provide('module2');\n" +
 "    exports.foo = function() {\n" +
 "        require('./module1');\n" +
 "    };\n" +
 "}");
 ProcessCommonJSModules pass2 = new ProcessCommonJSModules(compiler, ".", true);
 pass2.process(input2, null);

 String expectedError = "Error: Cyclic dependency: module1 -> module2";
 try {
 pass1.process(input2, null);
 fail("Expected error: " + expectedError);
 } catch (RuntimeException e) {
 assertEquals(expectedError, e.getMessage());
 }
 }

 @Test
 public void testProcessCommonJSModuleWithInvalidRequireCall() throws IOException, URISyntaxException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input = createTestInput(
 "require('invalidModule');\n" +
 "function main() {}");
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
 pass.process(input, null);
 String expectedError = "Error: Module 'invalidModule' not found";
 try {
 Node result = input.getOutput();
 fail("Expected error: " + expectedError);
 } catch (RuntimeException e) {
 assertEquals(expectedError, e.getMessage());
 }
 }

 @Test
 public void testProcessCommonJSModuleWithInvalidModuleName() throws IOException, URISyntaxException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input = createTestInput(
 "require('module$name');\n" +
 "function main() {}");
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
 pass.process(input, null);
 String expectedError = "Error: Invalid module name: module$name";
 try {
 Node result = input.getOutput();
 fail("Expected error: " + expectedError);
 } catch (RuntimeException e) {
 assertEquals(expectedError, e.getMessage());
 }
 }

 @Test
 public void testProcessCommonJSModuleWithInvalidRelativePath() throws IOException, URISyntaxException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input = createTestInput(
 "require('./../module1');\n" +
 "function main() {}");
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
 pass.process(input, null);
 String expectedError = "Error: File not found: module1";
 try {
 Node result = input.getOutput();
 fail("Expected error: " + expectedError);
 } catch (RuntimeException e) {
 assertEquals(expectedError, e.getMessage());
 }
 }

 @Test
 public void testProcessCommonJSModuleWithInvalidAbsolutePath() throws IOException, URISyntaxException {
 CompilerFlags flags = new CompilerFlags();
 Compiler compiler = new Compiler(flags);
 CompilerInput input = createTestInput(
 "require('file:/C:/module1');\n" +
 "function main() {}");
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
 pass.process(input, null);
 String expectedError = "Error: File not found: file:/C:/module1";
 try {
 Node result = input.getOutput();
 fail("Expected error: " + expectedError);
 } catch (RuntimeException e) {
 assertEquals(expectedError, e.getMessage());
 }
 }
}