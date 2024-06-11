import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.jscomp.RewrittenSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

/**
 * Test class for ProcessCommonJSModules.
 */
public class ProcessCommonJSModulesTest {

  private CompilerOptions options;
  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setSkipExternalLibraries(true);
    compiler = new Compiler(options);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSimpleModule() {
    String input = "// input.js\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    a: function() {}\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.b = function() {};\n";

    Node externs = nodeFactory.createExterns();
    Node root = nodeFactory.parse(input, null, null);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, root);

    Node output = traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(Node n) {
        if (n.isScript()) {
          RewrittenSourceFile rsf = (RewrittenSourceFile) n.getUserData();
          assertEquals("test_simple_module.js", rsf.getSourceFile().getName());
          return;
        }
      }
    }).getRootNode();

    String expectedOutput = "// test_simple_module.js\n" +
        "goog.provide('test_simple_module');\n" +
        "\n" +
        "test_simple_module.module$exports = {\n" +
        "  a: function() {}\n" +
        "};\n" +
        "\n" +
        "test_simple_module.module$exports.b = function() {};\n" +
        "\n" +
        "goog.require('test_simple_module');";

    String actualOutput = NodeUtils.toString(output);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testModuleWithRequire() {
    String input = "// input.js\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    a: function() {}\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.b = require('./other_module.js').c;\n";

    Node externs = nodeFactory.createExterns();
    Node root = nodeFactory.parse(input, null, null);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", true);
    pass.process(externs, root);

    Node output = traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(Node n) {
        if (n.isScript()) {
          RewrittenSourceFile rsf = (RewrittenSourceFile) n.getUserData();
          assertEquals("test_module_with_require.js", rsf.getSourceFile().getName());
          return;
        }
      }
    }).getRootNode();

    String expectedOutput = "// test_module_with_require.js\n" +
        "goog.provide('test_module_with_require');\n" +
        "\n" +
        "test_module_with_require.module$exports = {\n" +
        "  a: function() {}\n" +
        "};\n" +
        "\n" +
        "goog.require('other_module'); // required at line 3 of test_module_with_require.js\n" +
        "\n" +
        "goog.require('test_module_with_require');\n" +
        "\n" +
        "test_module_with_require.module$exports.b = other_module.module$c;";

    String actualOutput = NodeUtils.toString(output);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMultipleModules() {
    String input = "// input.js\n" +
        "var moduleA = {\n" +
        "  exports: {\n" +
        "    a: function() {}\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.b = require('./moduleB.js').c;\n" +
        "\n" +
        "var moduleB = {\n" +
        "  exports: {\n" +
        "    c: function() {}\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.d = function() {};\n";

    Node externs = nodeFactory.createExterns();
    Node root = nodeFactory.parse(input, null, null);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", true);
    pass.process(externs, root);

    Node output = traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(Node n) {
        if (n.isScript()) {
          RewrittenSourceFile rsf = (RewrittenSourceFile) n.getUserData();
          assertEquals("test_multiple_modules.js", rsf.getSourceFile().getName());
          return;
        }
      }
    }).getRootNode();

    String expectedOutput = "// test_multiple_modules.js\n" +
        "goog.provide('test_multiple_modules');\n" +
        "\n" +
        "test_multiple_modules.module$exports = {\n" +
        "  a: function() {}\n" +
        "};\n" +
        "\n" +
        "goog.require('moduleB'); // required at line 3 of test_multiple_modules.js\n" +
        "\n" +
        "goog.require('test_multiple_modules');\n" +
        "\n" +
        "goog.require('test_multiple_modules');\n" +
        "\n" +
        "test_multiple_modules.module$exports.b = moduleB.module$c;\n" +
        "\n" +
        "test_multiple_modules.module$exports.d = function() {};\n" +
        "\n" +
        "goog.require('test_multiple_modules');";

    String actualOutput = NodeUtils.toString(output);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testModuleWithCycle() {
    String input = "// input.js\n" +
        "var moduleA = {\n" +
        "  exports: {\n" +
        "    a: function() {}\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.b = require('./moduleA.js').a;\n";

    Node externs = nodeFactory.createExterns();
    Node root = nodeFactory.parse(input, null, null);

    try {
      ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", true);
      pass.process(externs, root);
      fail("Expected compilation error");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("Circular dependency"));
    }
  }

  @Test
  public void testModuleWithMissingDependency() {
    String input = "// input.js\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    a: function() {}\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.b = require('./non_existent_module.js').c;\n";

    Node externs = nodeFactory.createExterns();
    Node root = nodeFactory.parse(input, null, null);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", true);
    pass.process(externs, root);

    Node output = traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(Node n) {
        if (n.isScript()) {
          RewrittenSourceFile rsf = (RewrittenSourceFile) n.getUserData();
          assertEquals("test_module_with_missing_dependency.js",
              rsf.getSourceFile().getName());
          return;
        }
      }
    }).getRootNode();

    String expectedOutput = "// test_module_with_missing_dependency.js\n" +
        "goog.provide('test_module_with_missing_dependency');\n" +
        "\n" +
        "test_module_with_missing_dependency.module$exports = {\n" +
        "  a: function() {}\n" +
        "};\n" +
        "\n" +
        "goog.require('test_module_with_missing_dependency');";

    String actualOutput = NodeUtils.toString(output);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testModuleWithRelativePath() {
    String input = "// input.js\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    a: function() {}\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.b = require('../other_module.js').c;\n";

    Node externs = nodeFactory.createExterns();
    Node root = nodeFactory.parse(input, null, new SourceName("input.js"));

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
    pass.process(externs, root);

    Node output = traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(Node n) {
        if (n.isScript()) {
          RewrittenSourceFile rsf = (RewrittenSourceFile) n.getUserData();
          assertEquals("test_module_with_relative_path.js",
              rsf.getSourceFile().getName());
          return;
        }
      }
    }).getRootNode();

    String expectedOutput = "// test_module_with_relative_path.js\n" +
        "goog.provide('test_module_with_relative_path');\n" +
        "\n" +
        "test_module_with_relative_path.module$exports = {\n" +
        "  a: function() {}\n" +
        "};\n" +
        "\n" +
        "goog.require('other_module'); // required at line 3 of test_module_with_relative_path.js\n" +
        "\n" +
        "goog.require('test_module_with_relative_path');\n" +
        "\n" +
        "test_module_with_relative_path.module$exports.b = other_module.module$c;";

    String actualOutput = NodeUtils.toString(output);
    assertEquals(expectedOutput, actualOutput);
  }
}