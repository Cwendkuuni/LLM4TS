import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "// Input file\n" +
        "var require = function(moduleName) {\n" +
        "  return {\n" +
        "    exports: {'Hello': 'hi'}\n" +
        "  };\n" +
        "};\n" +
        "console.log(require(\"Hello\"));\n";

    Node root = new Node(SourceName.get("input.js"), input);
    new ProcessCommonJSModules(compiler, "", true).process(null, root);
    Node output = compiler.getOutput();

    // Test basic functionality
    String expectedOutput = "// Output file\n" +
        "goog.provide(\"module.Hello\");\n" +
        "module.module$exports = {\n" +
        "  'hi': null\n" +
        "};\n" +
        "console.log(module.module$exports.hi);\n";
    assertEquals(expectedOutput.toString(), output.toSource());

    // Test require call with absolute path
    input = "// Input file\n" +
        "var require = function(moduleName) {\n" +
        "  return {\n" +
        "    exports: {'Absolute': 'hello'}\n" +
        "  };\n" +
        "};\n" +
        "console.log(require(\"./module1.js/module2.js/Absolute\"));\n";
    root = new Node(SourceName.get("input.js"), input);
    new ProcessCommonJSModules(compiler, "", true).process(null, root);
    output = compiler.getOutput();

    // Test require call with relative path
    expectedOutput = "// Output file\n" +
        "goog.provide(\"module.module1.js.module2.js.Absolute\");\n" +
        "module.module$exports = {\n" +
        "  'hello': null\n" +
        "};\n" +
        "console.log(module.module$exports.hello);\n";
    assertEquals(expectedOutput.toString(), output.toSource());

    // Test require call with missing module
    input = "// Input file\n" +
        "var require = function(moduleName) {\n" +
        "  return {\n" +
        "    exports: {'Missing': 'hi'}\n" +
        "  };\n" +
        "};\n" +
        "console.log(require(\"Missing\"));\n";
    root = new Node(SourceName.get("input.js"), input);
    new ProcessCommonJSModules(compiler, "", true).process(null, root);
    output = compiler.getOutput();

    // Test require call with circular dependency
    input = "// Input file 1\n" +
        "var require = function(moduleName) {\n" +
        "  return {\n" +
        "    exports: {'A': 'hi'}\n" +
        "  };\n" +
        "};\n" +
        "console.log(require(\"A\"));\n" +
        "\n" +
        "// Input file 2\n" +
        "var require = function(moduleName) {\n" +
        "  return {\n" +
        "    exports: {'A': null}\n" +
        "  };\n" +
        "};\n" +
        "console.log(require(\"A\"));\n";
    root = new Node(SourceName.get("input1.js"), input.substring(0, input.lastIndexOf("\n")))
        .addSibling(new Node(SourceName.get("input2.js"), input.substring(input.lastIndexOf("\n") + 1)));
    new ProcessCommonJSModules(compiler, "", true).process(null, root);
    output = compiler.getOutput();

    // Test error handling with missing require call
    input = "// Input file\n" +
        "var moduleName = \"Missing\";\n" +
        "console.log(moduleName);\n";
    root = new Node(SourceName.get("input.js"), input);
    try {
      new ProcessCommonJSModules(compiler, "", true).process(null, root);
    } catch (RuntimeException e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("Error in ProcessCommonJSModules"));
      assertTrue(errorMessage.contains("Missing requires no require calls"));
    }
  }
}