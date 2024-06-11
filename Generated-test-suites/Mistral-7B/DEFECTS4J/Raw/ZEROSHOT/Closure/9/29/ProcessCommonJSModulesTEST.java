import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;

public class ProcessCommonJSModulesTest extends CompilerPassTestCase {

  public void testProcessCommonJSModules() throws Exception {
    String input = "// input.js\n" +
        "require(\"output.js\");\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    exported: 42\n" +
        "  }\n" +
        "};\n" +
        "goog.provide(\"input.module\");\n" +
        "goog.require(\"output.module\");\n" +
        "goog.exportPath(\"input\", module.exports.exported);\n";

    String expectedOutput = "// input.js\n" +
        "var input$module = {\n" +
        "  module$exports: {\n" +
        "    exported: 42\n" +
        "  }\n" +
        "};\n" +
        "goog.provide(\"input.module\");\n" +
        "goog.require(\"output.module\");\n" +
        "goog.exportPath(\"input\", input$module.module$exports);\n" +
        "require(\"output.module\");\n";

    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getProcessor(), "");

    Processor processor = getProcessor();
    processor.addPass(pass);

    Node root = parseTree(input);
    NodeTraversal traversal = new NodeTraversal(processor, root);
    traversal.traverse();

    assertEquals(expectedOutput, printTree(root));

