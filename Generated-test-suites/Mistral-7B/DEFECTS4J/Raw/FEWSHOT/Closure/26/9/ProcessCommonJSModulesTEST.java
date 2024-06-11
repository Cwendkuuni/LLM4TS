package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Test class for ProcessCommonJSModules.
 */
public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private CompilerInput input;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = new CompilerInput(new File("test.js"), null, null, null, null, null,
        Sets.newHashSet("test.js"), null, null, null);
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws URISyntaxException, IOException {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        ".");
    processor.process(input, NodeFactory.createTopLevel());

    Node root = input.getRoot();
    Node moduleExpr = root.getChildAtIndex(0);
    assertNotNull(moduleExpr);
    assertEquals("test$", moduleExpr.getChildAtIndex(0).getString());

    Node exports = moduleExpr.getChildAtIndex(1);
    assertEquals("test$", exports.getString());
    assertEquals(root.getChildCount() - 1, exports.getChildCount());

    Node requireCall = moduleExpr.getChildAtIndex(2);
    assertEquals("require", requireCall.getFirstChild().getString());

    Node requireModuleName = requireCall.getChildAtIndex(1);
    assertEquals("other$", requireModuleName.getString());

    Node googProvideCall = moduleExpr.getChildAtIndex(3);
    assertEquals("goog.provide", googProvideCall.getFirstChild().getString());
    assertEquals("test$", googProvideCall.getChildAtIndex(1).getString());

    Node googRequireCall = moduleExpr.getChildAtIndex(4);
    assertEquals("goog.require", googRequireCall.getFirstChild().getString());
    assertEquals("other$", googRequireCall.getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRelativePaths()
      throws URISyntaxException, IOException {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        ".", true);
    input.setSourceName("path/to/test.js");
    processor.process(input, NodeFactory.createTopLevel());

    Node root = input.getRoot();
    Node moduleExpr = root.getChildAtIndex(0);
    assertNotNull(moduleExpr);
    assertEquals("test$", moduleExpr.getChildAtIndex(0).getString());

    Node exports = moduleExpr.getChildAtIndex(1);
    assertEquals("test$", exports.getString());
    assertEquals(root.getChildCount() - 1, exports.getChildCount());

    Node requireCall = moduleExpr.getChildAtIndex(2);
    assertEquals("require", requireCall.getFirstChild().getString());

    Node requireModuleName = requireCall.getChildAtIndex(1);
    assertEquals("./other$", requireModuleName.getString());

    Node googProvideCall = moduleExpr.getChildAtIndex(3);
    assertEquals("goog.provide", googProvideCall.getFirstChild().getString());
    assertEquals("test$", googProvideCall.getChildAtIndex(1).getString());

    Node googRequireCall = moduleExpr.getChildAtIndex(4);
    assertEquals("goog.require", googRequireCall.getFirstChild().getString());
    assertEquals("./other$", googRequireCall.getChildAtIndex(1).getString());
  }
}