import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testProcessCommonJSModules() {
    String filename = "testFile.js";
    String source = "var a = require('module1');";
    Node root = nodeFactory.parse(source, filename, null);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
    pass.process(null, root);

    Node expectedResult = nodeFactory.parse(
        "var module$testFile = {\n" +
        "  'goog': {\n" +
        "    'provide': function(moduleName) {}\n" +
        "  },\n" +
        "  'module$exports': a\n" +
        "};\n" +
        "goog.provide('module$testFile');\n" +
        "\n" +
        "var module1 = {\n" +
        "  'goog': {\n" +
        "    'require': function(moduleName) {}\n" +
        "  },\n" +
        "  'module$exports': {\n" +
        "    a: null\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "goog.require('module1');\n" +
        "goog.provide('module1');\n" +
        "\n" +
        "var a = module1.module$exports.a;",
        filename, null);

    NodeTraversal.traverse(compiler, root, new AssertExpectedResultCallback(expectedResult));
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "testFolder/subFolder/testFile.js";
    String expected = "module$testFolder$subFolder$testFile";
    assertEquals(expected, guessCJSModuleName(filename));
  }

  @Test
  public void testToModuleName() {
    String filename = "testFolder/subFolder/testFile.js";
    String expected = "module$testFolder$subFolder$testFile";
    String currentFilename = "testFileToProcess.js";
    String requiredFilename = "testFolder/subFolder/testFile.js";
    assertEquals(expected, toModuleName(requiredFilename, currentFilename));
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "/testFolder/subFolder/testFile.js";
    String expected = "testFolder/subFolder/testFile.js";
    assertEquals(expected, normalizeSourceName(filename));
  }

  @Test
  public void testProcessCommonJsModulesCallback_requireCall() {
    String filename = "testFile.js";
    String moduleName = "module1";
    String requiredModuleName = "testFolder/subFolder/module1.js";
    String currentFilename = "testFileToProcess.js";

    Node requireCall = nodeFactory.parse("require('" + requiredModuleName + "')", filename, null);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.visitRequireCall(null, requireCall, null);

    Node expectedResult = nodeFactory.parse(
        "goog.require('module1');\n" +
        "module1 = require('module1');\n",
        filename, null);

    NodeTraversal.traverse(compiler, requireCall, new AssertExpectedResultCallback(expectedResult));
  }

  @Test
  public void testProcessCommonJsModulesCallback_scriptNode() {
    String filename = "testFile.js";
    String moduleName = "module1";

    Node script = nodeFactory.parse("var a = 1;", filename, null);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.visitScript(null, script);

    Node expectedResult = nodeFactory.parse(
        "var module$testFile = {\n" +
        "  'goog': {\n" +
        "    'provide': function(moduleName) {}\n" +
        "  },\n" +
        "  'module$exports': {}\n" +
        "};\n" +
        "\n" +
        "goog.provide('module$testFile');\n" +
        "\n" +
        "var a = 1;\n" +
        "\n" +
        "goog.provide('module1');\n",
        filename, null);

    NodeTraversal.traverse(compiler, script, new AssertExpectedResultCallback(expectedResult));
  }

  @Test
  public void testProcessCommonJsModulesCallback_moduleExports() {
    String filename = "testFile.js";
    String moduleName = "module1";

    Node moduleExports = nodeFactory.parse("module.exports = { a: 1 };", filename, null);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.visitModuleExports(moduleExports);

    Node expectedResult = nodeFactory.parse(
        "var module$testFile = {\n" +
        "  'goog': {\n" +
        "    'provide': function(moduleName) {}\n" +
        "  },\n" +
        "  'module$exports': {\n" +
        "    a: 1\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "goog.provide('module$testFile');\n" +
        "\n" +
        "goog.provide('module1');\n",
        filename, null);

    NodeTraversal.traverse(compiler, moduleExports, new AssertExpectedResultCallback(expectedResult));
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJsModulesCallback_uriSyntaxException() {
    String filename = "testFile.js";
    String currentFilename = "testFileToProcess.js";
    String requiredFilename = "../testFolder/subFolder/testFile.js";

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.toModuleName(requiredFilename, currentFilename);
  }

  private static class AssertExpectedResultCallback extends AbstractPostOrderCallback {

    private Node expectedResult;

    AssertExpectedResultCallback(Node expectedResult) {
      this.expectedResult = expectedResult;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getChildCount() != expectedResult.getChildCount()) {
        fail("Number of children mismatch");
      }
      for (int i = 0; i < n.getChildCount(); i++) {
        if (!expectedResult.getChildAtIndex(i).isIdenticalTo(n.getChildAtIndex(i))) {
          fail("Child " + i + " mismatch");
          return;
        }
      }
    }
  }
}