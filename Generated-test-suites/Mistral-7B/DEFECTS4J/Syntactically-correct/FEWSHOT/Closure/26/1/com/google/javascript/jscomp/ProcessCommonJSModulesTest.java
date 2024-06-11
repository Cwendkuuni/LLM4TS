package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Test class for ProcessCommonJSModules.
 */
public class ProcessCommonJSModulesTest {

  private CompilerInput input;
  private CompilerPass processor;
  private NodeFactory nodeFactory;
  private JSErrorReporter errorReporter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    nodeFactory = new NodeFactory();
    errorReporter = mock(JSErrorReporter.class);
    input = new CompilerInput("test.js", "test.js", null, null, null, null, null, null, null, errorReporter);
    processor = new ProcessCommonJSModules(new Compiler(null), ".", false);
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "src/commonjs/test.js";
    String moduleName = ProcessCommonJSModules.toModuleName(filename);
    assertEquals("module$src_commonjs_test", moduleName);
  }

  @Test
  public void testGuessCJSModuleNameWithRelativePath() {
    String requiredFilename = "test.js";
    String currentFilename = "src/commonjs/main.js";
    String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals("module$test", moduleName);
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "src/commonjs/test.js";
    String normalizedSourceName = ProcessCommonJSModules.normalizeSourceName(filename);
    assertEquals("test.js", normalizedSourceName);
  }

  @Test
  public void testProcess() {
    String sourceCode = "require('./test.js')";
    Node root = nodeFactory.parseTree(sourceCode);
    processor.process(input, root);

    NodeTraversal traversal = NodeTraversal.create(input, null, null, null, null, null, null);
    Node requireCall = traversal.getFirstDescendantOfType(root, Node.CALL_EXPR);
    assertNotNull(requireCall);

    Node moduleRef = requireCall.getChildAtIndex(1);
    String moduleName = moduleRef.getString();
    assertEquals("module$test", moduleName);

    Node script = traversal.getFirstDescendantOfType(root, Node.SCRIPT_NODE);
    assertEquals(1, script.getChildCount());

    Node googProvideCall = script.getChildAtIndex(0);
    assertEquals("goog.provide", googProvideCall.getQualifiedName());

    String expectedModuleName = "module$test";
    assertEquals(expectedModuleName, googProvideCall.getChildAtIndex(1).getString());
  }

  @Test
  public void testProcessWithDependenciesReporting() {
    String sourceCode = "require('./test.js');\n" +
            "goog.provide('module.name');\n" +
            "function test() {}";
    Node root = nodeFactory.parseTree(sourceCode);
    processor = new ProcessCommonJSModules(new Compiler(null), ".", true);
    processor.process(input, root);

    Set<String> requires = input.getRequires();
    assertEquals(1, requires.size());
    assertEquals("module$test", requires.iterator().next());
  }
}