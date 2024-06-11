import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.NodeTraversingCompilerPass;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.Node;

import java.util.Collections;
import java.util.List;

/**
 * Test class for CrossModuleMethodMotion.
 */
public class CrossModuleMethodMotionTest {

  private static AbstractCompiler createCompiler() {
    CompilerOptions options = new CompilerOptions(ParserOptions.standardJson());
    return new Compiler(options);
  }

  @org.junit.Test
  public void testCrossModuleMethodMotion() {
    // Set up the test case
    AbstractCompiler compiler = createCompiler();
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion testee = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // Set up the test code
    String sourceCode =
        "var foo = {\n" +
        "  bar: function() {\n" +
        "    return 'foo.bar';\n" +
        "  },\n" +
        "  baz: function() {\n" +
        "    return 'foo.baz';\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "var moduleA = function() {\n" +
        "};\n" +
        "\n" +
        "moduleA.prototype.qux = function() {\n" +
        "  return 'moduleA.qux';\n" +
        "};\n" +
        "\n" +
        "moduleA.prototype.quux = function() {\n" +
        "  return this.bar();\n" +
        "};\n" +
        "\n" +
        "var moduleB = function() {\n" +
        "};\n" +
        "\n" +
        "moduleB.prototype.quux = function() {\n" +
        "  return 'moduleB.quux';\n" +
        "};\n" +
        "\n" +
        "moduleB.prototype.quux2 = function() {\n" +
        "  return this.baz();\n" +
        "};\n";

    Node externs = Parser.parse(sourceCode.substring(0, sourceCode.indexOf("var")),
        ParserOptions.standardJson());
    Node root = Parser.parse(sourceCode.substring(sourceCode.indexOf("var")),
        ParserOptions.standardJson());

    // Run the test
    testee.process(externs, root);

    // Verify the output
    AbstractCompiler outputCompiler = createCompiler();
    Node outputRoot = outputCompiler.compile(externs, root, null, null);

    List<JSModule> modules = compiler.getModuleGraph().getModules();
    JSModule moduleA = null;
    JSModule moduleB = null;
    for (JSModule module : modules) {
      if (module.getName().equals("moduleA")) {
        moduleA = module;
      } else if (module.getName().equals("moduleB")) {
        moduleB = module;
      }
    }

    Node expectedOutput = makeNode(
        "var foo = {\n" +
        "  bar: function() {\n" +
        "    return 'foo.bar';\n" +
        "  },\n" +
        "  baz: function() {\n" +
        "    return 'foo.baz';\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "var moduleA = function() {\n" +
        "};\n" +
        "\n" +
        "moduleA.prototype.qux = function() {\n" +
        "  return 'moduleA.qux';\n" +
        "};\n" +
        "\n" +
        "moduleA.prototype.quux = JSCompiler_unstubMethod(0, function() {\n" +
        "  return this.bar();\n" +
        "});\n" +
        "\n" +
        "var moduleB = function() {\n" +
        "};\n" +
        "\n" +
        "moduleB.prototype.quux = function() {\n" +
        "  return 'moduleB.quux';\n" +
        "};\n" +
        "\n" +
        "moduleB.prototype.quux2 = JSCompiler_unstubMethod(1, function() {\n" +
        "  return this.baz();\n" +
        "});\n");

    checkNodeEquals(expectedOutput, outputRoot, "Output root nodes don't match");

    checkNodeEquals(
        makeNode("moduleA.prototype.quux = JSCompiler_stubMethod(0);"),
        moduleA.getBodyNode(),
        "moduleA.prototype.quux node doesn't match expected output");

    checkNodeEquals(
        makeNode("moduleB.prototype.quux2 = JSCompiler_stubMethod(1);"),
        moduleB.getBodyNode(),
        "moduleB.prototype.quux2 node doesn't match expected output");
  }

  private static Node makeNode(String code) {
    return Parser.parse(code, ParserOptions.standardJson()).getFirstChild();
  }

  private static void checkNodeEquals(Node expected, Node actual, String message) {
    assertEquals("Nodes don't match: " + message, expected, actual);
  }
}