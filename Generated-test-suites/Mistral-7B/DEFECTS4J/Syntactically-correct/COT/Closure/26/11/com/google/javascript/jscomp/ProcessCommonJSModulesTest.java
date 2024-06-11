import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.SuffixVarsCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static CompilerInput createTestInput(String fileName) {
    String testCode = "/** @suppress {extraRequire} */\n" +
        "require(\"module1\");\n" +
        "var x = 1;\n" +
        "function foo() {}\n" +
        "module.exports = {\n" +
        "  exports: 'hello'\n" +
        "};\n" +
        "var module1 = {\n" +
        "  module: {\n" +
        "    exports: function() {\n" +
        "      return {'hello': 'world'};\n" +
        "    }\n" +
        "  },\n" +
        "  exports: 'world'\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions(null);
    CompilerInput input = new CompilerInput(new SourceName(fileName), testCode);
    input.setSourceFile(new File(fileName));
    return input;
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String testFileName = "test.js";
    CompilerInput input = createTestInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, null);

    Node node = input.getRootNode();
    traverse(compiler, node, new ProcessCommonJsModulesCallback());

    Node rootNode = compiler.getIntermediateRepresentation();

    Node module = rootNode.getChildAtIndex(0);
    assertEquals(1, module.getChildCount());
    assertEquals("hello", module.getChildAtIndex(0).getString());

    Node exports = rootNode.getChildAtIndex(1);
    assertEquals("world", exports.getString());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testUriSyntaxException() throws IOException, URISyntaxException {
    String testFileName = "test.js";
    CompilerInput input = createTestInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    String wrongUri = "invalid:/test.js";
    Node requireNode = IR.call(IR.getprop(IR.name("require"),
        IR.string(toModuleName(wrongUri))), null);
    Node parent = IR.newVar("parent", IR.objectliteral());
    parent.addChildToFront(requireNode);
    input.setRootNode(IR.script(parent));

    processor.process(input, null);
  }

  @org.junit.Test
  public void testScriptNodeCount() throws IOException {
    String testFileName = "test1.js";
    CompilerInput input1 = createTestInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input1, null);

    String testFileName2 = "test2.js";
    CompilerInput input2 = createTestInput(testFileName2);
    Node parent = IR.newVar("parent", IR.objectliteral());
    parent.addChildToFront(input1.getRootNode().cloneTree());
    parent.addChildToFront(input2.getRootNode().cloneTree());
    input2.setRootNode(IR.script(parent));

    processor.process(input2, null);

    assertEquals(2, ((ProcessCommonJsModulesCallback) processor.callback)
        .scriptNodeCount);
  }

  @org.junit.Test
  public void testModuleExports() throws IOException {
    String testFileName = "test.js";
    CompilerInput input = createTestInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    processor.process(input, null);

    Node rootNode = compiler.getIntermediateRepresentation();
    Node module = rootNode.getChildAtIndex(0);
    Node exports = rootNode.getChildAtIndex(1);

    assertTrue(module.hasProp("exports"));
    assertTrue(exports.hasProp("module$exports"));

    Set<String> names = Sets.newHashSet();
    NodeTraversal.traverse(compiler, rootNode, new NodeVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        if (n.isName() && n.getString().startsWith("module")) {
          names.add(n.getString());
        }
      }
    });

    assertEquals(2, names.size());
    assertFalse(names.contains("module$exports"));
  }

  @org.junit.Test
  public void testSuffixVars() throws IOException {
    String testFileName = "test.js";
    CompilerInput input = createTestInput(testFileName);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    processor.process(input, null);

    Node rootNode = compiler.getIntermediateRepresentation();
    Node x = rootNode.getChildAtIndex(0);
    assertEquals("x$$x", x.getString());

    Node exports = rootNode.getChildAtIndex(1);
    Node exportsName = exports.getChildAtIndex(0);
    assertEquals("exports$$exports", exportsName.getString());
  }

  private static String toModuleName(String moduleName) {
    return ProcessCommonJSModules.toModuleName(moduleName, "test.js");
  }
}

class ProcessCommonJsModulesCallback extends NodeTraversal.AbstractPostOrderCallback {
  int scriptNodeCount = 0;

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    // ... (same as in the original class)
  }
}