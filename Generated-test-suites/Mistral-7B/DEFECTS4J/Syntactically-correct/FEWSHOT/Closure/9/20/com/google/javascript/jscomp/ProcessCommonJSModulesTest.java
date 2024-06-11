package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.jscomp.Rewritter;
import com.google.javascript.rhino.IR;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String TEST_FILE_NAME = "TestCommonJSModules.js";
  private static final String TEST_MODULE_NAME = "TestCommonJSModules";

  @Test
  public void testProcessCommonJSModules() throws IOException {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(TEST_FILE_NAME);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, TEST_MODULE_NAME);
    pass.process(input, null);
    Node root = input.getRootNode();

    Node module = pass.getModule();
    assertEquals("TestCommonJSModules", module.getName());
    assertEquals(1, module.getExportedNames().size());
    assertEquals("main", module.getExportedNames().get(0));

    NodeTraversal traverser = new NodeTraversal(input, root, null);
    traverser.traverse(new Rewritter(new ProcessCommonJSModulesCallback()));

    Node moduleExports = root.getFirstChildByName("module$exports");
    assertNotNull(moduleExports);

    Node main = root.getFirstChildByName("main");
    assertSame(main, moduleExports.getFirstChild());
  }

  @Test
  public void testProcessingRequireCalls() throws IOException {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(TEST_FILE_NAME);
    String requireModuleName = "RequiredModule";
    String currentModuleName = "CurrentModule";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, TEST_MODULE_NAME);
    pass.process(input, null);
    Node root = input.getRootNode();

    Node requireCall = root.getFirstChildByName("require");
    assertNotNull(requireCall);
    Node requireModule = requireCall.getFirstChild();
    assertEquals(requireModuleName, requireModule.getString());

    Node script = getCurrentScriptNode(root);
    Node moduleRef = script.getFirstChildByName("goog.require");
    Node moduleName = moduleRef.getChildAtIndex(0);
    assertEquals(requireModuleName, moduleName.getString());

    Node moduleExports = root.getFirstChildByName("module$exports");
    Node module = moduleExports.getFirstChild();
    module.putProp(Node.ORIGINALNAME_PROP, "module");
    module.setString(requireModuleName);

    Set<String> expectedModules = Sets.newHashSet("RequiredModule", "CurrentModule");
    Set<String> actualModules = input.getRequireMap().keySet();
    assertEquals(expectedModules, actualModules);
  }

  @Test
  public void testProcessCommonJSModulesWithExports() throws IOException {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(TEST_FILE_NAME);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, TEST_MODULE_NAME, true);
    pass.process(input, null);
    Node root = input.getRootNode();

    Node script = root.getFirstChild();
    Node ifNode = script.getLastChild();
    assertTrue(ifNode.isIfNode());
    Node condition = ifNode.getCondition();
    Node moduleName = condition.getChildAtIndex(0);
    Node module = condition.getChildAtIndex(1);
    assertEquals("TestCommonJSModules", moduleName.getString());
    assertEquals("TestCommonJSModules", module.getString());

    Node moduleExports = root.getFirstChildByName("module$exports");
    Node main = moduleExports.getFirstChild();
    Node moduleName2 = main.getFirstChild();
    assertEquals("TestCommonJSModules", moduleName2.getString());

    Node exports = main.getLastChild();
    Node exportedValue = exports.getFirstChild();
    assertEquals("main", exportedValue.getString());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() throws IOException {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(TEST_FILE_NAME);
    String currentModuleName = "CurrentModule";
    String requiredModuleName = "RequiredModule";
    String requiredRelativePath = "relative/RequiredModule.js";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, TEST_MODULE_NAME);
    pass.process(input, null);
    Node root = input.getRootNode();

    Node requireCall = root.getFirstChildByName("require");
    assertNotNull(requireCall);
    Node requireModule = requireCall.getFirstChild();
    assertEquals(requiredRelativePath, requireModule.getString());

    Node script = getCurrentScriptNode(root);
    Node moduleRef = script.getFirstChildByName("goog.require");
    Node moduleName = moduleRef.getChildAtIndex(0);
    assertEquals(requiredModuleName, moduleName.getString());

    Set<String> expectedModules = Sets.newHashSet("RequiredModule", "CurrentModule");
    Set<String> actualModules = input.getRequireMap().keySet();
    assertEquals(expectedModules, actualModules);
  }

  private Node getCurrentScriptNode(Node node) {
    while (true) {
      if (node.isScript()) {
        return node;
      }
      node = node.getParent();
    }
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleScripts() throws IOException {
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(TEST_FILE_NAME);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, TEST_MODULE_NAME);
    pass.process(input, null);
    Node root = input.getRootNode();

    Node module = pass.getModule();
    assertEquals("TestCommonJSModules", module.getName());
    assertEquals(2, module.getExportedNames().size());
    assertEquals("main", module.getExportedNames().get(0));
    assertEquals("sub", module.getExportedNames().get(1));

    Node moduleExports = root.getFirstChildByName("module$exports");
    assertNotNull(moduleExports);

    Node main = root.getFirstChildByName("main");
    assertSame(main, moduleExports.getFirstChild());

    Node sub = root.getFirstChildByName("sub");
    Node subModuleExports = sub.getFirstChildByName("module$exports");
    assertNotNull(subModuleExports);

    Node subMain = subModuleExports.getFirstChild();
    assertEquals("subMain", subMain.getString());
  }

  private static class ProcessCommonJSModulesCallback extends NodeVisitor {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "goog.require".equals(n.getFirstChild().getQualifiedName())) {
        visitGoogRequireCall(t, n, parent);
      }
    }

    private void visitGoogRequireCall(NodeTraversal t, Node requireCall, Node parent) {
      Node moduleNames = requireCall.getChildAtIndex(0);
      Node moduleNameList = moduleNames.getFirstChild();
      NodeList moduleNamesList = moduleNameList.getChildren();
      for (int i = 0; i < moduleNamesList.length(); i++) {
        Node moduleName = moduleNamesList.get(i);
        String moduleNameStr = moduleName.getString();
        Node moduleRef = parent.getFirstChildByName(moduleNameStr);
        if (moduleRef != null) {
          moduleRef.copyInformationFromForTree(requireCall.getChildAtIndex(1));
        }
      }
    }
  }
}