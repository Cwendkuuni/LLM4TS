package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.CallbackWithContext;
import com.google.javascript.jscomp.parser.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private static final String TEST_CASE_JS =
      "function evil() { this.useful = undefined; }";

  private AbstractCompiler compiler;
  private Callback checkGlobalThis;
  private CheckLevel checkLevel;
  private NodeTraversal nodeTraversal;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkLevel = CheckLevel.WARNING;
    nodeTraversal = mock(NodeTraversal.class);
    nodeFactory = mock(NodeFactory.class);
    checkGlobalThis = new CheckGlobalThis(compiler, checkLevel);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node node = Parser.parse(nodeFactory, TEST_CASE_JS, null, null).getRootNode();
    ArgumentCaptor<CallbackWithContext> callbackCaptor = ArgumentCaptor.forClass(CallbackWithContext.class);
    when(nodeTraversal.traverse(node, checkGlobalThis, null)).thenReturn(true);
    checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionConstructor() {
    Node node = Parser.parse(nodeFactory, "function Foo() {}", null, null).getRootNode();
    JSDocInfo jsDoc = new JSDocInfo("constructor");
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node node = Parser.parse(nodeFactory, "function Foo@this() {}", null, null).getRootNode();
    JSDocInfo jsDoc = new JSDocInfo("@this");
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldNotTraverseNonFunction() {
    Node node = Parser.parse(nodeFactory, "var x = 10;", null, null).getRootNode();
    checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node node = Parser.parse(nodeFactory, "var a = this;", null, null).getRootNode();
    checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node node = Parser.parse(nodeFactory, "var a = this;", null, null).getRootNode();
    when(compiler.report(nodeTraversal.makeError(node, checkLevel, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(true);
    checkGlobalThis.visit(nodeTraversal, node, null);
    assertTrue(checkGlobalThis.shouldReportThis(node, null));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Parser.parse(nodeFactory, "var a = 10;", null, null).getFirstChild();
    Node thisNode = Parser.parse(nodeFactory, "this;", null, null).getRootNode();
    Node node = new Node(Token.ASSIGN, null, assignNode, thisNode);
    when(compiler.report(nodeTraversal.makeError(node, checkLevel, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(false);
    checkGlobalThis.visit(nodeTraversal, node, null);
    assertFalse(checkGlobalThis.shouldReportThis(node, null));
  }

  @Test
  public void testShouldNotReportThisOnPrototypeProperty() {
    Node assignNode = Parser.parse(nodeFactory, "var a = {}; a.prototype.x = this;", null, null).getFirstChild();
    Node thisNode = Parser.parse(nodeFactory, "this;", null, null).getRootNode();
    Node node = new Node(Token.ASSIGN, null, assignNode, thisNode);
    checkGlobalThis.visit(nodeTraversal, node, null);
    assertFalse(checkGlobalThis.shouldReportThis(node, null));
  }

  @Test
  public void testShouldReportThisOnDeepPrototypeProperty() {
    Node assignNode = Parser.parse(nodeFactory, "var a = {}; a.prototype.b.prototype.x = this;", null, null).getFirstChild();
    Node thisNode = Parser.parse(nodeFactory, "this;", null, null).getRootNode();
    Node node = new Node(Token.ASSIGN, null, assignNode, thisNode);
    when(compiler.report(nodeTraversal.makeError(node, checkLevel, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(true);
    checkGlobalThis.visit(nodeTraversal, node, null);
    assertTrue(checkGlobalThis.shouldReportThis(node, null));
  }
}