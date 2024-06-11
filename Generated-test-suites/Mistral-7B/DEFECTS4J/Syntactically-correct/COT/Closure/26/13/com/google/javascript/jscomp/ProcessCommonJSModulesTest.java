import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// Input file\n" +
        "var a = require('module1');\n" +
        "var b = require('module2');\n" +
        "function main() {\n" +
        "  // Do something with a and b.\n" +
        "}\n" +
        "main();";

    CompilerInput ci = new CompilerInput("test.js", input, null);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(ci.getExterns(), ci.getRootNode());

    Node root = ci.getRootNode();
    assertEquals(3, root.getChildCount());

    Node main = root.getChildAtIndex(0);
    assertEquals(1, main.getChildCount());

    Node requireCalls = root.getChildAtIndex(1);
    assertEquals(2, requireCalls.getChildCount());

    Node module1Call = requireCalls.getChildAtIndex(0);
    assertEquals("module1", module1Call.getChildAtIndex(1).getString());

    Node module2Call = requireCalls.getChildAtIndex(1);
    assertEquals("module2", module2Call.getChildAtIndex(1).getString());

    Node script = root.getChildAtIndex(2);
    assertEquals(1, script.getChildCount());

    Node mainFunction = script.getChildAtIndex(0);
    assertEquals("main", mainFunction.getName());
    assertEquals(1, mainFunction.getChildCount());

    Node body = mainFunction.getChildAtIndex(0);
    assertEquals(2, body.getChildCount());

    Node aAssignment = body.getChildAtIndex(0);
    assertEquals("a", aAssignment.getChildAtIndex(1).getName());

    Node bAssignment = body.getChildAtIndex(1);
    assertEquals("b", bAssignment.getChildAtIndex(1).getName());
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidURISyntaxException() throws Exception {
    String input = "// Input file\n" +
        "var a = require('module://invalid/path/to/module');\n" +
        "function main() {\n" +
        "  // Do something with a.\n" +
        "}\n" +
        "main();";

    CompilerInput ci = new CompilerInput("test.js", input, null);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(ci.getExterns(), ci.getRootNode());
  }

  @Test
  public void testRelativePath() throws Exception {
    String input = "// Input file\n" +
        "var a = require('./module1');\n" +
        "var b = require('../module2');\n" +
        "function main() {\n" +
        "  // Do something with a and b.\n" +
        "}\n" +
        "main();";

    CompilerInput ci = new CompilerInput("test.js", input, null);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(ci.getExterns(), ci.getRootNode());

    Node root = ci.getRootNode();
    assertEquals(3, root.getChildCount());

    Node main = root.getChildAtIndex(0);
    assertEquals(1, main.getChildCount());

    Node requireCalls = root.getChildAtIndex(1);
    assertEquals(2, requireCalls.getChildCount());

    Node module1Call = requireCalls.getChildAtIndex(0);
    assertEquals("module1", module1Call.getChildAtIndex(1).getString());

    Node module2Call = requireCalls.getChildAtIndex(1);
    assertEquals("module2", module2Call.getChildAtIndex(1).getString());

    Node script = root.getChildAtIndex(2);
    assertEquals(1, script.getChildCount());

    Node mainFunction = script.getChildAtIndex(0);
    assertEquals("main", mainFunction.getName());
    assertEquals(1, mainFunction.getChildCount());

    Node body = mainFunction.getChildAtIndex(0);
    assertEquals(2, body.getChildCount());

    Node aAssignment = body.getChildAtIndex(0);
    assertEquals("a", aAssignment.getChildAtIndex(1).getName());

    Node bAssignment = body.getChildAtIndex(1);
    assertEquals("b", bAssignment.getChildAtIndex(1).getName());
  }

  @Test
  public void testProvide() throws Exception {
    String input = "// Input file\n" +
        "/**\n" +
        " * @preserve\n" +
        " */\n" +
        "goog.provide('module1');\n" +
        "\n" +
        "// Module 1 code.\n";

    CompilerInput ci = new CompilerInput("module1.js", input, null);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(ci.getExterns(), ci.getRootNode());

    String output = ci.getOutputText();
    assertTrue(output.contains("module1.module$exports = module1;"));
  }

  @Test
  public void testReportDependencies() throws Exception {
    String input = "// Input file\n" +
        "var a = require('module1');\n" +
        "function main() {\n" +
        "  // Do something with a.\n" +
        "}\n" +
        "main();";

    CompilerInput ci = new CompilerInput("test.js", input, null);
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.ERROR);
    options.setGenerateErrors(true);
    AbstractCompiler compiler = new AbstractCompiler(options);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", true);
    processor.process(ci.getExterns(), ci.getRootNode());

    Set<String> requires = Sets.newHashSet(compiler.getErrors().get(0)
        .getMessage().split("Requires module"));
    assertEquals(1, requires.size());
    assertEquals("module1", requires.iterator().next());
  }

  @Test
  public void testModuleExportsOverride() throws Exception {
    String input = "// Input file\n" +
        "goog.provide('module1');\n" +
        "\n" +
        "module1.exports = {'foo': 'bar'};\n";

    CompilerInput ci = new CompilerInput("module1.js", input, null);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(ci.getExterns(), ci.getRootNode());

    String output = ci.getOutputText();
    String expected = "module1.module$exports = {\n" +
        "  'foo': 'bar'\n" +
        "};\n";
    assertEquals(expected, output.substring(output.indexOf("module1.module$exports = "),
        output.indexOf("};\n")));
  }
}