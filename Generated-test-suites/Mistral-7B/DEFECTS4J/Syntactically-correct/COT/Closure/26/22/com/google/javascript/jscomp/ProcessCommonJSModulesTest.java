import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String input = "// File: input.js\n" +
        "var a = require('module1');\n" +
        "var b = require('./module2');\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "goog.require('module2');\n" +
        "test.main = function() {\n" +
        "  console.log(a);\n" +
        "  console.log(b);\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    compiler.compile(new File("input.js"), options, null, new ProcessCommonJSModules(
        compiler, ".", false));

    String expectedOutput = "// File: output.js\n" +
        "goog.provide('test');\n" +
        "goog.require('module1$module$exports');\n" +
        "goog.require('module2$module$exports');\n" +
        "test.main = function() {\n" +
        "  console.log(module1$module$exports);\n" +
        "  console.log(module2$module$exports);\n" +
        "};\n";

    assertEquals(expectedOutput, new String(new File("output.js").getText()));
  }

  @Test
  public void testProcessCommonJSModulesWithDependenciesReporting()
      throws IOException, URISyntaxException {
    String input = "// File: input.js\n" +
        "var a = require('module1');\n" +
        "var b = require('./module2');\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "goog.require('module2');\n" +
        "test.main = function() {\n" +
        "  console.log(a);\n" +
        "  console.log(b);\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarnOptions(CompilerOptions.WarnOption.UNDEFINED,
        CompilerOptions.WarnOption.UNUSED_VAR);

    Compiler compiler = new Compiler();
    compiler.compile(new File("input.js"), options, null,
        new ProcessCommonJSModules(compiler, ".", true));

    Set<String> expectedRequires = Sets.newHashSet("module1", "module2");

    assertEquals(expectedRequires, compiler.getInput().getRequireNames());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequires()
      throws IOException, URISyntaxException {
    String input = "// File: input.js\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "goog.require('./module2');\n" +
        "test.main = function() {\n" +
        "  console.log(module1);\n" +
        "  console.log(module2);\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    compiler.compile(new File("input.js"), options, null,
        new ProcessCommonJSModules(compiler, ".", false));

    String expectedOutput = "// File: output.js\n" +
        "goog.provide('test');\n" +
        "goog.require('module1$module$exports');\n" +
        "goog.require('input$module2$module$exports');\n" +
        "test.main = function() {\n" +
        "  console.log(module1$module$exports);\n" +
        "  console.log(input$module2$module$exports);\n" +
        "};\n";

    assertEquals(expectedOutput, new String(new File("output.js").getText()));
  }

  @Test
  public void testProcessCommonJSModulesWithScripts()
      throws IOException, URISyntaxException {
    String input = "// File: input.js\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "var a = 1;\n" +
        "goog.require('module2');\n" +
        "var b = 2;\n" +
        "test.main = function() {\n" +
        "  console.log(a);\n" +
        "  console.log(b);\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    compiler.compile(new File("input.js"), options, null,
        new ProcessCommonJSModules(compiler, ".", false));

    String expectedOutput = "// File: output.js\n" +
        "goog.provide('test');\n" +
        "goog.require('module1$module$exports');\n" +
        "goog.require('module2$module$exports');\n" +
        "goog.scope(\"test\");\n" +
        "var a = 1;\n" +
        "goog.scope(\"test\");\n" +
        "var b = 2;\n" +
        "test.main = function() {\n" +
        "  console.log(module1$module$exports);\n" +
        "  console.log(module2$module$exports);\n" +
        "};\n" +
        "goog.scope(\"test\");\n";

    assertEquals(expectedOutput, new String(new File("output.js").getText()));
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports()
      throws IOException, URISyntaxException {
    String input = "// File: module2.js\n" +
        "goog.provide('module2');\n" +
        "module2.exports = { foo: 1 };\n";

    String input2 = "// File: input.js\n" +
        "goog.provide('test');\n" +
        "goog.require('module2');\n" +
        "test.main = function() {\n" +
        "  console.log(module2.exports.foo);\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    compiler.compile(new File("input.js"), options, null,
        new ProcessCommonJSModules(compiler, ".", false));

    String expectedOutput = "// File: output.js\n" +
        "goog.provide('test');\n" +
        "goog.require('module2$module$exports');\n" +
        "goog.scope(\"test\");\n" +
        "test.main = function() {\n" +
        "  console.log(module2$module$exports.foo);\n" +
        "};\n" +
        "goog.scope(\"test\");\n";

    assertEquals(expectedOutput, new String(new File("output.js").getText()));
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithInvalidURISyntaxException()
      throws IOException, URISyntaxException {
    String input = "// File: input.js\n" +
        "goog.provide('test');\n" +
        "goog.require('./module2');\n" +
        "goog.require('module1');\n" +
        "test.main = function() {\n" +
        "  console.log(module1);\n" +
        "  console.log(module2);\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    compiler.compile(new File("input.js"), options, null,
        new ProcessCommonJSModules(compiler, ".", false));

    fail("Expected a RuntimeException due to the invalid URI syntax.");
  }
}

class JSModule {
  private String name;

  JSModule(String name) {
    this.name = name;
  }

  void addAndOverrideModule(CompilerInput ci) {
    ci.getInputFiles().add(new SourceName(name + ".js"));
  }
}