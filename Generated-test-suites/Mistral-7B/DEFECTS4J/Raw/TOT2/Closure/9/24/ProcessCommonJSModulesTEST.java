import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSDOMGlobal.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  private Compiler compiler;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() {
    compiler = new Compiler();
    processor = new ProcessCommonJSModules(compiler, "");
  }

  @Test
  public void testProcessCommonJSModules() {
    String source = "// A simple CommonJS module\n" +
        "define(function() {\n" +
        "  function hello() {\n" +
        "    return 'Hello, world!';\n" +
        "  }\n" +
        "  return { hello: hello };\n" +
        "});";

    Node root = compiler.parseCompilationUnit(source, null);
    processor.process(null, root);

    JSModule module = processor.getModule();
    assertNotNull(module);

    String expectedOutput = "// A simple CommonJS module\n" +
        "goog.provide(\"module$name\");\n" +
        "goog.require(\"goog.provide\");\n" +
        "goog.require(\"goog.require\");\n" +
        "module$name = function() {\n" +
        "  return 'Hello, world!';\n" +
        "};\n" +
        "module$name.module$exports = module$name;";

    String actualOutput = compiler.getOutputText();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithDependencies() {
    String source1 = "// Module A\n" +
        "define(function(require) {\n" +
        "  var foo = require(\"moduleB\");\n" +
        "  function bar() {\n" +
        "    return foo.baz();\n" +
        "  }\n" +
        "  return { bar: bar };\n" +
        "});";

    String source2 = "// Module B\n" +
        "define(function() {\n" +
        "  function baz() {\n" +
        "    return 'Hello, Module B!';\n" +
        "  }\n" +
        "  return { baz: baz };\n" +
        "});";

    Node rootA = compiler.parseCompilationUnit(source1, null);
    Node rootB = compiler.parseCompilationUnit(source2, null);

    compiler.addTopLevelCode(sourceB);
    processor = new ProcessCommonJSModules(compiler, "");
    processor.process(null, rootA);

    JSModule moduleA = processor.getModule();
    assertNotNull(moduleA);

    String expectedOutput = "// Module A\n" +
        "goog.provide(\"module$nameA\");\n" +
        "goog.require(\"goog.provide\");\n" +
        "goog.require(\"goog.require\");\n" +
        "goog.require(\"module$nameB\");\n" +
        "module$nameA = function() {\n" +
        "  var module$nameB = goog.require(\"module$nameB\");\n" +
        "  function bar() {\n" +
        "    return module$nameB.baz();\n" +
        "  }\n" +
        "  module$nameA.module$exports = {\n" +
        "    bar: bar\n" +
        "  };\n" +
        "};\n" +
        "module$nameA.module$exports.bar.module$exports = module$nameA.module$exports.bar;\n" +
        "module$nameB = function() {\n" +
        "  function baz() {\n" +
        "    return 'Hello, Module B!';\n" +
        "  }\n" +
        "  module$nameB.module$exports = module$nameB;\n" +
        "};\n" +
        "goog.provide(\"module$nameB\");";

    String actualOutput = compiler.getOutputText();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    String source = "// A simple CommonJS module\n" +
        "define(function() {\n" +
        "  var foo = 'Hello, World!';\n" +
        "  module.exports = foo;\n" +
        "});";

    Node root = compiler.parseCompilationUnit(source, null);
    processor = new ProcessCommonJSModules(compiler, "");
    processor.process(null, root);

    JSModule module = processor.getModule();
    assertNotNull(module);

    String expectedOutput = "// A simple CommonJS module\n" +
        "goog.provide(\"module$name\");\n" +
        "goog.require(\"goog.provide\");\n" +
        "goog.require(\"goog.require\");\n" +
        "module$name = 'Hello, World!';\n" +
        "module$name.module$exports = module$name;\n" +
        "goog.provide(\"module$name\");";

    String actualOutput = compiler.getOutputText();
    assertEquals(expectedOutput, actualOutput);
  }

  @After
  public void tearDown() {
    compiler = null;
  }
}