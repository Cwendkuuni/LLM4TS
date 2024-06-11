package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

/**
 * Test cases for {@code PrepareAst} class.
 */
public class PrepareAstTest {

  private AbstractCompiler compiler;
  private PrepareAst prepareAst;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testPrepareAst_NoCheckOnly() {
    Node root = compileTestCode("testPrepareAst_NoCheckOnly.js");
    prepareAst.process(null, root);
    verifyTree(root);
  }

  @Test
  public void testPrepareAst_CheckOnly() {
    prepareAst = new PrepareAst(compiler, true);
    Node root = compileTestCode("testPrepareAst_NoCheckOnly.js");
    prepareAst.process(null, root);
    // Check-only pass doesn't modify the tree, so just verify it as-is.
    verifyTree(root);
  }

  @Test
  public void testPrepareAst_ProcessExterns() {
    Node externs = compileTestCode("testPrepareAst_ProcessExterns.js");
    Node root = compileTestCode("testPrepareAst_NoCheckOnly.js");

    prepareAst.process(externs, root);
    verifyTree(root);
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node root = compileTestCode("testNormalizeNodeTypes.js");
    prepareAst.process(null, root);
    verifyTree(root);
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = compileTestCode("testNormalizeBlocks.js");
    prepareAst.process(null, root);
    verifyTree(root);
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = compileTestCode("testPrepareAnnotations.js");
    prepareAst.process(null, root);
    verifyTree(root);
  }

  private Node compileTestCode(String fileName) {
    return compiler.compile(loadFileIntoString(fileName), null, null, null);
  }

  private void verifyTree(Node root) {
    Node expectedRoot = loadNodeFromString(LOAD_EXPECTED_FILE + fileName);
    NodeUtil.assertTreeEquivalent(compiler, expectedRoot, root);
  }

  private String loadFileIntoString(String fileName) {
    // Load the test file content into a string.
    // Replace this with your file loading logic.
    return "Your file content goes here.";
  }

  private Node loadNodeFromString(String fileName) {
    // Load the expected tree from a string.
    // Replace this with your tree loading logic.
    return new IR().parseInitExpression("{}");
  }

  private static final String LOAD_EXPECTED_FILE = "Expected/";
}