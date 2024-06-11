package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testPrepareAst_process_EmptyAST() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node root = null;
    Node externs = null;
    prepareAst.process(externs, root);

    // No op since checkOnly is false by default
  }

  @Test
  public void testPrepareAst_process_NativeExterns() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = IR.objectLiteral();

    Node root = IR.nullLiteral();
    prepareAst.process(externs, root);

    // No op since checkOnly is false by default
  }

  @Test
  public void testPrepareAst_process_EmptyAST_CheckOnly() {
    PrepareAst prepareAst = new PrepareAst(null, true);

    Node externs = null;
    Node root = null;

    try {
      prepareAst.process(externs, root);
      Preconditions.checkState(false, "normalizeNodeType constraints not violated");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testPrepareAst_process_RootWithEmptyBlock() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = null;
    Node root = IR.block();

    prepareAst.process(externs, root);

    // No op since checkOnly is false by default
  }

  @Test
  public void testPrepareAst_process_RootWithBlockAndChild() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = null;
    Node root = IR.block()
        .addStatement(IR.printStatement("x"))
        .addChild(IR.returnStatement(IR.stringLiteral("Hello, World!")));

    prepareAst.process(externs, root);

    // No op since checkOnly is false by default
  }

  @Test
  public void testPrepareAst_process_ExternsWithEmptyBlock() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = IR.block();
    Node root = null;

    prepareAst.process(externs, root);

    // No op since checkOnly is false by default
  }

  @Test
  public void testPrepareAst_process_ExternsWithBlockAndChild() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = IR.block()
        .addStatement(IR.printStatement("x"))
        .addChild(IR.returnStatement(IR.stringLiteral("Hello, World!")));
    Node root = null;

    prepareAst.process(externs, root);

    // No op since checkOnly is false by default
  }

  @Test
  public void testPrepareAnnotations_visit_NoAnnotation() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = null;
    Node root = IR.numberLiteral(42);
    Node nodeToTraverse = root;

    NodeTraversal nodeTraversal = new NodeTraversal(compiler -> prepareAst.new PrepareAnnotations());

    nodeTraversal.traverse(externs, root);

    assertFalse(nodeToTraverse.getBooleanProp(Node.IS_DISPATCHER));
    assertFalse(nodeToTraverse.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations_visit_CallExpression_NoAnnotation() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = null;
    Node root = IR.callExpression(IR.stringLiteral("foo"),
        IR.arrayLiteral(IR.numberLiteral(1), IR.numberLiteral(2)));
    Node nodeToTraverse = root;

    NodeTraversal nodeTraversal = new NodeTraversal(compiler -> prepareAst.new PrepareAnnotations());

    nodeTraversal.traverse(externs, root);

    assertFalse(nodeToTraverse.getBooleanProp(Node.IS_DISPATCHER));
    assertFalse(nodeToTraverse.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations_visit_FunctionExpression_WithAnnotation() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = null;
    Node root = IR.functionExpression(IR.stringLiteral("foo"), null,
        IR.block().addStatement(IR.returnStatement(IR.stringLiteral("Hello, World!"))));
    Node nodeToTraverse = root;

    NodeTraversal nodeTraversal = new NodeTraversal(compiler -> prepareAst.new PrepareAnnotations());

    nodeTraversal.traverse(externs, root);

    assertFalse(nodeToTraverse.getBooleanProp(Node.IS_DISPATCHER));
    assertFalse(nodeToTraverse.getBooleanProp(Node.DIRECT_EVAL));

    Node parent = nodeToTraverse.getParent();
    assertTrue(parent.getJSDocInfo() != null);
  }

  @Test
  public void testPrepareAnnotations_visit_FunctionDeclaration_WithAnnotation() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = null;
    Node root = IR.functionDeclaration(IR.stringLiteral("foo"), null,
        IR.block().addStatement(IR.returnStatement(IR.stringLiteral("Hello, World!"))));
    Node nodeToTraverse = root;

    NodeTraversal nodeTraversal = new NodeTraversal(compiler -> prepareAst.new PrepareAnnotations());

    nodeTraversal.traverse(externs, root);

    assertFalse(nodeToTraverse.getBooleanProp(Node.IS_DISPATCHER));
    assertFalse(nodeToTraverse.getBooleanProp(Node.DIRECT_EVAL));

    Node parent = nodeToTraverse.getParent();
    assertTrue(parent.getJSDocInfo() != null);
  }

  @Test
  public void testPrepareAnnotations_visit_CallExpression_WithAnnotation() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node externs = null;
    Node root = IR.callExpression(IR.stringLiteral("eval"), null,
        IR.arrayLiteral(IR.stringLiteral("'use strict';"),
            IR.stringLiteral("console.log('Hello, World!');")));
    Node nodeToTraverse = root;

    NodeTraversal nodeTraversal = new NodeTraversal(compiler -> prepareAst.new PrepareAnnotations());

    nodeTraversal.traverse(externs, root);

    assertFalse(nodeToTraverse.getBooleanProp(Node.IS_DISPATCHER));
    assertTrue(nodeToTraverse.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations_normalizeObjectLiteralAnnotations() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node objlit = IR.objectLiteral()
        .addProperty(IR.stringLiteral("a"), IR.stringLiteral("b"))
        .addProperty(IR.stringLiteral("c"), IR.functionExpression(
            null, null, IR.block().addStatement(IR.returnStatement(IR.numberLiteral(42)))));

    prepareAst.new PrepareAnnotations().normalizeObjectLiteralAnnotations(objlit);

    Node a = objlit.getFirstChild();
    Node b = a.getFirstChild();
    Node c = objlit.getChildAt(1);

    assertSame(a.getJSDocInfo(), b.getJSDocInfo());
    assertNull(c.getJSDocInfo());
  }
}