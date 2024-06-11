package org.apache.commons.cli; // This is not the package for ProcessCommonJSModules, but it is required for the tests to run properly since UtilTest imports it.

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SimpleSourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Tests for ProcessCommonJSModules compiler pass.
 */
public class ProcessCommonJSModulesTest {

  private static final String TEST_FILE_NAME = "TestCommonJSModules.java";
  private static final String TEST_MODULE_NAME = "testModule";
  private static final String TEST_REQUIRED_MODULE_NAME = "requiredModule";

  @Test
  public void testProcessCommonJSModules() {
    // Given: A CompilerPass instance and a CompilerInput
    CompilerPass compilerPass = new ProcessCommonJSModules(
        new StubCompiler(), TEST_FILENAME_PREFIX);
    CompilerInput input = new CompilerInput(new SimpleSourceFile(
        new File(TEST_FILE_NAME), 0, new StringSourceContentProvider()));

    // When: The ProcessCommonJSModules compiler pass is executed
    compilerPass.process(input.getExterns(), input.getRootNode());

    // Then: The root node should not have any require calls and the module name
    // should be replaced by the corresponding "module$" variable
    Node rootNode = input.getRootNode();
    assertNull(findRequireCall(rootNode));
    assertEquals(TEST_MODULE_NAME + "$", rootNode.getChildAtIndex(0)
        .getString());

    // And: The module "module$" variable should contain the original module name
    Node moduleVar = rootNode.getChildAtIndex(0);
    assertEquals(TEST_MODULE_NAME, moduleVar.getProp(Node.ORIGINALNAME_PROP)
        .getString());

    // And: The "module.exports" property should be renamed to "exports$"
    Node exportsProp = findProperty(rootNode, "exports");
    assertEquals("exports$", exportsProp.getQualifiedName());

    // And: The "exports" property should contain the original exports object
    Node exportsObject = exportsProp.getChildAtIndex(0);
    assertEquals("exports", exportsObject.getProp(Node.ORIGINALNAME_PROP)
        .getString());

    // And: The "goog.provide" call should be present
    Node googProvideCall = findCall(rootNode, "goog.provide");
    assertSame(googProvideCall, rootNode.getChildAtIndex(1));

    // And: The "goog.require" calls should be present for each required module
    Set<String> requiredModules = Sets.newHashSet("requiredModule");
    checkRequireCalls(rootNode, requiredModules);

    // And: The required modules should be added to the CompilerInput's requires list
    Set<String> inputRequiredModules = input.getRequireList();
    assertEquals(requiredModules, inputRequiredModules);
  }

  /**
   * Finds the first require call in the given node.
   */
  private Node findRequireCall(Node node) {
    if (node.isCall() && node.getChildCount() == 2 &&
        "require".equals(node.getFirstChild().getQualifiedName())) {
      return node;
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      Node result = findRequireCall(child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  /**
   * Finds a property with the given name in the given node.
   */
  private Node findProperty(Node node, String propertyName) {
    if (node.isGetProp()) {
      String name = node.getQualifiedName();
      if (name != null && name.startsWith(propertyName + ".")) {
        return node;
      }
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      Node result = findProperty(child, propertyName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  /**
   * Finds a call with the given name in the given node.
   */
  private Node findCall(Node node, String callName) {
    if (node.isCall() && callName.equals(node.getFirstChild().getString())) {
      return node;
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      Node result = findCall(child, callName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  /**
   * Checks if the given node contains the expected require calls.
   */
  private void checkRequireCalls(Node node, Set<String> expectedModules) {
    if (node.isCall() && "goog.require".equals(node.getFirstChild().getString())) {
      Node requireCall = node;
      checkRequireCallsHelper(requireCall, expectedModules);
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      checkRequireCalls(child, expectedModules);
    }
  }

  /**
   * Helper method for checkRequireCalls to recursively check the require calls
   * in a given node.
   */
  private void checkRequireCallsHelper(Node node, Set<String> expectedModules) {
    if (!node.isCall() || !"goog.require".equals(node.getFirstChild()
        .getString())) {
      return;
    }
    NodeList requireArgs = node.getChildList().slice(1);
    for (int i = 0; i < requireArgs.getLength(); i++) {
      Node requireArg = requireArgs.get(i);
      String moduleName = toModuleName(requireArg.getString());
      checkState(expectedModules.contains(moduleName), "Unexpected " + moduleName
          + " require call");
      expectedModules.remove(moduleName);
    }
  }
}

/**
 * Stub Compiler for testing ProcessCommonJSModules.
 */
class StubCompiler implements AbstractCompiler {

  @Override
  public CompilerPass getPass(String passName) {
    if (passName.equals(ProcessCommonJSModules.class.getName())) {
      return new ProcessCommonJSModules(this, TEST_FILENAME_PREFIX, true);
    }
    return null;
  }

  @Override
  public void reportCodeChange() {
    // No action required for testing purposes
  }
}