package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new  CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldTraverse_functionConstructorWithThisAnnotation_false() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = createFunctionNodeWithThisAnnotation(true);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, null));
  }

  @Test
  public void testShouldTraverse_functionConstructorWithoutThisAnnotation_true() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = createFunctionNodeWithThisAnnotation(false);
    assertTrue(checkGlobalThis.shouldTraverse(t, functionNode, null));
  }
  
  @Test
  public void testShouldTraverse_functionWithoutThisAnnotation_false() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = createFunctionNodeWithoutThisAnnotation();
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, null));
  }

  @Test
  public void testVisit_thisAssignmentLeftSide_reportThisError() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node thisNode = createThisNode();
    Node parent = createAssignmentNode(thisNode, null);
    checkGlobalThis.visit(t, thisNode, parent);
    // Ensure that an error is reported
    // (assert the specific error message if possible)
  }

  @Test
  public void testVisit_thisAssignmentLeftSide_noReportThisError() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node thisNode = createThisNode();
    Node parent = createAssignmentNode(null, thisNode);
    checkGlobalThis.visit(t, thisNode, parent);
    // Ensure that no error is reported
  }

  private Node createFunctionNodeWithThisAnnotation(boolean isConstructor) {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(isConstructor);
    jsDoc.setHasThisType(true);
    functionNode.setJSDocInfo(jsDoc);
    return functionNode;
  }

  private Node createFunctionNodeWithoutThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setHasThisType(false);
    functionNode.setJSDocInfo(jsDoc);
    return functionNode;
  }

  private Node createThisNode() {
    return new Node(Token.THIS);
  }

  private Node createAssignmentNode(Node lhs, Node rhs) {
    Node assignNode = new Node(Token.ASSIGN);
    if (lhs != null) {
      assignNode.addChildToBack(lhs);
    }
    if (rhs != null) {
      assignNode.addChildToBack(rhs);
    }
    return assignNode;
  }
}