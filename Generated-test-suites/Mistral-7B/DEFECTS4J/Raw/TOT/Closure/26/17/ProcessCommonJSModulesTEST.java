import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// Test file\n" +
        "require('module1');\n" +
        "function test() {\n" +
        "  print(module1.foo);\n" +
        "}\n" +
        "test();";

    Node externs = null;
    Node root = IR.parse(compiler.getSourceFactory(), input).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(externs, root);

    String output = IR.toString(root);

    String expectedOutput = "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "function test() {\n" +
        "  print(module1.module$exports.foo);\n" +
        "}\n" +
        "test();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithDependencies() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// File module1.js\n" +
        "goog.provide('module1');\n" +
        "goog.exports = {'foo': 42};\n";

    String testInput = "// Test file\n" +
        "require('module1');\n" +
        "function test() {\n" +
        "  print(module1.foo);\n" +
        "}\n" +
        "test();";

    Node module1 = IR.parse(compiler.getSourceFactory(), input).getTree();
    Node externs = compiler.getSourceFactory().getSource(new URI("file:" +
        "module1.js"), null).asNode();

    Node root = IR.parse(compiler.getSourceFactory(), testInput).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(externs, root);

    String output = IR.toString(root);

    String expectedOutput = "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "function test() {\n" +
        "  print(module1.module$exports.foo);\n" +
        "}\n" +
        "test();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// Test file\n" +
        "require('./module1');\n" +
        "function test() {\n" +
        "  print(module1.foo);\n" +
        "}\n" +
        "test();";

    String testInput = "// File module1.js\n" +
        "goog.provide('module1');\n" +
        "goog.exports = {'foo': 42};\n";

    Node module1 = IR.parse(compiler.getSourceFactory(), testInput).getTree();
    Node externs = compiler.getSourceFactory().getSource(new URI("file:" +
        "module1.js"), null).asNode();

    Node root = IR.parse(compiler.getSourceFactory(), input).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(externs, root);

    String output = IR.toString(root);

    String expectedOutput = "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "function test() {\n" +
        "  print(module1.module$exports.foo);\n" +
        "}\n" +
        "test();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithEmptySourceFileName() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// Test file\n" +
        "require('module1');\n" +
        "function test() {\n" +
        "  print(module1.foo);\n" +
        "}\n" +
        "test();";

    Node root = IR.parse(compiler.getSourceFactory(), input).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        "", false);
    pass.process(null, root);

    String output = IR.toString(root);

    String expectedOutput = "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "function test() {\n" +
        "  print(module1.module$exports.foo);\n" +
        "}\n" +
        "test();";

    assertEquals(expectedOutput, output);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithIllegalRequireFormat() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// Test file\n" +
        "require('module1.js');\n" +
        "function test() {\n" +
        "  print(module1.foo);\n" +
        "}\n" +
        "test();";

    Node root = IR.parse(compiler.getSourceFactory(), input).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(null, root);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithInvalidModuleName() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// Test file\n" +
        "require('invalid-module-name');\n" +
        "function test() {\n" +
        "  print(module1.foo);\n" +
        "}\n" +
        "test();";

    Node root = IR.parse(compiler.getSourceFactory(), input).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(null, root);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithNonExistingModule() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// Test file\n" +
        "require('non-existing-module');\n" +
        "function test() {\n" +
        "  print(module1.foo);\n" +
        "}\n" +
        "test();";

    Node root = IR.parse(compiler.getSourceFactory(), input).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(null, root);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// File module1.js\n" +
        "goog.provide('module1');\n" +
        "goog.exports = {'foo': 42};\n";

    String testInput = "// Test file\n" +
        "require('module1');\n" +
        "function test() {\n" +
        "  print(module1.exports.foo);\n" +
        "}\n" +
        "test();";

    Node module1 = IR.parse(compiler.getSourceFactory(), input).getTree();
    Node externs = compiler.getSourceFactory().getSource(new URI("file:" +
        "module1.js"), null).asNode();

    Node root = IR.parse(compiler.getSourceFactory(), testInput).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(externs, root);

    String output = IR.toString(root);

    String expectedOutput = "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "function test() {\n" +
        "  print(module1.module$exports.foo);\n" +
        "}\n" +
        "test();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExportsRenamed() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// File module1.js\n" +
        "goog.provide('module1');\n" +
        "goog.exports = {'foo': 42};\n";

    String testInput = "// Test file\n" +
        "require('module1');\n" +
        "function test() {\n" +
        "  print(module1.exports.$$myCustomName);\n" +
        "}\n" +
        "test();";

    Node module1 = IR.parse(compiler.getSourceFactory(), input).getTree();
    Node externs = compiler.getSourceFactory().getSource(new URI("file:" +
        "module1.js"), null).asNode();

    Node root = IR.parse(compiler.getSourceFactory(), testInput).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(externs, root);

    String output = IR.toString(root);

    String expectedOutput = "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "function test() {\n" +
        "  print(module1.module$exports.$$myCustomName);\n" +
        "}\n" +
        "test();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleRequires() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);

    Compiler compiler = new Compiler(options);
    String input = "// Test file\n" +
        "require('module1');\n" +
        "require('module2');\n" +
        "function test() {\n" +
        "  print(module1.foo + module2.bar);\n" +
        "}\n" +
        "test();";

    Node module1 = IR.parse(compiler.getSourceFactory(),
        "goog.provide('module1');\n" +
        "goog.exports = {'foo': 42};").getTree();
    Node module2 = IR.parse(compiler.getSourceFactory(),
        "goog.provide('module2');\n" +
        "goog.exports = {'bar': 7};").getTree();

    Node externs = IR.newList(module1, module2).toNode(compiler.getSourceFactory());

    Node root = IR.parse(compiler.getSourceFactory(), input).getTree();

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
        ".", false);
    pass.process(externs, root);

    String output = IR.toString(root);

    String expectedOutput = "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "goog.require('module2');\n" +
        "function test() {\n" +
        "  print(module1.module$exports.foo + module2.module$exports.bar);\n" +
        "}\n" +
        "test();";

    assertEquals(expectedOutput, output);
  }
}