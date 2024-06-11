import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.abstractPostOrder;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  private AbstractCompiler compiler;
  private CompilerInput input;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    input = new CompilerInput("test.js", "test.js");
  }

  @Test
  public void testProcessCommonJSModules() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(input, null);
    Node root = input.getRootNode();
    Node expectedRoot = NodeOperations.fromString(
        "goog.provide(\"test\");\n" +
        "module$exports = { /* module code */ };\n" +
        "goog.require(\"test\");");
    assertNodeEquals(expectedRoot, root);
  }

  @Test
  public void testProcessCommonJSModulesWithRequireCall() {
    String sourceName = "test.js";
    String requiredName = "other.js";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    CompilerInput input = new CompilerInput(sourceName, sourceName);

    Node root = input.getRootNode();
    Node requireCall = NodeOperations.fromString("require(\"other.js\")").makeTree();
    root.addChildToFront(requireCall);

    processor.process(input, null);

    assertNodeEquals(
        NodeOperations.fromString(
            "goog.provide(\"test\");\n" +
            "goog.require(\"other.js\");\n" +
            "module$exports = { /* module code */ };\n" +
            "goog.require(\"other.js\");"),
        root);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithInvalidURI() {
    String sourceName = "test.js";
    String requiredName = "other";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    CompilerInput input = new CompilerInput(sourceName, sourceName);

    Node root = input.getRootNode();
    Node requireCall = NodeOperations.fromString("require(\"other\")").makeTree();
    root.addChildToFront(requireCall);

    processor.process(input, null);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    String sourceName = "test.js";
    String moduleName = "test";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    CompilerInput input = new CompilerInput(sourceName, sourceName);

    Node root = input.getRootNode();
    Node moduleExports = NodeOperations.fromString("module.exports = {}", 1).makeTree();
    root.addChildToFront(moduleExports);

    processor.process(input, null);

    assertNodeEquals(
        NodeOperations.fromString(
            "goog.provide(\"" + moduleName + "\");\n" +
            "module$exports = { /* module code */ };\n" +
            "goog.require(\"" + moduleName + "\");"),
        root);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequireCall() {
    String currentFileName = "test.js";
    String requiredFileName = "./other.js";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    CompilerInput input = new CompilerInput(currentFileName, currentFileName);

    Node root = input.getRootNode();
    Node requireCall = NodeOperations.fromString("require(\"" + requiredFileName + "\")").makeTree();
    root.addChildToFront(requireCall);

    processor.process(input, null);

    assertNodeEquals(
        NodeOperations.fromString(
            "goog.provide(\"test\");\n" +
            "goog.require(\"other.js\");\n" +
            "module$exports = { /* module code */ };\n" +
            "goog.require(\"other.js\");"),
        root);
  }

  @Test
  public void testProcessCommonJSModulesWithAbsoluteRequireCall() {
    String currentFileName = "test.js";
    String requiredFileName = "/other.js";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    CompilerInput input = new CompilerInput(currentFileName, currentFileName);

    Node root = input.getRootNode();
    Node requireCall = NodeOperations.fromString("require(\"" + requiredFileName + "\")").makeTree();
    root.addChildToFront(requireCall);

    try {
      processor.process(input, null);
      fail();
    } catch (RuntimeException e) {
      // expected
    }
  }

  private void assertNodeEquals(Node expected, Node actual) {
    assertEquals("Expected and actual nodes do not match.", expected, actual);
  }
}