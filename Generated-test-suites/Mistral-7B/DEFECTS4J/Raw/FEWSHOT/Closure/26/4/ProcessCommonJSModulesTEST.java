package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTypes;

import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  private static final String TEST_FILE_PREFIX = "test_";
  private static final String TEST_FILE_SUFFIX = ".js";

  @Test
  public void testProcessCommonJSModules() {
    String inputPath = "test_input";
    String outputPath = "test_output";

    // Create input files
    File inputDir = new File(inputPath);
    try {
      inputDir.mkdirs();
      writeStringToFile("require('foo')", inputDir, "foo.js");
      writeStringToFile("module.exports = {foo: 1}", inputDir, "bar.js");
      writeStringToFile("require('./foo')", inputDir, "baz.js");
    } finally {
      inputDir.deleteOnExit();
    }

    // Compile input files
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new StubCompiler(), TEST_FILE_PREFIX);
    CompilerInput input = new CompilerInput(new File(inputPath));
    new Compiler(processor).compile(input, new File(outputPath), null, null,
        null, null, null, null, null, null);

    // Check output files
    File outputDir = new File(outputPath);
    assertEquals(3, outputDir.listFiles().length);

    Node fooNode = readNodeFromFile(outputDir, "foo.js");
    assertEquals(NodeTypes.SCRIPT, fooNode.getType());
    assertNull(fooNode.getFirstChild());

    Node bazNode = readNodeFromFile(outputDir, "baz.js");
    assertEquals(NodeTypes.SCRIPT, bazNode.getType());
    Node bazRequireCall = bazNode.getFirstChild();
    assertEquals(NodeTypes.CALL, bazRequireCall.getType());
    assertEquals("require", bazRequireCall.getFirstChild().getQualifiedName());
    assertEquals("foo", bazRequireCall.getChildAtIndex(1).getString());
    Node bazModuleRef = bazRequireCall.getChildAtIndex(2);
    assertEquals(NodeTypes.NAME, bazModuleRef.getType());
    assertEquals("foo", bazModuleRef.getString());

    Node barNode = readNodeFromFile(outputDir, "bar.js");
    assertEquals(NodeTypes.SCRIPT, barNode.getType());
    Node barModuleExports = barNode.getChildAtIndex(0);
    assertEquals(NodeTypes.GET_PROP, barModuleExports.getType());
    assertEquals("module$exports", barModuleExports.getQualifiedName());
    Node barAssign = barNode.getChildAtIndex(1);
    assertEquals(NodeTypes.ASSIGN, barAssign.getType());
    Node barModuleName = barAssign.getChildAtIndex(0);
    assertEquals(NodeTypes.NAME, barModuleName.getType());
    assertEquals("module$foo", barModuleName.getString());
    Node barValue = barAssign.getChildAtIndex(1);
    assertEquals(NodeTypes.NUMBER, barValue.getType());
    assertEquals(1, barValue.getNumber().intValue());

    // Clean up
    outputDir.deleteOnExit();
  }

  private void writeStringToFile(String content, File dir, String filename) {
    try (java.io.FileWriter writer = new java.io.FileWriter(new File(dir, filename))) {
      writer.write(content);
    } catch (java.io.IOException e) {
      throw new RuntimeException("Failed to write file", e);
    }
  }

  private Node readNodeFromFile(File dir, String filename) {
    try {
      return ProcessCommonJSModulesTest.readNodeFromFile(new File(dir, filename));
    } catch (IOException e) {
      throw new RuntimeException("Failed to read file", e);
    }
  }

  private static Node readNodeFromFile(File file) throws IOException {
    java.io.FileReader reader = new java.io.FileReader(file);
    Node node = IR.parse(reader, file.getName());
    reader.close();
    return node;
  }
}