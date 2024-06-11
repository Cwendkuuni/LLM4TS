package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private CheckLevel level;
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    level = CheckLevel.VERBOSE;
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, level);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverse_constructor() {
    Node constructorNode = Node.newFunctionNode(null, true, null, null, null);
    JSDocInfo jsDoc = new JSDocInfo(null, null, null, null, "constructor");
    when(constructorNode.getJSDocInfo()).thenReturn(jsDoc);

    Node constructorParent = Node.newNameNode("MyConstructor");
    when(constructorNode.getParent()).thenReturn(constructorParent);

    Node constructorParentParent = Node.newNameNode("MyClass");
    when(constructorParent.getParent()).thenReturn(constructorParentParent);

    when(constructorParentParent.getType()).thenReturn(Token.NAME);

    assertThat(checker.shouldTraverse(NodeTraversal.TRAVERSAL, constructorNode, constructorParentParent)).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode(null, false, null, null, null);
    JSDocInfo jsDoc = new JSDocInfo(null, null, null, null, "@this");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node functionParent = Node.newNameNode("MyFunction");
    when(functionNode.getParent()).thenReturn(functionParent);

    Node functionParentParent = Node.newNameNode("MyObject");
    when(functionParent.getParent()).thenReturn(functionParentParent);

    when(functionParentParent.getType()).thenReturn(Token.NAME);

    assertThat(checker.shouldTraverse(NodeTraversal.TRAVERSAL, functionNode, functionParentParent)).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithoutAnnotation() {
    Node functionNode = Node.newFunctionNode(null, false, null, null, null);
    JSDocInfo jsDoc = new JSDocInfo(null, null, null, null, null);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node functionParent = Node.newNameNode("MyFunction");
    when(functionNode.getParent()).thenReturn(functionParent);

    Node functionParentParent = Node.newNameNode("MyObject");
    when(functionParent.getParent()).thenReturn(functionParentParent);

    when(functionParentParent.getType()).thenReturn(Token.NAME);

    assertThat(checker.shouldTraverse(NodeTraversal.TRAVERSAL, functionNode, functionParentParent)).isTrue();
  }

  @Test
  public void testShouldTraverse_assignmentToThis() {
    Node assignNode = Node.newAssignNode(null, null, null);
    Node thisNode = Node.newThisNode();
    Node thisParent = Node.newNameNode("MyObject");
    Node thisParentParent = Node.newNameNode("MyObject");

    when(assignNode.getLeftHandSide()).thenReturn(thisNode);
    when(assignNode.getParent()).thenReturn(assignParent);
    when(thisNode.getParent()).thenReturn(thisParent);
    when(thisParent.getParent()).thenReturn(thisParentParent);

    when(thisParentParent.getType()).thenReturn(Token.NAME);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    checker.visit(NodeTraversal.TRAVERSAL, assignNode, thisParentParent);

    assertThat(checker.shouldReportThis(assignNode, thisParentParent)).isTrue();
  }

  @Test
  public void testShouldTraverse_assignmentToPrototypeProperty() {
    Node assignNode = Node.newAssignNode(null, null, null);
    Node getPropNode = Node.newGetPropNode(null, null, Node.newNameNode("prototype"));
    Node thisNode = Node.newThisNode();
    Node thisParent = Node.newNameNode("MyObject");
    Node thisParentParent = Node.newNameNode("MyObject");

    when(assignNode.getLeftHandSide()).thenReturn(getPropNode);
    when(assignNode.getParent()).thenReturn(assignParent);
    when(getPropNode.getParent()).thenReturn(thisNode);
    when(thisNode.getParent()).thenReturn(thisParent);
    when(thisParent.getParent()).thenReturn(thisParentParent);

    when(thisParentParent.getType()).thenReturn(Token.NAME);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    checker.visit(NodeTraversal.TRAVERSAL, assignNode, thisParentParent);

    assertThat(checker.shouldReportThis(assignNode, thisParentParent)).isFalse();
  }

  @Test
  public void testShouldTraverse_assignmentToPrototypeSubProperty() {
    Node assignNode = Node.newAssignNode(null, null, null);
    Node getPropNode1 = Node.newGetPropNode(null, null, Node.newNameNode("prototype"));
    Node getPropNode2 = Node.newGetPropNode(null, null, Node.newNameNode("property"));
    Node thisNode = Node.newThisNode();
    Node thisParent = Node.newNameNode("MyObject");
    Node thisParentParent = Node.newNameNode("MyObject");

    when(assignNode.getLeftHandSide()).thenReturn(getPropNode2);
    when(assignNode.getParent()).thenReturn(assignParent);
    when(getPropNode2.getParent()).thenReturn(getPropNode1);
    when(getPropNode1.getParent()).thenReturn(thisNode);
    when(thisNode.getParent()).thenReturn(thisParent);
    when(thisParent.getParent()).thenReturn(thisParentParent);

    when(thisParentParent.getType()).thenReturn(Token.NAME);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    checker.visit(NodeTraversal.TRAVERSAL, assignNode, thisParentParent);

    assertThat(checker.shouldReportThis(assignNode, thisParentParent)).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithPrototypeMethod() {
    Node functionNode = Node.newFunctionNode(null, false, null, null, null);
    Node prototypeNode = Node.newNameNode("prototype");
    Node getPropNode = Node.newGetPropNode(null, null, prototypeNode);
    Node thisNode = Node.newThisNode();
    Node thisParent = Node.newNameNode("MyFunction");
    Node thisParentParent = Node.newNameNode("MyFunction.prototype");

    when(functionNode.getJSDocInfo()).thenReturn(null);
    when(functionNode.getParent()).thenReturn(thisParent);
    when(thisParent.getParent()).thenReturn(thisParentParent);
    when(thisParentParent.getFirstChild()).thenReturn(getPropNode);
    when(getPropNode.getParent()).thenReturn(thisNode);
    when(thisNode.getParent()).thenReturn(thisParent);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    checker.visit(NodeTraversal.TRAVERSAL, functionNode, thisParentParent);

    assertThat(checker.shouldReportThis(functionNode, thisParentParent)).isFalse();
  }

  @Test
  public void testReportGlobalThis() {
    Node assignNode = Node.newAssignNode(null, null, null);
    Node thisNode = Node.newThisNode();
    Node thisParent = Node.newNameNode("MyObject");

    when(assignNode.getLeftHandSide()).thenReturn(thisNode);
    when(thisNode.getParent()).thenReturn(thisParent);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    checker.visit(NodeTraversal.TRAVERSAL, assignNode, null);

    checker.compiler.report(NodeTraversal.TRAVERSAL.makeError(assignNode, level, GLOBAL_THIS));

    when(compiler.getDiagnostics()).thenReturn(new DiagnosticCollector());
    checker.reportGlobalThis(assignNode);

    DiagnosticCollector diagnostics = (DiagnosticCollector) compiler.getDiagnostics();
    assertThat(diagnostics.getDiagnostics()).hasSize(1);
    assertThat(diagnostics.getDiagnostics().get(0).getMessage()).isEqualTo("JSC_USED_GLOBAL_THIS: dangerous use of the global 'this' object");
  }
}