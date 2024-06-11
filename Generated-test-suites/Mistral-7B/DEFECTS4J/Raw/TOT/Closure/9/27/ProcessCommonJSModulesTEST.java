import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.CommonJsModule()
        .setFilenamePrefix("test_"));
    compiler = new Compiler(options);
  }

  @org.junit.Test
  public void testProcessCommonJsModules() throws IOException {
    String inputCode = "var a = require('./a.js');";
    String expectedOutput = "goog.provide('test_.a');\n" +
        "goog.require('test_.a');\n" +
        "var a = test_.a.module$exports;\n";

    SourceName sourceName = new SourceName("test.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    assertEquals(expectedOutput, actual);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesRelativePath() throws IOException {
    String inputCode = "var a = require('./a/b.js');";
    String expectedOutput = "goog.provide('test_.a.b');\n" +
        "goog.require('test_.a.b');\n" +
        "var a = test_.a.b.module$exports;\n";

    SourceName sourceName = new SourceName("test.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    assertEquals(expectedOutput, actual);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesRenamesVars() throws IOException {
    String inputCode = "var a = 'Hello, World!';\n" +
        "var b = require('./b.js');\n" +
        "b.c = a;\n";
    String expectedOutput = "goog.provide('test_.b');\n" +
        "goog.require('test_.b');\n" +
        "test_.b.module$exports.c = 'Hello, World!';\n";

    SourceName sourceName = new SourceName("test.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    assertEquals(expectedOutput, actual);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesModuleExports() throws IOException {
    String inputCode = "module.exports = function HelloWorld() {\n" +
        "  return 'Hello, World!';\n" +
        "};";
    String expectedOutput = "goog.provide('test_.HelloWorld');\n" +
        "goog.require('test_.HelloWorld');\n" +
        "goog.export(test_.HelloWorld, 'HelloWorld');\n";

    SourceName sourceName = new SourceName("b.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    assertEquals(expectedOutput, actual);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesMissingModule() throws IOException {
    String inputCode = "var a = require('./missing.js');";
    String expectedOutput = "goog.provide('test_.missing');\n" +
        "goog.require('test_.missing');\n";

    SourceName sourceName = new SourceName("test.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    assertEquals(expectedOutput, actual);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCyclicDependency() throws IOException {
    String inputCodeA = "require('./b.js');\n";
    String inputCodeB = "require('./a.js');\n";

    SourceName sourceNameA = new SourceName("a.js");
    SourceName sourceNameB = new SourceName("b.js");

    Node externs = Node.NULL;
    Node rootA = Node.newText("(function() {\n" + inputCodeA + "\n})();");
    Node rootB = Node.newText("(function() {\n" + inputCodeB + "\n})();");

    try {
      compiler.process(externs, rootA);
      compiler.process(externs, rootB);
      fail("Should have thrown an error due to cyclic dependency.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testProcessCommonJsModulesEmptyModule() throws IOException {
    String inputCode = "module.exports = {};";

    SourceName sourceName = new SourceName("b.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    String expectedOutput = "goog.provide('test_.b');\n" +
        "goog.require('test_.b');\n" +
        "goog.export(test_.b, 'module$exports');\n";
    assertEquals(expectedOutput, actual);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesMultipleModuleExports() throws IOException {
    String inputCode = "module.exports = {\n" +
        "  a: function() {\n" +
        "    return 'Hello, A!';\n" +
        "  },\n" +
        "  b: function() {\n" +
        "    return 'Hello, B!';\n" +
        "  },\n" +
        "  c: 'Hello, C!';\n" +
        "};\n";

    SourceName sourceName = new SourceName("b.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    String expectedOutput = "goog.provide('test_.b');\n" +
        "goog.require('test_.b');\n" +
        "goog.export(test_.b, 'module$exports');\n" +
        "goog.export(test_.b, 'a', test_.b.module$exports.a);\n" +
        "goog.export(test_.b, 'b', test_.b.module$exports.b);\n" +
        "goog.export(test_.b, 'c', test_.b.module$exports.c);\n";
    assertEquals(expectedOutput, actual);
  }

  @org.junit.Test
  public void testProcessCommonJsModulesWithUriSyntax() throws IOException {
    String inputCode = "var a = require('file:/path/to/a.js');";

    SourceName sourceName = new SourceName("test.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");

    try {
      compiler.process(externs, root);
      fail("Should have thrown an error due to non-standard require syntax.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testProcessCommonJsModulesWithUriSyntaxViaEmbeddedUri() throws IOException {
    String inputCode = "var a = require('test_a.js');";

    URL testResource = getClass().getResource("/test_a.js");
    if (testResource == null) {
      fail("Could not find test_a.js file.");
    }

    SourceName sourceName = new SourceName("test.js");
    Node externs = Node.NULL;
    Node root = Node.newText("(function() {\n" + inputCode + "\n})();");

    File testFile = new File(testResource.getFile());
    SourceName testSourceName = new SourceName(testFile.getAbsolutePath());

    compiler.setAdditionalFile(testSourceName, testFile.getAbsolutePath());
    compiler.process(externs, root);
    Node actualOutput = compiler.getIntermediateRepresentation();
    String actual = actualOutput.toString();
    String expectedOutput = "goog.provide('test_.a');\n" +
        "goog.require('test_.a');\n" +
        "var a = test_.a.module$exports;\n";
    assertEquals(expectedOutput, actual);
  }
}