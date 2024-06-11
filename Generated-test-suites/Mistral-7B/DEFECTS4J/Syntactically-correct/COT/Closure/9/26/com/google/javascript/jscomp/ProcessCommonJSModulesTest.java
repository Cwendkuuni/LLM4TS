import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    input = new CompilerInput("test.js", null);
    root = script(
        "require('./module1');",
        "module.exports = 'Hello World';"
    );
  }

  @Test
  public void testProcessCommonJSModules() {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.exports = 'Hello World';\n" +
        "goog.require('test.module1');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = require('" + moduleName + "');",
        "module.exports = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeModuleName() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = require('./" + moduleName + "');",
        "module.exports = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithAbsoluteModuleName() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = require('" + moduleName + ".js');",
        "module.exports = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    String expected = "goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "test.exports;";

    try {
      URI uri = new URI("test.js");
      String absoluteModuleName = new URI(uri, new URI(moduleName + ".js")).toString();
      assertEquals(expected, result.toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleModules() {
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = require('" + moduleName1 + "');",
        "module.exports = require('" + moduleName2 + "');",
        "module.exports = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    String expected = "goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.module2 = undefined;\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "goog.require('test.module2');\n" +
        "test.exports;";

    assertEquals(expected, result.toString());
  }

  @Test(expected = AssertionError.class)
  public void testProcessCommonJSModulesWithMultipleModulesAndConflictingNames() {
    String moduleName1 = "module1";
    String moduleName2 = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = require('" + moduleName1 + "');",
        "module.exports = require('" + moduleName2 + "');",
        "module.exports = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports_2() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "exports = require('" + moduleName + "');",
        "exports = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports_3() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = {foo: 'bar'};",
        "require('" + moduleName + "').foo = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = {\n" +
        "  foo: undefined\n" +
        "};\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "test.exports.foo;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleRequireCalls() {
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    input = new CompilerInput("test.js", null);
    root = script(
        "var m1 = require('" + moduleName1 + "');",
        "var m2 = require('" + moduleName2 + "');",
        "module.exports = m1 + m2;"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.module2 = undefined;\n" +
        "test.exports = test.module1 + test.module2;\n" +
        "goog.require('test.module1');\n" +
        "goog.require('test.module2');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithNamedRequireCalls() {
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    input = new CompilerInput("test.js", null);
    root = script(
        "var m1 = require('" + moduleName1 + "');",
        "var m2 = require('" + moduleName2 + "');",
        "module.exports = m1.foo + m2.bar;"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.module2 = undefined;\n" +
        "test.exports = test.module1.foo + test.module2.bar;\n" +
        "goog.require('test.module1');\n" +
        "goog.require('test.module2');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExportsReassignment() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = require('" + moduleName + "');",
        "module.exports = 42;"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.exports = 42;\n" +
        "goog.require('test.module1');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExportsNestedAssignment() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    root = script(
        "module.exports = {\n" +
        "  foo: require('" + moduleName + "'),\n" +
        "  bar: 'Hello World'\n" +
        "};",
        "module.exports = 'Hello World';"
    );

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithModulesDefinedOutsideOfScript() {
    String moduleName = "module1";
    input = new CompilerInput("test.js", null);
    Node module1Node = declaration(
        "var module1 = {'exports': 'Hello World'};\n" +
        "goog.provide('" + moduleName + "');"
    );
    root = root.addChildToFront(module1Node);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = {'exports': 'Hello World'};\n" +
        "test.exports = test.module1.exports;\n" +
        "goog.require('test.module1');\n" +
        "test.exports;", result.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithModulesDefinedOutsideOfScriptWithDependencies() {
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    input = new CompilerInput("test.js", null);
    Node module1Node = declaration(
        "var module1 = require('" + moduleName2 + "');\n" +
        "goog.provide('" + moduleName1 + "');"
    );
    Node module2Node = declaration(
        "goog.provide('" + moduleName2 + "');"
    );
    root = root.addChildToFront(module1Node).addChildToBack(module2Node);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, root);
    Node result = input.getCompiledResult();

    assertEquals("goog.provide('test');\n" +
        "test.module1 = undefined;\n" +
        "test.module2 = undefined;\n" +
        "test.exports = test.module1;\n" +
        "goog.require('test.module1');\n" +
        "goog.require('test.module2');\n" +
        "test.exports;", result.toString());
  }
}