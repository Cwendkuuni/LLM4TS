package org.apache.commons.cli.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.SuffixVarsCallback;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node scriptNode = NodeFactory.createScript("var module = { exports: {} };");
    rootNode.addChild(scriptNode);
    nodeTraversal(processor, rootNode);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals("test", module.getName());
  }

  @Test
  public void testProcessRequireCall() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node requireCallNode = NodeFactory.createCall(
        NodeFactory.createName("require"),
        NodeFactory.createString("other.js")
    );
    rootNode.addChild(requireCallNode);
    nodeTraversal(processor, rootNode);

    JSModule module = processor.getModule();
    Node requireCall = module.getNodesWithName("require")
        .get(0);
    Node moduleRefNode = requireCall.getChildAtIndex(1);
    assertEquals("other", moduleRefNode.getString());
  }

  @Test
  public void testProcessRequireCallWithRelativePath() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node requireCallNode = NodeFactory.createCall(
        NodeFactory.createName("require"),
        NodeFactory.createString("./other.js")
    );
    rootNode.addChild(requireCallNode);
    nodeTraversal(processor, rootNode);

    JSModule module = processor.getModule();
    Node requireCall = module.getNodesWithName("require")
        .get(0);
    String otherModuleName = toModuleName(requireCall.getChildAtIndex(1).getString(),
        "test.js").toString();
    Node moduleRefNode = requireCall.getChildAtIndex(1);
    assertEquals(otherModuleName, moduleRefNode.getString());
  }

  @Test
  public void testProcessRequireCallWithAbsolutePath() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node requireCallNode = NodeFactory.createCall(
        NodeFactory.createName("require"),
        NodeFactory.createString("/path/to/other.js")
    );
    rootNode.addChild(requireCallNode);
    nodeTraversal(processor, rootNode);

    JSModule module = processor.getModule();
    Node requireCall = module.getNodesWithName("require")
        .get(0);
    String otherModuleName = toModuleName(requireCall.getChildAtIndex(1).getString(),
        "test.js").toString();
    Node moduleRefNode = requireCall.getChildAtIndex(1);
    assertEquals(otherModuleName, moduleRefNode.getString());
  }

  @Test
  public void testProcessModuleExports() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node scriptNode = NodeFactory.createScript(
        "exports.foo = 'bar';"
    );
    rootNode.addChild(scriptNode);
    nodeTraversal(processor, rootNode);

    JSModule module = processor.getModule();
    Node moduleExportsNode = module.getNodesWithName("exports")
        .get(0);
    Node fooNode = moduleExportsNode.getChildAtIndex(0);
    assertEquals("foo", fooNode.getString());
    assertEquals("bar", fooNode.getFirstChild().getString());
  }

  @Test
  public void testRenameGlobalVariables() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node scriptNode = NodeFactory.createScript("var foo = 'bar';");
    rootNode.addChild(scriptNode);
    nodeTraversal(processor, rootNode);

    Node fooNode = rootNode.getNodesWithName("foo").get(0);
    assertEquals("foo$$test", fooNode.getString());
  }

  @Test
  public void testRenameModuleExports() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node scriptNode = NodeFactory.createScript("module.exports.foo = 'bar';");
    rootNode.addChild(scriptNode);
    nodeTraversal(processor, rootNode);

    Node exportsNode = rootNode.getNodesWithName("module$exports")
        .get(0);
    Node fooNode = exportsNode.getChildAtIndex(0);
    assertEquals("foo", fooNode.getString());
    assertEquals("bar", fooNode.getFirstChild().getString());
  }

  @Test
  public void testProcessMultipleScripts() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(options, ".");

    Node rootNode = NodeFactory.createRoot();
    Node script1Node = NodeFactory.createScript("exports.foo = 'bar1';");
    rootNode.addChild(script1Node);
    Node script2Node = NodeFactory.createScript("exports.foo = 'bar2';");
    rootNode.addChild(script2Node);
    nodeTraversal(processor, rootNode);

    JSModule module = processor.getModule();
    Node exportsNode = module.getNodesWithName("module$exports")
        .get(0);
    Node fooNode1 = exportsNode.getChildAtIndex(0);
    assertEquals("foo", fooNode1.getString());
    assertEquals("bar1", fooNode1.getFirstChild().getString());
    Node fooNode2 = exportsNode.getChildAtIndex(1);
    assertEquals("foo", fooNode2.getString());
    assertEquals("bar2", fooNode2.getFirstChild().getString());
  }

  private void nodeTraversal(ProcessCommonJSModules processor, Node node) {
    NodeTraversal.traverse(processor.getCompiler(), node, null);
  }

  private String toModuleName(String filename, String currentFilename) {
    return ProcessCommonJSModules.toModuleName(filename, currentFilename);
  }
}