import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private ProcessCommonJSModules processor;
  private NodeFactory nodeFactory;
  private CompilerResults results;

  @org.junit.Before
  public void setUp() throws IOException {
    compiler = mock(Compiler.class);
    nodeFactory = NodeFactory.getInstance();
    results = new CompilerResults();
    processor = new ProcessCommonJSModules(compiler, ".");
  }

  @org.junit.Test
  public void testProcess_noCommonJsModules() {
    Node root = nodeFactory.createTextNode("function foo() {}");
    processor.process(null, root);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(0, results.getWarnings().size());
  }

  @org.junit.Test
  public void testProcess_simpleCommonJsModule() {
    String input = "define(function() { function foo() {} })";
    Node root = NodeUtils.parseJsSrc(input, "test.js");
    processor.process(null, root);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(0, results.getWarnings().size());
    assertEquals("goog.provide('test')", results.getOutput().get(0).getContent());
    assertEquals("test.foo = function() {}\n", results.getOutput().get(1).getContent());
  }

  @org.junit.Test
  public void testProcess_commonJsModuleWithDependencies() {
    String input1 = "define(['a'], function(a) { function foo() {} })";
    Node root1 = NodeUtils.parseJsSrc(input1, "a.js");
    processor = new ProcessCommonJSModules(compiler, ".");
    processor.process(null, root1);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(1, results.getWarnings().size());
    assertEquals("a is used but never defined. Assuming a is a module.\n",
        results.getWarnings().get(0).getMessage());
    assertEquals("goog.provide('a')", results.getOutput().get(0).getContent());
    assertEquals("a.foo = function() {}\n", results.getOutput().get(1).getContent());

    String input2 = "define(function() { require('a'); function foo() {} })";
    Node root2 = NodeUtils.parseJsSrc(input2, "b.js");
    processor = new ProcessCommonJSModules(compiler, ".");
    processor.setReportDependencies(false);
    processor.process(null, root2);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(1, results.getWarnings().size());
    assertEquals("require('a') is called but no corresponding goog.require statement was found.\n",
        results.getWarnings().get(0).getMessage());
    assertEquals("goog.provide('b')", results.getOutput().get(0).getContent());
    assertEquals("b.foo = function() {}\n", results.getOutput().get(1).getContent());

    Set<String> expectedDependencies = new HashSet<String>(Arrays.asList("a"));
    processor.process(null, root1);
    processor.process(null, root2);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(0, results.getWarnings().size());
    assertEquals(expectedDependencies, results.getInput().getDependencies());
  }

  @org.junit.Test
  public void testProcess_multipleCommonJsModules() {
    String input1 = "define(function() { function foo() {} });";
    Node root1 = NodeUtils.parseJsSrc(input1, "a.js");
    String input2 = "define(function() { require('a'); function bar() {} })";
    Node root2 = NodeUtils.parseJsSrc(input2, "b.js");
    processor.process(null, root1);
    processor.process(null, root2);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(0, results.getWarnings().size());
    assertEquals("goog.provide('a')", results.getOutput().get(0).getContent());
    assertEquals("a.foo = function() {}\n", results.getOutput().get(1).getContent());
    assertEquals("goog.provide('b')", results.getOutput().get(2).getContent());
    assertEquals("b.bar = function() {}\n", results.getOutput().get(3).getContent());
  }

  @org.junit.Test
  public void testProcess_scriptNodeWithRequireCall() {
    String input = "// @filename: test.js\n" +
        "var module = {\n" +
        "  exports: { foo: function() {}\n" +
        "}\n;\n" +
        "\n" +
        "require('module').exports = {\n" +
        "  foo: function() { console.log('foo'); }\n" +
        "};\n" +
        "\n" +
        "// @filename: main.js\n" +
        "require('test');\n";
    Node root = NodeUtils.parseJsSrc(input, "main.js");
    processor.process(null, root);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(0, results.getWarnings().size());
    assertEquals("test.foo = function() {}\n", results.getOutput().get(0).getContent());
  }

  @org.junit.Test
  public void testProcess_scriptNodeWithRequireAndDefineCalls() {
    String input = "// @filename: test.js\n" +
        "define(function() { function foo() { console.log('foo'); } });\n" +
        "\n" +
        "// @filename: main.js\n" +
        "require('test');\n" +
        "require('test');\n";
    Node root = NodeUtils.parseJsSrc(input, "main.js");
    processor.process(null, root);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(0, results.getWarnings().size());
    assertEquals(2, results.getOutput().size());
    assertEquals("test.foo = function() {}\n", results.getOutput().get(0).getContent());
    assertEquals("test.foo = function() {}\n", results.getOutput().get(1).getContent());
  }

  @org.junit.Test
  public void testProcess_scriptNodeWithRequireCallWithCyclicDependency() {
    String input1 = "// @filename: a.js\n" +
        "define(function() { require('b'); function foo() {}\n" +
        "  return {'exports': {'foo': foo}};\n" +
        "});\n";
    Node root1 = NodeUtils.parseJsSrc(input1, "a.js");
    String input2 = "// @filename: b.js\n" +
        "define(function() { require('a'); function bar() {}\n" +
        "  return {'exports': {'bar': bar}};\n" +
        "});\n";
    Node root2 = NodeUtils.parseJsSrc(input2, "b.js");
    processor.process(null, root1);
    processor.process(null, root2);
    assertEquals(1, results.getExceptionMessages().size());
    assertEquals("Error: Circular dependency detected between module 'a' and 'b'.\n",
        results.getExceptionMessages().get(0).getMessage());
    assertNull(results.getOutput());
  }

  @org.junit.Test
  public void testProcess_scriptNodeWithRequireCallWithMissingModule() {
    String input = "// @filename: main.js\n" +
        "require('not_existing');\n";
    Node root = NodeUtils.parseJsSrc(input, "main.js");
    processor.process(null, root);
    assertEquals(1, results.getExceptionMessages().size());
    assertEquals("Error: Module 'not_existing' not found.\n",
        results.getExceptionMessages().get(0).getMessage());
    assertNull(results.getOutput());
  }

  @org.junit.Test
  public void testProcess_scriptNodeWithRequireCallWithRelativeModulePath() {
    String input = "// @filename: main.js\n" +
        "require('./sub/module');\n";
    Node root = NodeUtils.parseJsSrc(input, "main.js");
    Node subModule = NodeUtils.parseJsSrc("function foo() {}", "sub/module.js");
    Node subDirectory = new Node(Node.DIRECTORY_NODE);
    subDirectory.addChild(subModule);
    when(compiler.getSourceFiles()).thenReturn(new File(".").listFiles());
    when(compiler.getSourceDirectory()).thenReturn(new File("."));
    when(compiler.getInputFile(subModule)).thenReturn(new File("sub/module.js"));
    when(compiler.getSourceName()).thenReturn("main.js");
    processor.process(null, root);
    assertEquals(0, results.getExceptionMessages().size());
    assertEquals(0, results.getWarnings().size());
    assertEquals("sub/module.foo = function() {}\n", results.getOutput().get(0).getContent());
  }
}