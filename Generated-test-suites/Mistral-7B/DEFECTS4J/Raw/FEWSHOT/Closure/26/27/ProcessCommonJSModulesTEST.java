package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String filenamePrefix = "/test/commonjs/";
    AbstractCompiler compiler = new Compiler() {
      @Override
      public void process(Node externs, Node root) {
        new ProcessCommonJSModules(this, filenamePrefix, true).process(externs, root);
      }
    };

    CompilerPass pass = new ProcessCommonJSModules(compiler, filenamePrefix, true);

    // Test empty input
    Node root = Node.root();
    Node traversalRoot = NodeTraversal. Traverse(compiler, root);
    pass.process(null, traversalRoot);

    // Test simple require call
    Node requireCall = Node.newCall(Node.name("require"), Node.string("module"));
    Node script = Node.newScript("require('module')");
    Node traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalScript.addChild(requireCall);
    Node originalRoot = Node.root();
    Node traversalOriginalRoot = NodeTraversal.traverse(compiler, originalRoot);
    traversalOriginalRoot.replaceChild(0, traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node googRequireCall = traversalOriginalRoot.getChild(1);
    Node googProvideCall = traversalOriginalRoot.getChild(2);
    Node module = traversalOriginalRoot.getChild(3);

    // Check that require call is replaced with goog.require
    assertEquals("module", googRequireCall.getChild(0).getString());

    // Check that goog.provide is called with the correct module name
    assertEquals("module", googProvideCall.getChild(0).getString());

    // Check that goog.require is called with the correct module name
    assertEquals("module", traversalOriginalRoot.getChild(4).getChild(0).getString());

    // Check that module.exports is renamed to module$exports
    assertEquals("module$exports", module.getChildAtIndex(1).getString());

    // Test multiple require calls
    Node multipleRequireCalls = Node.newScript(
        "require('module1'), require('module2')");
    Node traversalMultipleRequireCalls = NodeTraversal.traverse(compiler, multipleRequireCalls).getChild(0);

    traversalOriginalRoot.addChild(traversalMultipleRequireCalls);

    pass.process(Node.root(), traversalOriginalRoot);

    assertEquals(2, traversalOriginalRoot.getChildCount());
    Node googRequireArray = traversalOriginalRoot.getChildAtIndex(1);

    Set<String> expectedRequireModules = Sets.newHashSet("module1", "module2");
    assertEquals(expectedRequireModules, getRequireModules(googRequireArray));

    // Test relative require calls
    String currentFilename = "/test/commonjs/main.js";
    String requiredFilename = "sub/module.js";

    script = Node.newScript("require('./sub/module.js')");
    traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalOriginalRoot = Node.root();
    traversalOriginalRoot.addChild(traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node requireCallRelative = traversalOriginalRoot.getChild(1);
    String relativeModuleName = toModuleName(requiredFilename, currentFilename);

    assertEquals(relativeModuleName, requireCallRelative.getChildAtIndex(1).getString());

    // Test absolute require calls
    requiredFilename = "another/module.js";

    script = Node.newScript("require('another/module.js')");
    traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalOriginalRoot = Node.root();
    traversalOriginalRoot.addChild(traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node requireCallAbsolute = traversalOriginalRoot.getChild(1);

    assertEquals("another/module.js", requireCallAbsolute.getChildAtIndex(1).getString());

    // Test traversing multiple levels of require calls
    String grandChildFilename = "sub/sub/grandchild.js";

    script = Node.newScript("require('./sub/sub/grandchild.js')");
    traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalOriginalRoot = Node.root();
    traversalOriginalRoot.addChild(traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node requireCallGrandChild = traversalOriginalRoot.getChild(1);
    String grandChildModuleName = toModuleName(grandChildFilename, currentFilename);

    assertEquals(grandChildModuleName, requireCallGrandChild.getChildAtIndex(1).getString());

    // Test module.exports
    script = Node.newScript("module.exports = 'hello'");
    traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalOriginalRoot = Node.root();
    traversalOriginalRoot.addChild(traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node moduleExports = traversalOriginalRoot.getChild(2);

    assertEquals("hello", moduleExports.getString());

    // Test multiple module.exports
    script = Node.newScript("module.exports = 'hello'; module.exports = 'world'");
    traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalOriginalRoot = Node.root();
    traversalOriginalRoot.addChild(traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node moduleExportsFirst = traversalOriginalRoot.getChild(3);
    Node moduleExportsSecond = traversalOriginalRoot.getChild(4);

    assertEquals("hello", moduleExportsFirst.getString());
    assertEquals("world", moduleExportsSecond.getString());

    // Test optional module.exports override
    script = Node.newScript("(function () { module.exports = 'world'; })()");
    traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalOriginalRoot = Node.root();
    traversalOriginalRoot.addChild(traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node moduleExportsOptional = traversalOriginalRoot.getChild(2);

    assertEquals("world", moduleExportsOptional.getString());

    // Test jscomp-annotations
    String annotationsFileName = "annotations.js";

    script = Node.newScript(
      "require('annotations.js'); @goog.export function foo() {}");
    traversalScript = NodeTraversal.traverse(compiler, script).getChild(0);
    traversalOriginalRoot = Node.root();
    traversalOriginalRoot.addChild(traversalScript);

    pass.process(Node.root(), traversalOriginalRoot);

    Node fooFunction = traversalOriginalRoot.getChild(1);

    assertEquals("foo", fooFunction.getQualifiedName());
  }

  private Set<String> getRequireModules(Node googRequireArray) {
    Set<String> result = Sets.newHashSet();
    for (Node child : googRequireArray.getChildren()) {
      result.add(child.getString());
    }
    return result;
  }

  private static String toModuleName(String requiredFilename,
      String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }
}