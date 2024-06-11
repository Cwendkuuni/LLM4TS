package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.CompilerPassTestHelper;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

/**
 * Test for InlineObjectLiterals class
 */
public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  private static final String TEST_FILE = "InlineObjectLiteralsTest.js";

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Override
  protected String getTestFile() {
    return TEST_FILE;
  }

  // Basic test cases for process() method
  public void testProcessBasic() {
    Node root = parseTestFile(TEST_FILE);
    CompilerPass pass = getPass();
    Node previousRoot = currentRoot();
    pass.process(externs, root);
    Node newRoot = currentRoot();
    CompilerPassTestHelper.assertCompiledWithWarnings(previousRoot, newRoot);
  }

  // Test case for object literals with no references
  public void testProcessNoReferenceObjectLiterals() {
    String testCode = "var x = {a: 1, b: 2};";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    Node previousRoot = currentRoot();
    pass.process(externs, root);
    Node newRoot = currentRoot();
    CompilerPassTestHelper.assertCompiledWithWarnings(previousRoot, newRoot);
  }

  // Test case for object literals with references
  public void testProcessObjectLiteralsWithReferences() {
    String testCode = "var x = {a: 1, b: function() {return x.a;}};";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    Node previousRoot = currentRoot();
    pass.process(externs, root);
    Node newRoot = currentRoot();
    CompilerPassTestHelper.assertCompiledWithWarnings(previousRoot, newRoot);
  }

  // Test case for object literals with cycles
  public void testProcessObjectLiteralsWithCycles() {
    String testCode = "var x = {a: {a: x}};";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    try {
      pass.process(externs, root);
      fail("Expected an exception for object literals with cycles.");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  // Test case for object literals with self-referential assignments
  public void testProcessSelfReferentialObjectLiterals() {
    String testCode = "var x = {a: x};";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    try {
      pass.process(externs, root);
      fail("Expected an exception for self-referential object literals.");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  // Test case for object literals with getter/setter properties
  public void testProcessObjectLiteralsWithGettersSetters() {
    String testCode = "var x = {get a() {return this.b;}, b: 1};";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    try {
      pass.process(externs, root);
      fail("Expected an exception for object literals with getter/setter properties.");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // Test case for object literals with exported variables
  public void testProcessExportedObjectLiterals() {
    String testCode = "var x = {exported: 1};\nexport let y = x.exported;";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    Node previousRoot = currentRoot();
    pass.process(externs, root);
    Node newRoot = currentRoot();
    CompilerPassTestHelper.assertCompiledWithWarnings(previousRoot, newRoot);
  }

  // Test case for object literals with globally defined variables
  public void testProcessGloballyDefinedObjectLiterals() {
    String testCode = "var x = {exported: 1};";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    Node previousRoot = currentRoot();
    pass.process(externs, root);
    Node newRoot = currentRoot();
    CompilerPassTestHelper.assertCompiledWithWarnings(previousRoot, newRoot);
  }

  // Test case for object literals with initializer references
  public void testProcessObjectLiteralsWithInitializerReferences() {
    String testCode = "var x = {a: function() {return this.b;}, b: 1};";
    Node root = parseTestCode(testCode);
    CompilerPass pass = getPass();
    Node previousRoot = currentRoot();
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    pass.process(externs, root);
    List<ReferenceCollection> referenceInfos = callback.getReferenceInfos();
    assertFalse("Expected no reference infos", referenceInfos.isEmpty());
    ReferenceCollection referenceInfo = referenceInfos.get(0);
    List<Reference> references = referenceInfo.references;
    assertTrue("Expected at least one reference", references.size() > 0);
    for (Reference ref : references) {
      if (ref.getNode().getType() == Token.GETPROP) {
        Node getprop = ref.getNode();
        assertFalse("Expected no getprop nodes", getprop.getParent().getType() == Token.GETPROP);
        String var = getprop.getChildAtIndex(1).getString();
        ReferenceMap referenceMap = referenceInfo.getReferenceMap();
        Reference declaredRef = referenceMap.get(var);
        assertTrue("Expected a declared reference", declaredRef != null);
        Reference initRef = referenceMap.getInitializingReference(var);
        assertEquals("Unexpected init reference", initRef, ref);
      }
    }
    pass.process(externs, root);
    Node newRoot = currentRoot();
    CompilerPassTestHelper.assertCompiledWithWarnings(previousRoot, newRoot);
  }
}