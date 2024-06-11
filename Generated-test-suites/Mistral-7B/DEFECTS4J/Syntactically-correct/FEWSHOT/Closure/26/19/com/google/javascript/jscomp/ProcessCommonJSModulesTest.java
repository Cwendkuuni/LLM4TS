package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", new File("test.js"));
    String filenamePrefix = "test";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node root = NodeFactory.createScript(input);
    processor.process(input, root);

    verifyProcessScriptNode(root, "test", "test.js");
  }

  private void verifyProcessScriptNode(Node root, String moduleName,
      String sourceFileName) {
    Node scriptNode = findScriptNode(root, sourceFileName);
    Node module = scriptNode.getFirstChild();
    Node exports = scriptNode.getChildAtIndex(1);

    assertEquals(moduleName, module.getString());
    assertEquals(moduleName + "$$exports", exports.getString());

    Node provide = findProvideNode(root);
    assertEquals(moduleName, provide.getFirstChild().getString());

    Node require = findRequireNode(root, "other");
    assertNull(require);
  }

  private Node findScriptNode(Node root, String sourceFileName) {
    NodeTraversal traversal = new NodeTraversal(root.getFactory(), root);
    Node scriptNode = null;
    for (Node node = traversal.firstChild(root); node != null;
        node = traversal.nextSibling(node)) {
      if (node.isScript()) {
        String nodeSourceFileName = node.getSourceFileName();
        if (nodeSourceFileName != null && nodeSourceFileName.equals(sourceFileName)) {
          scriptNode = node;
          break;
        }
      }
    }
    Preconditions.checkNotNull(scriptNode);
    return scriptNode;
  }

  private Node findProvideNode(Node root) {
    NodeTraversal traversal = new NodeTraversal(root.getFactory(), root);
    Node provideNode = null;
    for (Node node = traversal.firstChild(root); node != null;
        node = traversal.nextSibling(node)) {
      if (node.isCall() && node.getChildCount() > 0 &&
          "goog.provide".equals(node.getChildAtIndex(0).getQualifiedName())) {
        provideNode = node;
        break;
      }
    }
    Preconditions.checkNotNull(provideNode);
    return provideNode;
  }

  private Node findRequireNode(Node root, String moduleName) {
    NodeTraversal traversal = new NodeTraversal(root.getFactory(), root);
    Node requireNode = null;
    for (Node node = traversal.firstChild(root); node != null;
        node = traversal.nextSibling(node)) {
      if (node.isCall() && node.getChildCount() > 1 &&
          "require".equals(node.getChildAtIndex(0).getString()) &&
          node.getChildAtIndex(1).isString() &&
          node.getChildAtIndex(1).getString().equals(moduleName)) {
        requireNode = node;
        break;
      }
    }
    return requireNode;
  }
}