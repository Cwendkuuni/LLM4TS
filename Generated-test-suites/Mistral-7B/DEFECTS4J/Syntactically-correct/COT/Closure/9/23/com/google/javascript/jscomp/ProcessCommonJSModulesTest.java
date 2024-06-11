package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.util.Set;

public class ProcessCommonJSModulesTest extends CompilerPassTestBase {

  private static final String FILENAME_PREFIX = "test_";
  private static final String MODULE_NAME_SEPARATOR = "$";
  private static final String MODULE_NAME_PREFIX = "module$";

  @Override
  protected CompilerPass getPass() {
    return new ProcessCommonJSModules(getCompiler(), FILENAME_PREFIX, true);
  }

  @org.junit.Test
  public void testProcess() {
    String source = "// A simple CommonJS module.\n" +
        "define(function(require, exports, module) {\n" +
        "  exports.hello = function() {\n" +
        "    console.log('Hello, world!');\n" +
        "  };\n" +
        "});";

    Node root = parser.parse(source, SourceName.create("test.js"));
    getPass().process(null, root);

    Node module = root.getFirstChild();
    assertNotNull(module);
    assertEquals("module$test", module.getString());
    Node exports = module.getFirstChild();
    assertEquals("exports", exports.getString());
    Node helloFunction = exports.getLastChild();
    assertEquals("hello", helloFunction.getString());
    Node functionBody = helloFunction.getFirstChild();
    assertEquals("function () {", functionBody.getString());
    Node consoleLog = functionBody.getLastChild();
    assertEquals("console.log('Hello, world!');", consoleLog.getString());

    Node requireCall = root.getLastChild();
    assertEquals("require", requireCall.getFirstChild().getString());
    assertEquals("undefined", requireCall.getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessWithReportDependencies() {
    String source = "// A CommonJS module with a dependency.\n" +
        "define(function(require) {\n" +
        "  var foo = require('./bar');\n" +
        "});\n" +
        "\n" +
        "// A depended CommonJS module.\n" +
        "define(function(exports) {\n" +
        "  exports.bar = function() {\n" +
        "    return 'Bar';\n" +
        "  };\n" +
        "});";

    Node root = parser.parse(source, SourceName.create("test.js"));
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getCompiler(), FILENAME_PREFIX, true);
    pass.process(null, root);

    Set<String> requiredModules = getCompiler().getInput().getRequireList();
    assertEquals(1, requiredModules.size());
    assertEquals("bar", requiredModules.iterator().next());
  }

  @org.junit.Test
  public void testProcessWithNoDependencies() {
    String source = "// A CommonJS module without dependencies.\n" +
        "define(function(require, exports, module) {\n" +
        "  exports.hello = function() {\n" +
        "    console.log('Hello, world!');\n" +
        "  };\n" +
        "});";

    Node root = parser.parse(source, SourceName.create("test.js"));
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getCompiler(), FILENAME_PREFIX, false);
    pass.process(null, root);

    Node module = root.getFirstChild();
    assertNotNull(module);
    assertEquals("module$test", module.getString());
    Node exports = module.getFirstChild();
    assertEquals("exports", exports.getString());
    Node helloFunction = exports.getLastChild();
    assertEquals("hello", helloFunction.getString());
    Node functionBody = helloFunction.getFirstChild();
    assertEquals("function () {", functionBody.getString());
    Node consoleLog = functionBody.getLastChild();
    assertEquals("console.log('Hello, world!');", consoleLog.getString());

    assertNull(getCompiler().getInput().getRequireList());
  }

  @org.junit.Test
  public void testProcessWithRelativeRequire() {
    String source = "// A CommonJS module with a relative require.\n" +
        "define(function(require, exports, module) {\n" +
        "  var foo = require('../bar');\n" +
        "});\n" +
        "\n" +
        "// A depended CommonJS module.\n" +
        "define(function(exports) {\n" +
        "  exports.bar = function() {\n" +
        "    return 'Bar';\n" +
        "  };\n" +
        "});";

    Node root = parser.parse(source, SourceName.create("test.js"));
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getCompiler(), FILENAME_PREFIX, true);
    pass.process(null, root);

    Set<String> requiredModules = getCompiler().getInput().getRequireList();
    assertEquals(1, requiredModules.size());
    assertEquals("bar", requiredModules.iterator().next());
  }

  @org.junit.Test
  public void testProcessWithAbsoluteRequire() {
    String source = "// A CommonJS module with an absolute require.\n" +
        "define(function(require, exports, module) {\n" +
        "  var foo = require('http://example.com/bar');\n" +
        "});";

    Node root = parser.parse(source, SourceName.create("test.js"));
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getCompiler(), FILENAME_PREFIX, true);

    try {
      pass.process(null, root);
      fail("Expected URISyntaxException");
    } catch (URISyntaxException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testProcessWithEmptyFileName() {
    String source = "// A CommonJS module with an empty filename.\n" +
        "define(function(require, exports, module) {});";

    Node root = parser.parse(source, SourceName.create(""));
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getCompiler(), FILENAME_PREFIX, true);

    try {
      pass.process(null, root);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}