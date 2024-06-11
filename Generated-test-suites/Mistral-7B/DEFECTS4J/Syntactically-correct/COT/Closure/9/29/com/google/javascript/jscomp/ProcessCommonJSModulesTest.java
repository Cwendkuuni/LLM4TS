import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.NodeFactory.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private CompilerResult result;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testProcessCommonJSModules_BasicFunctionality() {
    String input = "// input file\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n";

    String expectedOutput = "// input file\n" +
        "goog.provide(\"module.foo\");\n" +
        "goog.module.exports = module.exports;\n" +
        "var module = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "./");
    result = compiler.compile(input, null, pass);
    assertEquals(expectedOutput, result.toSource());
  }

  @Test
  public void testProcessCommonJSModules_Require() {
    String input = "// input file 1\n" +
        "var module1 = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// input file 2\n" +
        "var module2 = {\n" +
        "  exports: {\n" +
        "    baz: function() {\n" +
        "      require('module1').foo();\n" +
        "    }\n" +
        "  }\n" +
        "};\n";

    String expectedOutput = "// input file 1\n" +
        "goog.provide(\"module1.foo\");\n" +
        "goog.module.exports = module1.exports;\n" +
        "var module1 = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// input file 2\n" +
        "goog.provide(\"module2.baz\");\n" +
        "goog.module.exports = module2.exports;\n" +
        "var module2 = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    baz: function() {\n" +
        "      goog.require('module1.foo');\n" +
        "      foo();\n" +
        "    }\n" +
        "  }\n" +
        "};\n";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "./");
    result = compiler.compile(input, null, pass, null, null, null);
    assertEquals(expectedOutput, result.toSource());
  }

  @Test
  public void testProcessCommonJSModules_ModuleExports() {
    String input = "// input file\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "goog.module.exports = module.exports;\n" +
        "exports.exportedFoo = function() {\n" +
        "  return foo();\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n";

    String expectedOutput = "// input file\n" +
        "goog.provide(\"module.foo\");\n" +
        "goog.module.exports = module.exports;\n" +
        "var module = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "goog.provide(\"module.exportedFoo\");\n" +
        "goog.module.exports.exportedFoo = function() {\n" +
        "  return goog.require('module.foo').foo;\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "./");
    result = compiler.compile(input, null, pass, null, null, null);
    assertEquals(expectedOutput, result.toSource());
  }

  @Test
  public void testProcessCommonJSModules_RelativeRequire()
      throws IOException, URISyntaxException {
    String input = "// input file 1\n" +
        "var module1 = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// input file 2\n" +
        "var module2 = {\n" +
        "  exports: {\n" +
        "    baz: function() {\n" +
        "      require('./module1').foo();\n" +
        "    }\n" +
        "  }\n" +
        "};\n";

    URI baseURI = new URI("file:/path/to/input");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, baseURI.getPath());
    result = compiler.compileFileInput(input, baseURI, pass, null, null, null);
    assertEquals(expectedOutput, result.getCode());

    String expectedOutput = "// input file 1\n" +
        "goog.provide(\"module1.foo\");\n" +
        "goog.module.exports = module1.exports;\n" +
        "var module1 = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// input file 2\n" +
        "goog.provide(\"module2.baz\");\n" +
        "goog.module.exports = module2.exports;\n" +
        "var module2 = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    baz: function() {\n" +
        "      goog.require('./module1.foo').foo;\n" +
        "    }\n" +
        "  }\n" +
        "};\n";
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_InvalidRequire() {
    String input = "// input file\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// invalid require statement\n" +
        "require('module');";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "./");
    result = compiler.compile(input, null, pass);
    fail("Expected a RuntimeException but got: " + result.toSource());
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_InvalidRequireRelativePath() {
    String input = "// input file\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// invalid require relative path statement\n" +
        "require('../module');";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "./");
    result = compiler.compile(input, null, pass);
    fail("Expected a RuntimeException but got: " + result.toSource());
  }

  @Test
  public void testProcessCommonJSModules_URIFragment() {
    String input = "// input file\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// input file with fragment\n" +
        "var module2 = {\n" +
        "  exports: {\n" +
        "    baz: function() {\n" +
        "      require('#module');\n" +
        "    }\n" +
        "  }\n" +
        "};\n";

    URI baseURI = new URI("file:/path/to/input");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, baseURI.getPath());
    result = compiler.compileFileInput(input, baseURI, pass, null, null, null);
    String expectedOutput = "// input file\n" +
        "goog.provide(\"module.foo\");\n" +
        "goog.module.exports = module.exports;\n" +
        "var module = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "function foo() {\n" +
        "}\n" +
        "\n" +
        "// input file with fragment\n" +
        "goog.provide(\"module2.baz\");\n" +
        "goog.module.exports = module2.exports;\n" +
        "var module2 = {\n" +
        "  __esModule: true,\n" +
        "  exports: {\n" +
        "    baz: function() {\n" +
        "      goog.require('#module.foo');\n" +
        "    }\n" +
        "  }\n" +
        "};\n";

    assertEquals(expectedOutput, result.getCode());
  }

  // ... Add more test cases for edge cases and exceptions as needed

  @After
  public void tearDown() {
    compiler = null;
    result = null;
  }
}