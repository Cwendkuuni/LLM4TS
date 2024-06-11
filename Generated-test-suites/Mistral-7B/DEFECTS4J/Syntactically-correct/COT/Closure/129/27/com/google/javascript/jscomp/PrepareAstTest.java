package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

/**
 * PrepareAstTest class to test the PrepareAst class
 */
public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    // Create a compiler instance and initialize the PrepareAst pass
    Compiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Prepare the test node for processing
    Node root = IR.program();

    // Test process method
    prepareAst.process(null, root);

    // Check if the normalizeNodeTypes method was called
    assertNormalizeNodeTypesCalled(root);

    // Check if the PrepareAnnotations method was called
    assertPrepareAnnotationsCalled(root);
  }

  /**
   * Test method to check if the normalizeNodeTypes method was called
   * @param root - the root Node of the AST
   */
  private void assertNormalizeNodeTypesCalled(Node root) {
    Preconditions.checkState(root != null, "root Node is null");

    // Check if the normalizeNodeTypes method was called recursively on all nodes
    NodeTraversal.traverse(
        compiler, root, new NodeTraversal.Callback() {
          @Override
          public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
            // Check if the normalizeNodeTypes method was called on the current node
            return n.getBooleanProp(PrepareAst.class.getName() + ".normalizeNodeTypes")
                || super.shouldTraverse(t, n, parent);
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            // Do nothing
          }
        });
  }

  /**
   * Test method to check if the PrepareAnnotations method was called
   * @param root - the root Node of the AST
   */
  private void assertPrepareAnnotationsCalled(Node root) {
    Preconditions.checkState(root != null, "root Node is null");

    // Check if the PrepareAnnotations method was called on the root Node and its children
    NodeTraversal.traverse(
        compiler, root, new PrepareAnnotations(), null, true);
  }

  /**
   * Static inner class PrepareAnnotations to test the PrepareAnnotations method
   */
  static class PrepareAnnotations
      extends PrepareAst.PrepareAnnotations {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      super.visit(t, n, parent);

      if (n.isObjectLit()) {
        // Test object literal annotations
        assertObjectLiteralAnnotationsPresent(n);
      }

      if (n.isFunction()) {
        // Test dispatcher annotations
        assertDispatcherAnnotationsPresent(n, parent);
      }
    }

    /**
     * Test method to check if object literal annotations are present
     * @param objlit - the object literal Node
     */
    private void assertObjectLiteralAnnotationsPresent(Node objlit) {
      Preconditions.checkState(objlit.isObjectLit(), "node is not an object literal");

      // Check if each key-value pair has the correct JSDoc info
      for (Node key = objlit.getFirstChild(); key != null; key = key.getNext()) {
        Node value = key.getFirstChild();

        assertObjectLiteralKeyAnnotationsPresent(objlit, key, value);
      }
    }

    /**
     * Test method to check if object literal key annotations are present
     * @param objlit - the object literal Node
     * @param key - the key Node
     * @param value - the value Node
     */
    private void assertObjectLiteralKeyAnnotationsPresent(Node objlit, Node key, Node value) {
      Preconditions.checkState(objlit.isObjectLit(), "node is not an object literal");
      Preconditions.checkState(key.isName(), "key is not a name");

      // Check if the JSDoc info is present on the value Node
      assertJsDocInfoPresent(value, key.getString());
    }

    /**
     * Test method to check if dispatcher annotations are present
     * @param n - the function Node
     * @param parent - the parent Node
     */
    private void assertDispatcherAnnotationsPresent(Node n, Node parent) {
      Preconditions.checkState(n.isFunction(), "node is not a function");

      if (parent != null && parent.getJSDocInfo() != null) {
        // Check if the dispatcher annotation is present on the function Node
        assertTrue(n.getBooleanProp(PrepareAst.class.getName() + ".IS_DISPATCHER"));
      }
    }
  }
}