package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.test.BaseCompilerTest;
import com.google.javascript.rhino.IR;

import java.util.Set;

public class ProcessCommonJSModulesTest extends BaseCompilerTest {

  public static void main(String[] args) throws Exception {
    junit.textui.TestRunner.run(ProcessCommonJSModulesTest.class);
  }

  @Override
  protected CompilerPass getProcessor(CompilerInput input) {
    return new ProcessCommonJSModules(getCompiler(), "test/");
  }

  @Override
  protected CompilerOptions getOptions() {
    CompilerOptions options = super.getOptions();
    options.setDeleteIntermediateFiles(false);
    return options;
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    String testModuleName = "testModule";
    String testFileName = "test.js";

    String testModuleSource = "var exports = {foo: 1};\n" +
        "function testFunction() {\n" +
        "  console.log('Hello World');\n" +
        "}\n" +
        "exports.testFunction = testFunction;\n";

    CompilerInput input = createInput(testFileName, testModuleSource);
    processTestSources(input);

    Node root = input.getRootNode();
    Node testFunction = findTestFunction(root, testModuleName);
    assertEquals("testFunction", testFunction.getQualifiedName());
    assertEquals(1, testFunction.getChildCount());
    assertEquals("console.log(\"Hello World\");", testFunction.getChildAtIndex(0)
        .getString());

    Node exports = findModuleExports(root, testModuleName);
    assertEquals("exports", exports.getQualifiedName());
    assertEquals(1, exports.getChildCount());
    assertEquals("{foo: 1}", exports.getChildAtIndex(0).getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesRelativeImport() throws Exception {
    String testParentModuleName = "parentModule";
    String testModuleName = "childModule";
    String testFileName = "child.js";
    String testParentModuleFileName = "parent.js";

    String testModuleSource = "var exports = {foo: 1};\n";

    String testParentModuleSource = "require('." + testModuleName + "');\n";

    CompilerInput parentInput = createInput(testParentModuleFileName,
        testParentModuleSource);
    processTestSources(parentInput);

    CompilerInput input = createInput(testFileName, testModuleSource);
    processTestSources(input);

    Node root = input.getRootNode();
    Node testFunction = findTestFunction(root, testModuleName);
    assertEquals("testFunction", testFunction.getQualifiedName());
    assertEquals(1, testFunction.getChildCount());
    assertEquals("console.log(\"Hello World\");", testFunction.getChildAtIndex(0)
        .getString());

    Node exports = findModuleExports(root, testModuleName);
    assertEquals("exports", exports.getQualifiedName());
    assertEquals(1, exports.getChildCount());
    assertEquals("{foo: 1}", exports.getChildAtIndex(0).getString());

    Node requireCall = findRequireCall(root, testModuleName, testParentModuleName);
    assertEquals("require", requireCall.getFirstChild().getQualifiedName());
    assertEquals(1, requireCall.getChildCount());
    assertEquals(".' + parentModule + '/" + childModule, requireCall
        .getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCaseInsensitiveImport() throws Exception {
    String testModuleName = "testModule";
    String testFileName = "test.js";

    String testModuleSource = "var exports = {foo: 1};\n" +
        "function testFunction() {\n" +
        "  console.log('Hello World');\n" +
        "}\n" +
        "exports.testFunction = testFunction;\n";

    CompilerInput input = createInput(testFileName, testModuleSource);
    processTestSources(input);

    Node root = input.getRootNode();
    Node testFunction = findTestFunction(root, testModuleName);
    assertEquals("testFunction", testFunction.getQualifiedName());
    assertEquals(1, testFunction.getChildCount());
    assertEquals("console.log(\"Hello World\");", testFunction.getChildAtIndex(0)
        .getString());

    Node exports = findModuleExports(root, testModuleName);
    assertEquals("exports", exports.getQualifiedName());
    assertEquals(1, exports.getChildCount());
    assertEquals("{foo: 1}", exports.getChildAtIndex(0).getString());

    // Test case insensitive import
    String testCaseInsensitiveImportSource = "var exports = " +
        "require('./" + testModuleName.toLowerCase() + "');\n";

    CompilerInput caseInsensitiveInput = createInput(testFileName,
        testModuleSource + "\n" + testCaseInsensitiveImportSource);
    processTestSources(caseInsensitiveInput);

    root = caseInsensitiveInput.getRootNode();
    testFunction = findTestFunction(root, testModuleName);
    assertEquals("testFunction", testFunction.getQualifiedName());
    assertEquals(1, testFunction.getChildCount());
    assertEquals("console.log(\"Hello World\");", testFunction.getChildAtIndex(0)
        .getString());

    exports = findModuleExports(root, testModuleName);
    assertEquals("exports", exports.getQualifiedName());
    assertEquals(1, exports.getChildCount());
    assertEquals("{foo: 1}", exports.getChildAtIndex(0).getString());

    requireCall = findRequireCall(root, testModuleName, null);
    assertEquals("require", requireCall.getFirstChild().getQualifiedName());
    assertEquals(1, requireCall.getChildCount());
    assertEquals("./" + testModuleName.toLowerCase(), requireCall
        .getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesInvalidImport() throws Exception {
    String testModuleName = "testModule";
    String testFileName = "test.js";

    String testModuleSource = "var exports = {foo: 1};\n" +
        "function testFunction() {\n" +
        "  console.log('Hello World');\n" +
        "}\n" +
        "exports.testFunction = testFunction;\n";

    CompilerInput input = createInput(testFileName, testModuleSource);
    processTestSources(input);

    Node root = input.getRootNode();

    // Test invalid import
    String invalidImportSource = "require('testModuleX');\n";

    CompilerInput invalidInput = createInput(testFileName,
        testModuleSource + "\n" + invalidImportSource);
    try {
      processTestSources(invalidInput);
      fail("Expected CompilationError");
    } catch (CompilerException e) {
      assertEquals("Required module 'testModuleX' does not exist.", e.getMessage());
    }

    root = invalidInput.getRootNode();
    Node requireCall = findRequireCall(root, "testModuleX", null);
    assertNull(requireCall);
  }

  private Node findTestFunction(Node root, String moduleName) {
    Node exports = findModuleExports(root, moduleName);
    return findChildWithName(exports, "testFunction");
  }

  private Node findModuleExports(Node root, String moduleName) {
    Node module = findChildWithName(root, moduleName);
    return findChildWithName(module, "exports");
  }

  private Node findRequireCall(Node root, String moduleName, String parentModuleName) {
    NodeTraversal traversal = new NodeTraversal(root);
    Node requireCall = null;
    for (Node child = root.getFirstChild(); child != null; child = child
        .getNextSibling()) {
      if (child.isCall() && "require".equals(child.getFirstChild().getName())) {
        Node requireModuleName = child.getChildAtIndex(1);
        if (moduleName == null) {
          requireCall = child;
        } else if (requireModuleName.isString() &&
            moduleName.equals(requireModuleName.getString())) {
          if (parentModuleName != null) {
            Node parentModule = findChildWithName(root, parentModuleName);
            if (parentModule != null && parentModule.getChildAtIndex(0)
                .equals(child.getParent())) {
              requireCall = child;
            }
          } else {
            requireCall = child;
          }
        }
      }
    }
    return requireCall;
  }

  private Node findChildWithName(Node parent, String name) {
    for (Node child = parent.getFirstChild(); child != null; child = child
        .getNextSibling()) {
      if (child.isName() && child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }

}