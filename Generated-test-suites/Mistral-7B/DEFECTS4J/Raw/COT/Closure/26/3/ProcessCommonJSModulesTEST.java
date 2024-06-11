package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    // Test with empty input
    CompilerInput emptyInput = new CompilerInput(new File(""), null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(null, "");
    CompilerPassManager manager = new CompilerPassManager(pass);
    manager.runPasses(emptyInput, null);
    Node root = emptyInput.getRoot();
    assertEquals(0, root.getChildCount());

    // Test with valid input
    String fileName = "input.js";
    CompilerInput input = new CompilerInput(new File(fileName), fileName);
    input.setSourceMap(new File(fileName + ".map"));
    ProcessCommonJSModules pass1 = new ProcessCommonJSModules(null, "");
    CompilerPassManager manager1 = new CompilerPassManager(pass1);
    manager1.runPasses(input, null);
    Node root1 = input.getRoot();
    NodeTraversal traversal1 = new NodeTraversal(input.getRoot(), null);
    Node scriptNode1 = getScriptNode(root1);
    String moduleName1 = "moduleA";
    assertEquals("moduleA", scriptNode1.getChildAtIndex(0).getString());
    assertEquals("goog.provide('moduleA');", scriptNode1.getChildAtIndex(1)
        .getString());
    assertEquals("goog.require('dependencyB');", scriptNode1.getChildAtIndex(2)
        .getString());
    assertNull(getRequireCallNode(root1, "dependencyB"));

    ProcessCommonJSModules pass2 = new ProcessCommonJSModules(null, "", false);
    CompilerPassManager manager2 = new CompilerPassManager(pass2);
    manager2.runPasses(input, manager1.getCompilationResult());
    Node root2 = input.getRoot();
    NodeTraversal traversal2 = new NodeTraversal(root2, null);
    Node scriptNode2 = getScriptNode(root2);
    assertEquals("moduleA", scriptNode2.getChildAtIndex(0).getString());
    assertEquals("goog.provide('moduleA');", scriptNode2.getChildAtIndex(1)
        .getString());
    assertEquals("dependencyB", scriptNode2.getChildAtIndex(2).getString());
    Node requireCallNode2 = getRequireCallNode(root2, "dependencyB");
    assertNull(requireCallCallNode2);

    // Test with relative require call
    String requiredModuleName = "dependencyC";
    String requiredFileName = "dependencyC.js";
    String currentFileName = "input.js";
    CompilerInput inputWithRequire = new CompilerInput(new File(fileName), fileName);
    inputWithRequire.setSourceMap(new File(fileName + ".map"));
    Node scriptNodeWithRequire = new NodeFactory().script(Arrays.asList(
        new NodeFactory().string("(function () { "),
        new NodeFactory().exprStatement(new NodeFactory().call(
            new NodeFactory().getprop(new NodeFactory().name("require"),
                new NodeFactory().string(requiredModuleName)),
            null)),
        new NodeFactory().string("})();"))).copyInformationFromForTree(
        scriptNode1);
    inputWithRequire.addChild(scriptNodeWithRequire);
    ProcessCommonJSModules pass3 = new ProcessCommonJSModules(null, "");
    CompilerPassManager manager3 = new CompilerPassManager(pass3);
    manager3.runPasses(inputWithRequire, null);
    Node root3 = inputWithRequire.getRoot();
    NodeTraversal traversal3 = new NodeTraversal(root3, null);
    Node scriptNode3 = getScriptNode(root3);
    assertEquals("moduleA", scriptNode3.getChildAtIndex(0).getString());
    assertEquals("goog.provide('moduleA');", scriptNode3.getChildAtIndex(1)
        .getString());
    assertEquals("goog.require('dependencyB');", scriptNode3.getChildAtIndex(2)
        .getString());
    assertEquals("dependencyC", scriptNode3.getChildAtIndex(3).getString());
    Node requireCallNode3 = getRequireCallNode(root3, "dependencyC");
    assertNull(requireCallNode3);

    ProcessCommonJSModules pass4 = new ProcessCommonJSModules(null, "", false);
    CompilerPassManager manager4 = new CompilerPassManager(pass4);
    manager4.runPasses(inputWithRequire, manager3.getCompilationResult());
    Node root4 = inputWithRequire.getRoot();
    NodeTraversal traversal4 = new NodeTraversal(root4, null);
    Node scriptNode4 = getScriptNode(root4);
    assertEquals("moduleA", scriptNode4.getChildAtIndex(0).getString());
    assertEquals("goog.provide('moduleA');", scriptNode4.getChildAtIndex(1)
        .getString());
    assertEquals("dependencyB", scriptNode4.getChildAtIndex(2).getString());
    assertEquals("dependencyC", scriptNode4.getChildAtIndex(3).getString());
    Node requireCallNode4 = getRequireCallNode(root4, "dependencyC");
    assertNull(requireCallNode4);

    // Test with multiple scripts and multiple requires
    CompilerInput inputWithMultiple = new CompilerInput(new File(fileName), fileName);
    inputWithMultiple.setSourceMap(new File(fileName + ".map"));
    Node multipleScriptNode1 = new NodeFactory().script(Arrays.asList(
        new NodeFactory().string("(function () { "),
        new NodeFactory().exprStatement(new NodeFactory().call(
            new NodeFactory().getprop(new NodeFactory().name("require"),
                new NodeFactory().string("dependencyB")), null)),
        new NodeFactory().string("})();"))).copyInformationFromForTree(
        scriptNode1);
    inputWithMultiple.addChild(multipleScriptNode1);
    Node multipleScriptNode2 = new NodeFactory().script(Arrays.asList(
        new NodeFactory().string("(function () { "),
        new NodeFactory().exprStatement(new NodeFactory().call(
            new NodeFactory().getprop(new NodeFactory().name("require"),
                new NodeFactory().string("dependencyC")), null)),
        new NodeFactory().string("})();"))).copyInformationFromForTree(
        scriptNode1);
    inputWithMultiple.addChild(multipleScriptNode2);
    ProcessCommonJSModules pass5 = new ProcessCommonJSModules(null, "");
    CompilerPassManager manager5 = new CompilerPassManager(pass5);
    manager5.runPasses(inputWithMultiple, null);
    Node root5 = inputWithMultiple.getRoot();
    NodeTraversal traversal5 = new NodeTraversal(root5, null);
    Node scriptNode5 = getScriptNode(root5);
    assertEquals("moduleA", scriptNode5.getChildAtIndex(0).getString());
    assertEquals("goog.provide('moduleA');", scriptNode5.getChildAtIndex(1)
        .getString());
    assertEquals("dependencyB", scriptNode5.getChildAtIndex(2).getString());
    assertEquals("dependencyC", scriptNode5.getChildAtIndex(3).getString());
    Node requireCallNode5B = getRequireCallNode(root5, "dependencyB");
    assertNull(requireCallNode5B);
    Node requireCallNode5C = getRequireCallNode(root5, "dependencyC");
    assertNull(requireCallNode5C);

    ProcessCommonJSModules pass6 = new ProcessCommonJSModules(null, "", false);
    CompilerPassManager manager6 = new CompilerPassManager(pass6);
    manager6.runPasses(inputWithMultiple, manager5.getCompilationResult());
    Node root6 = inputWithMultiple.getRoot();
    NodeTraversal traversal6 = new NodeTraversal(root6, null);
    Node scriptNode6 = getScriptNode(root6);
    assertEquals("moduleA", scriptNode6.getChildAtIndex(0).getString());
    assertEquals("goog.provide('moduleA');", scriptNode6.getChildAtIndex(1)
        .getString());
    assertEquals("dependencyB", scriptNode6.getChildAtIndex(2).getString());
    assertEquals("dependencyC", scriptNode6.getChildAtIndex(3).getString());
    Node requireCallNode6B = getRequireCallNode(root6, "dependencyB");
    assertNull(requireCallNode6B);
    Node requireCallNode6C = getRequireCallNode(root6, "dependencyC");
    assertNull(requireCallNode6C);

    // Test with circular dependency
    String circularFileName = "circularA.js";
    CompilerInput inputWithCircular = new CompilerInput(new File(fileName), fileName);
    inputWithCircular.setSourceMap(new File(fileName + ".map"));
    Node circularScriptNode1 = new NodeFactory().script(Arrays.asList(
        new NodeFactory().string("(function () { "),
        new NodeFactory().exprStatement(new NodeFactory().call(
            new NodeFactory().getprop(new NodeFactory().name("require"),
                new NodeFactory().string("circularB")), null)),
        new NodeFactory().string("})();"))).copyInformationFromForTree(
        scriptNode1);
    inputWithCircular.addChild(circularScriptNode1);
    Node circularScriptNode2 = new NodeFactory().script(Arrays.asList(
        new NodeFactory().string("(function () { "),
        new NodeFactory().exprStatement(new NodeFactory().call(
            new NodeFactory().getprop(new NodeFactory().name("require"),
                new NodeFactory().string("circularA")), null)),
        new NodeFactory().string("})();"))).copyInformationFromForTree(
        scriptNode1);
    inputWithCircular.addChild(circularScriptNode2);
    ProcessCommonJSModules pass7 = new ProcessCommonJSModules(null, "");
    CompilerPassManager manager7 = new CompilerPassManager(pass7);
    manager7.runPasses(inputWithCircular, null);
    Node root7 = inputWithCircular.getRoot();
    NodeTraversal traversal7 = new NodeTraversal(root7, null);
    Node scriptNode7 = getScriptNode(root7);
    assertEquals("circularA", scriptNode7.getChildAtIndex(0).getString());
    assertEquals("goog.provide('circularA');", scriptNode7.getChildAtIndex(1)
        .getString());
    assertEquals("circularB", scriptNode7.getChildAtIndex(2).getString());
    Node requireCallNode7A = getRequireCallNode(root7, "circularA");
    assertNull(requireCallNode7A);
    Node requireCallNode7B = getRequireCallNode(root7, "circularB");
    assertNull(requireCallNode7B);

    ProcessCommonJSModules pass8 = new ProcessCommonJSModules(null, "", false);
    CompilerPassManager manager8 = new CompilerPassManager(pass8);
    manager8.runPasses(inputWithCircular, manager7.getCompilationResult());
    Node root8 = inputWithCircular.getRoot();
    NodeTraversal traversal8 = new NodeTraversal(root8, null);
    Node scriptNode8 = getScriptNode(root8);
    assertEquals("circularA", scriptNode8.getChildAtIndex(0).getString());
    assertEquals("goog.provide('circularA');", scriptNode8.getChildAtIndex(1)
        .getString());
    assertEquals("circularB", scriptNode8.getChildAtIndex(2).getString());
    Node requireCallNode8A = getRequireCallNode(root8, "circularA");
    assertNull(requireCallNode8A);
    Node requireCallNode8B = getRequireCallNode(root8, "circularB");
    assertNull(requireCallNode8B);
  }

  private Node getScriptNode(Node node) {
    for (Node child : node.getChildren()) {
      if (child.isScript()) {
        return child;
      }
    }
    throw new RuntimeException("Expected script node, but found: " + node);
  }

  private Node getRequireCallNode(Node node, String moduleName) {
    for (Node child : node.getChildren()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "require".equals(child.getFirstChild().getQualifiedName()) &&
          child.getChildAtIndex(1).isString() &&
          moduleName.equals(child.getChildAtIndex(1).getString())) {
        return child;
      }
    }
    return null;
  }
}