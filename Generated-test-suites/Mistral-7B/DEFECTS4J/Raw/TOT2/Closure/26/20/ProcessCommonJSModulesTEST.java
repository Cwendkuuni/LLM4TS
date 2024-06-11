import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessingEnvironment.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String TEST_INPUT = "testInput.js";
  private static final String TEST_OUTPUT = "testOutput.js";

  @org.junit.Before
  public void setUp() throws Exception {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(WarningLevel.QUIET);
    options.setPrintDiagnostics(false);

    CompilerInput input = new CompilerInput(new File(TEST_INPUT), "testInput.js");
    AbstractCompiler compiler = new Compiler(options);
    compiler.addPasses(new ProcessCommonJSModules(compiler, "."));

    Node root = NodeTraversal.traverse(compiler, input.getRootNode(), new NodeFactory())
        .getStartNode();
    Node output = compile(compiler, root, new File(TEST_OUTPUT), options);

    testProcessCommonJSModules(input, output);
  }

  private void testProcessCommonJSModules(CompilerInput input, Node output) {
    // Test for basic functionality
    testModuleRewritten(input, output, "testModule1");
    testModuleRewritten(input, output, "testModule2", "testModule1");

    // Test for edge cases and exception handling
    testRequireCallWithInvalidArgument(input, output);
    testRequireCallWithNonExistentModule(input, output);
    testRequireCallWithCircularDependency(input, output);
    testModuleExportsWithInvalidName(input, output);
    testModuleExportsWithMissingValue(input, output);
    testModuleExportsWithValueOfDifferentType(input, output);
  }

  private void testModuleRewritten(CompilerInput input, Node output, String moduleName) {
    testModuleRewritten(input, output, moduleName, null);
  }

  private void testModuleRewritten(CompilerInput input, Node output, String moduleName, String expectedDependency) {
    JSModule module = getModule(input, moduleName);
    assertNotNull(module);

    if (expectedDependency != null) {
      assertEquals(expectedDependency, module.getDependency());
    }

    Node moduleScript = module.getModuleScript();
    Node expectedModuleScript = getExpectedModuleScript(moduleName);

    assertEquals(expectedModuleScript.getChildCount(), moduleScript.getChildCount());
    for (int i = 0; i < expectedModuleScript.getChildCount(); i++) {
      Node expectedChild = expectedModuleScript.getChildAt(i);
      Node actualChild = moduleScript.getChildAt(i);

      assertChildEqual(expectedChild, actualChild);
    }
  }

  private void testRequireCallWithInvalidArgument(CompilerInput input, Node output) {
    String invalidModuleName = "invalid$module";
    String moduleName = toModuleName(input.getSourceName(), invalidModuleName);

    Node requireCall = getRequireCall(output, moduleName);
    assertNotNull(requireCall);

    assertThrows(RuntimeException.class, () -> {
      getModule(input, moduleName);
    });
  }

  private void testRequireCallWithNonExistentModule(CompilerInput input, Node output) {
    String nonExistentModuleName = "nonExistentModule";
    String moduleName = toModuleName(input.getSourceName(), nonExistentModuleName);

    Node requireCall = getRequireCall(output, moduleName);
    assertNotNull(requireCall);

    JSModule module = getModule(input, moduleName);
    assertNull(module);
  }

  private void testRequireCallWithCircularDependency(CompilerInput input, Node output) {
    String module1Name = "module1";
    String module2Name = "module2";

    Node module1Script = getExpectedModuleScript(module1Name);
    Node module2Script = getExpectedModuleScript(module2Name);

    input.addProvide(module1Name);
    input.addProvide(module2Name);

    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(
        new Compiler(new CompilerOptions()), ".");
    Node processedRoot = NodeTraversal.traverse(
        new Compiler(new CompilerOptions()), input.getRootNode(), new NodeFactory())
        .getStartNode();
    processCommonJSModules.process(input, processedRoot);

    Node requireModule1 = getRequireCall(processedRoot, module2Name);
    Node requireModule2 = getRequireCall(processedRoot, module1Name);

    assertNotNull(requireModule1);
    assertNotNull(requireModule2);

    assertNotSame(requireModule1, requireModule2);
  }

  private void testModuleExportsWithInvalidName(CompilerInput input, Node output) {
    String moduleName = "moduleWithInvalidName";

    Node moduleScript = getModuleScript(input, moduleName);
    Node exportsProp = moduleScript.getFirstChild();

    assertNotNull(exportsProp);
    assertFalse(exportsProp.isName());

    assertThrows(RuntimeException.class, () -> {
      getModule(input, moduleName);
    });
  }

  private void testModuleExportsWithMissingValue(CompilerInput input, Node output) {
    String moduleName = "moduleWithMissingValue";

    Node moduleScript = getModuleScript(input, moduleName);
    Node exportsProp = moduleScript.getFirstChild();

    assertNotNull(exportsProp);
    assertFalse(exportsProp.isName());

    Node exports = exportsProp.getChildAtIndex(0);
    assertNull(exports);

    assertThrows(RuntimeException.class, () -> {
      getModule(input, moduleName);
    });
  }

  private void testModuleExportsWithValueOfDifferentType(CompilerInput input, Node output) {
    String moduleName = "moduleWithValueOfDifferentType";

    Node moduleScript = getModuleScript(input, moduleName);
    Node exportsProp = moduleScript.getFirstChild();

    assertNotNull(exportsProp);
    assertTrue(exportsProp.isName());

    Node exports = exportsProp.getChildAtIndex(0);
    Node value = exports.getChildAtIndex(0);

    assertNotSame(NodeType.NAME, value.getType());

    assertThrows(RuntimeException.class, () -> {
      getModule(input, moduleName);
    });
  }

  private JSModule getModule(CompilerInput input, String moduleName) {
    return input.getModule(moduleName);
  }

  private Node getRequireCall(Node root, String moduleName) {
    Node requireCall = null;
    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName()) &&
            n.getChildAtIndex(1).isString() &&
            toModuleName(n.getSourceFileName(), n.getChildAtIndex(1).getString())
                    .equals(moduleName)) {
          requireCall = n;
        }
      }
    }, root);

    return requireCall;
  }

  private Node getModuleScript(CompilerInput input, String moduleName) {
    return getModule(input, moduleName).getModuleScript();
  }

  private Node getExpectedModuleScript(String moduleName) {
    String testData = "(" + System.lineSeparator() +
        "// " + moduleName + ".js" + System.lineSeparator() +
        ")" + System.lineSeparator() +
        "goog.provide(\"" + moduleName + "\");" + System.lineSeparator() +
        "goog.require(\"" + toModuleName(TEST_INPUT, moduleName) + "\");" + System.lineSeparator() +
        "var " + moduleName + " = " + moduleName + " || {" + System.lineSeparator() +
        "  'module$exports': " + moduleName + "$$" + moduleName + "};" + System.lineSeparator() +
        "(" + System.lineSeparator() +
        "// Module code goes here" + System.lineSeparator() +
        ")" + System.lineSeparator() +
        "module$" + moduleName + ";" + System.lineSeparator();

    try {
      return new NodeFactory().createFromString(testData);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void assertChildEqual(Node expected, Node actual) {
    assertEquals(expected.getChildCount(), actual.getChildCount());

    for (int i = 0; i < expected.getChildCount(); i++) {
      Node expectedChild = expected.getChildAt(i);
      Node actualChild = actual.getChildAt(i);

      assertChildEqual(expectedChild, actualChild);
    }
  }

  private Node compile(AbstractCompiler compiler, Node root, File outputFile,
      CompilerOptions options) throws CompilationFailedException {
    CompilerInput input = new CompilerInput(root, null, null, outputFile, null);
    compiler.process(input, root, options);

    return input.getCompiledResult();
  }

  private static String toModuleName(String inputFileName, String moduleName) {
    return toModuleName(inputFileName, moduleName, null);
  }

  private static String toModuleName(String inputFileName, String moduleName,
      String currentFileName) {
    return ProcessCommonJSModules.toModuleName(inputFileName, moduleName,
        currentFileName);
  }
}