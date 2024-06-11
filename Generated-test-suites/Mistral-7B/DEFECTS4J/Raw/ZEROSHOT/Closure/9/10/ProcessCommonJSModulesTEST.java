package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String FILENAME_PREFIX = "test/commonjs/";
  private static final String REQUIRED_FILENAME = "module1.js";
  private static final String CURRENT_FILENAME = "module2.js";

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", null);
    CompilerPass processor = new ProcessCommonJSModules(compiler, FILENAME_PREFIX);
    Node root = NodeFactory.createRootNode();
    Traverse.traverse(compiler, root, input);
    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();
    callback.process(input, root);

    Node scriptNode = findScriptNode(root, REQUIRED_FILENAME);
    assertRequireCallsCorrectlyRewritten(callback, scriptNode, REQUIRED_FILENAME);

    Node scriptNode2 = findScriptNode(root, CURRENT_FILENAME);
    assertModuleExportsCorrectlyRewritten(callback, scriptNode2, CURRENT_FILENAME);
  }

  private Node findScriptNode(Node root, String name) {
    for (int i = 0; i < root.getChildCount(); i++) {
      Node node = root.getChildAt(i);
      if (node.isScript()) {
        if (node.getString().equals(name)) {
          return node;
        }
      }
    }
    throw new RuntimeException("Script node not found: " + name);
  }

  private static void assertRequireCallsCorrectlyRewritten(
      ProcessCommonJsModulesCallback callback, Node scriptNode, String requiredFilename) {
    Node requireCall = findRequireCall(scriptNode);
    assertEquals(requiredFilename, callback.moduleName);
    assertEquals(requiredFilename, requireCall.getChildAtIndex(1).getString());

    Node moduleRef = requireCall.getChildAtIndex(0);
    assertEquals(toModuleName(requiredFilename), moduleRef.getString());
  }

  private static Node findRequireCall(Node scriptNode) {
    for (int i = 0; i < scriptNode.getChildCount(); i++) {
      Node node = scriptNode.getChildAt(i);
      if (node.isCall() && "require".equals(node.getFirstChild().getString())) {
        return node;
      }
    }
    throw new RuntimeException("Require call not found.");
  }

  private static void assertModuleExportsCorrectlyRewritten(
      ProcessCommonJsModulesCallback callback, Node scriptNode, String currentFilename) {
    Node moduleExports = findModuleExports(scriptNode);
    assertEquals(toModuleName(currentFilename), callback.moduleName);
    assertEquals("exports", moduleExports.getChildAtIndex(0).getString());
    assertEquals("module$exports", moduleExports.getChildAtIndex(1).getString());
  }

  private static Node findModuleExports(Node scriptNode) {
    for (int i = 0; i < scriptNode.getChildCount(); i++) {
      Node node = scriptNode.getChildAt(i);
      if (node.isGetProp() && "module.exports".equals(node.getQualifiedName())) {
        return node;
      }
    }
    throw new RuntimeException("Module exports not found.");
  }

  private static String toModuleName(String filename) {
    return "module$" + filename.replaceAll("/", "$")
        .replaceAll("\\.js$", "").replaceAll("-", "_");
  }
}

class ProcessCommonJSModulesCallback extends
AbstractPostOrderCallback {
  private String moduleName;

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isCall() && "require".equals(n.getFirstChild().getString())) {
      visitRequireCall(t, n, parent);
    }
    if (n.isScript()) {
      visitScript(t, n);
    }
    if (n.isGetProp() && "module.exports".equals(n.getQualifiedName())) {
      visitModuleExports(n);
    }
  }

  private void visitRequireCall(NodeTraversal t, Node require, Node parent) {
    String requiredFilename = toModuleName(require.getChildAtIndex(1).getString(),
        normalizeSourceName(t.getSourceName()));
    String moduleName = toModuleName(requiredFilename);
    this.moduleName = moduleName;
  }

  private String normalizeSourceName(String filename) {
    return filename.replaceAll("/", "\\\\");
  }

  private void visitScript(NodeTraversal t, Node script) {
    String moduleName = guessCJSModuleName(normalizeSourceName(script.getSourceFileName()));
    this.moduleName = moduleName;
  }

  private String guessCJSModuleName(String filename) {
    return "module$" + filename.replaceAll("^\\.", "")
        .replaceAll("/", "$")
        .replaceAll("\\.js$", "")
        .replaceAll("-", "_");
  }

  private void visitModuleExports(Node prop) {
    // No need to test this in JUnit as it's just renaming.
  }
}