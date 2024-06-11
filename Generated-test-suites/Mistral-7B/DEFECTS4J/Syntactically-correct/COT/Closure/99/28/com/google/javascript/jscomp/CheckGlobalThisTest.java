package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = Node.newFunction(null, null, null, null, null, null);
    JSDocInfo jsDoc = Node.newJsDocInfo("constructor");
    functionNode.setJSDocInfo(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = Node.newFunction(null, null, null, null, null, null);
    JSDocInfo jsDoc = Node.newJsDocInfo("this");
    functionNode.setJSDocInfo(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithOverride() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = Node.newFunction(null, null, null, null, null, null);
    JSDocInfo jsDoc = Node.newJsDocInfo("override");
    functionNode.setJSDocInfo(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoAnnotations() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = Node.newFunction(null, null, null, null, null, null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseAssignmentToLeftSide() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node assignNode = Node.newAssign(null, null, null, null, null, null);
    Node leftSide = Node.newThis();

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, leftSide, parent));
  }

  @Test
  public void testShouldNotTraverseAssignmentToRightSide() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node assignNode = Node.newAssign(null, null, null, null, null, null);
    Node leftSide = Node.newName("leftSide");
    Node rightSide = Node.newThis();

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, rightSide, parent));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node assignNode = Node.newAssign(null, null, null, null, null, null);
    Node leftSide = Node.newName("leftSide");
    Node prototypeProperty = Node.newGetProp(leftSide, "prototype");
    Node rightSide = Node.newThis();

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, rightSide, parent));
  }

  @Test
  public void testShouldNotTraverseAssignmentToSubProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node assignNode = Node.newAssign(null, null, null, null, null, null);
    Node leftSide = Node.newName("leftSide");
    Node subProperty = Node.newGetProp(leftSide, "subProperty");
    Node rightSide = Node.newThis();

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, rightSide, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node assignNode = Node.newAssign(null, null, null, null, null, null);
    Node leftSide = Node.newThis();

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("parent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertTrue(checkGlobalThis.shouldReportThis(assignNode, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node getPropNode = Node.newGetProp(Node.newName("parent"), "this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newName("grandparent");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertTrue(checkGlobalThis.shouldReportThis(getPropNode, parent));
  }
}