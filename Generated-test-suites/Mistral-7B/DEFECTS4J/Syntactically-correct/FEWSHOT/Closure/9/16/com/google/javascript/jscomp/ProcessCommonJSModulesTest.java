package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.Scope;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String inputCode = "// Input code\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      return 'Hello, World!';\n" +
        "    }\n" +
        "  },\n" +
        "  require: function(name) {\n" +
        "    return {\n" +
        "      foo: function() {\n" +
        "        return 'Hello from require!';\n" +
        "      }\n" +
        "    };\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Call require('foo') from module\n" +
        "console.log(require('foo')());\n" +
        "\n" +
        "// Output code\n" +
        "goog.provide('myModule');\n" +
        "myModule.exports = myModule;\n" +
        "\n" +
        "myModule.exports.foo = function() {\n" +
        "  return 'Hello, World!';\n" +
        "};\n" +
        "\n" +
        "myModule.requires = {};\n" +
        "myModule.require = function(name) {\n" +
        "  return myModule.requires[name] || {\n" +
        "    foo: function() {\n" +
        "      return 'Hello from require!';\n" +
        "    }\n" +
        "  };\n" +
        "};\n" +
        "\n" +
        "// Use myModule.require('foo') instead of require('foo')\n" +
        "console.log(myModule.require('foo')());";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    AbstractCompiler compiler = new ProcessCommonJSModules(new Processor(), "");
    Processor processor = new Processor(options);
    processor.addPass(compiler);
    Node root = processor.processString(input, null, null);
    Node module = root.getFirstChild();
    Node exports = module.getChildAtIndex(0);
    Node require = module.getChildAtIndex(1);

    // Check that the correct code has been generated
    assertEquals("myModule", exports.getQualifiedName());
    assertEquals("myModule.exports", exports.getChildAtIndex(0).getQualifiedName());
    assertEquals("myModule.requires", require.getQualifiedName());
    assertEquals("myModule.require", require.getChildAtIndex(0).getQualifiedName());

    // Check that the require call has been rewritten
    Node call = require.getChildAtIndex(1);
    assertEquals("myModule.require", call.getQualifiedName());
    assertNotNull(call.getFirstChild());
    assertEquals("foo", call.getFirstChild().getQualifiedName());

    // Check that the module export function has been renamed to avoid conflicts
    Node exportFunction = exports.getChildAtIndex(0);
    assertEquals("foo", exportFunction.getQualifiedName() + "$$myModule");

    // Check that the require function call in the console.log statement has been rewritten
    Node logCall = root.getLastChild();
    assertEquals("myModule.require", logCall.getChildAtIndex(0).getQualifiedName());
    assertEquals("foo", logCall.getChildAtIndex(1).getQualifiedName());
  }

  @Test
  public void testProcessCommonJSModulesRelativeRequire() throws Exception {
    String inputCode = "// Input code\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      return 'Hello, World!';\n" +
        "    }\n" +
        "  },\n" +
        "  require: function(name) {\n" +
        "    var m = this.require;\n" +
        "    return m(name.slice(2));\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Call require('./foo') from module\n" +
        "console.log(require('./foo')());\n" +
        "\n" +
        "// Output code\n" +
        "goog.provide('myModule');\n" +
        "myModule.exports = myModule;\n" +
        "\n" +
        "myModule.exports.foo = function() {\n" +
        "  return 'Hello, World!';\n" +
        "};\n" +
        "\n" +
        "myModule.requires = {};\n" +
        "myModule.require = function(name) {\n" +
        "  return myModule.requires[name] || {\n" +
        "    foo: function() {\n" +
        "      return 'Hello from require!';\n" +
        "    }\n" +
        "  };\n" +
        "};\n" +
        "\n" +
        "// Use myModule.require('./foo') instead of require('./foo')\n" +
        "console.log(myModule.require('./foo')());";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    AbstractCompiler compiler = new ProcessCommonJSModules(new Processor(), "");
    Processor processor = new Processor(options);
    processor.addPass(compiler);
    Node root = processor.processString(input, null, null);
    Node module = root.getFirstChild();
    Node exports = module.getChildAtIndex(0);
    Node require = module.getChildAtIndex(1);

    // Check that the correct code has been generated
    assertEquals("myModule", exports.getQualifiedName());
    assertEquals("myModule.exports", exports.getChildAtIndex(0).getQualifiedName());
    assertEquals("myModule.requires", require.getQualifiedName());
    assertEquals("myModule.require", require.getChildAtIndex(0).getQualifiedName());

    // Check that the require call with a relative path has been rewritten
    Node call = require.getChildAtIndex(1);
    assertEquals("myModule.require", call.getQualifiedName());
    Node relativePath = call.getChildAtIndex(0);
    assertEquals("../foo", relativePath.getString());

    // Check that the module export function and require function call in the console.log statement have been rewritten
    Node logCall = root.getLastChild();
    assertEquals("myModule.require", logCall.getChildAtIndex(0).getQualifiedName());
    Node logCallRelativePath = logCall.getChildAtIndex(1);
    assertEquals("../foo", logCallRelativePath.getString());
  }

  @Test
  public void testProcessCommonJSModulesAbsoluteRequire() throws Exception {
    String inputCode = "// Input code\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      return 'Hello, World!';\n" +
        "    }\n" +
        "  },\n" +
        "  require: function(name) {\n" +
        "    return require(name);\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Call require('path/to/foo') from module\n" +
        "console.log(require('path/to/foo')());\n" +
        "\n" +
        "// Output code\n" +
        "goog.provide('myModule');\n" +
        "myModule.exports = myModule;\n" +
        "\n" +
        "myModule.exports.foo = function() {\n" +
        "  return 'Hello, World!';\n" +
        "};\n" +
        "\n" +
        "myModule.requires = {\n" +
        "  'path/to/foo': {\n" +
        "    foo: function() {\n" +
        "      return 'Hello from require!';\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "myModule.require = function(name) {\n" +
        "  return myModule.requires[name] || {\n" +
        "    foo: function() {\n" +
        "      return 'Hello from require!';\n" +
        "    }\n" +
        "  };\n" +
        "};\n" +
        "\n" +
        "// Use myModule.require('path/to/foo') instead of require('path/to/foo')\n" +
        "console.log(myModule.require('path/to/foo')());";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    AbstractCompiler compiler = new ProcessCommonJSModules(new Processor(), "");
    Processor processor = new Processor(options);
    processor.addPass(compiler);
    Node root = processor.processString(input, null, null);
    Node module = root.getFirstChild();
    Node exports = module.getChildAtIndex(0);
    Node require = module.getChildAtIndex(1);

    // Check that the correct code has been generated
    assertEquals("myModule", exports.getQualifiedName());
    assertEquals("myModule.exports", exports.getChildAtIndex(0).getQualifiedName());
    assertEquals("myModule.requires", require.getQualifiedName());
    assertEquals("myModule.require", require.getChildAtIndex(0).getQualifiedName());

    // Check that the require call with an absolute path has been rewritten
    Node call = require.getChildAtIndex(1);
    assertEquals("myModule.require", call.getQualifiedName());
    Node absolutePath = call.getChildAtIndex(0);
    assertEquals("path/to/foo", absolutePath.getString());

    // Check that the module export function and require function call in the console.log statement have been rewritten
    Node logCall = root.getLastChild();
    assertEquals("myModule.require", logCall.getChildAtIndex(0).getQualifiedName());
    Node logCallAbsolutePath = logCall.getChildAtIndex(1);
    assertEquals("path/to/foo", logCallAbsolutePath.getString());
  }

  @Test
  public void testProcessCommonJSModulesNoRequire() throws Exception {
    String inputCode = "// Input code\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      return 'Hello, World!';\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Output code\n" +
        "goog.provide('myModule');\n" +
        "myModule.exports = myModule;\n" +
        "\n" +
        "myModule.exports.foo = function() {\n" +
        "  return 'Hello, World!';\n" +
        "};";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    AbstractCompiler compiler = new ProcessCommonJSModules(new Processor(), "");
    Processor processor = new Processor(options);
    processor.addPass(compiler);
    Node root = processor.processString(input, null, null);
    Node module = root.getFirstChild();
    Node exports = module.getChildAtIndex(0);

    // Check that the correct code has been generated without require statements
    assertEquals("myModule", exports.getQualifiedName());
    assertEquals("myModule.exports", exports.getChildAtIndex(0).getQualifiedName());
  }

  @Test
  public void testProcessCommonJSModulesNoExports() throws Exception {
    String inputCode = "// Input code\n" +
        "var module = {\n" +
        "  require: function(name) {\n" +
        "    return require(name);\n" +
        "  }\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    AbstractCompiler compiler = new ProcessCommonJSModules(new Processor(), "");
    Processor processor = new Processor(options);
    processor.addPass(compiler);
    Node root = processor.processString(input, null, null);

    // Check that no code is generated for a module without exports
    assertNull(root);
  }
}