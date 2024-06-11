import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testProcessCommonJSModules_Normal() throws IOException {
    String input = "var a = require('module1');";
    String expectedOutput = "goog.require('module1');\nvar a = module1;";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    Node expected = Node.parse(expectedOutput);
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModules_WithExports() throws IOException {
    String input = "var module1 = {\n  exports: {\n    a: 1\n  }\n};\nmodule.exports = module1;\n";
    String expectedOutput = "goog.require('module1');\nvar module1 = {\n  module$exports: {\n    a: 1\n  }\n};\n";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    Node expectedNode = Node.parse(expectedOutput);
    assertEquals(expectedNode.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModules_RelativeRequire() throws IOException, URISyntaxException {
    String input = "var a = require('./module1');";
    String currentFileName = "test.js";
    String expectedOutput = "goog.require('module1');\nvar a = module1;";

    URI uri = new URI("file:test.js");
    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, uri.getPath(), true).process(null, root);

    Node expected = Node.parse(expectedOutput);
    assertEquals(expected.toString(), root.toString());
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_InvalidRequire() throws IOException {
    String input = "var a = require('invalid');";
    String filename = "test.js";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, filename, true).process(null, root);
  }

  @Test
  public void testProcessCommonJSModules_MultipleRequires() throws IOException {
    String input = "var a = require('module1'), b = require('module2');";
    String expectedOutput = "goog.require('module1');\nvar a = module1;\n" +
        "goog.require('module2');\nvar b = module2;";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    Node expectedNode = Node.parse(expectedOutput);
    assertEquals(expectedNode.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModules_Provide() throws IOException {
    String input = "goog.provide('myModule');";
    String expectedOutput = "goog.provide('myModule');";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    assertEquals(expectedOutput, root.toString());
  }

  @Test
  public void testProcessCommonJSModules_ModuleWithExportsAndProvide()
      throws IOException {
    String input = "goog.provide('myModule');\n" +
        "var module1 = {\n  exports: {\n    a: 1\n  }\n};\n" +
        "module.exports = module1;";
    String expectedOutput = "goog.provide('myModule');\n" +
        "goog.require('module1');\n" +
        "var myModule = {\n  module$exports: {\n    a: 1\n  }\n};";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    Node expectedNode = Node.parse(expectedOutput);
    assertEquals(expectedNode.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModules_ModuleWithExportsAndProvide_ParentHasExports()
      throws IOException {
    String input = "goog.provide('myModule');\n" +
        "var module1 = {\n  exports: {\n    a: 1\n  }\n};\n" +
        "module.exports = module1;\nvar myParentModule = {\n  exports: {\n    b: 2\n  }\n};";
    String expectedOutput = "goog.provide('myModule');\n" +
        "goog.require('module1');\n" +
        "var myModule = {\n  module$exports: {\n    a: 1\n  }\n};\n" +
        "goog.provide('myParentModule');\n" +
        "goog.require('module1');\n" +
        "var myParentModule = {\n  module$exports: {\n    b: 2\n  }\n};";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    Node expectedNode = Node.parse(expectedOutput);
    assertEquals(expectedNode.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModules_ModuleWithExportsAndProvide_ParentHasExportsAndRequires()
      throws IOException {
    String input = "goog.provide('myParentModule');\n" +
        "goog.require('module1');\n" +
        "var myParentModule = {\n  exports: {\n    b: 2\n  }\n};\n" +
        "goog.provide('myModule');\n" +
        "var module1 = {\n  exports: {\n    a: 1\n  }\n};\n" +
        "module.exports = module1;";
    String expectedOutput = "goog.provide('myParentModule');\n" +
        "goog.require('module1');\n" +
        "goog.require('module1');\n" +
        "var myParentModule = {\n  module$exports: {\n    b: 2\n  }\n};\n" +
        "goog.provide('myModule');\n" +
        "goog.require('module1');\n" +
        "var myModule = {\n  module$exports: {\n    a: 1\n  }\n};";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    Node expectedNode = Node.parse(expectedOutput);
    assertEquals(expectedNode.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModules_ModuleWithExportsAndProvide_ParentHasExportsAndOverrides()
      throws IOException {
    String input = "goog.provide('myParentModule');\n" +
        "goog.require('module1');\n" +
        "var myParentModule = {\n  module$exports: {\n    b: 2\n  }\n};\n" +
        "goog.provide('myModule');\n" +
        "var module1 = {\n  exports: {\n    a: 1\n  }\n};\n" +
        "module.exports = module1;\n" +
        "myParentModule.exports = myModule;";
    String expectedOutput = "goog.provide('myParentModule');\n" +
        "goog.require('module1');\n" +
        "goog.require('module1');\n" +
        "var myParentModule = {\n  module$exports: {\n    b: 2\n  }\n};\n" +
        "goog.provide('myModule');\n" +
        "goog.require('module1');\n" +
        "var myModule = {\n  module$exports: {\n    a: 1\n  }\n};\n" +
        "myParentModule.exports = myModule;";

    Node root = Node.parse("(function() {\n" + input + "\n})())");
    new ProcessCommonJSModules(compiler, "", true).process(null, root);

    Node expectedNode = Node.parse(expectedOutput);
    assertEquals(expectedNode.toString(), root.toString());
  }
}