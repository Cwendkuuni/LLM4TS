package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String inputFilePath = "input/ProcessCommonJSModulesInput.js";
    String expectedOutputFilePath = "input/ProcessCommonJSModulesExpectedOutput.js";
    File inputFile = new File(inputFilePath);
    File expectedOutputFile = new File(expectedOutputFilePath);

    CompilerOptions options = new CompilerOptions();
    options.setPrintWarningMessages(false);
    options.setPrintProgressToStderr(false);

    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile(inputFile.getAbsolutePath(), null, null);
    Node rootNode = input.getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    NodeTraversal.traverse(compiler, rootNode, pass);

    Node output = compiler.getJSFile(expectedOutputFile).getTree();
    Node expectedOutput = NodeFactory.compile(expectedOutputFile.getAbsolutePath());

    assertEquals(output.toPrintableTree(), expectedOutput.toPrintableTree());
  }

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "");
    String filename = "path/to/file.js";
    String expected = "module$path_to_file";
    assertEquals(pcjm.guessCJSModuleName(filename), expected);
  }

  @Test(expected = RuntimeException.class)
  public void testToModuleNameWithInvalidURI() {
    ProcessCommonJSModules.toModuleName("invalid_uri", "current_filename");
  }

  @Test
  public void testToModuleNameWithRelativeModulePath() {
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "");
    String currentFilename = "path/to/current_file.js";
    String requiredFilename = "path/to/required/module.js";
    String expected = "module$path_to_required_module";
    assertEquals(pcjm.toModuleName(requiredFilename, currentFilename), expected);
  }

  @Test
  public void testNormalizeSourceName() {
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "");
    String filename = "path/to/filename.js";
    String expected = "filename";
    assertEquals(pcjm.normalizeSourceName(filename), expected);
  }

  @Test
  public void testVisitRequireCall() throws IOException, URISyntaxException {
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "");
    Compiler compiler = mock(Compiler.class);
    NodeTraversal nt = mock(NodeTraversal.class);
    Node traverseResult = mock(Node.class);
    when(compiler.getJSFile(anyString())).thenReturn(new SourceFile("", null, null));
    when(nt.getInput()).thenReturn(compiler);
    when(nt.getSourceName()).thenReturn("input.js");

    Node requireNode = NodeFactory.createCall("require", NodeFactory.createString("module"));
    Node parentNode = NodeFactory.createBlock();

    pcjm.visitRequireCall(nt, requireNode, parentNode);

    Node expectedModuleRef = NodeFactory.createName("module");
    Node actualModuleRef = parentNode.getChildAtIndex(0);
    assertEquals(expectedModuleRef, actualModuleRef);
  }

  @Test
  public void testVisitScript() throws IOException, URISyntaxException {
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "", false);
    Compiler compiler = mock(Compiler.class);
    NodeTraversal nt = mock(NodeTraversal.class);
    Node scriptNode = NodeFactory.createScript("module.exports = 1;");
    Node parentNode = NodeFactory.createBlock();

    pcjm.visitScript(nt, scriptNode);

    JSModule expectedModule = new JSModule("module");
    Node actualModule = pcjm.getModule();
    assertEquals(expectedModule, actualModule);
  }

  @Test
  public void testVisitModuleExports() {
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, "");
    Node propNode = NodeFactory.createGetProp("module", "exports");

    pcjm.visitModuleExports(propNode);

    String expectedModuleName = "module";
    String expectedVarName = "exports";
    String suffix = "$$test";

    assertEquals(expectedModuleName, propNode.getFirstChild().getString());
    assertEquals(expectedVarName, propNode.getChildAtIndex(1).getString());
    assertEquals(expectedModuleName + "$$" + suffix,
        propNode.getChildAtIndex(1).getFirstChild().getString());
  }
}