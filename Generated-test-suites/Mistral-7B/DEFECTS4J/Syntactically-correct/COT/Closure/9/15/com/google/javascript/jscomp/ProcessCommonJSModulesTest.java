import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSDocInfo.getAnnotationValue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Set;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testProcessCommonJSModules_BasicFunctionality() {
    String input = "function main() {" +
        "require('module1');" +
        "require('module2');" +
        "}";

    Node externs = Node.createTree(input);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, Node.createTree(input));

    Node root = pass.getModule().getRootNode();
    assertNotNull(root);

    Node requireCalls = root.lookupByName("require");
    assertEquals(2, requireCalls.getChildCount());

    Node module1 = root.lookupByName("module1");
    assertNotNull(module1);

    Node module2 = root.lookupByName("module2");
    assertNotNull(module2);
  }

  @Test
  public void testProcessCommonJSModules_RequireRelativePath() {
    String input = "function main() {" +
        "require('./module1');" +
        "require('../module2');" +
        "}";

    Node externs = Node.createTree(input);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, Node.createTree(input));

    Node root = pass.getModule().getRootNode();
    assertNotNull(root);

    Node requireCalls = root.lookupByName("require");
    assertEquals(2, requireCalls.getChildCount());

    Node module1 = root.lookupByName("module1");
    assertNotNull(module1);

    Node module2 = root.lookupByName("../module2");
    assertNotNull(module2);
  }

  @Test
  public void testProcessCommonJSModules_RequireAbsolutePath() {
    String input = "function main() {" +
        "require('/path/to/module1');" +
        "require('/path/to/module2');" +
        "}";

    Node externs = Node.createTree(input);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, Node.createTree(input));

    Node root = pass.getModule().getRootNode();
    assertNotNull(root);

    Node requireCalls = root.lookupByName("require");
    assertEquals(2, requireCalls.getChildCount());

    Node module1 = root.lookupByName("/path/to/module1");
    assertNotNull(module1);

    Node module2 = root.lookupByName("/path/to/module2");
    assertNotNull(module2);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_InvalidURI() {
    String input = "function main() {" +
        "require('foo:bar');" +
        "}";

    Node externs = Node.createTree(input);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, Node.createTree(input));
  }

  @Test
  public void testProcessCommonJSModules_ScriptNodes() {
    String input = "// module1.js" +
        "function module1() {" +
        "  var x = 1;" +
        "}" +

        "// module2.js" +
        "function module2() {" +
        "  var y = 2;" +
        "}" +

        "function main() {" +
        "  require('module1');" +
        "  require('module2');" +
        "}" +
        "module1.exports = {x: 1};" +
        "module2.exports = {y: 2};";

    Node externs = Node.createTree(input);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, Node.createTree(input));

    Node root = pass.getModule().getRootNode();
    assertNotNull(root);

    Node module1 = root.lookupByName("module1");
    assertNotNull(module1);

    Node module2 = root.lookupByName("module2");
    assertNotNull(module2);

    Node module1Exports = module1.lookupByName("exports");
    assertNotNull(module1Exports);

    Node module2Exports = module2.lookupByName("exports");
    assertNotNull(module2Exports);

    Node main = root.lookupByName("main");
    assertNotNull(main);

    Node mainCallsModule1 = main.getChildAtIndex(0);
    Node mainCallsModule2 = main.getChildAtIndex(1);

    assertEquals("module1", mainCallsModule1.getString());
    assertEquals("module2", mainCallsModule2.getString());
  }

  @Test
  public void testProcessCommonJSModules_ModuleExports() {
    String input = "// module1.js" +
        "function module1() {" +
        "  var x = 1;" +
        "}" +

        "// module2.js" +
        "function module2() {" +
        "  var y = 2;" +
        "}" +

        "function main() {" +
        "  require('module1').exports.x = 1;" +
        "  require('module2').exports.y = 2;" +
        "}" +
        "module1.exports = {x: null};" +
        "module2.exports = {y: null};";

    Node externs = Node.createTree(input);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, Node.createTree(input));

    Node root = pass.getModule().getRootNode();
    assertNotNull(root);

    Node module1 = root.lookupByName("module1");
    assertNotNull(module1);

    Node module2 = root.lookupByName("module2");
    assertNotNull(module2);

    Node module1Exports = module1.lookupByName("exports");
    assertNotNull(module1Exports);

    Node module2Exports = module2.lookupByName("exports");
    assertNotNull(module2Exports);

    Node main = root.lookupByName("main");
    assertNotNull(main);

    Node mainAssignsModule1 = main.getChildAtIndex(0);
    Node mainAssignsModule2 = main.getChildAtIndex(1);

    Node module1X = module1Exports.getChildAtIndex(0);
    Node module2Y = module2Exports.getChildAtIndex(0);

    assertEquals(1, mainAssignsModule1.getChildCount());
    assertEquals("x", mainAssignsModule1.getChildAtIndex(0).getString());
    assertEquals(1, mainAssignsModule1.getChildAtIndex(0).getInt());

    assertEquals(1, mainAssignsModule2.getChildCount());
    assertEquals("y", mainAssignsModule2.getChildAtIndex(0).getString());
    assertEquals(2, mainAssignsModule2.getChildAtIndex(0).getInt());
  }

  @Test
  public void testProcessCommonJSModules_ModuleExportsOverridden() {
    String input = "// module1.js" +
        "function module1() {" +
        "  var x = 1;" +
        "}" +

        "// module2.js" +
        "function module2() {" +
        "  var y = 2;" +
        "}" +

        "function main() {" +
        "  require('module1').exports = {z: 3};" +
        "  require('module2').exports = {w: 4};" +
        "}" +
        "module1.exports = {x: null};" +
        "module2.exports = {y: null};";

    Node externs = Node.createTree(input);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, Node.createTree(input));

    Node root = pass.getModule().getRootNode();
    assertNotNull(root);

    Node module1 = root.lookupByName("module1");
    assertNotNull(module1);

    Node module2 = root.lookupByName("module2");
    assertNotNull(module2);

    Node module1Exports = module1.lookupByName("exports");
    assertNotNull(module1Exports);

    Node module2Exports = module2.lookupByName("exports");
    assertNotNull(module2Exports);

    Node main = root.lookupByName("main");
    assertNotNull(main);

    Node mainAssignsModule1 = main.getChildAtIndex(0);
    Node mainAssignsModule2 = main.getChildAtIndex(1);

    assertEquals(0, module1Exports.getChildCount());
    assertNull(getAnnotationValue(module1, "goog.provide"));

    assertEquals(0, module2Exports.getChildCount());
    assertNull(getAnnotationValue(module2, "goog.provide"));

    assertEquals(1, mainAssignsModule1.getChildCount());
    assertEquals("z", mainAssignsModule1.getChildAtIndex(0).getString());
    assertEquals(3, mainAssignsModule1.getChildAtIndex(0).getInt());

    assertEquals(1, mainAssignsModule2.getChildCount());
    assertEquals("w", mainAssignsModule2.getChildAtIndex(0).getString());
    assertEquals(4, mainAssignsModule2.getChildAtIndex(0).getInt());
  }

  @Test
  public void testProcessCommonJSModules_ReportDependencies() {
    String input = "function main() {" +
        "require('module1');" +
        "require('module2');" +
        "}";

    CompilerOptions options = new CompilerOptions();
    options.setBooleanProperty("--processCommonJsModulesReportDependencies", true);

    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(Node.createTree(input), Node.createTree(input));

    String[] dependencies = compiler.getStats().getErrorStats().get(0)
        .getMessages().get(0).getDescription().split(": ");

    assertEquals(2, dependencies.length);

    assertEquals("module1", dependencies[0]);
    assertEquals("module2", dependencies[1]);
  }
}