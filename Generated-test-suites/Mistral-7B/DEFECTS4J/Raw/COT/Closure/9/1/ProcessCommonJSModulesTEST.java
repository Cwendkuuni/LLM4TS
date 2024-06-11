import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testProcess() {
    String input = "function main() {" +
        "require('./module1.js')();" +
        "require('module2.js')();" +
        "}" +
        "function module1() {" +
        "module.exports = function() {" +
        "console.log('Module 1');" +
        "}" +
        "}" +
        "function module2() {" +
        "module.exports = function() {" +
        "console.log('Module 2');" +
        "}" +
        "}";

    Node externs = null;
    Node root = IR.parseTree(input);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(externs, root);

    Node output = compiler.getOutput();
    assertTrue(output.toString().contains("goog.provide('module1')"));
    assertTrue(output.toString().contains("goog.provide('module2')"));
    assertTrue(output.toString().contains("module1 = module1.module$exports;"));
    assertTrue(output.toString().contains("module2 = module2.module$exports;"));
  }

  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module$module1", guessCJSModuleName("module1.js"));
    assertEquals("module$module2", guessCJSModuleName("module2.js"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$module1", toModuleName("module1.js"));
    assertEquals("module$module1", toModuleName("./module1.js"));
    assertEquals("module$module2", toModuleName("module2.js"));
    assertEquals("module$module2", toModuleName("../module2.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals("module1", normalizeSourceName("module1.js"));
    assertEquals("module1", normalizeSourceName("./module1.js"));
    assertEquals("module2", normalizeSourceName("module2.js"));
    assertEquals("module2", normalizeSourceName("../module2.js"));
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitRequireCall() {
    String input = "function main() {" +
        "require('./module1.js')();" +
        "require('module2.js')();" +
        "}" +
        "function module1() {" +
        "module.exports = function() {" +
        "console.log('Module 1');" +
        "}" +
        "}" +
        "function module2() {" +
        "module.exports = function() {" +
        "console.log('Module 2');" +
        "}" +
        "}" +
        "module.exports = function() {}";

    Node externs = null;
    Node root = IR.parseTree(input);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    NodeTraversal traversal = new NodeTraversal(compiler, root, processor);
    traversal.traverse();

    Node output = compiler.getOutput();
    assertTrue(output.toString().contains("goog.require('module1')"));
    assertTrue(output.toString().contains("goog.require('module2')"));
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitScript() {
    String input = "// module1.js\n" +
        "function module1() {" +
        "module.exports = function() {" +
        "console.log('Module 1');" +
        "}" +
        "}";

    String expected = "// module1.js\n" +
        "goog.provide('module1');\n" +
        "module1 = module1.module$exports;";

    Node externs = null;
    Node root = IR.parseTree(input);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "module1.js", false);
    NodeTraversal traversal = new NodeTraversal(compiler, root, processor);
    traversal.traverse();

    Node output = compiler.getOutput();
    assertEquals(expected, output.toString());
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitModuleExports() {
    String input = "function module1() {" +
        "module.exports = { a: 1 }";

    String expected = "function module1() {" +
        "module1.module$exports = { a: 1 }";

    Node externs = null;
    Node root = IR.parseTree(input);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    NodeTraversal traversal = new NodeTraversal(compiler, root, processor);
    traversal.traverse();

    Node output = compiler.getOutput();
    Node moduleExports = output.findChildByType(Node.VARDECL);
    assertEquals("module1.module$exports", moduleExports.toString());
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJsModulesCallbackVisitRequireCallWithInvalidURI() {
    String input = "function main() {" +
        "require('..//module1.js')();" +
        "}" +
        "function module1() {" +
        "module.exports = function() {" +
        "console.log('Module 1');" +
        "}" +
        "}";

    Node externs = null;
    Node root = IR.parseTree(input);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    NodeTraversal traversal = new NodeTraversal(compiler, root, processor);
    traversal.traverse();

    fail("Expected RuntimeException");
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJsModulesCallbackVisitRequireCallWithRelativeInvalidURI() {
    String input = "function main() {" +
        "require('./../module1.js')();" +
        "}" +
        "function module1() {" +
        "module.exports = function() {" +
        "console.log('Module 1');" +
        "}" +
        "}";

    Node externs = null;
    Node root = IR.parseTree(input);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    NodeTraversal traversal = new NodeTraversal(compiler, root, processor);
    traversal.traverse();

    fail("Expected RuntimeException");
  }
}