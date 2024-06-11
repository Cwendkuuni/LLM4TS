import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EmitNode;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.CompilerTestHelper;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ProcessCommonJSModulesTest extends CompilerPassTestBase {

  private static final String TEST_MODULE_NAME = "TestModule";
  private static final String TEST_INPUT_FILE = "testModule.js";
  private static final String TEST_OUTPUT_FILE = "testModuleCompiled.js";

  @Override
  protected CompilerPass getCompilerPass() {
    return new ProcessCommonJSModules(new Compiler(), ".") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
        Node testModuleScript = getTestModuleScript();
        Node testModule = compiler.getRoot();
        testModule.addChildToFront(testModuleScript);
      }

      private Node getTestModuleScript() {
        NodeFactory nf = compiler.getFactory();
        String testModuleSource = "var TestModule = " +
            "function() {" +
            "  this.testMethod = function() {" +
            "    console.log('testMethod');" +
            "  };" +
            "};";
        return nf.parseTree(testModuleSource);
      }
    };
  }

  @Override
  protected CompilerOptions getDefaultOptions() {
    return defaultOptions().withWarningLevels(WarningLevel.ERROR, WarningLevel.DEPRECATED, WarningLevel.UNUSED, WarningLevel.CHECK, WarningLevel.TYPE);
  }

  @Override
  protected void checkCompilationResult(CompilerResults results) throws IOException {
    List<Node> nodes = results.getTreeList();
    Node testModule = nodes.get(0).getChildAtIndex(0);
    assertEquals(TEST_MODULE_NAME, testModule.getString());
    Node testModuleExports = testModule.getChildAtIndex(1);
    assertEquals("module$exports", testModuleExports.getString());
    Node testModuleProvide = testModule.getChildAtIndex(2);
    assertEquals("goog.provide('" + TEST_MODULE_NAME + "');",
        testModuleProvide.getString());

    // Check that the test method is emitted to the output file.
    Node outputRoot = nodes.get(nodes.size() - 1);
    Node testMethodNode = findTestMethodNode(outputRoot);
    assertNotNull(testMethodNode);
    EmitNode(testMethodNode, "testOutputFile.js");

    // Check that require calls are replaced with direct references.
    Node requireCall = findRequireCallNode(outputRoot, "TestModule");
    assertNotNull(requireCall);
    Node testModuleReference = requireCall.getParent();
    Node testModuleReferenceNameNode = testModuleReference.getChildAtIndex(0);
    assertEquals("TestModule", testModuleReferenceNameNode.getString());
  }

  private Node findTestMethodNode(Node node) {
    if (node.isFunction()) {
      String name = node.getQualifiedName();
      if (name != null && name.equals(TEST_MODULE_NAME + ".testMethod")) {
        return node;
      }
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      Node found = findTestMethodNode(child);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  private Node findRequireCallNode(Node node, String moduleName) {
    if (node.isCall() && node.getChildCount() == 2 &&
        "require".equals(node.getFirstChild().getQualifiedName()) &&
        node.getChildAtIndex(1).isString() &&
        node.getChildAtIndex(1).getString().equals(moduleName)) {
      return node;
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      Node found = findRequireCallNode(child, moduleName);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  @Override
  protected void setup() throws Exception {
    deleteFile(new File(TEST_OUTPUT_FILE));
    CompilerTestHelper.setUpSources(sourceDirectory, TEST_INPUT_FILE);
  }

  @Override
  protected void tearDown() throws Exception {
    CompilerTestHelper.tearDown();
    deleteFile(new File(TEST_OUTPUT_FILE));
  }

  private void deleteFile(File file) {
    if (file.exists()) {
      file.delete();
    }
  }
}