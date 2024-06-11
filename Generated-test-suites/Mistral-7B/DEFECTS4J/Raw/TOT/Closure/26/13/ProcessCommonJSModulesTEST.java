import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String inputCode = "// Input code.\n" +
        "var module1 = {\n" +
        "  exports: {\n" +
        "    x: 1,\n" +
        "  }\n" +
        "};\n" +
        "require('module2');\n" +
        "function module2RequiringModule1() {\n" +
        "  var x = require('module1').x;\n" +
        "}\n" +
        "module2RequiringModule1();\n";

    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "./");
    processor.process(input.getExterns(), input.getRootNode());

    Node root = input.getRootNode();
    Node output = traverse(compiler, root, new GetRootNodeCallback()).getTree();

    String expectedCode = "// Output code.\n" +
        "goog.provide('module1.module$');\n" +
        "goog.require('module2.module$');\n" +
        "var module1 = {\n" +
        "  module$exports: {\n" +
        "    x: 1,\n" +
        "  }\n" +
        "};\n" +
        "goog.require('module1.module$');\n" +
        "function module2RequiringModule1() {\n" +
        "  var x = module1.module$exports.x;\n" +
        "}\n" +
        "module2RequiringModule1();\n";

    String actualCode = IR.toString(output);
    assertEquals(expectedCode, actualCode);
  }
}