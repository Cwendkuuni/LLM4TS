import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.ScriptContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String inputPath = "input/";
    String outputPath = "output/";

    // Set up the compiler
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPassManager(new CompilerPassManager(new CompilerPass[] {
        new ProcessCommonJSModules(new AbstractCompiler() {}, DEFAULT_FILENAME_PREFIX),
    }));
    Compiler compiler = new Compiler(options);

    // Test with a simple CommonJS module
    Node node = NodeFactory.compile(new File(new URI(inputPath + "test.js")))
        .getRootNode();
    SourceFile input = new SourceFile("test.js", node);
    CompilerPassManager passManager = compiler.getPassManager();
    PassManagerUtils.runPasses(passManager, input);
    Node output = input.getCompiledResult();

    // Verify the output
    Node expectedOutput = NodeFactory.compile(new File(new URI(outputPath + "test_out.js")))
        .getRootNode();
    assertEquals(expectedOutput, output);

    // Test with a CommonJS module that depends on another module
    node = NodeFactory.compile(new File(new URI(inputPath + "test_dependency.js")))
        .getRootNode();
    input = new SourceFile("test_dependency.js", node);
    passManager.runPasses(input);
    output = input.getCompiledResult();

    // Verify the output
    Node expectedOutputDependency = NodeFactory.compile(new File(new URI(outputPath + "test_dependency_out.js")))
        .getRootNode();
    assertEquals(expectedOutputDependency, output);

    // Test with a CommonJS module that exports multiple variables
    node = NodeFactory.compile(new File(new URI(inputPath + "test_exports.js")))
        .getRootNode();
    input = new SourceFile("test_exports.js", node);
    passManager.runPasses(input);
    output = input.getCompiledResult();

    // Verify the output
    Node expectedOutputExports = NodeFactory.compile(new File(new URI(outputPath + "test_exports_out.js")))
        .getRootNode();
    assertEquals(expectedOutputExports, output);

    // Test with a CommonJS module that uses require and exports
    node = NodeFactory.compile(new File(new URI(inputPath + "test_require_exports.js")))
        .getRootNode();
    input = new SourceFile("test_require_exports.js", node);
    passManager.runPasses(input);
    output = input.getCompiledResult();

    // Verify the output
    Node expectedOutputRequireExports = NodeFactory.compile(new File(new URI(outputPath + "test_require_exports_out.js")))
        .getRootNode();
    assertEquals(expectedOutputRequireExports, output);

    // Test with a CommonJS module that uses require relative to the current file
    node = NodeFactory.compile(new File(new URI(inputPath + "test_relative_require.js")))
        .getRootNode();
    input = new SourceFile("test_relative_require.js", node);
    passManager.runPasses(input);
    output = input.getCompiledResult();

    // Verify the output
    Node expectedOutputRelativeRequire = NodeFactory.compile(new File(new URI(outputPath + "test_relative_require_out.js")))
        .getRootNode();
    assertEquals(expectedOutputRelativeRequire, output);

    // Test with a CommonJS module that has an incorrect filename
    node = NodeFactory.compile(new File(new URI(inputPath + "test_incorrect_filename.js")))
        .getRootNode();
    input = new SourceFile("test_incorrect_filename.js", node);
    try {
      passManager.runPasses(input);
      fail("Expected CompilationError");
    } catch (CompilerException e) {
      // Expected
    }

    // Test with a CommonJS module that has a missing require
    node = NodeFactory.compile(new File(new URI(inputPath + "test_missing_require.js")))
        .getRootNode();
    input = new SourceFile("test_missing_require.js", node);
    try {
      passManager.runPasses(input);
      fail("Expected CompilationError");
    } catch (CompilerError error) {
      // Expected
      assertEquals("require not found: test_dependency", error.getMessage());
    }

    // Test with a CommonJS module that has a duplicate require
    node = NodeFactory.compile(new File(new URI(inputPath + "test_duplicate_require.js")))
        .getRootNode();
    input = new SourceFile("test_duplicate_require.js", node);
    try {
      passManager.runPasses(input);
      fail("Expected CompilationError");
    } catch (CompilerError error) {
      // Expected
      assertEquals("Duplicate require statement: test_dependency", error.getMessage());
    }
  }

  /**
   * Utilities for running the passes on a SourceFile and comparing the results
   */
  static class PassManagerUtils {

    static void runPasses(CompilerPassManager passManager, SourceFile input) {
      ScriptContext context = new ScriptContext();
      NodeTraversal traversal = new NodeTraversal(input.getRootNode(), passManager, context);
      traversal.traverse();
    }
  }
}