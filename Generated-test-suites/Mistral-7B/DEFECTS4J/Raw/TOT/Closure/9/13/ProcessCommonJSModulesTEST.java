import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import java.io.File;
import java.io.IOException;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private Processor processor;

 @Before
 public void setUp() throws IOException {
 compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 processor = compiler.getProcessor();
 }

 @Test
 public void testProcessCommonJSModules() throws IOException {
 String inputPath = "src/test/resources/";
 String outputPath = "target/";
 String filename = "moduleA.js";
 String requireModuleB = "require('./moduleB')";
 String expectedOutput = "goog.provide('moduleA');\n" +
 "goog.require('moduleB');\n" +
 "var moduleA = moduleA || { module$exports: {}};\n" +
 "moduleA.module$exports = moduleB;\n";

 // Given
 File inputFile = new File(inputPath + filename);
 Node inputNode = Node.jsParse(inputFile, null, null, null);
 ProcessCommonJSModules pcm = new ProcessCommonJSModules(processor, ".", false);

 // When
 pcm.process(null, inputNode);
 Node outputNode = pcm.getModule().getOutput();

 // Then
 Node expectedOutputNode = Node.jsParse(expectedOutput, null, null, null);
 NodeTraversal.traverse(processor, outputNode, new NodeDifferentialTestCallback());
 Node expectedNode = expectedOutputNode.getFirstChild();
 Node actualNode = outputNode.getFirstChild();
 assertEquals("Expected and actual code should be equal", expectedNode, actualNode);
 }

 @Test
 public void testProcessCommonJSModulesWithDependencies() throws IOException {
 String inputPath = "src/test/resources/";
 String outputPath = "target/";
 String filenameA = "moduleA.js";
 String filenameB = "moduleB.js";
 String requireModuleB = "require('./moduleB')";
 String expectedOutputA = "goog.provide('moduleA');\n" +
 "goog.require('moduleB');\n" +
 "var moduleA = moduleA || { module$exports: {}};\n" +
 "moduleA.module$exports = moduleB;\n";
 String expectedOutputB = "goog.provide('moduleB');\n" +
 "goog.require('moduleA');\n" +
 "var moduleB = moduleB || { module$exports: {}};\n" +
 "moduleB.module$exports = {};\n";

 // Given
 File inputFileA = new File(inputPath + filenameA);
 Node inputNodeA = Node.jsParse(inputFileA.getAbsolutePath(), null, null, null);
 File inputFileB = new File(inputPath + filenameB);
 Node inputNodeB = Node.jsParse(inputFileB.getAbsolutePath(), null, null, null);
 ProcessCommonJSModules pcmA = new ProcessCommonJSModules(processor, ".", false);
 ProcessCommonJSModules pcmB = new ProcessCommonJSModules(processor, ".", false);

 // When
 pcmA.process(null, inputNodeA);
 Node outputNodeA = pcmA.getModule().getOutput();
 pcmB.process(null, inputNodeB);
 Node outputNodeB = pcmB.getModule().getOutput();

 // Then
 Node expectedOutputNodeA = Node.jsParse(expectedOutputA, null, null, null);
 NodeTraversal.traverse(processor, outputNodeA, new NodeDifferentialTestCallback());
 Node actualNodeA = outputNodeA.getFirstChild();
 assertEquals("Expected and actual code for moduleA should be equal",
 expectedOutputNodeA, actualNodeA);

 Node expectedOutputNodeB = Node.jsParse(expectedOutputB, null, null, null);
 NodeTraversal.traverse(processor, outputNodeB, new NodeDifferentialTestCallback());
 Node actualNodeB = outputNodeB.getFirstChild();
 assertEquals("Expected and actual code for moduleB should be equal",
 expectedOutputNodeB, actualNodeB);
 }

 @Test
 public void testProcessCommonJSModulesWithExports() throws IOException {
 String inputPath = "src/test/resources/";
 String outputPath = "target/";
 String filenameA = "moduleA.js";
 String filenameB = "moduleB.js";
 String requireModuleB = "require('./moduleB')";
 String moduleAModuleExports = "module.exports = { foo: 42 }";
 String expectedOutputA = "goog.provide('moduleA');\n" +
 "goog.require('moduleB');\n" +
 "var moduleA = moduleA || { module$exports: {}};\n" +
 "moduleA.module$exports = moduleB.module$exports;\n" +
 "moduleA.module$exports.foo = 42;\n";
 String expectedOutputB = "goog.provide('moduleB');\n" +
 "goog.require('moduleA');\n" +
 "var moduleB = moduleB || { module$exports: {}};\n" +
 "moduleB.module$exports = { foo: 42 };\n";

 // Given
 File inputFileA = new File(inputPath + filenameA);
 Node inputNodeA = Node.jsParse(inputFileA.getAbsolutePath(), null, null, null);
 inputNodeA.addChild(Node.parse(moduleAModuleExports));
 File inputFileB = new File(inputPath + filenameB);
 Node inputNodeB = Node.jsParse(inputFileB.getAbsolutePath(), null, null, null);
 ProcessCommonJSModules pcmA = new ProcessCommonJSModules(processor, ".", false);
 ProcessCommonJSModules pcmB = new ProcessCommonJSModules(processor, ".", false);

 // When
 pcmA.process(null, inputNodeA);
 Node outputNodeA = pcmA.getModule().getOutput();
 pcmB.process(null, inputNodeB);
 Node outputNodeB = pcmB.getModule().getOutput();

 // Then
 Node expectedOutputNodeA = Node.jsParse(expectedOutputA, null, null, null);
 NodeTraversal.traverse(processor, outputNodeA, new NodeDifferentialTestCallback());
 Node actualNodeA = outputNodeA.getFirstChild();
 assertEquals("Expected and actual code for moduleA should be equal",
 expectedOutputNodeA, actualNodeA);

 Node expectedOutputNodeB = Node.jsParse(expectedOutputB, null, null, null);
 NodeTraversal.traverse(processor, outputNodeB, new NodeDifferentialTestCallback());
 Node actualNodeB = outputNodeB.getFirstChild();
 assertEquals("Expected and actual code for moduleB should be equal",
 expectedOutputNodeB, actualNodeB);
 }

 @Test
 public void testProcessCommonJSModulesWithUriSyntax() throws IOException {
 String inputPath = "src/test/resources/";
 String outputPath = "target/";
 String filenameA = "moduleA.js";
 String filenameB = "moduleB.js";
 String requireModuleB = "require('./moduleB')";
 String requireModuleC = "require('moduleC')";
 String expectedOutputA = "goog.provide('moduleA');\n" +
 "goog.require('moduleB');\n" +
 "goog.require('moduleC');\n" +
 "var moduleA = moduleA || { module$exports: {}};\n" +
 "moduleA.module$exports = moduleB.module$exports;\n" +
 "var moduleB = moduleB || { module$exports: {}};\n" +
 "moduleB.module$exports = {};\n" +
 "var moduleC = moduleC || { module$exports: {}};\n" +
 "moduleC.module$exports = {};\n";
 String inputFileA = "src/test/resources/moduleA.js";
 String inputFileB = "src/test/resources/moduleB.js";
 String inputFileC = "src/test/resources/moduleC.js";

 // Given
 Node inputNodeA = Node.jsParse(inputFileA, null, null, null);
 Node inputNodeB = Node.jsParse(inputFileB, null, null, null);
 Node inputNodeC = Node.jsParse(inputFileC, null, null, null);
 ProcessCommonJSModules pcmA = new ProcessCommonJSModules(processor, ".", false);
 ProcessCommonJSModules pcmB = new ProcessCommonJSModules(processor, ".", false);
 ProcessCommonJSModules pcmC = new ProcessCommonJSModules(processor, ".", false);

 // When
 pcmA.process(null, inputNodeA);
 pcmB.process(null, inputNodeB);
 pcmC.process(null, inputNodeC);
 Node outputNodeA = pcmA.getModule().getOutput();
 Node outputNodeB = pcmB.getModule().getOutput();
 Node outputNodeC = pcmC.getModule().getOutput();

 // Then
 Node expectedOutputNodeA = Node.jsParse(expectedOutputA, null, null, null);
 NodeTraversal.traverse(processor, outputNodeA, new NodeDifferentialTestCallback());
 Node actualNodeA = outputNodeA.getFirstChild();
 assertEquals("Expected and actual code for moduleA should be equal",
 expectedOutputNodeA, actualNodeA);

 Node expectedOutputNodeB = Node.jsParse(expectedOutputA.substring(0, 110), null, null, null);
 NodeTraversal.traverse(processor, outputNodeB, new NodeDifferentialTestCallback());
 Node actualNodeB = outputNodeB.getFirstChild();
 assertEquals("Expected and actual code for moduleB should be equal (prefix)",
 expectedOutputNodeB, actualNodeB);

 Node expectedOutputNodeC = Node.jsParse(expectedOutputA.substring(111), null, null, null);
 NodeTraversal.traverse(processor, outputNodeC, new NodeDifferentialTestCallback());
 Node actualNodeC = outputNodeC.getFirstChild();
 assertEquals("Expected and actual code for moduleC should be equal",
 expectedOutputNodeC, actualNodeC);
 }

 // Helper classes
 private static class NodeDifferentialTestCallback
 extends NodeTraversal.AbstractCallback {

 @Override
 public void visit(NodeTraversal t, Node n, Node parent) {
 if (n != null) {
 super.visit(t, n, parent);
 }
 }
 }
}