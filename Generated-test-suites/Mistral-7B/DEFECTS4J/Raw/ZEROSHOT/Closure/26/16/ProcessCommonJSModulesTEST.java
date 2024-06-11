import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.TopLevelCodeTester.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.Node;

import java.io.File;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  public static void main(String[] args) throws Exception {
    testProcessCommonJSModules();
  }

  public static void testProcessCommonJSModules() throws Exception {
    String input =
        "// Input file: commonjs1.js\n" +
        "var module$exports = {\n" +
        "  exports: function() {\n" +
        "    return this;\n" +
        "  },\n" +
        "  require: function(name) {\n" +
        "    return require(name);\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "goog.provide('commonjs1');\n" +
        "goog.require('commonjs2');\n" +
        "\n" +
        "var commonjs1_exports = commonjs1.module$exports;\n" +
        "function commonjs1_require(name) {\n" +
        "  return commonjs1_exports.require(name);\n" +
        "}\n" +
        "\n" +
        "// Input file: commonjs2.js\n" +
        "goog.provide('commonjs2');\n" +
        "goog.require('commonjs1');\n" +
        "\n" +
        "var module$exports = {\n" +
        "  exports: function() {\n" +
        "    return this;\n" +
        "  },\n" +
        "  require: function(name) {\n" +
        "    return require(name);\n" +
        "  }\n" +
        "};\n" +
        "function commonjs2_require(name) {\n" +
        "  return commonjs2.module$exports.require(name);\n" +
        "}\n" +
        "\n" +
        "goog.require('commonjs1');\n" +
        "\n" +
        "commonjs1.exports.sayHello = function() {\n" +
        "  print('Hello from commonjs1');\n" +
        "};\n" +
        "\n" +
        "goog.require('commonjs2');\n" +
        "\n" +
        "commonjs1.exports.sayHello();\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    Compiler.InitializationData initData = new Compiler.InitializationData();
    initData.sourceFile = new File("test.js");
    initData.input = Arrays.asList(new SourceFile(new File("commonjs1.js"), input));
    compiler.init(initData, options);
    Node root = compiler.parseTree(initData.input.get(0).getCode());
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(compiler.getExterns(), root);

    String expectedOutput =
        "// Output file: test.js\n" +
        "goog.provide('commonjs1');\n" +
        "goog.require('commonjs2');\n" +
        "\n" +
        "var commonjs1$$module$exports = {\n" +
        "  exports: function() {\n" +
        "    return this;\n" +
        "  },\n" +
        "  require: function(name) {\n" +
        "    return require(name);\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "goog.require('commonjs2');\n" +
        "\n" +
        "var commonjs1$$exports = commonjs1$$module$exports;\n" +
        "\n" +
        "goog.require('commonjs2');\n" +
        "\n" +
        "/**\n" +
        " * @const\n" +
        " */\n" +
        "var require = require;\n" +
        "\n" +
        "/**\n" +
        " * @const\n" +
        " */\n" +
        "var commonjs2$$module$exports = {\n" +
        "  exports: function() {\n" +
        "    return this;\n" +
        "  },\n" +
        "  require: function(name) {\n" +
        "    return require(name);\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "goog.require('commonjs2');\n" +
        "\n" +
        "commonjs1$$exports.sayHello = function() {\n" +
        "  print('Hello from commonjs1');\n" +
        "};\n";

    String output = getProcessedSource(root, compiler.options().getOutputFile());
    assertEquals(expectedOutput, output);

    Set<String> requires = Sets.newHashSet("commonjs2");
    assertTrue(requires.containsAll(compiler.getExterns().getProvides()));
    Set<String> provides = Sets.newHashSet("commonjs1");
    assertTrue(provides.equals(compiler.getExterns().getRequires()));
  }
}