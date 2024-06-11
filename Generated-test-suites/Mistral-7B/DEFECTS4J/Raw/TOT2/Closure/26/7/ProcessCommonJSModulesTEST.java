import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private SourceName sourceName;

  @Before
  public void setUp() throws IOException {
    compiler = new Compiler();
    compiler.init();
    nodeFactory = compiler.getFactory();
    sourceName = new SourceName("test.js");
  }

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String filenamePrefix = "test_";
    String input = "// Test CommonJS module\n" +
        "require('./test2');\n" +
        "goog.provide('test1');\n" +
        "goog.require('test2');\n" +
        "exports.test = function() {\n" +
        "  return 42;\n" +
        "};\n" +
        "exports.test2 = function() {\n" +
        "  return 'hello';\n" +
        "};";

    CompilerOptions options = new CompilerOptions(null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix);
    Node externs = nodeFactory.literalNull();
    Node root = nodeFactory.parse(input);
    pass.process(externs, root);

    Node result = pass.getModule().getOutput();
    Node expected = nodeFactory.parse("" +
        "goog.provide('test1');\n" +
        "var test = (function() {\n" +
        "  /**\n" +
        "   * @const\n" +
        "   */\n" +
        "  var module = {\n" +
        "    module$test: function() {\n" +
        "      return 42;\n" +
        "    }\n" +
        "  };\n" +
        "  return module.module$test;\n" +
        "})();\n" +
        "goog.require('test2');\n" +
        "var test2 = (function() {\n" +
        "  /**\n" +
        "   * @const\n" +
        "   */\n" +
        "  var module = {\n" +
        "    module$test2: function() {\n" +
        "      return 'hello';\n" +
        "    }\n" +
        "  };\n" +
        "  return module.module$test2;\n" +
        "})();\n");

    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithDependencies() throws Exception {
    String filenamePrefix = "test_";
    String input1 = "// Test CommonJS module 1\n" +
        "goog.provide('test1');\n" +
        "goog.require('test2');\n" +
        "exports.test = function() {\n" +
        "  return 42;\n" +
        "};";

    String input2 = "// Test CommonJS module 2\n" +
        "goog.provide('test2');\n" +
        "exports.test = function() {\n" +
        "  return 'hello';\n" +
        "};";

    CompilerOptions options = new CompilerOptions(null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix, true);
    Node externs = nodeFactory.literalNull();
    Node root1 = nodeFactory.parse(input1);
    Node root2 = nodeFactory.parse(input2);
    pass.process(externs, root1);
    pass.process(externs, root2);

    List<String> requires = compiler.getInputs().get(0).getProvides();
    assertEquals(Arrays.asList("test1", "test2"), requires);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithCircularDependencies() throws Exception {
    String filenamePrefix = "test_";
    String input1 = "// Test CommonJS module 1\n" +
        "goog.provide('test1');\n" +
        "goog.require('test2');\n" +
        "exports.test = function() {\n" +
        "  return 42;\n" +
        "};";

    String input2 = "// Test CommonJS module 2\n" +
        "goog.provide('test2');\n" +
        "goog.require('test1');\n" +
        "exports.test = function() {\n" +
        "  return 'hello';\n" +
        "};";

    CompilerOptions options = new CompilerOptions(null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix);
    Node externs = nodeFactory.literalNull();
    Node root1 = nodeFactory.parse(input1);
    Node root2 = nodeFactory.parse(input2);
    pass.process(externs, root1);
    pass.process(externs, root2);
  }

  @Test(expected = URISyntaxException.class)
  public void testProcessCommonJSModulesWithInvalidSourceNames() throws Exception {
    String filenamePrefix = "test_";
    String input = "// Test CommonJS module with invalid source names\n" +
        "goog.provide('test1');\n" +
        "goog.require('test2');\n" +
        "exports.test = function() {\n" +
        "  return 42;\n" +
        "};\n" +
        "// Bad source name: test2.js/test3.js\n" +
        "goog.require('test2.js/test3.js');";

    CompilerOptions options = new CompilerOptions(null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix);
    Node externs = nodeFactory.literalNull();
    Node root = nodeFactory.parse(input);
    pass.process(externs, root);
  }
}