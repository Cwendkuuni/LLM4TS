import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String inputFileName = "testInput.js";
    String outputFileName = "testOutput.js";
    File inputFile = new File(inputFileName);
    File outputFile = new File(outputFileName);

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    CompilerInput input = new CompilerInput(inputFile, SourceName.get("test"));
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(input, null);

    Node root = input.getTree();
    Node output = compiler.getProgramText();

    // Basic functionality test
    assertEquals(getStringFromFile("testExpectedOutput.js"),
        getStringFromNode(output));

    // Test with multiple require calls
    String inputWithRequires = "testInputWithRequires.js";
    input = new CompilerInput(new File(inputWithRequires), SourceName.get("test"));
    pass.process(input, root);
    output = compiler.getProgramText();
    assertEquals(getStringFromFile("testExpectedOutputWithRequires.js"),
        getStringFromNode(output));

    // Test with module exports
    String inputWithModuleExports = "testInputWithModuleExports.js";
    input = new CompilerInput(new File(inputWithModuleExports), SourceName.get("test"));
    pass.process(input, root);
    output = compiler.getProgramText();
    assertEquals(getStringFromFile("testExpectedOutputWithModuleExports.js"),
        getStringFromNode(output));

    // Test with invalid require call
    String inputWithInvalidRequire = "testInputWithInvalidRequire.js";
    input = new CompilerInput(new File(inputWithInvalidRequire), SourceName.get("test"));
    assertThrows(RuntimeException.class, () -> pass.process(input, root));

    // Test with missing require argument
    String inputWithMissingRequireArgument = "testInputWithMissingRequireArgument.js";
    input = new CompilerInput(new File(inputWithMissingRequireArgument), SourceName.get("test"));
    assertThrows(RuntimeException.class, () -> pass.process(input, root));

    // Test with incorrect filename prefix
    CompilerInput inputWithIncorrectFilenamePrefix = new CompilerInput(
        new File("testInputWithIncorrectFilenamePrefix.js"), SourceName.get("test"));
    ProcessCommonJSModules incorrectPrefixPass = new ProcessCommonJSModules(
        compiler, "wrongPrefix/");
    assertThrows(IllegalArgumentException.class, () -> incorrectPrefixPass.process(inputWithIncorrectFilenamePrefix, root));

    // Test with missing moduleNamePrefix
    CompilerInput inputWithMissingModuleNamePrefix = new CompilerInput(
        new File("testInputWithMissingModuleNamePrefix.js"), SourceName.get("test"));
    ProcessCommonJSModules missingPrefixPass = new ProcessCommonJSModules(
        compiler, "");
    assertThrows(IllegalArgumentException.class, () -> missingPrefixPass.process(inputWithMissingModuleNamePrefix, root));

    // Test with invalid moduleName
    String inputWithInvalidModuleName = "testInputWithInvalidModuleName.js";
    input = new CompilerInput(new File(inputWithInvalidModuleName), SourceName.get("test"));
    assertThrows(IllegalArgumentException.class, () -> pass.process(input, root));

    // Test with invalid require argument
    String inputWithInvalidRequireArgument = "testInputWithInvalidRequireArgument.js";
    input = new CompilerInput(new File(inputWithInvalidRequireArgument), SourceName.get("test"));
    assertThrows(IllegalArgumentException.class, () -> pass.process(input, root));

    // Test with empty source name
    CompilerInput inputWithEmptySourceName = new CompilerInput(
        new File("testInputWithEmptySourceName.js"), SourceName.get(""));
    assertThrows(IllegalArgumentException.class, () -> pass.process(inputWithEmptySourceName, root));

    // Test with null source name
    CompilerInput inputWithNullSourceName = new CompilerInput(
        new File("testInputWithNullSourceName.js"), null);
    assertThrows(NullPointerException.class, () -> pass.process(inputWithNullSourceName, root));

    // Test with null root node
    assertThrows(NullPointerException.class, () -> pass.process(input, null));

    // Test with null compiler
    assertThrows(NullPointerException.class, () -> new ProcessCommonJSModules(null, "."));

    // Test with null filenamePrefix
    assertThrows(NullPointerException.class, () -> new ProcessCommonJSModules(compiler, null));

    // Test with null reportDependencies
    ProcessCommonJSModules noReportDependenciesPass = new ProcessCommonJSModules(
        compiler, "", false);
    input = new CompilerInput(new File("testInput.js"), SourceName.get("test"));
    noReportDependenciesPass.process(input, root);
    JSModule module = pass.getModule();
    assertNull(module);
  }

  private String getStringFromFile(String fileName) throws IOException, URISyntaxException {
    File file = new File(fileName);
    return new String(Files.readAllBytes(file.toPath()));
  }

  private String getStringFromNode(Node node) {
    return new String(new StringWriter(new StringWriterSettings())
        .writeTree(node)).replaceAll("\\n", "");
  }
}