    JSModule inputModule = pass.getModule();
    assertNotNull(inputModule);
    assertEquals("input.module", inputModule.getName());
  }

  public void testProcessCommonJSModulesWithRelativeRequire() throws Exception {
    String input = "// input.js\n" +
        "require(\"./output.js\");\n" +
        "goog.provide(\"input.module\");\n";

    String outputDir = "/tmp/output";
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getProcessor(), outputDir, true);

    Processor processor = getProcessor();
    processor.addPass(pass);

    String inputFileName = "input.js";
    String outputFileName = "output.js";

    String inputSourceName = "/path/to/input.js";
    String outputSourceName = "/path/to/output.js";

    Node root = parseTree(input);
    NodeTraversal traversal = new NodeTraversal(processor, root);
    traversal.traverse();

    String expectedOutput = "// " + inputFileName + "\n" +
        "goog.provide(\"input.module\");\n" +
        "goog.require(\"output.module\");\n" +
        "require(\"output.module\");\n";

    String actualOutput = readFile(outputDir, outputFileName);
    assertEquals(expectedOutput, actualOutput);

    String expectedModuleName = toModuleName("output.js", inputSourceName);
    JSModule outputModule = pass.getModule();
    assertEquals(expectedModuleName, outputModule.getName());
  }

  public void testProcessCommonJSModulesWithMultipleScripts() throws Exception {
    String input = "// input1.js\n" +
        "require(\"output1.js\");\n" +
        "goog.provide(\"input1.module\");\n" +
        "goog.require(\"output1.module\");\n" +
        "goog.exportPath(\"input1\", 1);\n" +
        "\n" +
        "// input2.js\n" +
        "require(\"output2.js\");\n" +
        "goog.provide(\"input2.module\");\n" +
        "goog.require(\"output2.module\");\n" +
        "goog.exportPath(\"input2\", 2);\n" +
        "\n" +
        "// output1.js\n" +
        "goog.provide(\"output1.module\");\n" +
        "goog.exports = 1;\n" +
        "\n" +
        "// output2.js\n" +
        "goog.provide(\"output2.module\");\n" +
        "goog.exports = 2;\n";

    String expectedOutput = "// input1.js\n" +
        "var input1$module = {\n" +
        "  module$exports: 1\n" +
        "};\n" +
        "goog.provide(\"input1.module\");\n" +
        "goog.require(\"output1.module\");\n" +
        "goog.exportPath(\"input1\", input1$module.module$exports);\n" +
        "\n" +
        "// input2.js\n" +
        "var input2$module = {\n" +
        "  module$exports: 2\n" +
        "};\n" +
        "goog.provide(\"input2.module\");\n" +
        "goog.require(\"output2.module\");\n" +
        "goog.exportPath(\"input2\", input2$module.module$exports);\n" +
        "\n" +
        "// output1.js\n" +
        "goog.provide(\"output1.module\");\n" +
        "goog.exports = 1;\n" +
        "\n" +
        "// output2.js\n" +
        "goog.provide(\"output2.module\");\n" +
        "goog.exports = 2;\n";

    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getProcessor(), "");

    Processor processor = getProcessor();
    processor.addPass(pass);

    Node root = parseTrees(input.split("\n")).get(0);
    NodeTraversal traversal = new NodeTraversal(processor, root);
    traversal.traverse();

    String actualOutput = printTrees(parser.parse(input)).get(0).toString();
    assertEquals(expectedOutput, actualOutput);
  }

  public void testProcessCommonJSModulesWithMultipleRequireCalls() throws Exception {
    String input = "// input.js\n" +
        "require(\"output1.js\");\n" +
        "require(\"output2.js\");\n" +
        "goog.provide(\"input.module\");\n";

    String outputDir = "/tmp/output";
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getProcessor(), outputDir, true);

    Processor processor = getProcessor();
    processor.addPass(pass);

    String inputSourceName = "/path/to/input.js";

    Node root = parseTree(input);
    NodeTraversal traversal = new NodeTraversal(processor, root);
    traversal.traverse();

    String expectedOutput = "// " + inputSourceName + "\n" +
        "goog.provide(\"input.module\");\n" +
        "require(\"output1.module\");\n" +
        "require(\"output2.module\");\n";

    String actualOutput = readFile(outputDir, inputSourceName.substring(0, inputSourceName.lastIndexOf("/") + 1) + "/" + inputSourceName.substring(inputSourceName.lastIndexOf("/") + 1) + ".js");
    assertEquals(expectedOutput, actualOutput);

    JSModule outputModule = pass.getModule();
    assertNotNull(outputModule);
    JSModule output1Module = outputModule.getModule("output1.module");
    assertNotNull(output1Module);
    JSModule output2Module = outputModule.getModule("output2.module");
    assertNotNull(output2Module);
  }

  public void testProcessCommonJSModulesWithMultipleRequireCallsAndExports() throws Exception {
    String input = "// input.js\n" +
        "require(\"output1.js\");\n" +
        "require(\"output2.js\");\n" +
        "goog.provide(\"input.module\");\n" +
        "goog.exports = 1;\n";

    String outputDir = "/tmp/output";
    ProcessCommonJSModules pass = new ProcessCommonJSModules(getProcessor(), outputDir, true);

    Processor processor = getProcessor();
    processor.addPass(pass);

    String inputSourceName = "/path/to/input.js";

    Node root = parseTree(input);
    NodeTraversal traversal = new NodeTraversal(processor, root);
    traversal.traverse();

    String expectedOutput = "// " + inputSourceName + "\n" +
        "goog.provide(\"input.module\");\n" +
        "require(\"output1.module\");\n" +
        "require(\"output2.module\");\n" +
        "goog.exports = 1;\n";

    String actualOutput = readFile(outputDir, inputSourceName.substring(0, inputSourceName.lastIndexOf("/") + 1) + "/" + inputSourceName.substring(inputSourceName.lastIndexOf("/") + 1) + ".js");
    assertEquals(expectedOutput, actualOutput);

    JSModule outputModule = pass.getModule();
    assertNotNull(outputModule);
    JSModule output1Module = outputModule.getModule("output1.module");
    assertNotNull(output1Module);
    JSModule output2Module = outputModule.getModule("output2.module");
    assertNotNull(output2Module);
    JSModule inputModule = outputModule.getModule("input.module");
    assertNotNull(inputModule);
    assertEquals(1, inputModule.getExportedValues().size());
    assertEquals(1, inputModule.getExportedValues().get(0).getValue().getInt());
  }
}