package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Test class for ProcessCommonJSModules.
 */
public class ProcessCommonJSModulesTest {

  private static final String FILENAME_PREFIX = "/test/";
  private static final String MODULE_NAME = "testModule";
  private static final String REQUIRED_FILENAME = "requiredModule";
  private static final String SCRIPT_FILENAME = "script.js";

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(
        new SourceName(new File(SCRIPT_FILENAME)), File.separator, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, FILENAME_PREFIX);
    processor.process(input, null);

    Node root = input.getRootNode();
    Node script = getScriptNode(root);

    // Check moduleName and goog.provide
    assertEquals(MODULE_NAME, script.getChildAtIndex(0).getString());
    assertEquals("goog.provide('" + MODULE_NAME + "');", script.getChildAtIndex(1)
        .toString());

    // Check require call
    Node requireCall = script.getChildAtIndex(2);
    assertEquals("goog.require('" + REQUIRED_FILENAME + "');", requireCall.toString());

    // Check script body
    assertEquals("var " + MODULE_NAME + "_module$exports = " +  // NOSONAR
        "{'module$exports': module$exports};" + // NOSONAR
        "module.exports = " + MODULE_NAME + "_module$exports;" + // NOSONAR
        "goog.provide('" + MODULE_NAME + "');", script.getChildAtIndex(3)
        .toString());

    // Check require call replaced
    Node requireNode = getRequireNode(script);
    assertNull(requireNode);
  }

  @Test
  public void testProcessCommonJSModulesWithDependencies() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(
        new SourceName(new File(SCRIPT_FILENAME)), File.separator, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, FILENAME_PREFIX, true);
    processor.process(input, null);

    Set<String> dependencies = input.getRequireSet();
    assertEquals(1, dependencies.size());
    assertEquals(REQUIRED_FILENAME, dependencies.iterator().next());
  }

  @Test
  public void testToModuleName() {
    assertEquals(MODULE_NAME_PREFIX + "testModule", toModuleName("testModule.js"));
    assertEquals(MODULE_NAME_PREFIX + "test" + MODULE_NAME_SEPARATOR + "module",
        toModuleName("test/module.js"));
    assertEquals(MODULE_NAME_PREFIX + "test" + MODULE_NAME_SEPARATOR + "sub" +
        MODULE_NAME_SEPARATOR + "folder" + MODULE_NAME_SEPARATOR + "module.js",
        toModuleName("test/sub/folder/module.js"));
  }

  @Test
  public void testToModuleNameWithRelativePath() {
    assertEquals(MODULE_NAME_PREFIX + "test" + MODULE_NAME_SEPARATOR +
        "required" + MODULE_NAME_SEPARATOR + "module",
        toModuleName("test/required/module.js", "test/script.js"));
  }

  private static Node getScriptNode(Node root) {
    for (int i = 0; i < root.getChildCount(); i++) {
      Node child = root.getChildAtIndex(i);
      if (child.isScript()) {
        return child;
      }
    }
    throw new RuntimeException("Expected script node.");
  }

  private static Node getRequireNode(Node root) {
    for (int i = 0; i < root.getChildCount(); i++) {
      Node child = root.getChildAtIndex(i);
      if (child.isCall() && child.getChildCount() == 2 &&
          "require".equals(child.getFirstChild().getQualifiedName())) {
        return child;
      }
    }
    return null;
  }

  private static String toModuleName(String filename) {
    return Util.toModuleName(filename);
  }

  private static String toModuleName(String requiredFilename,
      String currentFilename) {
    return Util.toModuleName(requiredFilename, currentFilename);
  }
}

class Util {
  public static String toModuleName(String filename) {
    return ProcessCommonJSModules.toModuleName(filename);
  }

  public static String toModuleName(String requiredFilename,
      String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }
}