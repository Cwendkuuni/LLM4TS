import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private CompilerInput createTestInput(String filename) throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    return new CompilerInput(null, sourceName(filename), null, null, null,
        null, null, null, options);
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    String inputFileName = "testInput.js";
    String expectedOutputFileName = "expectedOutput.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
    Node output = root;

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(expectedOutputFileName)));
    assertEquals(expectedOutput, printer().printToText(output));
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    String filename = "testModule.js";
    String expectedModuleName = "testModule$";
    assertEquals(expectedModuleName, ProcessCommonJSModules.toModuleName(filename));
  }

  @org.junit.Test
  public void testGuessCJSModuleNameWithRelativePath() {
    String currentFilename = "testModuleParent/testModule.js";
    String requiredFilename = "testModuleSibling.js";
    String expectedModuleName = "testModuleParent$testModule$";
    assertEquals(expectedModuleName, ProcessCommonJSModules.toModuleName(
        requiredFilename, currentFilename));
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    String filename = "test/testModule.js";
    String expectedNormalizedSourceName = "testModule.js";
    assertEquals(expectedNormalizedSourceName, ProcessCommonJSModules
        .normalizeSourceName(filename));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallbackVisitRequireCall() {
    String inputFileName = "testInputWithRequireCall.js";
    String expectedOutputFileName = "expectedOutputWithRequireCall.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, false);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
    Node output = root;

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(expectedOutputFileName)));
    assertEquals(expectedOutput, printer().printToText(output));
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testProcessCommonJsModulesCallbackVisitRequireCallWithInvalidURI() {
    String inputFileName = "testInputWithInvalidURI.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, false);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallbackVisitScript() {
    String inputFileName = "testInputWithScript.js";
    String expectedOutputFileName = "expectedOutputWithScript.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, true);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
    Node output = root;

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(expectedOutputFileName)));
    assertEquals(expectedOutput, printer().printToText(output));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallbackVisitModuleExports() {
    String inputFileName = "testInputModuleExports.js";
    String expectedOutputFileName = "expectedOutputModuleExports.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, false);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
    Node output = root;

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(expectedOutputFileName)));
    assertEquals(expectedOutput, printer().printToText(output));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallbackVisitModuleExportsWithModuleName() {
    String inputFileName = "testInputModuleExportsWithModuleName.js";
    String expectedOutputFileName = "expectedOutputModuleExportsWithModuleName.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, false);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
    Node output = root;

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(expectedOutputFileName)));
    assertEquals(expectedOutput, printer().printToText(output));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallbackSuffixVars() {
    String inputFileName = "testInputSuffixVars.js";
    String expectedOutputFileName = "expectedOutputSuffixVars.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, false);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
    Node output = root;

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(expectedOutputFileName)));
    assertEquals(expectedOutput, printer().printToText(output));
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallbackSuffixVarsWithModuleExports() {
    String inputFileName = "testInputSuffixVarsWithModuleExports.js";
    String expectedOutputFileName = "expectedOutputSuffixVarsWithModuleExports.js";

    CompilerInput input = createTestInput(inputFileName);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, false);

    Node root = parseJs(Files.readAllBytes(Paths.get(inputFileName)));
    pass.process(input, root);
    Node output = root;

    String expectedOutput = new String(Files.readAllBytes(
        Paths.get(expectedOutputFileName)));
    assertEquals(expectedOutput, printer().printToText(output));
  }
}