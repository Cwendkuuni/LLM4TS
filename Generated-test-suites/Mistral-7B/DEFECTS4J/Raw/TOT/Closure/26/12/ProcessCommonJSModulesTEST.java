import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.JSModule;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String inputFile = "input.js";
    String moduleFile = "module.js";

    CompilerInput input = new CompilerInput(inputFile);
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    Node root = Node.parse(input.getContent());
    processor.process(input, root);

    CompilerInput output = input.getCompiledInput();
    Node outputRoot = output.getRootNode();

    JSModule module = processor.getModule();
    assertEquals("module$moduleName", module.getName());
    assertEquals(Sets.newHashSet(inputFile, moduleFile), module.getDependencies());

    Node expectedOutput = Node.parse("" +
        "goog.provide(\"" + module.getName() + "\");\n" +
        "goog.require(\"" + inputFile + "\");\n" +
        "var " + module.getName() + " = (function(){\n" +
        "  var " + module.getName() + "$" + module.getName() + ";\n" +
        "  " + input.getContent() + "\n" +
        "  " + module.getName() + "$" + module.getName() + " = " + module.getName() + ";\n" +
        "  return " + module.getName() + ";\n" +
        "})();\n");

    Node actualOutput = outputRoot.getChildAtIndex(0);
    assertEquals(expectedOutput.getChildCount(), actualOutput.getChildCount());
    for (int i = 0; i < expectedOutput.getChildCount(); i++) {
      Node expectedChild = expectedOutput.getChildAtIndex(i);
      Node actualChild = actualOutput.getChildAtIndex(i);
      assertEquals(expectedChild.getClass(), actualChild.getClass());
      if (expectedChild instanceof Node && actualChild instanceof Node) {
        assertEquals(expectedChild.getString(), actualChild.getString());
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testUnsupportedMultipleScriptNodes() {
    String inputFile = "input.js";
    String moduleFile = "module.js";

    CompilerInput input = new CompilerInput(inputFile);
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    Node root = Node.parse(input.getContent() + "\n" +
        Node.parse(input.getContent()).getFirstChild().getFirstChild()
            .getFirstChild().cloneTree());

    processor.process(input, root);
  }

  @Test(expected = AssertionError.class)
  public void testUnsupportedRequireCallWithInvalidArgument() {
    String inputFile = "input.js";

    CompilerInput input = new CompilerInput(inputFile);
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    Node root = Node.parse(input.getContent() + "\n" +
        "require('not_a_valid_module');");

    processor.process(input, root);
  }

  @Test(expected = AssertionError.class)
  public void testUnsupportedRequireCallWithRelativePath() {
    String inputFile = "input.js";
    String moduleFile = "module.js";

    CompilerInput input = new CompilerInput(inputFile);
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    Node root = Node.parse(input.getContent() + "\n" +
        "require('./" + moduleFile + "');");

    processor.process(input, root);
  }

  @Test
  public void testNoModule() {
    String inputFile = "input.js";

    CompilerInput input = new CompilerInput(inputFile);
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    Node root = Node.parse(input.getContent());

    processor.process(input, root);

    CompilerInput output = input.getCompiledInput();
    Node outputRoot = output.getRootNode();

    assertNull(processor.getModule());
  }
}