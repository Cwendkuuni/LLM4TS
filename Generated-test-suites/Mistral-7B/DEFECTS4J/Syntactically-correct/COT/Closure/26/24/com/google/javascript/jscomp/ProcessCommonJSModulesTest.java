import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessingEnvironment.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessingEnvironment;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  private static final String TEST_FILE = "ProcessCommonJSModulesTest.js";

  private CompilerInput input;
  private ProcessingEnvironment env;
  private AbstractCompiler compiler;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() throws IOException, URISyntaxException {
    compiler = new CompilerOptions();
    input = new CompilerInput(new File(TEST_FILE), TEST_FILE, null);
    env = new ProcessingEnvironment(input, compiler);
    processor = new ProcessCommonJSModules(compiler, ".", false);
  }

  @Test
  public void testProcess_NoRequireCall() {
    Node root = IR.script(node(TEST_FILE));
    processor.process(input, root);
    assertEquals(0, input.getRequireCount());
  }

  @Test
  public void testProcess_SimpleRequireCall() {
    String requireModuleName = "TestModule";
    String scriptFileName = "TestScript.js";

    Node root = IR.script(
        node("(function() {\n" +
            "  var TestModule = require('" + requireModuleName + "');\n" +
            "  TestModule.doSomething();\n" +
            "})();"), scriptFileName);

    processor.process(input, root);

    JSModule module = processor.getModule();
    assertEquals(1, input.getRequireCount());
    assertEquals(requireModuleName, input.getRequire(0).getName());
    assertEquals(module, module.getModule(requireModuleName));
  }

  @Test
  public void testProcess_MultipleRequireCalls() {
    String requireModule1Name = "Module1";
    String requireModule2Name = "Module2";
    String scriptFileName = "TestScript.js";

    Node root = IR.script(
        node("(function() {\n" +
            "  var Module1 = require('" + requireModule1Name + "');\n" +
            "  var Module2 = require('" + requireModule2Name + "');\n" +
            "  Module1.doSomething();\n" +
            "  Module2.doSomething();\n" +
            "})();"), scriptFileName);

    processor.process(input, root);

    JSModule module1 = processor.getModule().getModule(requireModule1Name);
    JSModule module2 = processor.getModule().getModule(requireModule2Name);

    assertEquals(2, input.getRequireCount());
    assertEquals(requireModule1Name, input.getRequire(0).getName());
    assertEquals(requireModule2Name, input.getRequire(1).getName());
    assertNotNull(module1);
    assertNotNull(module2);
  }

  @Test(expected = RuntimeException.class)
  public void testProcess_RelativeRequireCall() {
    String requireModuleName = "./TestModule.js";
    String scriptFileName = "TestScript.js";

    Node root = IR.script(
        node("(function() {\n" +
            "  var TestModule = require('" + requireModuleName + "');\n" +
            "  TestModule.doSomething();\n" +
            "})();"), scriptFileName);

    processor.process(input, root);
  }

  @Test
  public void testProcess_ModuleExports() {
    String moduleName = "TestModule";
    String scriptFileName = "TestScript.js";

    Node root = IR.script(
        node("(function() {\n" +
            "  var module = {\n" +
            "    exports: {\n" +
            "      doSomething: function() {\n" +
            "        console.log('Hello World!');\n" +
            "      }\n" +
            "    },\n" +
            "    module: {\n" +
            "      exports: module.exports\n" +
            "    }\n" +
            "  };\n" +
            "  module.exports = module;\n" +
            "})();"), scriptFileName);

    processor.process(input, root);

    JSModule module = processor.getModule();
    assertEquals(0, input.getRequireCount());
    assertEquals(moduleName, module.getName());
    assertEquals(1, module.getVariables().size());
    assertEquals("doSomething", module.getVariables().keySet().iterator().next());
    assertNotNull(module.getVariable("doSomething"));
  }

  @Test
  public void testProcess_ModuleExportsOverride() {
    String moduleName = "TestModule";
    String scriptFileName = "TestScript.js";

    Node root = IR.script(
        node("(function() {\n" +
            "  var module = {\n" +
            "    exports: {\n" +
            "      doSomething: function() {\n" +
            "        console.log('Hello World!');\n" +
            "      }\n" +
            "    },\n" +
            "    module: {\n" +
            "      exports: {} // override module.exports\n" +
            "    }\n" +
            "  };\n" +
            "  module.exports = module.module.exports;\n" +
            "})();"), scriptFileName);

    processor.process(input, root);

    JSModule module = processor.getModule();
    assertEquals(0, input.getRequireCount());
    assertEquals(moduleName, module.getName());
    assertEquals(0, module.getVariables().size());
  }

  @Test
  public void testProcess_ScriptWithGlobalVariables() {
    String moduleName = "TestModule";
    String scriptFileName = "TestScript.js";

    Node root = IR.script(
        node("(function() {\n" +
            "  var globalVar = 42;\n" +
            "  TestModule.doSomething();\n" +
            "})();"), scriptFileName);

    processor.process(input, root);

    JSModule module = processor.getModule();
    assertEquals(0, input.getRequireCount());
    assertEquals(moduleName, module.getName());
    assertEquals(1, module.getVariables().size());
    assertEquals("globalVar", module.getVariables().keySet().iterator().next());
    assertNotNull(module.getVariable("globalVar"));
  }

  @Test
  public void testProcess_GlobalVariablesWithSameNameAsModule() {
    String moduleName = "TestModule";
    String scriptFileName = "TestScript.js";

    Node root = IR.script(
        node("(function() {\n" +
            "  var TestModule = 42;\n" +
            "})();"), scriptFileName);

    try {
      processor.process(input, root);
      fail("Expected an error due to variable name conflict.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  private Node node(String code) {
    return IR.parseTree(code, null, null, null, null, null, null, null, null, null);
  }
}